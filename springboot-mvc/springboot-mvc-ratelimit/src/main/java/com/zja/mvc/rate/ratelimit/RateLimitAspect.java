package com.zja.mvc.rate.ratelimit;

import com.alibaba.fastjson.JSON;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.reflect.MethodSignature;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.expression.EvaluationContext;
import org.springframework.expression.Expression;
import org.springframework.expression.ExpressionParser;
import org.springframework.expression.spel.standard.SpelExpressionParser;
import org.springframework.expression.spel.support.StandardEvaluationContext;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.lang.reflect.Method;
import java.security.Principal;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 限流 AOP 切面
 * <p>
 * 拦截标注了 {@link RateLimit} 注解的方法（支持方法级和类级注解），
 * 根据注解配置的算法和维度类型执行限流逻辑。
 * <p>
 * 优先级说明：方法级注解 > 类级注解。
 *
 * @author: zhengja
 * @since: 2024/03/11
 */
@Aspect
@Component
@Order(1)
public class RateLimitAspect {

    private static final Logger log = LoggerFactory.getLogger(RateLimitAspect.class);

    private final ExpressionParser spelParser = new SpelExpressionParser();

    @Autowired
    private RateLimitHandler rateLimitHandler;

    /**
     * 环绕通知：拦截所有标注 @RateLimit 的方法
     */
    @Around("@annotation(com.zja.mvc.rate.ratelimit.RateLimit) || @within(com.zja.mvc.rate.ratelimit.RateLimit)")
    public Object around(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取注解（方法级优先，其次类级）
        RateLimit rateLimit = resolveAnnotation(joinPoint);
        if (rateLimit == null) {
            return joinPoint.proceed();
        }

        HttpServletRequest request = getRequest();
        if (request == null) {
            // 非 Web 请求环境，跳过限流
            return joinPoint.proceed();
        }

        // 白名单检查
        String clientIp = IpUtils.getClientIp(request);
        if (isWhitelisted(clientIp, rateLimit.whitelistIps())) {
            return joinPoint.proceed();
        }

        // 构建限流 key
        String limitKey = buildKey(rateLimit, request, joinPoint);

        // 执行限流判断
        boolean allowed = rateLimitHandler.isAllowed(limitKey, rateLimit);

        if (!allowed) {
            handleRejected(request, rateLimit, limitKey, clientIp);
            return null;
        }

        return joinPoint.proceed();
    }

    // =====================================================================
    // 私有方法
    // =====================================================================

    /**
     * 解析注解：方法级优先，其次类级
     */
    private RateLimit resolveAnnotation(ProceedingJoinPoint joinPoint) {
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        Method method = signature.getMethod();

        // 优先取方法上的注解
        RateLimit rateLimit = method.getAnnotation(RateLimit.class);
        if (rateLimit != null) {
            return rateLimit;
        }
        // 其次取类上的注解
        return joinPoint.getTarget().getClass().getAnnotation(RateLimit.class);
    }

    /**
     * 根据限流类型构建唯一 key
     */
    private String buildKey(RateLimit rateLimit, HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        String className = joinPoint.getTarget().getClass().getSimpleName();
        MethodSignature signature = (MethodSignature) joinPoint.getSignature();
        String methodName = signature.getMethod().getName();
        String baseKey = "rate_limit:" + className + ":" + methodName;

        String clientIp = IpUtils.getClientIp(request);
        String userId = resolveUserId(request);
        String url = request.getMethod() + ":" + request.getRequestURI();

        switch (rateLimit.type()) {
            case GLOBAL:
                return baseKey;
            case IP:
                return baseKey + ":ip:" + clientIp;
            case USER:
                return baseKey + ":user:" + userId;
            case URL:
                return "rate_limit:url:" + url;
            case IP_URL:
                return "rate_limit:ip_url:" + clientIp + ":" + url;
            case USER_URL:
                return "rate_limit:user_url:" + userId + ":" + url;
            case CUSTOM:
                return baseKey + ":custom:" + resolveCustomKey(rateLimit.customKey(), request, joinPoint);
            default:
                return baseKey;
        }
    }

    /**
     * 解析 SpEL 自定义 key 表达式
     */
    private String resolveCustomKey(String spelExpression, HttpServletRequest request, ProceedingJoinPoint joinPoint) {
        if (spelExpression == null || spelExpression.isEmpty()) {
            return "default";
        }
        try {
            MethodSignature signature = (MethodSignature) joinPoint.getSignature();
            String[] paramNames = signature.getParameterNames();
            Object[] args = joinPoint.getArgs();

            EvaluationContext context = new StandardEvaluationContext();
            context.setVariable("request", request);
            context.setVariable("args", args);
            if (paramNames != null) {
                for (int i = 0; i < paramNames.length; i++) {
                    context.setVariable(paramNames[i], args[i]);
                    context.setVariable("p" + i, args[i]);
                }
            }

            Expression expression = spelParser.parseExpression(spelExpression);
            Object value = expression.getValue(context);
            return value != null ? value.toString() : "null";
        } catch (Exception e) {
            log.warn("[RateLimit] SpEL 表达式解析失败: {}, 使用默认 key", spelExpression, e);
            return "spel_error";
        }
    }

    /**
     * 解析用户标识：依次从 Principal、X-User-Id 请求头中取
     */
    private String resolveUserId(HttpServletRequest request) {
        // 1. 从 Spring Security Principal 获取
        Principal principal = request.getUserPrincipal();
        if (principal != null) {
            return principal.getName();
        }
        // 2. 从请求头获取
        String userId = request.getHeader("X-User-Id");
        if (userId != null && !userId.isEmpty()) {
            return userId;
        }
        // 3. 降级到 IP
        return IpUtils.getClientIp(request);
    }

    /**
     * 白名单判断
     */
    private boolean isWhitelisted(String clientIp, String[] whitelistIps) {
        if (whitelistIps == null || whitelistIps.length == 0) {
            return false;
        }
        List<String> whitelist = Arrays.asList(whitelistIps);
        boolean result = whitelist.contains(clientIp);
        if (result) {
            log.debug("[RateLimit] IP {} 在白名单中，跳过限流", clientIp);
        }
        return result;
    }

    /**
     * 处理被限流的请求
     */
    private void handleRejected(HttpServletRequest request, RateLimit rateLimit,
                                 String limitKey, String clientIp) throws IOException {
        if (rateLimit.enableLog()) {
            log.warn("[RateLimit] 请求被限流 | key={} | ip={} | uri={} | limit={}/{}{} | algorithm={}",
                    limitKey, clientIp, request.getRequestURI(),
                    rateLimit.limit(), rateLimit.period(), rateLimit.timeUnit().name().toLowerCase(),
                    rateLimit.algorithm());
        }

        HttpServletResponse response = getResponse();
        if (response != null) {
            response.setContentType(MediaType.APPLICATION_JSON_VALUE + ";charset=UTF-8");
            if (rateLimit.httpStatus429()) {
                response.setStatus(HttpStatus.TOO_MANY_REQUESTS.value());
            } else {
                response.setStatus(HttpStatus.OK.value());
            }

            Map<String, Object> result = new HashMap<>();
            result.put("code", HttpStatus.TOO_MANY_REQUESTS.value());
            result.put("message", rateLimit.message());
            result.put("success", false);
            result.put("timestamp", System.currentTimeMillis());
            response.getWriter().write(JSON.toJSONString(result));
        }

        throw new RateLimitException(rateLimit.message(), limitKey, rateLimit.limit(),
                rateLimit.timeUnit().toMillis(rateLimit.period()));
    }

    /**
     * 获取当前请求对象
     */
    private HttpServletRequest getRequest() {
        try {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes != null ? attributes.getRequest() : null;
        } catch (Exception e) {
            return null;
        }
    }

    /**
     * 获取当前响应对象
     */
    private HttpServletResponse getResponse() {
        try {
            ServletRequestAttributes attributes =
                    (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
            return attributes != null ? attributes.getResponse() : null;
        } catch (Exception e) {
            return null;
        }
    }
}

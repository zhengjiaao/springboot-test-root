package com.zja.log.skywalking.controller;

import com.zja.log.skywalking.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

/**
 * SkyWalking 链路追踪测试接口
 * <p>
 * 注意：需要挂载 SkyWalking Java Agent 才能真正生效。
 * 不挂载 Agent 时，@Trace 注解和 TraceContext 仍可编译运行，但 traceId 为空。
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 16:00
 */
@Slf4j
@RestController
@RequestMapping("/api/skywalking")
public class SkyWalkingController {

    @Autowired
    private NotificationService notificationService;

    /**
     * 发送邮件通知
     * GET http://localhost:8080/api/skywalking/notify/email?target=test@example.com&content=你好
     */
    @GetMapping("/notify/{type}")
    public Map<String, Object> sendNotification(@PathVariable String type,
                                                 @RequestParam String target,
                                                 @RequestParam(defaultValue = "测试消息") String content) {
        String traceId = TraceContext.traceId();
        log.info("[SkyWalking示例] 收到通知请求, traceId={}, type={}", traceId, type);

        boolean success = notificationService.sendNotification(type, target, content);

        Map<String, Object> result = new HashMap<>();
        result.put("code", success ? 200 : 500);
        result.put("traceId", traceId);
        result.put("type", type);
        result.put("target", target);
        result.put("message", success ? "通知发送成功" : "通知发送失败");
        result.put("说明", "在 SkyWalking UI 中可通过 traceId 查看完整调用链");
        return result;
    }

    /**
     * 获取当前 traceId
     * GET http://localhost:8080/api/skywalking/traceId
     */
    @GetMapping("/traceId")
    public Map<String, Object> getTraceId() {
        String traceId = TraceContext.traceId();
        log.info("获取当前 traceId={}", traceId);

        Map<String, Object> result = new HashMap<>();
        result.put("traceId", traceId);
        result.put("说明", "未挂载 SkyWalking Agent 时 traceId 为空字符串");
        return result;
    }

    /**
     * 模拟完整业务调用链
     * GET http://localhost:8080/api/skywalking/full-chain?userId=U001
     */
    @GetMapping("/full-chain")
    public Map<String, Object> fullChain(@RequestParam String userId) {
        String traceId = TraceContext.traceId();
        log.info("开始完整业务链路, traceId={}, userId={}", traceId, userId);

        // 模拟多个服务调用
        notificationService.sendNotification("email", userId + "@example.com", "订单确认");
        notificationService.sendNotification("sms", "13800138000", "验证码: 123456");
        notificationService.sendNotification("push", userId, "您有新消息");

        log.info("完整业务链路执行完毕, traceId={}", traceId);

        Map<String, Object> result = new HashMap<>();
        result.put("code", 200);
        result.put("traceId", traceId);
        result.put("message", "完整调用链执行完毕，在 SkyWalking UI 中查看拓扑和链路详情");
        return result;
    }
}

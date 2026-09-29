package com.zja.security;

import com.zja.common.Constants;
import com.zja.common.exception.BusinessException;
import com.zja.entity.AppEntity;
import com.zja.service.AuditService;
import com.zja.store.PlatformStore;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.concurrent.TimeUnit;

/**
 * 在线接入认证拦截器：/api/key/** 与 /api/crypto/** 必须携带接入签名
 *
 * <p>校验流程：appKey有效性 → 应用状态 → 时间戳窗口 → HMAC-SM3请求签名 → nonce防重放。
 * 签名通过后才登记nonce，避免未认证请求占满防重放缓存。</p>
 *
 * @author: zhengja
 */
@Component
public class PlatformAuthInterceptor implements HandlerInterceptor {

	private final PlatformStore platformStore;
	private final ReplayProtector replayProtector;
	private final AuditService auditService;
	/** 时间戳允许窗口（毫秒） */
	private final long timestampWindowMillis;
	private final long windowMinutes;

	public PlatformAuthInterceptor(PlatformStore platformStore,
	                                ReplayProtector replayProtector,
	                                AuditService auditService,
	                                @Value("${crypto.platform.auth-timestamp-window:5}") long windowMinutes) {
		this.platformStore = platformStore;
		this.replayProtector = replayProtector;
		this.auditService = auditService;
		this.windowMinutes = windowMinutes;
		this.timestampWindowMillis = TimeUnit.MINUTES.toMillis(windowMinutes);
	}

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) {
		String appKey = request.getHeader(Constants.HEADER_APP_KEY);
		String timestamp = request.getHeader(Constants.HEADER_TIMESTAMP);
		String nonce = request.getHeader(Constants.HEADER_NONCE);
		String signature = request.getHeader(Constants.HEADER_SIGNATURE);

		if (isBlank(appKey) || isBlank(timestamp) || isBlank(nonce) || isBlank(signature)) {
			auditService.record(appKey, "AUTH", null, null, false, "缺少接入认证请求头", 0);
			throw BusinessException.unauthorized("缺少接入认证请求头，请携带 X-App-Key、X-Timestamp、X-Nonce、X-Signature（或直接使用平台在线SDK）");
		}

		AppEntity app = platformStore.findApp(appKey);
		if (app == null) {
			auditService.record(appKey, "AUTH", null, null, false, "appKey不存在", 0);
			throw BusinessException.unauthorized("appKey不存在，请先在平台注册应用");
		}
		if (!Constants.STATUS_ENABLED.equals(app.getStatus())) {
			auditService.record(appKey, "AUTH", null, null, false, "应用已停用", 0);
			throw BusinessException.unauthorized("应用已停用，请联系平台管理员");
		}

		long ts;
		try {
			ts = Long.parseLong(timestamp);
		} catch (NumberFormatException e) {
			auditService.record(appKey, "AUTH", null, null, false, "时间戳格式非法", 0);
			throw BusinessException.badRequest("X-Timestamp必须为毫秒时间戳");
		}
		if (Math.abs(System.currentTimeMillis() - ts) > timestampWindowMillis) {
			auditService.record(appKey, "AUTH", null, null, false, "时间戳超出允许窗口", 0);
			throw BusinessException.unauthorized("请求时间戳超出允许窗口（" + windowMinutes + "分钟），请校准客户端时间");
		}

		byte[] body = resolveBody(request);
		String uri = request.getRequestURI();
		if (request.getQueryString() != null) {
			uri = uri + "?" + request.getQueryString();
		}
		String stringToSign = SignatureUtil.buildStringToSign(appKey, timestamp, nonce, request.getMethod(), uri, body);
		if (!SignatureUtil.verify(app.getAppSecret(), stringToSign, signature)) {
			auditService.record(appKey, "AUTH", null, null, false, "请求签名校验失败", 0);
			throw BusinessException.unauthorized("请求签名校验失败，请检查appSecret与签名算法（HMAC-SM3）");
		}

		// 签名通过后再登记nonce：未认证请求无法污染防重放缓存
		if (!replayProtector.checkAndRemember(nonce, ts, timestampWindowMillis)) {
			auditService.record(appKey, "AUTH", null, null, false, "nonce重复，疑似重放攻击", 0);
			throw BusinessException.unauthorized("重复的请求随机串，疑似重放攻击");
		}

		// 认证通过，向控制器透传当前应用标识
		request.setAttribute(Constants.ATTR_APP_KEY, appKey);
		return true;
	}

	private byte[] resolveBody(HttpServletRequest request) {
		if (request instanceof CachedBodyRequestWrapper) {
			return ((CachedBodyRequestWrapper) request).getCachedBody();
		}
		return new byte[0];
	}

	private boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
}

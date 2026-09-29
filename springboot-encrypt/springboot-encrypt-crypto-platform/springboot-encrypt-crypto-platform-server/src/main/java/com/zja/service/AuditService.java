package com.zja.service;

import com.zja.entity.AuditLogEntity;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestAttributes;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.ConcurrentLinkedDeque;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.atomic.AtomicLong;
import java.util.stream.Collectors;

/**
 * 密码运算审计服务（密评必备能力）
 *
 * <p>记录每一次密钥操作与密码运算：操作类型、涉及密钥、算法、成败、耗时、来源IP。
 * 演示实现为内存有界队列；生产环境应写入数据库/审计平台并支持留存三年以上。</p>
 *
 * @author: zhengja
 */
@Slf4j
@Service
public class AuditService {

	/** 容量下限（防止配置过小导致审计快速丢失） */
	private static final int MIN_CAPACITY = 1000;
	/** 容量上限（防止配置过大带来内存风险） */
	private static final int MAX_CAPACITY = 1000000;
	/** 单次查询返回条数上限 */
	private static final int MAX_QUERY_LIMIT = 1000;

	/** 内存保留上限（条） */
	private final int capacity;

	private final ConcurrentLinkedDeque<AuditLogEntity> logDeque = new ConcurrentLinkedDeque<>();
	/** 当前队列条数（ConcurrentLinkedDeque.size()为O(n)，用计数器保证淘汰判断为O(1)） */
	private final AtomicInteger dequeSize = new AtomicInteger(0);
	private final AtomicLong idGenerator = new AtomicLong(0);

	public AuditService(@Value("${crypto.platform.audit-log-capacity:10000}") int capacity) {
		int clamped = Math.max(MIN_CAPACITY, Math.min(MAX_CAPACITY, capacity));
		if (clamped != capacity) {
			log.warn("审计日志容量配置{}超出范围[{}, {}]，已调整为{}", capacity, MIN_CAPACITY, MAX_CAPACITY, clamped);
		}
		this.capacity = clamped;
	}

	/**
	 * 记录审计日志
	 */
	public void record(String appKey, String action, String keyId, String algorithm,
	                   boolean success, String message, long costMs) {
		AuditLogEntity auditLog = new AuditLogEntity();
		auditLog.setId(idGenerator.incrementAndGet());
		auditLog.setAppKey(appKey);
		auditLog.setAction(action);
		auditLog.setKeyId(keyId);
		auditLog.setAlgorithm(algorithm);
		auditLog.setSuccess(success);
		auditLog.setMessage(message);
		auditLog.setCostMs(costMs);
		auditLog.setClientIp(resolveClientIp());
		auditLog.setTimestamp(new java.util.Date());

		logDeque.addLast(auditLog);
		dequeSize.incrementAndGet();
		// 超出容量时淘汰最旧记录（计数器判断，避免O(n)遍历）
		while (dequeSize.get() > capacity && logDeque.pollFirst() != null) {
			dequeSize.decrementAndGet();
		}

		if (log.isDebugEnabled()) {
			log.debug("审计日志：appKey={}, action={}, keyId={}, success={}, cost={}ms",
					appKey, action, keyId, success, costMs);
		}
	}

	/**
	 * 查询审计日志（最新在前）
	 *
	 * @param appKey 按应用过滤，null表示不过滤
	 * @param limit  返回条数上限
	 */
	public List<AuditLogEntity> list(String appKey, int limit) {
		return logDeque.stream()
				.filter(logEntity -> appKey == null || appKey.isEmpty() || appKey.equals(logEntity.getAppKey()))
				.sorted(Comparator.comparing(AuditLogEntity::getId).reversed())
				.limit(limit <= 0 ? 100 : Math.min(limit, MAX_QUERY_LIMIT))
				.collect(Collectors.toList());
	}

	/** 当前审计记录数（O(1)，供观测） */
	public int count() {
		return dequeSize.get();
	}

	/** 审计保留上限（钳制后的真实值，供观测） */
	public int capacity() {
		return capacity;
	}
	
	/** 解析客户端IP（优先取代理头） */
	private String resolveClientIp() {
		try {
			RequestAttributes attributes = RequestContextHolder.getRequestAttributes();
			if (attributes instanceof ServletRequestAttributes) {
				HttpServletRequest request = ((ServletRequestAttributes) attributes).getRequest();
				String forwarded = request.getHeader("X-Forwarded-For");
				if (forwarded != null && !forwarded.trim().isEmpty()) {
					return forwarded.split(",")[0].trim();
				}
				return request.getRemoteAddr();
			}
		} catch (Exception ignore) {
			// 非Web上下文（如启动初始化）无法解析IP
		}
		return null;
	}
}

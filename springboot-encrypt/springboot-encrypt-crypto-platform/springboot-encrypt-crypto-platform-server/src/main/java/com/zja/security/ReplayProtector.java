package com.zja.security;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.annotation.PreDestroy;
import java.util.Iterator;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * 请求防重放：nonce唯一性校验
 *
 * <p>同一nonce在时间窗口内只允许出现一次；过期记录由后台守护线程定时清理
 * （替代原先的"每请求全表扫描"，避免高频请求下的O(n)开销）；
 * nonce表达到容量上限且无可清理记录时拒绝新请求（fail-closed），防止海量nonce耗尽内存。</p>
 *
 * <p>生产环境建议使用Redis的SETNX+TTL实现，天然支持集群部署。</p>
 *
 * @author: zhengja
 */
@Component
public class ReplayProtector {

	private static final Logger log = LoggerFactory.getLogger(ReplayProtector.class);

	/** 过期记录保留时长：2倍时间窗口（毫秒），随校验窗口动态更新 */
	private volatile long retentionMillis;

	/** nonce缓存容量上限（条） */
	private final int maxCapacity;

	private final Map<String, Long> nonceTable = new ConcurrentHashMap<>();
	/** 过期nonce清理线程（守护线程，不阻塞应用退出） */
	private final ScheduledExecutorService cleaner;

	public ReplayProtector(@Value("${crypto.platform.nonce-cache-capacity:100000}") int maxCapacity,
	                       @Value("${crypto.platform.auth-timestamp-window:5}") long windowMinutes) {
		this.maxCapacity = Math.max(1000, maxCapacity);
		this.retentionMillis = TimeUnit.MINUTES.toMillis(windowMinutes) * 2;
		this.cleaner = Executors.newSingleThreadScheduledExecutor(runnable -> {
			Thread thread = new Thread(runnable, "nonce-cleaner");
			thread.setDaemon(true);
			return thread;
		});
		// 启动1分钟后开始，每分钟清理一次过期nonce
		this.cleaner.scheduleWithFixedDelay(this::safePurge, 1, 1, TimeUnit.MINUTES);
	}

	/**
	 * 检查nonce是否首次出现，首次出现则记录并返回true，重复出现返回false
	 *
	 * @param nonce        请求随机串
	 * @param timestamp    请求时间戳（毫秒）
	 * @param windowMillis 时间窗口（毫秒），超出2倍窗口的记录由定时任务清理
	 */
	public boolean checkAndRemember(String nonce, long timestamp, long windowMillis) {
		this.retentionMillis = windowMillis * 2;
		if (nonceTable.size() >= maxCapacity) {
			// 达到容量上限：先兜底清理一次，仍满则拒绝新请求（fail-closed，防内存耗尽）
			purgeExpired();
			if (nonceTable.size() >= maxCapacity) {
				log.warn("nonce缓存已达容量上限（{}条），拒绝新请求，疑似nonce泛洪攻击", maxCapacity);
				return false;
			}
		}
		Long prev = nonceTable.putIfAbsent(nonce, timestamp);
		return prev == null;
	}

	/** 清理超过保留时长的nonce记录，返回清理条数（包内可见，供测试与观测） */
	int purgeExpired() {
		long deadline = System.currentTimeMillis() - retentionMillis;
		int removed = 0;
		Iterator<Map.Entry<String, Long>> it = nonceTable.entrySet().iterator();
		while (it.hasNext()) {
			if (it.next().getValue() < deadline) {
				it.remove();
				removed++;
			}
		}
		return removed;
	}

	/** 当前缓存nonce数量（供测试与观测） */
	public int size() {
		return nonceTable.size();
	}

	/** 缓存容量上限（供观测） */
	public int capacity() {
		return maxCapacity;
	}

	private void safePurge() {
		try {
			int removed = purgeExpired();
			if (removed > 0 && log.isDebugEnabled()) {
				log.debug("已清理过期nonce {}条，当前缓存 {}条", removed, nonceTable.size());
			}
		} catch (Exception e) {
			log.warn("nonce定期清理任务异常", e);
		}
	}

	@PreDestroy
	public void shutdown() {
		cleaner.shutdownNow();
	}
}

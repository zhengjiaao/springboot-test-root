package com.zja.security;

import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

/**
 * 防重放单元测试：nonce唯一性、过期清理、容量保护
 *
 * @author: zhengja
 */
@DisplayName("防重放：nonce唯一性校验")
class ReplayProtectorTest {

	private static final long WINDOW_MILLIS = 5 * 60 * 1000;

	private ReplayProtector replayProtector;

	@AfterEach
	void tearDown() {
		if (replayProtector != null) {
			replayProtector.shutdown();
		}
	}

	@Test
	@DisplayName("同一nonce在窗口内重复提交被拒绝")
	void duplicateNonceRejected() {
		replayProtector = new ReplayProtector(10000, 5);
		long now = System.currentTimeMillis();

		assertTrue(replayProtector.checkAndRemember("nonce-1", now, WINDOW_MILLIS));
		assertFalse(replayProtector.checkAndRemember("nonce-1", now, WINDOW_MILLIS));
		assertTrue(replayProtector.checkAndRemember("nonce-2", now, WINDOW_MILLIS));
		assertEquals(2, replayProtector.size());
	}

	@Test
	@DisplayName("超过保留时长的nonce被清理，窗口内记录不受影响")
	void expiredNoncePurged() {
		replayProtector = new ReplayProtector(10000, 5);
		long now = System.currentTimeMillis();
		// 11分钟前的历史nonce（超过2倍窗口=10分钟的保留时长）
		long oldTimestamp = now - 11 * 60 * 1000;

		assertTrue(replayProtector.checkAndRemember("old-nonce", oldTimestamp, WINDOW_MILLIS));
		assertTrue(replayProtector.checkAndRemember("fresh-nonce", now, WINDOW_MILLIS));
		assertEquals(2, replayProtector.size());

		assertEquals(1, replayProtector.purgeExpired());
		assertEquals(1, replayProtector.size());
		// 窗口内记录仍被拦截
		assertFalse(replayProtector.checkAndRemember("fresh-nonce", now, WINDOW_MILLIS));
	}

	@Test
	@DisplayName("缓存达到容量上限时拒绝新nonce（fail-closed防内存耗尽）")
	void capacityGuard() {
		replayProtector = new ReplayProtector(1000, 5);
		long now = System.currentTimeMillis();

		for (int i = 0; i < 1000; i++) {
			assertTrue(replayProtector.checkAndRemember("nonce-" + i, now, WINDOW_MILLIS));
		}
		// 缓存已满且无过期记录可清理 → 拒绝新请求
		assertFalse(replayProtector.checkAndRemember("nonce-overflow", now, WINDOW_MILLIS));
		assertEquals(1000, replayProtector.size());
	}
}

package com.zja.store;

import com.zja.common.Constants;
import com.zja.entity.AppEntity;
import com.zja.entity.KeyEntity;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.stream.Collectors;

/**
 * 平台内存存储（演示实现）
 *
 * <p>演示环境使用内存Map保存应用、密钥与状态；生产环境应替换为数据库 + 密码机(HSM)托管密钥材料，
 * 并保证密钥材料加密存储、访问受控。</p>
 *
 * @author: zhengja
 */
@Component
public class PlatformStore {

	/** 应用表：appKey -> AppEntity */
	private final Map<String, AppEntity> appTable = new ConcurrentHashMap<>();
	/** 密钥表：keyId -> KeyEntity */
	private final Map<String, KeyEntity> keyTable = new ConcurrentHashMap<>();

	// ===== 应用 =====

	public void saveApp(AppEntity app) {
		appTable.put(app.getAppKey(), app);
	}

	public AppEntity findApp(String appKey) {
		return appKey == null ? null : appTable.get(appKey);
	}

	public List<AppEntity> listApps() {
		return appTable.values().stream()
				.sorted(Comparator.comparing(AppEntity::getCreateTime, Comparator.nullsLast(Comparator.naturalOrder())))
				.collect(Collectors.toList());
	}

	/** 应用总数（供观测） */
	public int appCount() {
		return appTable.size();
	}

	/** 启用状态的应用数（供观测） */
	public int enabledAppCount() {
		int count = 0;
		for (AppEntity app : appTable.values()) {
			if (Constants.STATUS_ENABLED.equals(app.getStatus())) {
				count++;
			}
		}
		return count;
	}

	// ===== 密钥 =====

	public void saveKey(KeyEntity key) {
		keyTable.put(key.getKeyId(), key);
	}

	public KeyEntity findKey(String keyId) {
		return keyId == null ? null : keyTable.get(keyId);
	}

	/** 密钥销毁：移除记录前先抹除密钥材料 */
	public KeyEntity removeKey(String keyId) {
		KeyEntity removed = keyTable.remove(keyId);
		if (removed != null) {
			removed.setSecretKeyBase64(null);
			removed.setPrivateKeyBase64(null);
		}
		return removed;
	}

	public List<KeyEntity> listKeysByApp(String appKey) {
		return keyTable.values().stream()
				.filter(key -> key.getAppKey().equals(appKey))
				.sorted(Comparator.comparing(KeyEntity::getCreateTime, Comparator.nullsLast(Comparator.naturalOrder())))
				.collect(Collectors.toList());
	}

	/** 平台密钥总数（供演示观测） */
	public int keyCount() {
		return keyTable.size();
	}

	/** 返回密钥表的快照副本（供审计遍历，避免暴露内部Map） */
	public List<KeyEntity> snapshotKeys() {
		return new ArrayList<>(keyTable.values());
	}
}

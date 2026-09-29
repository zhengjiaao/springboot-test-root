package com.zja.service;

import com.zja.common.Constants;
import com.zja.common.enums.AlgorithmType;
import com.zja.common.enums.KeyCategory;
import com.zja.common.exception.BusinessException;
import com.zja.core.AesCbcUtil;
import com.zja.core.HmacUtil;
import com.zja.core.RsaCryptoUtil;
import com.zja.core.Sm2CryptoUtil;
import com.zja.core.Sm4CbcUtil;
import com.zja.dto.request.KeyCreateRequest;
import com.zja.dto.response.KeyCreateResponse;
import com.zja.dto.response.KeyView;
import com.zja.entity.KeyEntity;
import com.zja.store.PlatformStore;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collectors;

/**
 * 密钥管理服务：创建、查询、停启用、销毁（密钥全生命周期管理）
 *
 * <p>密钥按应用隔离：应用只能操作自己的密钥；密钥材料保存在平台内部，永不外泄；
 * 非对称公钥可分发，对称/MAC密钥与私钥只在平台侧参与运算。</p>
 *
 * @author: zhengja
 */
@Service
public class KeyManageService {

	private final PlatformStore platformStore;
	private final AuditService auditService;

	public KeyManageService(PlatformStore platformStore, AuditService auditService) {
		this.platformStore = platformStore;
		this.auditService = auditService;
	}

	/**
	 * 为指定应用创建密钥
	 */
	public KeyCreateResponse createKey(String appKey, KeyCreateRequest request) {
		if (request == null || isBlank(request.getAlgorithm())) {
			throw BusinessException.badRequest("algorithm不能为空");
		}
		AlgorithmType algorithm = AlgorithmType.ofName(request.getAlgorithm());
		if (algorithm == null) {
			throw BusinessException.badRequest("不支持的算法：" + request.getAlgorithm()
					+ "，可选值：SM4/AES_128/AES_256/SM2/RSA_2048/HMAC_SM3/HMAC_SHA_256");
		}
		if (!algorithm.isKeyCreatable()) {
			throw BusinessException.badRequest("算法" + algorithm + "为摘要算法，无需创建密钥");
		}

		KeyEntity key = new KeyEntity();
		key.setKeyId(generateKeyId());
		key.setAppKey(appKey);
		key.setAlgorithm(algorithm);
		key.setAlias(isBlank(request.getAlias()) ? algorithm.name() + "-default" : request.getAlias().trim());
		key.setRemark(request.getRemark());
		key.setStatus(Constants.STATUS_ENABLED);
		key.setCreateTime(new Date());

		// 按类别生成密钥材料
		if (algorithm.getCategory() == KeyCategory.SYMMETRIC) {
			key.setSecretKeyBase64(generateSymmetricKey(algorithm));
		} else if (algorithm.getCategory() == KeyCategory.MAC) {
			key.setSecretKeyBase64(HmacUtil.generateKeyBase64());
		} else if (algorithm.getCategory() == KeyCategory.ASYMMETRIC) {
			Map<String, String> keyPair = algorithm == AlgorithmType.SM2
					? Sm2CryptoUtil.generateKeyPairBase64()
					: RsaCryptoUtil.generateKeyPairBase64();
			key.setPublicKeyBase64(keyPair.get(Sm2CryptoUtil.PUBLIC_KEY));
			key.setPrivateKeyBase64(keyPair.get(Sm2CryptoUtil.PRIVATE_KEY));
		}
		platformStore.saveKey(key);

		auditService.record(appKey, "KEY_CREATE", key.getKeyId(), algorithm.name(), true, "创建密钥：" + key.getAlias(), 0);

		KeyCreateResponse response = new KeyCreateResponse();
		response.setKeyId(key.getKeyId());
		response.setAlgorithm(algorithm.name());
		response.setCategory(algorithm.getCategory().name());
		response.setAlias(key.getAlias());
		response.setPublicKey(key.getPublicKeyBase64());
		response.setStatus(key.getStatus());
		response.setCreateTime(key.getCreateTime());
		return response;
	}

	/** 应用名下密钥列表 */
	public List<KeyView> listKeys(String appKey) {
		return platformStore.listKeysByApp(appKey).stream()
				.map(this::toKeyView)
				.collect(Collectors.toList());
	}

	/** 密钥详情 */
	public KeyView getKey(String appKey, String keyId) {
		return toKeyView(loadOwnedKey(appKey, keyId));
	}

	/** 停用密钥：停用期间所有运算请求将被拒绝 */
	public KeyView disableKey(String appKey, String keyId) {
		KeyEntity key = loadOwnedKey(appKey, keyId);
		key.setStatus(Constants.STATUS_DISABLED);
		platformStore.saveKey(key);
		auditService.record(appKey, "KEY_DISABLE", keyId, key.getAlgorithm().name(), true, "停用密钥", 0);
		return toKeyView(key);
	}

	/** 启用密钥 */
	public KeyView enableKey(String appKey, String keyId) {
		KeyEntity key = loadOwnedKey(appKey, keyId);
		key.setStatus(Constants.STATUS_ENABLED);
		platformStore.saveKey(key);
		auditService.record(appKey, "KEY_ENABLE", keyId, key.getAlgorithm().name(), true, "启用密钥", 0);
		return toKeyView(key);
	}

	/**
	 * 销毁密钥：先抹除密钥材料再移除记录，不可恢复
	 */
	public void destroyKey(String appKey, String keyId) {
		KeyEntity key = loadOwnedKey(appKey, keyId);
		platformStore.removeKey(keyId);
		auditService.record(appKey, "KEY_DESTROY", keyId, key.getAlgorithm().name(), true, "销毁密钥（密钥材料已抹除）", 0);
	}

	/**
	 * 加载并校验密钥：归属校验（跨应用隔离）+ 状态校验 + 类别校验
	 */
	public KeyEntity loadOwnedEnabledKey(String appKey, String keyId, KeyCategory expectedCategory, String action) {
		KeyEntity key = loadOwnedKey(appKey, keyId);
		if (!Constants.STATUS_ENABLED.equals(key.getStatus())) {
			auditService.record(appKey, action, keyId, key.getAlgorithm().name(), false, "密钥状态为" + key.getStatus(), 0);
			throw BusinessException.badRequest("密钥已" + (Constants.STATUS_DISABLED.equals(key.getStatus()) ? "停用" : "销毁") + "，无法执行该操作");
		}
		if (key.getAlgorithm().getCategory() != expectedCategory) {
			auditService.record(appKey, action, keyId, key.getAlgorithm().name(), false, "算法类别不匹配", 0);
			throw BusinessException.badRequest("密钥算法类别不匹配：该接口需要" + expectedCategory + "类别密钥，当前密钥为"
					+ key.getAlgorithm().getCategory() + "（" + key.getAlgorithm() + "）");
		}
		return key;
	}

	private KeyEntity loadOwnedKey(String appKey, String keyId) {
		if (isBlank(keyId)) {
			throw BusinessException.badRequest("keyId不能为空");
		}
		KeyEntity key = platformStore.findKey(keyId);
		if (key == null) {
			throw BusinessException.notFound("密钥不存在：" + keyId);
		}
		if (!key.getAppKey().equals(appKey)) {
			// 跨应用访问其它应用的密钥，仅提示不存在，避免密钥标识遍历探测
			auditService.record(appKey, "KEY_ACCESS_DENY", keyId, null, false, "跨应用访问密钥被拒绝", 0);
			throw BusinessException.notFound("密钥不存在：" + keyId);
		}
		return key;
	}

	/** 实体转安全视图（不含密钥材料） */
	private KeyView toKeyView(KeyEntity key) {
		KeyView view = new KeyView();
		view.setKeyId(key.getKeyId());
		view.setAppKey(key.getAppKey());
		view.setAlgorithm(key.getAlgorithm().name());
		view.setCategory(key.getAlgorithm().getCategory().name());
		view.setAlias(key.getAlias());
		view.setRemark(key.getRemark());
		view.setPublicKey(key.getPublicKeyBase64());
		view.setStatus(key.getStatus());
		view.setCreateTime(key.getCreateTime());
		return view;
	}

	private String generateSymmetricKey(AlgorithmType algorithm) {
		switch (algorithm) {
			case SM4:
				return Sm4CbcUtil.generateKeyBase64();
			case AES_128:
				return AesCbcUtil.generateKeyBase64(128);
			case AES_256:
				return AesCbcUtil.generateKeyBase64(256);
			default:
				throw BusinessException.badRequest("非法对称算法：" + algorithm);
		}
	}

	/** 生成密钥标识：key-前缀 + 32位随机串 */
	private String generateKeyId() {
		return "key-" + UUID.randomUUID().toString().replace("-", "");
	}

	private boolean isBlank(String s) {
		return s == null || s.trim().isEmpty();
	}
}

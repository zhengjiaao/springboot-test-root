package com.zja.controller;

import com.zja.common.ApiResponse;
import com.zja.common.Constants;
import com.zja.dto.request.KeyCreateRequest;
import com.zja.dto.response.KeyCreateResponse;
import com.zja.dto.response.KeyView;
import com.zja.service.KeyManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 密钥管理（需接入认证：X-App-Key/X-Timestamp/X-Nonce/X-Signature）
 *
 * @author: zhengja
 */
@RestController
@RequestMapping(value = "/api/key")
@Api(tags = {"密钥管理（在线接入）"})
public class KeyController {

	private final KeyManageService keyManageService;

	public KeyController(KeyManageService keyManageService) {
		this.keyManageService = keyManageService;
	}

	@ApiOperation(value = "创建密钥", notes = "在平台生成并托管密钥，密钥材料不出平台；支持SM4/AES/SM2/RSA/HMAC；非对称密钥返回公钥")
	@PostMapping("create")
	public ApiResponse<KeyCreateResponse> create(
			@RequestAttribute(Constants.ATTR_APP_KEY) String appKey,
			@RequestBody KeyCreateRequest request) {
		return ApiResponse.ok(keyManageService.createKey(appKey, request));
	}

	@ApiOperation(value = "我的密钥列表", notes = "查询当前应用名下全部密钥（仅元信息与公钥，不含密钥材料）")
	@GetMapping("list")
	public ApiResponse<List<KeyView>> list(
			@RequestAttribute(Constants.ATTR_APP_KEY) String appKey) {
		return ApiResponse.ok(keyManageService.listKeys(appKey));
	}

	@ApiOperation(value = "密钥详情", notes = "查询单个密钥元信息与公钥")
	@GetMapping("{keyId}")
	public ApiResponse<KeyView> detail(
			@RequestAttribute(Constants.ATTR_APP_KEY) String appKey,
			@ApiParam(value = "密钥标识") @PathVariable String keyId) {
		return ApiResponse.ok(keyManageService.getKey(appKey, keyId));
	}

	@ApiOperation(value = "停用密钥", notes = "密钥进入停用状态后，相关运算请求将被拒绝（用于密钥轮换过渡期）")
	@PostMapping("{keyId}/disable")
	public ApiResponse<KeyView> disable(
			@RequestAttribute(Constants.ATTR_APP_KEY) String appKey,
			@ApiParam(value = "密钥标识") @PathVariable String keyId) {
		return ApiResponse.ok(keyManageService.disableKey(appKey, keyId));
	}

	@ApiOperation(value = "启用密钥", notes = "恢复密钥可用状态")
	@PostMapping("{keyId}/enable")
	public ApiResponse<KeyView> enable(
			@RequestAttribute(Constants.ATTR_APP_KEY) String appKey,
			@ApiParam(value = "密钥标识") @PathVariable String keyId) {
		return ApiResponse.ok(keyManageService.enableKey(appKey, keyId));
	}

	@ApiOperation(value = "销毁密钥", notes = "抹除密钥材料并删除记录，不可恢复，请谨慎操作")
	@DeleteMapping("{keyId}")
	public ApiResponse<Void> destroy(
			@RequestAttribute(Constants.ATTR_APP_KEY) String appKey,
			@ApiParam(value = "密钥标识") @PathVariable String keyId) {
		keyManageService.destroyKey(appKey, keyId);
		return ApiResponse.ok();
	}
}

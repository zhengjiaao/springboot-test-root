package com.zja.controller;

import com.zja.common.ApiResponse;
import com.zja.dto.request.AppRegisterRequest;
import com.zja.dto.response.AppRegisterResponse;
import com.zja.entity.AppEntity;
import com.zja.service.AppManageService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 三方应用管理（管理端接口，生产环境应增加管理员权限控制）
 *
 * @author: zhengja
 */
@RestController
@RequestMapping(value = "/api/app")
@Api(tags = {"三方应用管理"})
public class AppController {

	private final AppManageService appManageService;

	public AppController(AppManageService appManageService) {
		this.appManageService = appManageService;
	}

	@ApiOperation(value = "注册三方应用", notes = "获取appKey/appSecret接入凭证，appSecret仅此一次返回；随后即可使用在线SDK或HTTP接口接入")
	@PostMapping("register")
	public ApiResponse<AppRegisterResponse> register(@RequestBody AppRegisterRequest request) {
		return ApiResponse.ok(appManageService.register(request));
	}

	@ApiOperation(value = "应用列表", notes = "查询平台已注册的三方应用（不含appSecret）")
	@GetMapping("list")
	public ApiResponse<List<AppEntity>> list() {
		return ApiResponse.ok(appManageService.listApps());
	}

	@ApiOperation(value = "应用详情", notes = "查询单个应用信息（不含appSecret）")
	@GetMapping("{appKey}")
	public ApiResponse<AppEntity> detail(@ApiParam(value = "应用接入标识") @PathVariable String appKey) {
		return ApiResponse.ok(appManageService.getApp(appKey));
	}

	@ApiOperation(value = "停用应用", notes = "停用后该应用的所有在线接入请求将被拒绝")
	@PostMapping("{appKey}/disable")
	public ApiResponse<AppEntity> disable(@ApiParam(value = "应用接入标识") @PathVariable String appKey) {
		return ApiResponse.ok(appManageService.disable(appKey));
	}

	@ApiOperation(value = "启用应用", notes = "恢复应用的在线接入能力")
	@PostMapping("{appKey}/enable")
	public ApiResponse<AppEntity> enable(@ApiParam(value = "应用接入标识") @PathVariable String appKey) {
		return ApiResponse.ok(appManageService.enable(appKey));
	}
}

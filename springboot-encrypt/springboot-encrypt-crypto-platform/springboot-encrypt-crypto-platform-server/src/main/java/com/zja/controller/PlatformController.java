package com.zja.controller;

import com.zja.common.ApiResponse;
import com.zja.common.enums.AlgorithmType;
import com.zja.dto.response.AlgorithmView;
import com.zja.dto.response.OverviewResponse;
import com.zja.security.ReplayProtector;
import com.zja.service.AuditService;
import com.zja.store.PlatformStore;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.List;

/**
 * 平台概览（控制台首页/运维观测，管理端接口，演示环境不拦截）
 *
 * <p>仅暴露统计数量与运行参数，不包含应用凭证、密钥材料等敏感数据。</p>
 *
 * @author: zhengja
 */
@RestController
@RequestMapping(value = "/api/platform")
@Api(tags = {"平台概览"})
public class PlatformController {

	private final PlatformStore platformStore;
	private final ReplayProtector replayProtector;
	private final AuditService auditService;
	private final long authTimestampWindow;
	private final long maxBodySize;

	public PlatformController(PlatformStore platformStore,
	                          ReplayProtector replayProtector,
	                          AuditService auditService,
	                          @Value("${crypto.platform.auth-timestamp-window:5}") long authTimestampWindow,
	                          @Value("${crypto.platform.max-body-size:1048576}") long maxBodySize) {
		this.platformStore = platformStore;
		this.replayProtector = replayProtector;
		this.auditService = auditService;
		this.authTimestampWindow = authTimestampWindow;
		this.maxBodySize = maxBodySize;
	}

	@ApiOperation(value = "平台概览", notes = "应用/密钥/审计统计、防重放缓存水位、运行参数与算法矩阵；供控制台渲染首页统计卡片")
	@GetMapping("overview")
	public ApiResponse<OverviewResponse> overview() {
		OverviewResponse overview = new OverviewResponse();
		overview.setAppCount(platformStore.appCount());
		overview.setEnabledAppCount(platformStore.enabledAppCount());
		overview.setKeyCount(platformStore.keyCount());
		overview.setAuditLogCount(auditService.count());
		overview.setAuditLogCapacity(auditService.capacity());
		overview.setNonceCacheSize(replayProtector.size());
		overview.setNonceCacheCapacity(replayProtector.capacity());
		overview.setAuthTimestampWindowMinutes(authTimestampWindow);
		overview.setMaxBodySize(maxBodySize);
		overview.setAlgorithms(buildAlgorithmViews());
		return ApiResponse.ok(overview);
	}

	/** 算法矩阵来自唯一的算法枚举定义，避免前端硬编码算法清单 */
	private List<AlgorithmView> buildAlgorithmViews() {
		List<AlgorithmView> views = new ArrayList<>();
		for (AlgorithmType type : AlgorithmType.values()) {
			AlgorithmView view = new AlgorithmView();
			view.setName(type.name());
			view.setCategory(type.getCategory().name());
			view.setKeyCreatable(type.isKeyCreatable());
			view.setDescription(type.getDescription());
			views.add(view);
		}
		return views;
	}
}

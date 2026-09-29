package com.zja.controller;

import com.zja.common.ApiResponse;
import com.zja.entity.AuditLogEntity;
import com.zja.service.AuditService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * 审计日志查询（密评要求：密码运算全程可审计）
 *
 * @author: zhengja
 */
@RestController
@RequestMapping(value = "/api/audit")
@Api(tags = {"审计日志"})
public class AuditController {

	private final AuditService auditService;

	public AuditController(AuditService auditService) {
		this.auditService = auditService;
	}

	@ApiOperation(value = "审计日志查询", notes = "按应用过滤，最新在前；记录密钥操作与密码运算的操作类型、成败、耗时、来源IP")
	@GetMapping("list")
	public ApiResponse<List<AuditLogEntity>> list(
			@ApiParam(value = "按应用过滤（可选）") @RequestParam(required = false) String appKey,
			@ApiParam(value = "返回条数上限，默认100") @RequestParam(required = false, defaultValue = "100") Integer limit) {
		return ApiResponse.ok(auditService.list(appKey, limit == null ? 100 : limit));
	}
}

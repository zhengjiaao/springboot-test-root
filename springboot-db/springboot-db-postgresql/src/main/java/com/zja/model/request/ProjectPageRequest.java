package com.zja.model.request;

import com.zja.model.base.BasePageRequest;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.Setter;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDateTime;
import java.util.Date;

/**
 * 分页请求参数 分页参数
 *
 * @author: zhengja
 * @since: 2024/09/27 9:34
 */
@Getter
@Setter
@ApiModel("Project 分页参数")
public class ProjectPageRequest extends BasePageRequest {
    @ApiModelProperty("名称")
    private String name;

    @ApiModelProperty("类型")
    private String type;

    @ApiModelProperty("创建时间-开始时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 2025-05-01 00:00:00
    // @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME)  // 2025-05-01T00:00:00
    private LocalDateTime createTimeStart; // 2025-05-01 00:00:00
    @ApiModelProperty("创建时间-结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 2025-06-01 00:00:00
    private LocalDateTime createTimeEnt; // 2025-06-01 00:00:00

    @ApiModelProperty("批复时间-开始时间")
    // @DateTimeFormat(pattern = "yyyy-MM-dd") // 2025-05-01
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 2025-05-9 00:00:00
    private Date approvalTimeStart;
    @ApiModelProperty("批复时间-结束时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") // 2025-06-01 00:00:00
    private Date approvalTimeEnt;
}
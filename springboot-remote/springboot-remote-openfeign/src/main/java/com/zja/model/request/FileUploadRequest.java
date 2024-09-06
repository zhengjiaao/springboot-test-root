package com.zja.model.request;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * 文档上传时参数
 * @Author: zhengja
 * @Date: 2024-09-05 13:07
 */
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
@ApiModel("FileUploadRequest 文件上传信息")
public class FileUploadRequest implements Serializable {

    @NotNull
    @ApiModelProperty("必传项，上传文件")
    private MultipartFile file;
    // private MultipartFile[] files;

    @NotNull
    @ApiModelProperty(value = "文件名称")
    private String filename;
}

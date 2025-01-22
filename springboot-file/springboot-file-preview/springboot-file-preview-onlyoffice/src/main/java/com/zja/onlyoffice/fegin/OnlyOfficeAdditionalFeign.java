package com.zja.onlyoffice.fegin;

import com.zja.onlyoffice.fegin.model.CommandArgs;
import com.zja.onlyoffice.fegin.model.ConversionArgs;
import com.zja.onlyoffice.fegin.model.DocBuilderArgs;
import io.swagger.annotations.ApiOperation;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

/**
 * onlyoffice  附加 API
 * <p>
 * 参考：转换API、命令API、文档生成器API
 * <a href="https://api.onlyoffice.com/docs/docs-api/additional-api/">...</a>
 * </p>
 *
 * @Author: zhengja
 * @Date: 2025-01-17 14:05
 */
public interface OnlyOfficeAdditionalFeign {

    @PostMapping(value = "/converter", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ApiOperation(value = "转换", notes = "文档转换API")
    Object converter(ConversionArgs args);

    // 以下API不做测试

    @PostMapping(value = "/command")
    @ApiOperation(value = "命令服务", notes = "命令服务API")
    Object command(CommandArgs args);

    @PostMapping(value = "/docbuilder")
    @ApiOperation(value = "文档构建", notes = "文档生成器API")
    Object docBuilder(DocBuilderArgs args);
}

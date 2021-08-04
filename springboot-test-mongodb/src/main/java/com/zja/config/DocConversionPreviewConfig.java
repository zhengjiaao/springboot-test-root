package com.zja.config;

import com.dist.zja.aspose.AsposeConvertToPDF;
import com.zja.utils.DocConversionPreviewUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-08-02 11:12
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：
 */
@Configuration
public class DocConversionPreviewConfig {

    @Autowired
    private AsposeConvertToPDF asposeConvertToPDF;

    @Bean
    public DocConversionPreviewUtil docConversionPreviewUtil() {
        return new DocConversionPreviewUtil(ConfigConstants.file.proxyBaseURL(), ConfigConstants.file.localStoragePath, asposeConvertToPDF);
    }
}

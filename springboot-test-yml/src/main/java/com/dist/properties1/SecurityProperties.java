package com.dist.properties1;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Data
//将该类注入到spring容器
@Component
//指定配置文件中属性的前缀为nrsc.security是可以传递到该类
@ConfigurationProperties(prefix = "nrsc.security")
public class SecurityProperties {
    private BrowserProperties browser = new BrowserProperties();
    private AppProperties app = new AppProperties();
}

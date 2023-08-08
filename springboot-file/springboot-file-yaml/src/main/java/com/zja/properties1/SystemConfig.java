/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-08 13:15
 * @Since:
 */
package com.zja.properties1;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/08/08 13:15
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "system")
public class SystemConfig {

    private String name;
    private User user;

    @Data
    private static class User {
        private String username;
        private String password;
        private List<RoleObject> roleObjectList;
        private List<String> authorityList;
        private String[] friendArray;
    }

    @Data
    private static class RoleObject {
        private String roleId;
        private String roleName;
    }

}

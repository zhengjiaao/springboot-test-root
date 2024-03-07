/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-08-08 10:01
 * @Since:
 */
package com.zja.pool.druid.config;

import com.alibaba.druid.filter.config.ConfigTools;
import com.alibaba.druid.util.DruidPasswordCallback;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;

import java.util.Properties;

/**
 * @author: zhengja
 * @since: 2023/08/08 10:01
 */
@Slf4j
public class DruidDBPasswordCallback extends DruidPasswordCallback {

    // 私钥
    private static final String PRIVATE_KEY = "MIIBUwIBADANBgkqhkiG9w0BAQEFAASCAT0wggE5AgEAAkEAgDdRqM8hvgSU0/1shdzds3gnfH8g5mp+4Vt1gpgNIVQy1vrJss+ZNdHReHbGRX3zZ0b9mmtU/deAhawbfWWxCwIDAQABAkA11IPhRs1Y1N2TPzPf48Hkxo51c35hntaUUOy+Ho5srib22IoNpHKkbTbNdQAUaDveu2nciFXxQ3vsuvsTzGupAiEA/ssIqdjnwI7YKJ7qvtbFFpFct3/EK2kCbLrMxJquBO8CIQCA0su8qy9+bq/xFHY76aTUGXWrnuhhEKCqgZhJUomtpQIgGpqilfGDCVUg9uTZCAIu5BNbhgF+PzYgva+nj+PCxdsCIGhSzR04AFlCKAdyy6EPQCVnjMxz/roEbihGlN3kEozlAiBIfLrQjtZ0XyYP8WoUPbAJ15dJMRw7OUjw8uUqI6ZyMA==";
    // 公钥
    private static final String PUBLIC_KEY = "MFwwDQYJKoZIhvcNAQEBBQADSwAwSAJBAIA3UajPIb4ElNP9bIXc3bN4J3x/IOZqfuFbdYKYDSFUMtb6ybLPmTXR0Xh2xkV982dG/ZprVP3XgIWsG31lsQsCAwEAAQ==";
    // 私钥加密后的密码
    private static final String PASS = "L5HAwCsRs0vhoJbvq/ZYTUJDIQaGkS3UZA35IN9ctHcc0OndVaiXiPueXU7L6UaxpSUwVPrP+hVfMcBrN0Tc6Q==";

    @Override
    public void setProperties(Properties properties) {
        super.setProperties(properties);
        // 获取配置文件中的已经加密的密码，和yaml文件中的connectionProperties属性配置相关
        String pwd = (String) properties.get("dbPassword");
        System.out.println("DruidDBPasswordCallback 数据库加密后的密码=" + pwd);
        if (!StringUtils.isEmpty(pwd)) {
            try {
                // 密码解密，并设置
                String password = ConfigTools.decrypt(PUBLIC_KEY, pwd);
                System.out.println("DruidDBPasswordCallback 数据库解密后的密码：" + password);
                setPassword(password.toCharArray());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }
}

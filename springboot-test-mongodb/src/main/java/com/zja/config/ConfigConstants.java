package com.zja.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2021-06-24 14:33
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：常量配置
 */
public class ConfigConstants {

    @Component
    public static class file {
        //文件代理地址
        public static String proxyAddress;
        //文件代理路径
        public static String proxyPath;
        //文件本地存储路径
        public static String localStoragePath;

        @Value("${file.proxyAddress}")
        public void setProxyAddress(String proxyAddress) {
            if (null == proxyAddress) {
                throw new RuntimeException("必须配置 file.proxyAddress:");
            }
            file.proxyAddress = proxyAddress;
        }

        @Value("${file.proxyPath}")
        public void setProxyPath(String proxyPath) {
            if (null == proxyPath) {
                throw new RuntimeException("必须配置 file.proxyPath:");
            }
            file.proxyPath = proxyPath;
        }

        @Value("${file.localStoragePath}")
        public void setLocalStoragePath(String localStoragePath) {
            if (null == localStoragePath) {
                throw new RuntimeException("必须配置 file.localStoragePath:");
            }
            file.localStoragePath = localStoragePath;
        }

        /**
         * 代理地址全路径
         * @return http://IP:8080/dgp-web/public/file
         */
        public static String proxyBaseURL() {
            return file.proxyAddress + file.proxyPath;
        }
    }

}

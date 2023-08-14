package com.zja.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-07-17 9:50
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc： windwos环境启动项目自动打开指定页面(swagger-ui.html)
 */
@Component
public class StepExecutorConfig implements ApplicationRunner {

    private static final Logger LOG = LoggerFactory.getLogger(StepExecutorConfig.class);

    @Value("${spring.web.loginurl}")
    private String loginUrl;

    @Value("${spring.web.googleexcute}")
    private String googleExcutePath;

    @Value("${spring.web.isopenurl}")
    private boolean isOpen;

    @Override
    public void run(ApplicationArguments args) throws Exception{
        //先判断当前环境
        if (!exitisVersion()){
            //启动swagger-ui
            application();
        }
    }

    private Boolean exitisVersion() {
        String osName = System.getProperties().getProperty("os.name");
        if("Linux".equals(osName)) {
            LOG.info("running in Linux");
            return true;
        }
        else{
            LOG.info("don't running in Linux");
            return false;
        }
    }

    private void application() {

        if (isOpen){
            String cmd = googleExcutePath +" "+ loginUrl;
            LOG.info("浏览地址：" + cmd);
            Runtime run = Runtime.getRuntime();
            try{
                run.exec(cmd);
                LOG.info("启动浏览器打开项目成功");
            }catch (Exception e){
                e.printStackTrace();
                LOG.error(e.getMessage());
            }
        }
    }
}

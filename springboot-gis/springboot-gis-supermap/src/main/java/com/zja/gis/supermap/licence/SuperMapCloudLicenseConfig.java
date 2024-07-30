package com.zja.gis.supermap.licence;

import com.supermap.data.Workspace;
import com.supermap.data.bslicense.BSLicense;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

import javax.annotation.PostConstruct;
import java.util.List;

/**
 * 超图 云许可配置
 *
 * @Author: zhengja
 * @Date: 2024-07-17 15:10
 */
@Configuration
public class SuperMapCloudLicenseConfig {

    private static final Logger log = LoggerFactory.getLogger(SuperMapCloudLicenseConfig.class);

    @Value("${supermap.cloud-license.enabled:true}")
    private boolean licenceEnabled;

    @Value("${supermap.cloud-license.ip}")
    private String licenceIp; // 移动到配置文件中

    @Value("${supermap.cloud-license.port}")
    private int licencePort; // 移动到配置文件中

    @Value("#{'${supermap.cloud-license.module:65400}'.split(',')}")
    private List<String> module;

    @PostConstruct
    public void validateSuperMapLicence() {
        if (!licenceEnabled) {
            log.info("SuperMap 云许可未启用，已跳过云许可验证.");
            return;
        }

        if (licenceIp == null || licenceIp.isEmpty()) {
            log.error("SuperMap  云许可校验失败，licenceIp 不能为空.");
            return; // 在捕获异常后返回，避免继续执行
        }

        log.info("SuperMap 开始获取云许可... ...");
        log.info("licenceIp:{}", licenceIp);
        log.info("licencePort:{}", licencePort);
        log.info("module:{}", module);
        boolean login;

        try {
            int[] arrayOfInt = module.stream().mapToInt(Integer::valueOf).toArray();
            login = BSLicense.login(licenceIp, licencePort, arrayOfInt);
            log.info("SuperMap 获取云许可状态为：{}", login ? "成功" : "失败");
            if (!login) {
                log.error("SuperMap 获取云许可失败，请检查配置信息是否正确.");
                return;
            }
        } catch (Exception ex) {
            log.error("SuperMap 云许可校验失败.", ex);
            return; // 在捕获异常后返回，避免继续执行
        }

        // 验证 SuperMap iObjects Java 组件环境(前提，先获取到本地或者web云许可)
        try {
            log.info("SuperMap 开始验证本地开发环境 SuperMap iObjects Java 组件... ...");
            Workspace workspace = new Workspace();
            workspace.dispose(); // 释放资源
            log.info("SuperMap 本地开发环境 SuperMap iObjects Java 组件验证成功.");
        } catch (Exception | Error ex) {
            log.error("SuperMap 本地开发环境 SuperMap iObjects Java 组件验证失败.\n" +
                            " 问题排查思路：\n" +
                            " 1.请安装 SuperMap iObjects Java 组件，并配置系统环境变量，且重启主机后再试.\n" +
                            " 2.请确认 SuperMap iObjects Java 组件的版本，云许可是否支持此组件版本.\n" +
                            " 3.请排查 com.supermap.* 依赖项是否缺失，且依赖项版本要与SuperMap iObjects Java 组件的版本一致.\n",
                    ex);
        }
    }
}

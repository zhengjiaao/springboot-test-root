package com.zja.hadoop.yarn;

import org.apache.hadoop.conf.Configuration;
import org.apache.hadoop.fs.Path;
import org.apache.hadoop.yarn.api.ApplicationConstants;
import org.apache.hadoop.yarn.api.protocolrecords.GetNewApplicationResponse;
import org.apache.hadoop.yarn.api.records.*;
import org.apache.hadoop.yarn.client.api.YarnClient;
import org.apache.hadoop.yarn.client.api.YarnClientApplication;
import org.apache.hadoop.yarn.conf.YarnConfiguration;
import org.apache.hadoop.yarn.util.ConverterUtils;
import org.apache.hadoop.yarn.util.Records;
import org.junit.jupiter.api.Test;

import java.util.Collections;

/**
 * @author: zhengja
 * @since: 2024/02/01 16:06
 */
public class YarnExample {

    @Test
    public void test() {
        Configuration configuration = new YarnConfiguration();

        try (YarnClient yarnClient = YarnClient.createYarnClient()) {
            yarnClient.init(configuration);
            yarnClient.start();

            YarnClientApplication app = yarnClient.createApplication();
            GetNewApplicationResponse appResponse = app.getNewApplicationResponse();

            ApplicationSubmissionContext appContext = app.getApplicationSubmissionContext();
            ApplicationId appId = appContext.getApplicationId();

            // 设置应用程序名称
            appContext.setApplicationName("YarnExample");

            // 设置资源
            Resource resource = Records.newRecord(Resource.class);
            resource.setMemorySize(1024); // 设置内存资源，单位为MB
            resource.setVirtualCores(1); // 设置虚拟核心资源

            // 设置应用程序的启动命令
            ContainerLaunchContext amContainer = Records.newRecord(ContainerLaunchContext.class);
            amContainer.setCommands(Collections.singletonList("java -jar /path/to/application.jar")); // 设置启动命令

            // 设置本地资源
            LocalResource appJar = Records.newRecord(LocalResource.class);
            appJar.setResource(URL.fromPath(new Path("/path/to/application.jar"))); // 设置应用程序JAR文件的路径
            appJar.setSize(-1);
            appJar.setTimestamp(System.currentTimeMillis());
            appJar.setType(LocalResourceType.FILE);
            appJar.setVisibility(LocalResourceVisibility.APPLICATION);
            amContainer.setLocalResources(Collections.singletonMap("application.jar", appJar)); // 添加本地资源

            // 设置环境变量
            amContainer.setEnvironment(Collections.singletonMap(
                    ApplicationConstants.Environment.CLASSPATH.name(),
                    ApplicationConstants.Environment.CLASSPATH.$$() + ":/path/to/dependencies/*"));

            // 设置应用程序的启动上下文
            appContext.setAMContainerSpec(amContainer);

            // 提交应用程序到YARN集群
            yarnClient.submitApplication(appContext);

            // 监控应用程序状态
            while (true) {
                ApplicationReport report = yarnClient.getApplicationReport(appId);
                YarnApplicationState state = report.getYarnApplicationState();
                if (state == YarnApplicationState.FINISHED || state == YarnApplicationState.KILLED
                        || state == YarnApplicationState.FAILED) {
                    break;
                }
                Thread.sleep(1000);
            }

            // 关闭YARN客户端连接
            yarnClient.stop();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}

package com.zja.log.skywalking;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;
import org.springframework.boot.web.servlet.support.SpringBootServletInitializer;

/**
 * SkyWalking 分布式链路追踪示例
 * <p>
 * 说明：SkyWalking 是一个 APM（应用性能监控）系统，提供：
 * - 分布式链路追踪（Trace）
 * - 服务拓扑图
 * - 性能指标分析
 * - 日志关联（通过 traceId）
 * <p>
 * SkyWalking 通过 Java Agent（无侵入）方式工作，
 * 本模块通过 toolkit 提供手动埋点和日志 traceId 集成。
 * </p>
 * <p>
 * 启动命令（需要 SkyWalking Agent）：
 * java -javaagent:skywalking-agent.jar
 *      -Dskywalking.agent.service_name=springboot-log-system-skywalking
 *      -Dskywalking.collector.backend_service=127.0.0.1:11800
 *      -jar springboot-log-system-skywalking.jar
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 15:48
 */
@SpringBootApplication
public class SkyWalkingApplication extends SpringBootServletInitializer {

    public static void main(String[] args) {
        SpringApplication.run(SkyWalkingApplication.class, args);
    }

    @Override
    protected SpringApplicationBuilder configure(SpringApplicationBuilder application) {
        return application.sources(SkyWalkingApplication.class);
    }
}

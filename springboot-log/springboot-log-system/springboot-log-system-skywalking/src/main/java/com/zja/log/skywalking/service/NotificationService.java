package com.zja.log.skywalking.service;

import lombok.extern.slf4j.Slf4j;
import org.apache.skywalking.apm.toolkit.trace.ActiveSpan;
import org.apache.skywalking.apm.toolkit.trace.Tag;
import org.apache.skywalking.apm.toolkit.trace.Trace;
import org.apache.skywalking.apm.toolkit.trace.TraceContext;
import org.springframework.stereotype.Service;

/**
 * 通知服务：演示 SkyWalking 手动埋点
 * <p>
 * SkyWalking 自动追踪 HTTP、数据库、RPC 等调用，
 * 对于自定义业务方法，可通过 @Trace 注解 + TraceContext API 手动埋点。
 * </p>
 *
 * @author: zhengja
 * @since: 2024/01/23 16:00
 */
@Slf4j
@Service
public class NotificationService {

    /**
     * 发送通知（手动追踪）
     * <p>
     * @Trace 注解将此方法加入链路追踪
     * @Tag 注解将方法参数记录到 Span 中
     * </p>
     */
    @Trace(operationName = "NotificationService.sendNotification")
    @Tag(key = "notifyType", value = "arg[0]")
    @Tag(key = "target", value = "arg[1]")
    public boolean sendNotification(String type, String target, String content) {
        // 获取当前 traceId
        String traceId = TraceContext.traceId();
        log.info("发送通知, traceId={}, type={}, target={}", traceId, type, target);

        try {
            // 模拟不同通知渠道
            switch (type) {
                case "email":
                    sendEmail(target, content);
                    break;
                case "sms":
                    sendSms(target, content);
                    break;
                case "push":
                    sendPush(target, content);
                    break;
                default:
                    log.warn("未知通知类型, type={}", type);
                    // 在当前 Span 中添加错误标记
                    ActiveSpan.error(new IllegalArgumentException("未知通知类型: " + type));
                    return false;
            }

            log.info("通知发送成功, type={}, target={}", type, target);
            return true;
        } catch (Exception e) {
            log.error("通知发送失败, type={}, target={}", type, target, e);
            // 记录错误到 SkyWalking
            ActiveSpan.error(e);
            return false;
        }
    }

    @Trace(operationName = "NotificationService.sendEmail")
    private void sendEmail(String email, String content) {
        log.info("发送邮件, email={}, content长度={}", email, content.length());
        // 在 Span 上添加自定义标签
        ActiveSpan.tag("email.to", email);
        // 模拟耗时
        sleep(80);
        log.info("邮件发送完成, email={}", email);
    }

    @Trace(operationName = "NotificationService.sendSms")
    private void sendSms(String phone, String content) {
        log.info("发送短信, phone={}", phone);
        ActiveSpan.tag("sms.phone", phone);
        sleep(50);
        log.info("短信发送完成, phone={}", phone);
    }

    @Trace(operationName = "NotificationService.sendPush")
    private void sendPush(String userId, String content) {
        log.info("发送推送, userId={}", userId);
        ActiveSpan.tag("push.userId", userId);
        sleep(30);
        log.info("推送发送完成, userId={}", userId);
    }

    private void sleep(long millis) {
        try {
            Thread.sleep(millis);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}

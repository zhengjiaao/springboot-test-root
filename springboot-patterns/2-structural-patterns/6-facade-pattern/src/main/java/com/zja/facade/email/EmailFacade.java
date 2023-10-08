/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 17:43
 * @Since:
 */
package com.zja.facade.email;

import com.zja.facade.email.service.EmailContentBuilder;
import com.zja.facade.email.service.EmailSender;
import com.zja.facade.email.service.EmailValidator;

/**
 * @author: zhengja
 * @since: 2023/10/08 17:43
 */
public class EmailFacade {
    private EmailValidator emailValidator;
    private EmailContentBuilder contentBuilder;
    private EmailSender emailSender;

    public EmailFacade() {
        emailValidator = new EmailValidator();
        contentBuilder = new EmailContentBuilder();
        emailSender = new EmailSender();
    }

    public void sendEmail(String recipient, String subject, String message) {
        if (emailValidator.validateEmail(recipient)) {
            String emailContent = contentBuilder.buildEmailContent(subject, message);
            emailSender.sendEmail(recipient, emailContent);
            System.out.println("Email sent successfully.");
        } else {
            System.out.println("Invalid email address.");
        }
    }
}

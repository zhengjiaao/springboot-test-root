/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-10-08 17:44
 * @Since:
 */
package com.zja.facade.email;

/**
 * @author: zhengja
 * @since: 2023/10/08 17:44
 */
public class Client {
    // 客户端只需要与外观类 EmailFacade 进行交互，而不需要知道发送邮件的具体步骤和子系统的实现细节。
    // 外观类封装了邮箱地址验证、邮件内容构建和邮件发送等复杂操作，使得客户端代码更加简洁和易于理解。
    public static void main(String[] args) {
        EmailFacade emailFacade = new EmailFacade();
        emailFacade.sendEmail("recipient@example.com", "Hello", "This is a test email.");

        //输出结果：
        //Email sent successfully.
    }
}

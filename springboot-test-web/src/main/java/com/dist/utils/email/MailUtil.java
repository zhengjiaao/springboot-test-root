package com.dist.utils.email;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.FileSystemResource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Component;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.File;
import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/10/14 17:30
 */
@Component
public class MailUtil {

    final Logger logger= LoggerFactory.getLogger(this.getClass());

    @Value("${spring.mail.username}")
    private String from;

    @Autowired
    private JavaMailSender javaMailSender;

    public void mailTest(){
        System.out.println("进入：MailUtil");
    }

    /**
     * 文本发送 邮件
     * @param to  发送给谁
     * @param subject 主题
     * @param content 内容
     */
    public void sendTxtEmail(String to,String subject,String content){
        SimpleMailMessage message =new SimpleMailMessage();
        // 邮件收件人
        message.setTo(to);
        // 邮件标题
        message.setSubject(subject);
        // 邮件正文
        message.setText(content);
        // 邮件发送人
        message.setFrom(from);

        javaMailSender.send(message);
    }

    /**
     * Html发送 邮件
     * @param to  发送给谁
     * @param subject 主题
     * @param content 内容
     */
    public void sendHtmlEmail(String to,String subject,String content){
        logger.info("发送静态邮件开始：{},{},{}",to,subject,content);
        MimeMessage mineMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = null;
        try {
            mimeMessageHelper = new MimeMessageHelper(mineMessage, true);
            // 邮件发送人
            mimeMessageHelper.setFrom(from);
            // 邮件收件人
            mimeMessageHelper.setTo(to);
            // 邮件标题
            mimeMessageHelper.setSubject(subject);
            // 邮件正文
            mimeMessageHelper.setText(content, true);//"<span style='color:red'>" + LocalDateTime.now() + "</span>"

            // 调用 api, 发送邮件
            javaMailSender.send(mineMessage);
            logger.info("发送静态邮件成功!");
        } catch (MessagingException e) {
            logger.error("发送静态邮件失败: ",e);
        }



    }

    /**
     * 附件发送 邮件
     * @param to  发送给谁
     * @param subject 主题
     * @param content 内容
     * @param filePaths 附件路径列表
     */
    public void sendAnnexEmail(String to,String subject,String content,List<String> filePaths)throws MessagingException{
        MimeMessage mineMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mineMessage, true);

        // 邮件发送人
        mimeMessageHelper.setFrom(from);

        // 邮件收件人
        mimeMessageHelper.setTo(to);
        // 邮件标题
        mimeMessageHelper.setSubject(subject);
        // 邮件正文
        mimeMessageHelper.setText(content, true);


        // 添加多个或重复 添加附件
        for (String filePath : filePaths){
            FileSystemResource file = new FileSystemResource(new File(filePath));
            if (file.exists()){
                String fileName = file.getFilename();
                mimeMessageHelper.addAttachment(fileName, file);
            }else {
                System.out.println("不存在的文件路径："+filePath);
            }
        }

        // 调用 api, 发送邮件
        javaMailSender.send(mineMessage);

    }

    /**
     * 图片发送 邮件
     * @param to  发送给谁
     * @param subject 主题
     * @param content 内容
     * @param rscPath 图片路径
     * @param rscId 图片id
     */
    public void sendImgEmail(String to,String subject,String content,String rscPath,String rscId)throws MessagingException{
        MimeMessage mineMessage = javaMailSender.createMimeMessage();
        MimeMessageHelper mimeMessageHelper = new MimeMessageHelper(mineMessage, true);

        // 邮件发送人
        mimeMessageHelper.setFrom(from);

        // 邮件收件人
        mimeMessageHelper.setTo(to);
        // 邮件标题
        mimeMessageHelper.setSubject(subject);
        // 邮件正文
        mimeMessageHelper.setText(content, true);

        // 添加附件
        FileSystemResource resource = new FileSystemResource(new File(rscPath));
        mimeMessageHelper.addInline(rscId, resource);

        // 调用 api, 发送邮件
        javaMailSender.send(mineMessage);
    }
}

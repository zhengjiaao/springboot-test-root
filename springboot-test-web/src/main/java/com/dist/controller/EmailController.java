package com.dist.controller;

import com.dist.utils.email.MailUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/10/16 11:41
 */
@RequestMapping(value = "rest/mail")
@RestController
@Api(tags = {"EmailController"}, description = "springboot 邮件发送")
public class EmailController {

    @Autowired
    MailUtil mailUtil;

    @Resource
    TemplateEngine templateEngine;

    @ApiOperation(value = "test-mailUtil", httpMethod = "GET")
    @RequestMapping(value = "v1/test", method = RequestMethod.GET)
    public void contextLoads() {
        mailUtil.mailTest();
    }

    @ApiOperation(value = "发送文本邮件", httpMethod = "GET")
    @RequestMapping(value = "v1/send/txt", method = RequestMethod.GET)
    public void sendTxtEmail(){
        String content = "文本邮件内容：小神喜欢小金鱼";
        mailUtil.sendTxtEmail("1263598336@qq.com","文本邮件发送",content);
    }

    @ApiOperation(value = "发送html邮件", httpMethod = "GET")
    @RequestMapping(value = "v1/send/html", method = RequestMethod.GET)
    public void sendHtmlEmail(){
        String content = "<html>\n" +
                "<body>\n" +
                "<h3> 这是一封html邮件 </h3>" +
                "</body>\n"+
                "</html>";
        // Html发送
        mailUtil.sendHtmlEmail("1263598336@qq.com","html邮件发送",content);
    }


    @ApiOperation(value = "附件邮件发送", httpMethod = "GET")
    @RequestMapping(value = "v1/send/annexEmail", method = RequestMethod.GET)
    public void sendAnnexEmail() throws MessagingException {
        String content = "这是一封带有附件的邮件";
        String filePath = "D:\\FileTest\\txt\\邮件发送文件测试.txt";
        List<String> filePaths = new ArrayList<>();
        //添加文件路径列表
        filePaths.add(filePath);
        filePaths.add(filePath);

        // 附件发送
        mailUtil.sendAnnexEmail("1263598336@qq.com","附件邮件发送",content,filePaths);
    }


    @ApiOperation(value = "发送带图片邮件", httpMethod = "GET")
    @RequestMapping(value = "v1/send/imgEmail", method = RequestMethod.GET)
    public void sendImgEmail() throws MessagingException {
        String imgPath ="D:\\FileTest\\img\\头像.jpg";
        //src 不能有中文字符，不然不显示图片
        String srcId = "001";
        String content = "<html><body>这是有图片的邮件:<img src=\'cid:"+srcId+"\'></img></body></html>";

        // 附件发送
        mailUtil.sendImgEmail("1263598336@qq.com","这是有图片的邮件",content,imgPath,srcId);
    }

    @ApiOperation(value = "发送html模板邮件", httpMethod = "GET")
    @RequestMapping(value = "v1/send/templateEmail", method = RequestMethod.GET)
    public void sendHtmlEmail2() {
        //org.thymeleaf.context
        Context context = new Context();
        context.setVariable("id","9efaff9e9437");

        String emailContent = templateEngine.process("emailTemplate",context);
        mailUtil.sendHtmlEmail("1263598336@qq.com","这是一个模板邮件",emailContent);
    }

}

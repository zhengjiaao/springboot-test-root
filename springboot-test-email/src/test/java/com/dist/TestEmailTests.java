package com.dist;

import com.dist.service.MailService;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import javax.annotation.Resource;
import javax.mail.MessagingException;
import java.util.ArrayList;
import java.util.List;

@RunWith(SpringRunner.class)
@SpringBootTest
public class TestEmailTests {

	@Resource
	MailService mailService;

	@Resource
	TemplateEngine templateEngine;

	@Test
	public void contextLoads() {
		mailService.mailTest();
	}

	@Test
	public void sendTxtEmailTest(){
		String content = "文本邮件内容：小神喜欢小金鱼";
		mailService.sendTxtEmail("1263598336@qq.com","文本邮件发送",content);
	}

	@Test
	public void sendHtmlEmailTest() throws MessagingException {
		String content = "<html>\n" +
				"<body>\n" +
				"<h3> 这是一封html邮件 </h3>" +
				"</body>\n"+
				"</html>";
		// Html发送
		mailService.sendHtmlEmail("1263598336@qq.com","html邮件发送",content);
	}


	@Test
	public void sendAnnexEmailTest() throws MessagingException {
		String content = "这是一封带有附件的邮件";
		String filePath = "D:\\FileTest\\txt\\邮件发送文件测试.txt";
		List<String> filePaths = new ArrayList<>();
		//添加文件路径列表
		filePaths.add(filePath);
		filePaths.add(filePath);

		// 附件发送
		mailService.sendAnnexEmail("1263598336@qq.com","附件邮件发送",content,filePaths);
	}

	@Test
	public void sendImgEmailTest() throws MessagingException {
		String imgPath ="D:\\FileTest\\img\\头像.jpg";
		//src 不能有中文字符，不然不显示图片
		String srcId = "001";
		String content = "<html><body>这是有图片的邮件:<img src=\'cid:"+srcId+"\'></img></body></html>";

		// 附件发送
		mailService.sendImgEmail("1263598336@qq.com","这是有图片的邮件",content,imgPath,srcId);
	}

	//测试邮件模板发送
	@Test
	public void testTmeplateMailTest() throws MessagingException {
		//org.thymeleaf.context
		Context context = new Context();
		context.setVariable("id","9efaff9e9437");

		String emailContent = templateEngine.process("emailTemplate",context);
		mailService.sendHtmlEmail("1263598336@qq.com","这是一个模板邮件",emailContent);
	}

}

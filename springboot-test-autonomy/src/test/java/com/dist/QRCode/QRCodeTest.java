package com.dist.QRCode;

import com.dist.base.BaseTest;
import net.glxn.qrgen.core.AbstractQRCode;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.core.vcard.VCard;
import net.glxn.qrgen.javase.QRCode;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;

/**
 * 引入jar：net.glxn.qrgen.javase
 *
 * @program: springbootdemo
 * @Date: 2019/1/25 9:01
 * @Author: Mr.Zheng
 * @Description:
 */
public class QRCodeTest extends BaseTest {

    private final Logger log = LoggerFactory.getLogger(QRCodeTest.class);

    /**
     * 二维码生成测试1
     */
    @Test
    public void testQrcode1() {

        AbstractQRCode qrCode = QRCode.from("https://www.baidu.com");
        // 设置字符集，支持中文
        qrCode.withCharset("utf-8");
        // 设置生成的二维码图片大小
        qrCode.withSize(260,260);
        ByteArrayOutputStream out = qrCode.to(ImageType.PNG).stream();
        File file = new File("D:\\qrCode.png");
        FileOutputStream fout = null;
        try {
            fout = new FileOutputStream(file);
            fout.write(out.toByteArray());
            fout.flush();
            System.out.println("***********二维码生成成功！**********");
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            try {
                fout.close();
                out.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 二维码生成测试2
     */
    @Test
    public void testQrcode2() {
        ByteArrayOutputStream bout =
                QRCode.from("https://www.baidu.com")
                        .withCharset("utf-8")
                        .withSize(250, 250)
                        .to(ImageType.PNG)
                        .stream();

        try {
            OutputStream out = new FileOutputStream("D:\\qr-code.png");
            bout.writeTo(out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模板
     * 二维码生成测试3 :VCard是标准电子商务名片格式，包含元数据有，名称，地址，公司，手机号，职位，邮箱，网站等
     */
    @Test
    public void testQrcode3() {

        VCard vCard = new VCard();
        vCard.setName("郑家骜");
        vCard.setAddress("上海市浦东新区张江镇");
        vCard.setCompany("DIST");
        vCard.setPhoneNumber("15837866351");
        vCard.setTitle("Java开发");
        vCard.setEmail("1263598336@qq.com");
        vCard.setWebsite("https://www.jianshu.com/u/70d69269bd09");

        ByteArrayOutputStream bout =
                QRCode.from(vCard)
                        .withCharset("utf-8")
                        .withSize(250, 250)
                        .to(ImageType.PNG)
                        .stream();

        try {
            OutputStream out = new FileOutputStream("D:\\qr-code-vcard.png");
            bout.writeTo(out);
            out.flush();
            out.close();

        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

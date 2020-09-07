package com.dist.QRCode;

import com.google.zxing.BarcodeFormat;
import com.google.zxing.EncodeHintType;
import com.google.zxing.MultiFormatWriter;
import com.google.zxing.WriterException;
import com.google.zxing.common.BitMatrix;
import net.glxn.qrgen.core.AbstractQRCode;
import net.glxn.qrgen.core.image.ImageType;
import net.glxn.qrgen.core.vcard.VCard;
import net.glxn.qrgen.javase.QRCode;
import org.junit.Test;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.HashMap;
import java.util.Map;

import static com.google.zxing.client.j2se.MatrixToImageConfig.BLACK;
import static com.google.zxing.client.j2se.MatrixToImageConfig.WHITE;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-08-14 11:18
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：jar：net.glxn.qrgen.javase  不推荐
 */
public class QrgenJavaseTest {

    /**
     * 二维码生成 测试 1
     */
    @Test
    public void testQrcode1() {

        AbstractQRCode qrCode = QRCode.from("https://www.baidu.com");
        // 设置字符集，支持中文
        qrCode.withCharset("utf-8");
        // 设置生成的二维码图片大小
        qrCode.withSize(260,260);
        ByteArrayOutputStream out = qrCode.to(ImageType.PNG).stream();
        File file = new File("D:\\qrCode-1.png");
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
     * 二维码生成 测试 2
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
            OutputStream out = new FileOutputStream("D:\\qrCode-2.png");
            bout.writeTo(out);
            out.flush();
            out.close();
            System.out.println("***********二维码生成成功！**********");
        } catch (FileNotFoundException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 模板
     * 二维码生成 测试 3 :VCard是标准电子商务名片格式，包含元数据有，名称，地址，公司，手机号，职位，邮箱，网站等
     */
    @Test
    public void testQrcode3() {

        VCard vCard = new VCard();
        vCard.setName("Zhengja");
        vCard.setAddress("上海市浦东新区张江镇");
        vCard.setCompany("公司名称");
        vCard.setPhoneNumber("15937966356");
        vCard.setTitle("Java开发");
        vCard.setEmail("126354652@qq.com");
        vCard.setWebsite("https://www.jianshu.com/u/70d69269bd09");

        ByteArrayOutputStream bout =
                QRCode.from(vCard)
                        .withCharset("utf-8")
                        .withSize(250, 250)
                        .to(ImageType.PNG)
                        .stream();
        try {
            OutputStream out = new FileOutputStream("D:\\qrCode-3.png");
            bout.writeTo(out);
            out.flush();
            out.close();
            System.out.println("***********二维码生成成功！**********");
        }catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 模板-去除二维码白边
     * 二维码生成 测试 4 :VCard是标准电子商务名片格式，包含元数据有，名称，地址，公司，手机号，职位，邮箱，网站等
     */
    @Test
    public void testQrcode4() {

        VCard vCard = new VCard();
        vCard.setName("Zhengja");
        vCard.setAddress("上海市浦东新区张江镇");
        vCard.setCompany("公司名称");
        vCard.setPhoneNumber("15937966356");
        vCard.setTitle("Java开发");
        vCard.setEmail("126354652@qq.com");
        vCard.setWebsite("https://www.jianshu.com/u/70d69269bd09");

        try {
            Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
            hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
            BitMatrix bitMatrix = new MultiFormatWriter().encode(vCard.toString(),
                    BarcodeFormat.QR_CODE, 250, 250, hints);// 生成矩阵
            if(true){ //删除白边
                BufferedImage image = deleteWhite(bitMatrix); //去白边的话加这一行
                File outputfile = new File("D:\\qrCode-4.png");
                ImageIO.write(image, "png", outputfile);
                System.out.println("***********二维码生成成功！**********");
                return;
            }
        } catch (WriterException | IOException e) {
            e.printStackTrace();
        }
    }

    //去白边的话，调用这个方法
    private static BufferedImage  deleteWhite(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }

        int width = resMatrix.getWidth();
        int height = resMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, resMatrix.get(x, y) ? BLACK
                        : WHITE);
            }
        }
        return image;
    }
}

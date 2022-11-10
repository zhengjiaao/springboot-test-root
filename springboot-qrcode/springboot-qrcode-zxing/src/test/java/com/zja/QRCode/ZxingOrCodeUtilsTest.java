package com.zja.QRCode;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.zja.util.QRcodeZxingUtil;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.google.zxing.client.j2se.MatrixToImageConfig.BLACK;
import static com.google.zxing.client.j2se.MatrixToImageConfig.WHITE;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-08-14 11:21
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：推荐
 */
public class ZxingOrCodeUtilsTest {

    public static void main(String[] args) throws NotFoundException, IOException {

        //二位码 内容
        String content = "西秀区农村土地承包经营权证\n" +
                "权证编码：398881111222211J\n" +
                "发包方名称：轿子山镇大进村民委员会\n" +
                "承包方代表：杨井岗\n" +
                "确权总面积：12.33亩\n" +
                "地块总数: 13块";

        try {
            String fileName = "zxing.png";
            //1、自定义生成二维码
            QRcodeZxingUtil.generateQRcode(content, "D://", fileName, true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }

        //2、简单的生成二维码例子
        //String basepath= URLDecoder.decode("D:\\zxing.png","utf-8");

        //读取二位码 内容
        Result result = QRcodeZxingUtil.readQRcode("D:\\zxing.png");
        System.out.println(result.getText());
    }
}

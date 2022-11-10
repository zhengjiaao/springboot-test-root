/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-11-10 16:56
 * @Since:
 */
package com.zja.util;

import com.google.zxing.*;
import com.google.zxing.client.j2se.BufferedImageLuminanceSource;
import com.google.zxing.client.j2se.MatrixToImageWriter;
import com.google.zxing.common.BitMatrix;
import com.google.zxing.common.HybridBinarizer;
import com.google.zxing.qrcode.QRCodeWriter;
import com.google.zxing.qrcode.decoder.ErrorCorrectionLevel;

import javax.imageio.ImageIO;
import javax.servlet.http.HttpServletResponse;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileSystems;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.Map;

import static com.google.zxing.client.j2se.MatrixToImageConfig.BLACK;
import static com.google.zxing.client.j2se.MatrixToImageConfig.WHITE;

public class QRcodeZxingUtil {

    /**
     * 生成二维码图片
     * @param content 二维码中的数据
     * @param filePath 生成二维码的根路径   fileName文件名
     * @param deleteWhite 默认二维码边上是带有白边的，传true时会去掉白边
     */
    public static String generateQRcode(String content, String filePath, String fileName, boolean deleteWhite) throws WriterException, IOException {

        String fileAbsolutePath = filePath + fileName;

        int width = 200; // 图像宽度
        int height = 200; // 图像高度
        String format = "png";// 图像类型

        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 生成矩阵
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, width, height, hints);
        //删除白边
        if (deleteWhite) {
            BufferedImage image = deleteQRcodeWhiteBorder(bitMatrix);
            File outputfile = new File(filePath + fileName);
            // 输出图像
            ImageIO.write(image, format, outputfile);
        } else {
            Path path = FileSystems.getDefault().getPath(filePath, fileName);
            // 输出图像
            MatrixToImageWriter.writeToPath(bitMatrix, format, path);
        }

        System.out.println("***********二维码生成成功！**********");
        return fileAbsolutePath;
    }

    /**
     * 生成彩色二维码
     * @param content 内容
     */
    public static void generateColorQRcode(String content, File file) throws WriterException, IOException {
        if (file.exists()) {
            file.delete();
        }

        int width = 400, height = 400;
        Map<EncodeHintType, Object> hints = new HashMap<>();
        hints.put(EncodeHintType.CHARACTER_SET, "utf-8");
        hints.put(EncodeHintType.ERROR_CORRECTION, ErrorCorrectionLevel.M);
        BitMatrix matrix = new QRCodeWriter().encode(content, BarcodeFormat.QR_CODE, width, height, hints);

        int[] pixels = new int[width * height];

        for (int y = 0; y < matrix.getHeight(); y++) {
            for (int x = 0; x < matrix.getWidth(); x++) {
                // 二维码颜色（RGB）
                int num1 = (int) (50 - (50.0 - 13.0) / matrix.getHeight()
                        * (y + 1));
                int num2 = (int) (165 - (165.0 - 72.0) / matrix.getHeight()
                        * (y + 1));
                int num3 = (int) (162 - (162.0 - 107.0)
                        / matrix.getHeight() * (y + 1));
                Color color = new Color(num1, num2, num3);
                int colorInt = color.getRGB();
                pixels[y * width + x] = matrix.get(x, y) ? colorInt : 16777215;
            }
        }

        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);
        image.getRaster().setDataElements(0, 0, width, height, pixels);

        ByteArrayOutputStream out = new ByteArrayOutputStream();
        ImageIO.write(image, "png", out);

        FileOutputStream fout = new FileOutputStream(file);
        fout.write(out.toByteArray());
        fout.flush();
    }

    /**
     * 读取二维码图片内容
     * @param filePath  文件的绝对路径 例如："D://zxing.png";
     */
    public static Result readQRcode(String filePath) throws NotFoundException, IOException {
        BufferedImage image = ImageIO.read(new File(filePath));
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        // 对图像进行解码
        return new MultiFormatReader().decode(binaryBitmap, hints);
    }

    //去二维码白边
    private static BufferedImage deleteQRcodeWhiteBorder(BitMatrix matrix) {
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

/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-06 11:25
 * @Since:
 */
package com.zja.opencv;

import net.sourceforge.tess4j.ITesseract;
import net.sourceforge.tess4j.Tesseract;
import net.sourceforge.tess4j.TesseractException;
import org.opencv.core.Point;
import org.opencv.core.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.datatransfer.StringSelection;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 将OCR的文本层叠加在原始图片的上方，并支持复制文本内容
 * todo 文本层不支持复制，且效果不佳。类似添加水印效果。另外，就是复制不在图层上方。
 *
 * @author: zhengja
 * @since: 2023/11/06 11:25
 */
@Deprecated //todo 未成功，效果不佳
public class OpenCVTesseractOCR {
    private static JTextArea textArea;

    public static void main(String[] args) {
        // 加载本机OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 创建图形用户界面
        JFrame frame = new JFrame("OCR Application");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

        // 创建图像显示面板
        JPanel imagePanel = new JPanel();
        JLabel imageLabel = new JLabel();
        imagePanel.add(imageLabel);

        // 创建文本显示面板
        JPanel textPanel = new JPanel();
        textArea = new JTextArea(10, 40);
        textArea.setEditable(false);
        JScrollPane scrollPane = new JScrollPane(textArea);
        textPanel.add(scrollPane);

        // 创建复制按钮
        JButton copyButton = new JButton("Copy Text");
        copyButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                StringSelection selection = new StringSelection(textArea.getText());
                Toolkit.getDefaultToolkit().getSystemClipboard().setContents(selection, null);
            }
        });
        textPanel.add(copyButton);

        // 创建主面板并设置布局
        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BorderLayout());
        mainPanel.add(imagePanel, BorderLayout.CENTER);
        mainPanel.add(textPanel, BorderLayout.SOUTH);

        // 将主面板添加到框架中
        frame.getContentPane().add(mainPanel);

        // 调整框架大小并显示
        frame.pack();
        frame.setVisible(true);

        // 读取原始图像
        Mat originalImage = Imgcodecs.imread("D:\\temp\\ocr\\input.png");

        // 调用Tesseract进行文本识别，获取识别结果
        String result = performOCR(originalImage);

        // 在文本区域显示识别结果
        textArea.setText(result);

        // 克隆原始图像，创建一个新的Mat对象
        Mat imageWithText = originalImage.clone();

        // 设置文本的字体、颜色和位置
        Scalar color = new Scalar(0, 0, 255); // 红色
        int thickness = 2;
        int fontFace = Imgproc.FONT_HERSHEY_SIMPLEX;
        double fontScale = 1.5;
        int[] baseline = new int[1];
        Size textSize = Imgproc.getTextSize(result, fontFace, fontScale, thickness, baseline);

        // 计算文本的位置
        int x = 10;
        int y = (int) (textSize.height + 20);

        // 在图像上绘制文本
        Imgproc.putText(imageWithText, result, new Point(x, y), fontFace, fontScale, color, thickness);

        // 将图像显示在图像标签上
        ImageIcon imageIcon = new ImageIcon(matToBufferedImage(imageWithText));
        imageLabel.setIcon(imageIcon);

        // 重新调整框架大小以适应图像
        frame.pack();
    }

    private static String performOCR(Mat image) {
        // 将Mat图像保存为临时文件
        File tempImage = new File("temp.jpg");
        Imgcodecs.imwrite(tempImage.getAbsolutePath(), image);

        // 使用Tesseract进行文本识别
        ITesseract tesseract = new Tesseract();
        tesseract.setDatapath("E:\\App\\tesseract-ocr\\tessdata"); // 设置Tesseract的数据文件路径
        tesseract.setLanguage("chi_sim");
        String result = "";
        try {
            result = tesseract.doOCR(tempImage);
        } catch (TesseractException e) {
            e.printStackTrace();
        }

        // 删除临时文件
        tempImage.delete();

        return result;
    }

    private static BufferedImage matToBufferedImage(Mat mat) {
        // 将OpenCV的Mat对象转换为Java的BufferedImage对象
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".jpg", mat, matOfByte);
        byte[] byteArray = matOfByte.toArray();
        BufferedImage bufferedImage = null;
        try {
            InputStream in = new ByteArrayInputStream(byteArray);
            bufferedImage = ImageIO.read(in);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return bufferedImage;
    }
}

/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-06 11:09
 * @Since:
 */
package com.zja.opencv;

import org.opencv.core.*;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * 将OCR的文本层叠加在原始图片的上方，并支持复制文本内容
 * todo 文本层不支持复制，且效果不佳。类似添加水印效果。
 *
 * @author: zhengja
 * @since: 2023/11/06 11:09
 */
@Deprecated //todo 未成功，效果不佳
public class OpenCVOCR {
    public static void main(String[] args) {
        // 加载本机OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 读取原始图像
        Mat originalImage = Imgcodecs.imread("D:\\temp\\ocr\\input.png");

        // 调用OCR进行文本识别，获取识别结果
        String result = performOCR(originalImage);

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

        // 显示带有文本层的图像
        HighGui.imshow("Image with Text Overlay", imageWithText);

        // 等待按键事件
        HighGui.waitKey();

        // 关闭图像窗口
        HighGui.destroyAllWindows();
    }

    private static String performOCR(Mat image) {
        // 在这里调用OCR引擎进行图像识别
        // 返回识别结果
        return "Text recognized from OCR";
    }
}

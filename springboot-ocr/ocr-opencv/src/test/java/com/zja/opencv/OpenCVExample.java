/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-06 10:28
 * @Since:
 */
package com.zja.opencv;

import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

/**
 * @author: zhengja
 * @since: 2023/11/06 10:28
 */
public class OpenCVExample {
    public static void main(String[] args) {
        //先把 E:\App\opencv-4.7.0\build\java\x64\opencv_java470.dll 父目录添加到系统环境变量中。

        // 加载本机OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 读取图像
        Mat image = Imgcodecs.imread("D:\\temp\\ocr\\input.png");

        // 显示图像
        HighGui.imshow("Original Image", image);

        // 等待按键事件
        HighGui.waitKey();

        // 关闭图像窗口
        HighGui.destroyAllWindows();
    }
}

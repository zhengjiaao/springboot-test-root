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

    static {
        //先把 E:\App\opencv-4.7.0\build\java\x64\opencv_java470.dll 父目录添加到系统环境变量中。
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        if ((1 == args.length) && (0 == args[0].compareTo("--build"))) {

            System.out.println(Core.getBuildInformation());
        } else if ((1 == args.length) && (0 == args[0].compareTo("--help"))) {

            System.out.println("\t--build\n\t\tprint complete build info");
            System.out.println("\t--help\n\t\tprint this help");
        } else {

            System.out.println("Welcome to OpenCV " + Core.VERSION);
        }
    }
}

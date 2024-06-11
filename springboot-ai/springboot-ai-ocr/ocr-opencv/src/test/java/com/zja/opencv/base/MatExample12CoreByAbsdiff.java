package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.Scalar;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.List;

/**
 * java openCV4.x 入门-Core之图像差异比对(动态预警)
 *
 * @Author: zhengja
 * @Date: 2024-06-07 15:26
 */
public class MatExample12CoreByAbsdiff {

    /**
     * 绝对差值
     * <p>
     * absdiff函数用于计算两个矩阵或者矩阵与常量之间的差异，并生成一个新的矩阵，其中每个像素表示对应位置上两者像素的绝对差值。
     * </P>
     */
    @Test
    public void test_absdiff() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 计算两个数组(矩阵)之间的绝对差值
        // absdiff(Mat src1, Mat src2, Mat dst) 第一个数组(矩阵)、第二个数组(矩阵)、结果数组(矩阵)

        Mat mat = new Mat(3, 3, CvType.CV_8UC1);
        mat.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        Mat clone = mat.clone();
        clone.put(1, 1, 33, 34, 66);

        Mat dst = new Mat();
        Core.absdiff(mat, clone, dst); // 绝对差值 计算公式为:dst(I)=saturate(|src1(I)−src2|)

        System.out.println("mat.dump() = \n" + mat.dump());
        System.out.println("clone.dump() = \n" + clone.dump());
        System.out.println("dst.dump() = \n" + dst.dump());

        // 计算数组与标量之间的每个元素的绝对差值.
        // absdiff(Mat src1, Scalar src2, Mat dst) 第一个数组(矩阵)、第二个标量、结果数组(矩阵)

        Mat mat1 = new Mat(3, 3, CvType.CV_8UC1);
        mat1.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Mat dst1 = new Mat();
        Core.absdiff(mat1, new Scalar(10), dst1);
        System.out.println("mat1.dump() = \n" + mat1.dump());
        System.out.println("dst1.dump() = \n" + dst1.dump());

    }

    /**
     * 扩展示例 : 通过了解此函数，我们可以用它来比较两幅图像的差异。例如从下图中找不同。
     * 还可以使用Core.absdiff它实现运动检测（通过差分，标记图像中发生的运动或变化）、背景减除（通过差分将动的对象从静止背景中分离出来）`等。
     */
    @Test
    public void test_absdiff_2() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // absdiff  找不同
        Mat src1 = Imgcodecs.imread("input/Brush1.png");
        Mat src2 = Imgcodecs.imread("input/Brush2.png");
        Mat dst = new Mat();
        Core.absdiff(src1, src2, dst);
        HighGui.imshow("dst", dst);
        HighGui.waitKey();

    }

    /**
     * 动态预警:通过这个方法，还可以实现摄像头类似动态预警的效果，当我们的监控中的画面出现变化时，发出预警通知。
     * 非正常示例：这里通过读取多图像的方式代替视频帧
     */
    @Test
    public void test_absdiff_3() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        List<Mat> mats = new ArrayList<>();
        // 转换成灰度图
        Imgcodecs.imreadmulti("D:\\temp\\gif\\car.tif", mats, Imgcodecs.IMREAD_GRAYSCALE);

        // 第一帧
        Mat mat = mats.get(0);
        int rows = mat.rows();
        int cols = mat.cols();

        int size = mats.size();
        Mat src2 = new Mat();

        Mat dst = new Mat();
        // 降低动态检测灵敏度
        int sensitivity = rows * cols - (rows * cols / 8);
        System.out.println("sensitivity = " + sensitivity);
        for (int i = 0; i < size; i++) {
            src2 = mats.get(i);
            Core.absdiff(mat, src2, dst);
            HighGui.imshow("src2", src2);
            int index = 0;
            // dst 处理
            for (int r = 0; r < rows; r++) {
                for (int c = 0; c < cols; c++) {
                    double[] doubles = dst.get(r, c);
                    double val = doubles[0];
                    if (val > 0) {
                        index++;
                    }
                }
            }

            HighGui.imshow("src2", src2);
            if (index > sensitivity) {
                System.out.println(">>>>动态预警");
            } else {
                System.out.println(">>>>预警解除");
            }

            HighGui.waitKey(1000);
        }
    }

}

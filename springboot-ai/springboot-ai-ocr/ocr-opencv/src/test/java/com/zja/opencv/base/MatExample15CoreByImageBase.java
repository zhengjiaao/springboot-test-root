package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;

/**
 * @Author: zhengja
 * @Date: 2024-06-07 16:36
 */
public class MatExample15CoreByImageBase {

    /**
     * 翻转
     */
    @Test
    public void test_Core_flip() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // flip 围绕垂直、水平或两个轴翻转2D数组
        // flip(Mat src, Mat dst, int flipCode) 翻转src；并将结果存储在dst中。；一个标志，用于指定如何翻转数组。0 表示围绕 x 轴翻转，正数（例如 1）表示围绕 y 轴翻转，负数（例如 -1）表示同时围绕 x 和 y 轴翻转
        Mat src = Imgcodecs.imread("input/image2.jpg");
        // 创建一个与原始图像相同大小和类型的目标图像
        Mat dst = new Mat(src.rows(), src.cols(), src.type());
        // 进行水平翻转
        Core.flip(src, dst, 1);
        // 保存翻转后的图像
        Imgcodecs.imwrite("target/res_flip_image2.jpg", dst);

        HighGui.imshow("原图", src);
        HighGui.imshow("翻转图", dst);
        HighGui.waitKey();

        // flipND 在给定的轴上翻转n维数组。下方参数的解释仅对二维矩阵进行说明
        // flipND(Mat src, Mat dst, int axis) 输入数组、输出数组、轴：与flip类似沿轴翻转，0 表示沿x轴，1表示沿y轴翻转。不同的是 -1仅翻转y轴

        Mat mat = new Mat(3, 3, CvType.CV_8UC1);
        mat.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Mat dst2 = new Mat();
        Core.flipND(mat, dst2, -1);
        System.out.println("mat.dump() = \n" + mat.dump());
        System.out.println("dst2.dump() = \n" + dst2.dump());
    }

    /**
     * 旋转
     */
    @Test
    public void test_Core_rotate() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // rotate(Mat src, Mat dst, int rotateCode) 输入矩阵、输出矩阵、旋转标识。ROTATE_*

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat imread = Imgcodecs.imread("input/image2.jpg");
        Mat dst = new Mat();
        Core.rotate(imread, dst, Core.ROTATE_180); // 旋转180度
        HighGui.imshow("imread", imread);
        HighGui.imshow("dst", dst);
        HighGui.waitKey();
    }

    /**
     * 转置 :  对矩阵进行行列互换。与Mat.t()方法作用一样。
     */
    @Test
    public void test_Core_transpose() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // transpose(Mat src, Mat dst)
        Mat imread = Imgcodecs.imread("input/image2.jpg");
        Mat dst = new Mat();
        Core.transpose(imread, dst);
        HighGui.imshow("imread", imread);
        HighGui.imshow("dst", dst);
        HighGui.waitKey();


        // transposeND(Mat src, MatOfInt order, Mat dst)
        // 适用于 n 维矩阵的转置.。下方参数的解释仅对二维矩阵进行说明.了解即可
        // 注意: 输入应为连续的单通道矩阵
        Mat mat = new Mat(3, 3, CvType.CV_8UC1);
        mat.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);
        Mat dst2 = new Mat();

        MatOfInt matOfInt = new MatOfInt();
        // y轴和x轴互换
        matOfInt.fromArray(1, 0);
        Core.transposeND(mat, matOfInt, dst2);// [0,1,..,N-1] 的排列，其中 N 是 src 的轴数。dst 的第 i 个轴将对应输入的 order[i] 轴

        System.out.println("mat.dump() = \n" + mat.dump());
        System.out.println("dst2.dump() = \n" + dst2.dump());
    }

    /**
     * 复制
     */
    @Test
    public void test_Core_completeSymm() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 将一个方阵的下半部分或上半部分复制到其另一半。矩阵的对角线保持不变。
        // completeSymm(Mat m, boolean lowerToUpper) 输入(输出)矩阵，矩阵必须为方矩阵、如果为 true，则将下半部分复制到上半部分。否则，将上半部分复制到下半部分
        Mat mat = new Mat(3, 3, CvType.CV_8UC1);
        mat.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        System.out.println("mat.dump() = \n" + mat.dump());
        Core.completeSymm(mat, true);
        System.out.println("mat.dump() = \n" + mat.dump());

        // repeat（重复复制） 每个轴上重复输入数组一次或多次。
        // repeat(Mat src, int ny, int nx, Mat dst) 输入矩阵、沿y轴复制次数、沿x轴复制次数、输出矩阵
        Mat imread = Imgcodecs.imread("input/image2.jpg");
        Mat dst = new Mat();
        Core.repeat(imread, 2, 2, dst);
        HighGui.imshow("imread", imread);
        HighGui.imshow("dst", dst);
        HighGui.waitKey();
    }


    /**
     * 缩放
     */
    @Test
    public void test_Core_convertScaleAbs() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 缩放、计算绝对值，并将结果转换为8位
        // convertScaleAbs(Mat src, Mat dst, double alpha, double beta) 输入矩阵、输出矩阵，大小和类型与输入数组相同、缩放系数(缩放因子)、偏移量
        Mat src = Mat.eye(3, 3, CvType.CV_32F);
        src.put(0, 0, 1, 2, 3, 4, 5, 6, 7, 8, 9);

        // 创建输出矩阵
        Mat dst = new Mat();
        // 设置缩放因子和偏移量
        double alpha = 2.0;
        double beta = 1.0;
        // 调用convertScaleAbs方法进行缩放、取绝对值和转换
        Core.convertScaleAbs(src, dst, alpha, beta);

        System.out.println("src.dump() = \n" + src.dump());
        System.out.println("dst.dump() = \n" + dst.dump());
    }

    // 缩放: 可以用这个函数改变图像的亮度。
    @Test
    public void test_Core_convertScaleAbs_2() throws InterruptedException {
        // 加载OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        Mat imread = Imgcodecs.imread("input/image2.jpg");
        JFrame frame = HighGui.createJFrame("img", HighGui.WINDOW_NORMAL);

        JPanel jPanel = new JPanel();
        jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
        JSlider slider = new JSlider(0, 10, 1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);

        JSlider slider2 = new JSlider(0, 100, 10);
        slider2.setPaintTicks(true);
        slider2.setPaintLabels(true);
        slider2.setMajorTickSpacing(10);
        slider2.setMinorTickSpacing(1);

        jPanel.add(slider);
        jPanel.add(slider2);
        // 创建输出矩阵
        Mat dst = new Mat();

        frame.add(jPanel, BorderLayout.NORTH);

        JLabel jLabel = new JLabel();
        Image image = HighGui.toBufferedImage(imread);
        jLabel.setIcon(new ImageIcon(image));

        frame.add(jLabel, BorderLayout.CENTER);

        final int[] alpha = {1};
        final int[] beta = {0};

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                alpha[0] = source.getValue();
                Core.convertScaleAbs(imread, dst, alpha[0], beta[0]);
                Image image1 = HighGui.toBufferedImage(dst);
                jLabel.setIcon(new ImageIcon(image1));
                frame.repaint();
            }
        });

        slider2.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                beta[0] = source.getValue();
                System.out.println("beta[0] = " + beta[0]);
                Core.convertScaleAbs(imread, dst, alpha[0], beta[0]);
                Image image1 = HighGui.toBufferedImage(dst);
                jLabel.setIcon(new ImageIcon(image1));
                frame.repaint();
            }
        });
        frame.pack();
        frame.setVisible(true);

        Thread.sleep(Long.MAX_VALUE);
    }

    /**
     * 加边框
     */
    @Test
    public void test_Core_copyMakeBorder() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 在图像的边界周围添加边框
        // copyMakeBorder(Mat src, Mat dst, int top, int bottom, int left, int right, int borderType, Scalar value) 输入矩阵、输出矩阵、上边框大小(顶部像素)、下边框大小、左边框大小、右边框大小(右部像素)、边框类型(BORDER_*)、边框值(如果 borderType 是 BORDER_CONSTANT，则此参数指定边框的颜色)
        Mat src = Imgcodecs.imread("input/image2.jpg");
        // 频域图像
        Mat dst = new Mat();
        Core.copyMakeBorder(src, dst, 100, 100, 100, 100, Core.BORDER_REPLICATE);

        HighGui.imshow("src", src);
        HighGui.imshow("dst", dst);
        HighGui.waitKey();
    }
}

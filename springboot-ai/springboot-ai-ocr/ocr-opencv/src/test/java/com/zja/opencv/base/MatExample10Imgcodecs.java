package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfInt;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import java.util.ArrayList;
import java.util.List;

/**
 * Imgcodecs 类用于读取和保存图像
 * <p>
 * 图像读写：单图像、多图像等读写
 * 学习地址：https://blog.csdn.net/qq_27185879/article/details/137461389?spm=1001.2014.3001.5501
 * </P>
 *
 * @Author: zhengja
 * @Date: 2024-06-07 14:27
 */
public class MatExample10Imgcodecs {


    // 读取单页图像
    @Test
    public void test_Imgcodecs_imread() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 从文件中加载图像
        // Mat imread = Imgcodecs.imread("D:\\temp\\opencv\\Images\\bus.jpg", Imgcodecs.IMREAD_GRAYSCALE); // 标志,可以是IMREAD_*其一
        Mat imread = Imgcodecs.imread("input/image.jpg", Imgcodecs.IMREAD_GRAYSCALE); // 标志,可以是IMREAD_*其一
        HighGui.imshow("imraed", imread);
        HighGui.waitKey();

        // imdecode(Mat buf, int flags) 从内存中的缓冲区读取图像。如果缓冲区太短或包含无效数据，则该函数返回一个空矩阵。
        Mat imdecode = Imgcodecs.imdecode(new Mat(), Imgcodecs.IMREAD_GRAYSCALE);
        System.out.println("imdecode = " + imdecode);
    }


    /**
     * 读取多页图像
     * 假设你有一个名为 multipage_tiff.tif 的多页TIFF图像文件
     */
    @Test
    public void test_Imgcodecs_imreadmulti() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String imagePath = "input/multipage_image.tif";

        List<Mat> mats = new ArrayList<>(); // Mat 对象的列表，用于存储读取的每一页图像
        boolean imreadmulti = Imgcodecs.imreadmulti(imagePath, mats, 0, 2, Imgcodecs.IMREAD_GRAYSCALE);
        System.out.println("读取结果 = " + imreadmulti);
        System.out.println(mats.size());

        // imdecodemulti 从内存中的缓冲区读取多页图像
        boolean imdecodemulti = Imgcodecs.imdecodemulti(new Mat(), Imgcodecs.IMREAD_GRAYSCALE, mats);
        System.out.println("imdecodemulti = " + imdecodemulti);

        // haveImageReader 如果指定的图像可以被OpenCV解码，则返回true
        boolean haveImageReader = Imgcodecs.haveImageReader(imagePath);
        System.out.println("haveImageReader = " + haveImageReader);

        // imcount 获取给定文件中的图像数量。
        long imcount = Imgcodecs.imcount(imagePath);
        System.out.println("imcount = " + imcount);
    }

    // 保存图像 : 将图像写入到文件或缓存中

    /**
     * 保存单页图像
     * <p>
     * imwrite(String filename, Mat img) 将图像写入到文件或缓存中。
     * imwrite(String filename, Mat img, List<Integer> params) 将图像写入到文件或缓存中。
     * imwrite(String filename, Mat img, List<Integer> params, int type) 将图像写入到文件或缓存中。
     * </p>
     */
    @Test
    public void test_Imgcodecs_imwrite() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 1.将图像写入到文件
        Mat imread = Imgcodecs.imread("D:\\temp\\opencv\\Images\\bus.jpg", Imgcodecs.IMREAD_GRAYSCALE);
        boolean imwrite = Imgcodecs.imwrite("target/bus.png", imread);
        System.out.println("imwrite = " + imwrite);
        HighGui.imshow("imraed", imread);
        HighGui.waitKey();
        // HighGui.destroyAllWindows();

        // imwritemulti(String filename, List<Mat> img, MatOfInt params)  // 文件的名称、要保存的图像、保存参数
        // Mat imread2 = Imgcodecs.imread("D:\\temp\\opencv\\Images\\bus.jpg");
        Mat imread2 = Imgcodecs.imread("input/image.jpg");
        // 质量压缩 80%
        MatOfInt params = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 80);
        Imgcodecs.imwrite("target/image.png", imread2, params);


        // 2.将图像写入到缓存
        // imencode 将图像编码为内存缓冲区中的数据
        // imencode(java.lang.String ext, Mat img, MatOfByte buf, MatOfInt params) // 文件的扩展名、要编码的图像、保存参数
        Mat imread3 = Imgcodecs.imread("input/image.jpg");
        // 质量压缩 80%
        MatOfInt params2 = new MatOfInt(Imgcodecs.IMWRITE_JPEG_QUALITY, 80);
        // 写入缓存中
        MatOfByte matOfByte = new MatOfByte();
        Imgcodecs.imencode(".png", imread3, matOfByte, params2);
        System.out.println("matOfByte = " + matOfByte);
        // 读取缓存中的数据
        Mat imdecode = Imgcodecs.imdecode(matOfByte, Imgcodecs.IMREAD_COLOR);
        HighGui.imshow("imdecode", imdecode);
        HighGui.waitKey();

    }

    /**
     * 保存多页图像 : 将多个图像保存到一个多页图像文件中。
     */
    @Test
    public void test_Imgcodecs_imwritemulti() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 将多个图像保存到一个多页图像文件中
        // imwritemulti(java.lang.String filename, java.util.List img, MatOfInt params)
        Mat src1 = Imgcodecs.imread("input/image.jpg");
        Mat src2 = Imgcodecs.imread("input/image2.jpg");
        List<Mat> matList = new ArrayList<>();
        matList.add(src1);
        matList.add(src2);
        Imgcodecs.imwritemulti("target/multipage_image.tif", matList);

        // haveImageWriter 如果具有指定文件名的图像可以被OpenCV编码，则返回true
    }
}

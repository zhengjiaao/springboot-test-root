package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.*;
import org.opencv.highgui.HighGui;

/**
 * java openCV4.x 入门-数据类型（CvType）与Scalar
 *
 * @Author: zhengja
 * @Date: 2024-06-07 10:17
 */
public class MatExample2CvTypeAndScalar {

    // Mat CvType
    @Test
    public void test_Mat_CvType() {
        System.out.println("通道数=" + CvType.channels(CvType.CV_8SC4));
        System.out.println("CV_8sc1 深度=" + CvType.depth(CvType.CV_8SC1));
        System.out.println("cV_8uc3 字节大小=" + CvType.ELEM_SIZE(CvType.CV_8UC3));
        System.out.println("CV_8UC1 是否为整数类型=" + CvType.isInteger(CvType.CV_8UC1));
        System.out.println("CV_16FC2 是否为整数类型=" + CvType.isInteger(CvType.makeType(CvType.CV_16F, 2)));
        System.out.println("cvType.typeTostring(CvType.CV_8UC1)=" + CvType.typeToString(CvType.CV_8UC1));
    }

    // Mat Scalar 颜色
    @Test
    public void test_Mat_Scalar() {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 显示矩阵
        // Mat mat5 = new Mat(new Size(200, 200), CvType.CV_8UC3, new Scalar(255, 0, 0)); // 3通道：蓝、绿、红
        Mat mat5 = new Mat(new Size(200, 200), CvType.CV_8UC3, new Scalar(0, 255, 0));
        // Mat mat5 = new Mat(new Size(200, 200), CvType.CV_8UC3, new Scalar(0, 0, 255));
        // Mat mat5 = new Mat(new Size(200, 200), CvType.CV_8UC3, new Scalar(0, 255, 0, 0)); // 4通道  蓝、绿、红、最后一个是透明度
        // System.out.println("mat5.dump()=\n" + mat5.dump());

        HighGui.imshow("mat5", mat5);
        HighGui.waitKey();

        // 通过构造函数创建颜色空间对象
        Scalar scalar_1 = new Scalar(100);
        Scalar scalar_4 = new Scalar(10, 20, 30, 1);
        System.out.println("scalar_1=" + scalar_1);
        System.out.println("scalar_4.tostring()=" + scalar_4.toString());

        // 方法验证
        Scalar all = Scalar.all(30);
        System.out.println("all =" + all);

        // 复制
        Scalar clone = all.clone();
        System.out.println("clone =" + clone);
        Scalar conj = clone.conj();
        System.out.println("conj=" + conj);

        // 共轭
        Scalar conj1 = conj.conj();
        System.out.println("conj1=" + conj1);

        // 对比
        System.out.println("all.equals(clone) = " + all.equals(clone));
        System.out.println("all.equals(conj1) = " + all.equals(conj1));

        // 可以用来验证是否是 灰度图
        System.out.println("是否有实数 = " + all.isReal());

        // 乘法运算
        Scalar scalar_m1 = new Scalar(1, 2, 3, 4);
        Scalar scalar_m2 = new Scalar(1, 2, 3, 4);
        Scalar mul_res = scalar_m1.mul(scalar_m2);
        System.out.println("mul_res =" + mul_res);
        Scalar mul_res_mul = scalar_m1.mul(scalar_m2, 2);
        System.out.println("mul res_mul=" + mul_res_mul);

        // set set(double[] vals)函数等同于Scalar(double[] vals)
        double[] vals = {2, 3, 4, 5};
        all.set(vals);
        System.out.println("all set= " + all);
    }

}

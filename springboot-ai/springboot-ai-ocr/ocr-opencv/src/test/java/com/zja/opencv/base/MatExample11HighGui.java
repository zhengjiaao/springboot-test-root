package com.zja.opencv.base;

import org.junit.Test;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;

import javax.swing.*;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;
import java.awt.*;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;
import java.util.concurrent.ExecutionException;

/**
 * java openCV4.x 入门-HighGui之图像窗口显示
 *
 * <p>
 * 主要方法：
 * setWindowTitle：设置窗口标题
 * imshow：显示图像
 * waitKey：等待键盘输入
 * moveWindow: 移动窗口
 * resizeWindow：调整窗口大小
 * destroyAllWindows：销毁所有窗口
 * destroyWindow：销毁指定窗口
 * namedWindow：创建窗口
 * setWindowProperty：设置窗口属性
 * setWindowTitle：设置窗口标题
 * setMouseCallback：设置鼠标回调函数
 * getWindowProperty：获取窗口属性
 * getWindowImageRect：获取窗口图像区域
 * getWindowName：获取窗口名称
 * getWindowHandle：获取窗口句柄
 * getWindowAt：获取窗口位置
 * getWindowFrame():获取窗口边框
 * getWindowPropertyNames：获取窗口属性名称
 * <p>
 * 学习地址：https://blog.csdn.net/qq_27185879/article/details/137467250?spm=1001.2014.3001.5501
 * </P>
 *
 * @Author: zhengja
 * @Date: 2024-06-07 15:12
 */
public class MatExample11HighGui {

    // Jfram示例 通过按钮指定格式读取图像
    @Test
    public void test_Jfram_1() throws InterruptedException {
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String imagePath = "input/image.jpg";

        // 读取图片
        Mat imread = Imgcodecs.imread(imagePath);
        // 窗口
        JFrame frame = HighGui.createJFrame("imread", HighGui.WINDOW_AUTOSIZE);
        // 创建按钮
        JButton b1 = new JButton("Gray");
        JButton b2 = new JButton("REDUCED_COLOR_2");
        JPanel panel = new JPanel();
        panel.add(b1);
        panel.add(b2);
        frame.add(panel, BorderLayout.NORTH);

        // 转换Mat到BufferedImage
        Image bufferedImage = HighGui.toBufferedImage(imread);
        final ImageIcon[] imageIcon = {new ImageIcon(bufferedImage)};

        // 创建JLabel来显示图像
        JLabel jLabel = new JLabel(imageIcon[0]);
        frame.add(jLabel, BorderLayout.CENTER);


        b1.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Mat imread = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_GRAYSCALE);
                Image image = HighGui.toBufferedImage(imread);
                imageIcon[0] = new ImageIcon(image);
                jLabel.setIcon(imageIcon[0]);
                // 重绘
                frame.repaint();
            }
        });
        b2.addMouseListener(new MouseAdapter() {
            @Override
            public void mouseClicked(MouseEvent e) {
                super.mouseClicked(e);
                Mat imread = Imgcodecs.imread(imagePath, Imgcodecs.IMREAD_REDUCED_COLOR_2);
                Image image = HighGui.toBufferedImage(imread);
                imageIcon[0] = new ImageIcon(image);
                jLabel.setIcon(imageIcon[0]);
                frame.repaint();
            }
        });
        //
        frame.pack();
        frame.setVisible(true);

        Thread.sleep( Long.MAX_VALUE );  // 解决由于JUnit框架影响，导致窗口打开后不会停留，当然，也可以放到main方法里面解决
    }

    //JFrame 通过滑动块调节图像亮度
    @Test
    public void test_Jfram_2() throws InterruptedException {

        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
        String imagePath = "input/image.jpg";
        //读取图片
        Mat imread = Imgcodecs.imread(imagePath);
        //窗口
        JFrame frame = HighGui.createJFrame("imread", HighGui.WINDOW_AUTOSIZE);
        // 创建滑块
        JSlider slider = new JSlider(0,10,1);
        slider.setMajorTickSpacing(10);
        slider.setMinorTickSpacing(1);
        slider.setPaintTicks(true);
        slider.setPaintLabels(true);
        frame.add(slider,BorderLayout.NORTH);
        // 转换Mat到BufferedImage
        Image bufferedImage = HighGui.toBufferedImage(imread);
        final ImageIcon[] imageIcon = {new ImageIcon(bufferedImage)};
        // 创建JLabel来显示图像
        JLabel jLabel = new JLabel(imageIcon[0]);
        frame.add(jLabel, BorderLayout.CENTER);

        slider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                JSlider source = (JSlider) e.getSource();
                int value = source.getValue();
                Mat dst =new Mat();
                imread.convertTo(dst,imread.type(),value);
                Image image = HighGui.toBufferedImage(dst);
                imageIcon[0] =new ImageIcon(image);
                jLabel.setIcon(imageIcon[0]);
                frame.repaint();
            }
        });
        //
        frame.pack();
        frame.setVisible(true);

        Thread.sleep( Long.MAX_VALUE );  // 解决由于JUnit框架影响，导致窗口打开后不会停留
    }

}

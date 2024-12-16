package com.zja.image.java;

import org.junit.jupiter.api.Test;

import javax.imageio.ImageIO;
import javax.swing.*;
import java.awt.*;
import java.awt.image.BufferedImage;
import java.awt.image.BufferedImageOp;
import java.awt.image.ColorConvertOp;
import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;

/**
 * Java JDK 提供了一些基本的图像处理和操作功能，主要通过 java.awt.image 和 javax.imageio 包来实现。
 *
 * @Author: zhengja
 * @Date: 2024-09-13 13:07
 */
public class JdkImageExample {

    static final String image_path = Paths.get("D:\\temp\\images\\test", "1.jpeg").toString();
//    static final String image_path = Paths.get("D:\\temp\\images\\test", "2.png").toString();
//    static final String image_path = Paths.get("D:\\temp\\images\\test", "3.jpg").toString();
//    static final String image_path = Paths.get("D:\\temp\\images\\test", "4.tif").toString();
//    static final String image_path = Paths.get("D:\\temp\\images\\test", "5.tif").toString();

    // 创建图像
    @Test
    public void CreateImage_test_1() {
        int width = 400; // 图像宽度
        int height = 400; // 图像高度

        // 创建一个 BufferedImage 对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 获取 Graphics 对象以绘制图像
        Graphics g = image.getGraphics();

        // 设置背景色为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 绘制一个红色填充的矩形
        g.setColor(Color.RED);
        g.fillRect(50, 50, 300, 300);

        // 绘制一个蓝色边框的矩形
        g.setColor(Color.BLUE);
        g.drawRect(50, 50, 300, 300);

        g.dispose();

        // 保存绘制后的图像
        try {
            File outputImage = new File("target/created_image.png");
            ImageIO.write(image, "png", outputImage);
            System.out.println("图像创建成功并保存为 target/created_image.png");

            File outputImage_jpg = new File("target/created_image.jpg");
            ImageIO.write(image, "jpg", outputImage_jpg);
            System.out.println("图像创建成功并保存为 target/created_image.jpg");
        } catch (IOException e) {
            System.err.println("保存图像时出现错误：" + e.getMessage());
        }
    }

    // 加载和保存图像
    @Test
    public void ImageFormatConversion_test_1() {

        String inputImagePath = "target/created_image.jpg";
//        String inputImagePath = "input_image.jpg";
        String outputImagePath = "target/output_image-jpg-png.png";

        try {
            // 加载图像文件
            File inputFile = new File(inputImagePath);
            BufferedImage image = ImageIO.read(inputFile);

            // 保存图像为新的文件
            File outputFile = new File(outputImagePath);
            ImageIO.write(image, "png", outputFile);

            System.out.println("图像加载和保存成功");
        } catch (IOException e) {
            System.err.println("处理图像时出现错误：" + e.getMessage());
        }
    }

    // 图像格式转换: PNG 转 JPEG, 加载一张 PNG 格式的图像，将其转换为 JPEG 格式，并保存为新的 JPEG 格式的图像文件。
    @Test
    public void ImageFormatConversion_test_2() {
        try {
            // 加载 PNG 格式的图像
            BufferedImage image = ImageIO.read(new File("target/created_image.png"));

            // 创建一个新的 BufferedImage 以及指定格式
            BufferedImage newImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_RGB);
            newImage.createGraphics().drawImage(image, 0, 0, null);

            // 保存为 JPEG 格式的图像
            File outputImage = new File("target/converted_image.jpg");
            ImageIO.write(newImage, "jpg", outputImage);
            System.out.println("图像已转换为 JPEG 格式并保存为 target/converted_image.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 绘制图像并保存为新的图像文件
    @Test
    public void ImageDrawing_test_1() throws Exception {
        int width = 400;
        int height = 400;

        // 创建一个 BufferedImage 对象
        BufferedImage image = new BufferedImage(width, height, BufferedImage.TYPE_INT_ARGB);

        // 获取 Graphics 对象以绘制图像
        Graphics g = image.getGraphics();

        // 设置背景色为白色
        g.setColor(Color.WHITE);
        g.fillRect(0, 0, width, height);

        // 绘制一个红色矩形
        g.setColor(Color.RED);
        g.fillRect(50, 50, 300, 300);

        // 绘制一个蓝色边框
        g.setColor(Color.BLUE);
        g.drawRect(50, 50, 300, 300);

        g.dispose();

        // 保存绘制后的图像
        try {
            File outputImage = new File("target/drawn_image.png");
            ImageIO.write(image, "png", outputImage);
            System.out.println("图像已绘制并保存为 target/drawn_image.png");
        } catch (IOException e) {
            System.err.println("保存图像时出现错误：" + e.getMessage());
        }
    }

    // 加载一张 PNG 格式的图像，对其进行缩放和裁剪操作，然后保存为新的 PNG 格式的图像文件。
    @Test
    public void ImageResizeAndCrop_test_1() throws Exception {
        try {
            // 加载 PNG 格式的图像
            BufferedImage originalImage = ImageIO.read(new File("target/created_image.png"));

            // 缩放图像到指定尺寸
            int newWidth = 200;
            int newHeight = 200;
            Image scaledImage = originalImage.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g = resizedImage.createGraphics();
            g.drawImage(scaledImage, 0, 0, null);
            g.dispose();

            // 裁剪图像
            int x = 50;
            int y = 50;
            int width = 100;
            int height = 100;
            BufferedImage croppedImage = resizedImage.getSubimage(x, y, width, height);

            // 保存为 PNG 格式的图像
            File outputImage = new File("target/resized_and_cropped_image.png");
            ImageIO.write(croppedImage, "png", outputImage);
            System.out.println("图像已缩放和裁剪并保存为 target/resized_and_cropped_image.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 使用 Java Swing 来渲染文本并显示在界面上
    @Test
    public void TextRendering_test_1() throws Exception {
        // 创建一个 JFrame
        JFrame frame = new JFrame("Text Rendering Example");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(400, 200);

        // 创建一个 JPanel 用于显示文本
        JPanel panel = new JPanel() {
            @Override
            protected void paintComponent(Graphics g) {
                super.paintComponent(g);

                // 设置字体和颜色
                g.setFont(new Font("Arial", Font.BOLD, 20));
                g.setColor(Color.BLUE);

                // 渲染文本
                g.drawString("Hello, World!", 50, 100);
            }
        };

        frame.add(panel);
        frame.setVisible(true);

        Thread.sleep(5000);
    }

    // 加载、保存、缩放、裁剪和绘制图像
    @Test
    public void TextRendering_test_2() throws Exception {
        try {
            // 加载图像
            BufferedImage image = ImageIO.read(new File(image_path));

            // 创建一个新的 BufferedImage 用于缩放和绘制
            int newWidth = image.getWidth() / 2;
            int newHeight = image.getHeight() / 2;
            Image scaledImage = image.getScaledInstance(newWidth, newHeight, Image.SCALE_SMOOTH);
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = resizedImage.createGraphics();
            g2d.drawImage(scaledImage, 0, 0, null);
            g2d.dispose();

            // 裁剪图像
            int x = newWidth / 4;
            int y = newHeight / 4;
            int w = newWidth / 2;
            int h = newHeight / 2;
            BufferedImage croppedImage = resizedImage.getSubimage(x, y, w, h);

            // 创建 JFrame 并在面板上显示图像
            JFrame frame = new JFrame("Image Processing Example");
            JPanel panel = new JPanel() {
                @Override
                protected void paintComponent(Graphics g) {
                    super.paintComponent(g);
                    g.drawImage(croppedImage, 0, 0, null);
                }
            };
            panel.setPreferredSize(new java.awt.Dimension(w, h));
            frame.add(panel);
            frame.pack();
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setVisible(true);

            Thread.sleep(5000);

            // 保存图像
            File outputImage = new File("target/output_1.png");
            ImageIO.write(croppedImage, "png", outputImage);
            System.out.println("图像处理完成，已保存为 output.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 颜色空间转换: 加载一张 RGB 格式的图像，将其转换为灰度图像，然后保存为新的 PNG 格式的图像文件。
    @Test
    public void ColorSpaceConversion_test_2() throws Exception {
        try {
            // 加载 RGB 格式的图像
            BufferedImage image = ImageIO.read(new File("target/created_image.jpg"));

            // 创建一个新的 BufferedImage 以及指定颜色空间
            BufferedImage grayImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_BYTE_GRAY);
            Color color = null;
            int grayValue;

            // 转换为灰度图像
            for (int y = 0; y < image.getHeight(); y++) {
                for (int x = 0; x < image.getWidth(); x++) {
                    color = new Color(image.getRGB(x, y));
                    grayValue = (int) (color.getRed() * 0.299 + color.getGreen() * 0.587 + color.getBlue() * 0.114);
                    Color grayColor = new Color(grayValue, grayValue, grayValue);
                    grayImage.setRGB(x, y, grayColor.getRGB());
                }
            }

            // 保存为 PNG 格式的图像
            File outputImage = new File("target/gray_image.png");
            ImageIO.write(grayImage, "png", outputImage);
            System.out.println("图像已转换为灰度图像并保存为 target/gray_image.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 透明度处理: 加载一张 PNG 格式的图像，将其透明度降低为50%，然后保存为新的 PNG 格式的图像文件。
    @Test
    public void ImageTransparency_test_1() throws Exception {
        try {
            // 加载 PNG 格式的图像
            BufferedImage image = ImageIO.read(new File("target/created_image.png"));

            // 创建一个新的 BufferedImage 以及指定透明度
            BufferedImage transparentImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = transparentImage.createGraphics();
            g2d.setComposite(AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f)); // 设置透明度为50%
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            // 保存为 PNG 格式的图像
            File outputImage = new File("target/transparent_image.png");
            ImageIO.write(transparentImage, "png", outputImage);
            System.out.println("图像透明度已降低并保存为 target/transparent_image.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    // 图像像素操作: 加载一张图像，读取并修改图像的像素值，然后保存修改后的图像。
    @Test
    public void ImagePixelOperation_test_1() throws Exception {
        try {
            // 加载图像
            BufferedImage image = ImageIO.read(new File("target/created_image.jpg"));

            // 获取图像的宽度和高度
            int width = image.getWidth();
            int height = image.getHeight();

            // 遍历图像的每个像素并修改像素值
            for (int y = 0; y < height; y++) {
                for (int x = 0; x < width; x++) {
                    int rgb = image.getRGB(x, y); // 获取像素值
                    int alpha = (rgb >> 24) & 0xFF;
                    int red = (rgb >> 16) & 0xFF;
                    int green = (rgb >> 8) & 0xFF;
                    int blue = rgb & 0xFF;

                    // 修改像素值，这里简单地将每个通道的值取反
                    int newRGB = (alpha << 24) | ((255 - red) << 16) | ((255 - green) << 8) | (255 - blue);

                    image.setRGB(x, y, newRGB); // 设置修改后的像素值
                }
            }

            // 保存修改后的图像
            File outputImage = new File("target/modified_image.jpg");
            ImageIO.write(image, "jpg", outputImage);
            System.out.println("修改后的图像已保存为 target/modified_image.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 图像滤镜效果: 加载一张图像，应用一个简单的灰度化滤镜效果，并保存处理后的图像。
    @Test
    public void ImageFilter_test_1() throws Exception {
        try {
            // 加载图像
            BufferedImage image = ImageIO.read(new File("target/created_image.jpg"));

            // 创建一个灰度化滤镜效果
            BufferedImageOp grayscaleOp = new ColorConvertOp(java.awt.color.ColorSpace.getInstance(java.awt.color.ColorSpace.CS_GRAY), null);

            // 应用滤镜效果
            BufferedImage filteredImage = new BufferedImage(image.getWidth(), image.getHeight(), BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = filteredImage.createGraphics();
            g2d.drawImage(image, 0, 0, null);
            g2d.dispose();

            // 将滤镜效果应用到图像
            filteredImage = grayscaleOp.filter(filteredImage, null);

            // 保存处理后的图像
            File outputImage = new File("target/filtered_image.jpg");
            ImageIO.write(filteredImage, "jpg", outputImage);
            System.out.println("应用滤镜效果后的图像已保存为 target/filtered_image.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 叠加图像：将图像缩放、合并，加载一张图像，将其缩放到指定大小，然后将另一张图像合成到缩放后的图像上，并保存为新的图像文件。
    @Test
    public void CombinedImage_test_1() throws Exception {
        try {
            // 加载第一张图像
            // BufferedImage image1 = ImageIO.read(new File("target/created_image.jpg"));
            // 加载第二张图像
            // BufferedImage image2 = ImageIO.read(new File("target/created_image.png"));

            BufferedImage image1 = ImageIO.read(new File("D:\\temp\\images\\test.png"));
            BufferedImage image2 = ImageIO.read(new File("D:\\temp\\images\\test.jpg"));

            // 创建一个新的 BufferedImage 用于缩放
            int newWidth = 200;
            int newHeight = 200;
            BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();

            // 缩放第一张图像到指定大小
            g2d.drawImage(image1, 0, 0, newWidth, newHeight, null);
            g2d.dispose();

            // 合成第二张图像到缩放后的图像上，并指定合成位置，这里是在左上角，你可以根据需要调整位置
            Graphics2D g2dCombined = scaledImage.createGraphics();
            g2dCombined.drawImage(image2, 50, 50, null); // 在坐标 (50, 50) 处合成第二张图像
            g2dCombined.dispose();

            // 保存合成后的图像
            File outputImage = new File("target/combined_image_1.png");
            ImageIO.write(scaledImage, "png", outputImage);
            System.out.println("合成后的图像已保存为 target/combined_image_1.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 叠加图像：加载两张图像，缩放第二张图像为第一张图像的十分之一，然后将第二张图像叠加到第一张图像的右下角，并保存为新的图像文件。
    @Test
    public void CombinedImage_test_2() throws Exception {
        // 加载第一张图像
        // BufferedImage image1 = ImageIO.read(new File("target/created_image.png"));
        // 加载第二张图像
        // BufferedImage image2 = ImageIO.read(new File("target/created_image.jpg"));

        BufferedImage image1 = ImageIO.read(new File("D:\\temp\\images\\test.png"));
        BufferedImage image2 = ImageIO.read(new File("D:\\temp\\images\\test.jpg"));

        // 计算缩放后的第二张图像的宽度和高度
        int scaledWidth = image1.getWidth() / 10;
        int scaledHeight = image1.getHeight() / 10;

        // 创建一个缩放后的第二张图像
        BufferedImage scaledImage2 = new BufferedImage(scaledWidth, scaledHeight, BufferedImage.TYPE_INT_ARGB);
        Graphics2D g2d = scaledImage2.createGraphics();
        g2d.setRenderingHint(RenderingHints.KEY_INTERPOLATION, RenderingHints.VALUE_INTERPOLATION_BILINEAR);
        g2d.drawImage(image2, 0, 0, scaledWidth, scaledHeight, null);
        g2d.dispose();

        // 创建一个新的图像，其大小与第一张图像相同
        BufferedImage combinedImage = new BufferedImage(
                image1.getWidth(),
                image1.getHeight(),
                BufferedImage.TYPE_INT_ARGB);

        // 绘制第一张图像到新图像的左上角
        g2d = combinedImage.createGraphics();
        g2d.drawImage(image1, 0, 0, null);

        // 绘制缩放后的第二张图像到新图像的右下角
        g2d.drawImage(scaledImage2, image1.getWidth() - scaledWidth, image1.getHeight() - scaledHeight, null);

        // 释放资源
        g2d.dispose();

        // 保存新图像
        File outputFile = new File("target/combined_image_2.jpg");
        ImageIO.write(combinedImage, "jpg", outputFile);
    }

    // 图像合并：合并多个图像并保存为新的图像文件，将第一张图像放在左侧，第二张图像放在右侧，并将合并后的图像保存为新的 PNG 格式的图像文件。
    @Test
    public void ImageMerge_test_1() throws Exception {
        try {
            // 加载两张图像
            BufferedImage image1 = ImageIO.read(new File("target/created_image.jpg"));
            BufferedImage image2 = ImageIO.read(new File("target/created_image.png"));

            // 创建一个新的 BufferedImage 用于合并图像
            int combinedWidth = image1.getWidth() + image2.getWidth();
            int combinedHeight = Math.max(image1.getHeight(), image2.getHeight());
            BufferedImage combinedImage = new BufferedImage(combinedWidth, combinedHeight, BufferedImage.TYPE_INT_ARGB);

            // 合并两张图像
            Graphics2D g = combinedImage.createGraphics();
            g.drawImage(image1, 0, 0, null);
            g.drawImage(image2, image1.getWidth(), 0, null);
            g.dispose();

            // 保存合并后的图像
            File outputImage = new File("target/merged_image.png");
            ImageIO.write(combinedImage, "png", outputImage);
            System.out.println("图像合并成功并保存为 target/merged_image.png");
        } catch (IOException e) {
            System.err.println("处理图像时出现错误：" + e.getMessage());
        }
    }

    // 缩放图像: 缩放图像并保存为新的图像文件, 定义了目标图像的新尺寸为 200x200 像素, 将缩放后的图像保存为 JPEG 格式的新图像文件。
    @Test
    public void ImageResize_test_1() throws Exception {
        try {
            // 加载原始图像
            BufferedImage originalImage = ImageIO.read(new File("target/created_image.jpg"));

            // 定义目标图像尺寸
            int newWidth = 200;
            int newHeight = 200;

            // 创建目标图像
            BufferedImage resizedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
            Graphics2D g = resizedImage.createGraphics();

            // 执行图像缩放
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();

            // 保存缩放后的图像
            File outputImage = new File("target/resized_image.jpg");
            ImageIO.write(resizedImage, "jpg", outputImage);
            System.out.println("图像缩放成功并保存为 target/resized_image.jpg");
        } catch (IOException e) {
            System.err.println("处理图像时出现错误：" + e.getMessage());
        }
    }

    // 图像像素，降低像素: 要降低图像的像素，可以通过将图像缩小来实现。
    @Test
    public void DecreaseImagePixels_test_1() throws Exception {
        try {
            // 加载原始图像
            BufferedImage originalImage = ImageIO.read(new File("target/created_image.jpg"));

            // 定义缩小后的尺寸
            int newWidth = originalImage.getWidth() / 2;
            int newHeight = originalImage.getHeight() / 2;

            // 创建新的缩小后的图像
            BufferedImage decreasedImage = new BufferedImage(newWidth, newHeight, originalImage.getType());
            Graphics2D g = decreasedImage.createGraphics();

            // 进行像素降低
            g.drawImage(originalImage, 0, 0, newWidth, newHeight, null);
            g.dispose();

            // 保存像素降低后的图像
            File outputImage = new File("target/decreased_image.jpg");
            ImageIO.write(decreasedImage, "jpg", outputImage);
            System.out.println("图像像素降低成功并保存为 target/decreased_image.jpg");
        } catch (IOException e) {
            System.err.println("处理图像时出现错误：" + e.getMessage());
        }
    }

    // 图像像素，降低像素，尺寸不变(不缩小): 加载一张图片，定义了一个下采样因子为 2，以便每隔一个像素取一个像素。创建了一个新的 BufferedImage 对象来存储下采样后的图像，并在循环中执行像素下采样操作。
    @Test
    public void DecreaseImageResolution_test_1() throws Exception {
        try {
            // 加载原始图像
            BufferedImage originalImage = ImageIO.read(new File("target/created_image.jpg"));

            // 定义下采样因子（例如，每隔一个像素取一个像素）
            int sampleFactor = 2;

            // 获取原始图像的宽度和高度
            int width = originalImage.getWidth();
            int height = originalImage.getHeight();

            // 创建新的 BufferedImage 以存储下采样后的图像
            BufferedImage decreasedResolutionImage = new BufferedImage(width / sampleFactor, height / sampleFactor, originalImage.getType());

            // 进行像素下采样
            for (int y = 0; y < height; y += sampleFactor) {
                for (int x = 0; x < width; x += sampleFactor) {
                    int rgb = originalImage.getRGB(x, y);
                    decreasedResolutionImage.setRGB(x / sampleFactor, y / sampleFactor, rgb);
                }
            }

            // 保存下采样后的图像
            File outputImage = new File("target/decreased_resolution_image.jpg");
            ImageIO.write(decreasedResolutionImage, "jpg", outputImage);
            System.out.println("图像像素下采样成功并保存为 target/decreased_resolution_image.jpg");
        } catch (IOException e) {
            System.err.println("处理图像时出现错误：" + e.getMessage());
        }
    }

    // 添加文本
    @Test
    public void test_2() throws Exception {
        try {
            // 加载图像
            BufferedImage image = ImageIO.read(new File(image_path));

            // 创建 Graphics2D 对象以在图像上绘制
            Graphics2D g2d = image.createGraphics();

            // 设置字体和颜色
            Font font = new Font("Arial", Font.BOLD, 24);
            g2d.setFont(font);
            g2d.setColor(Color.RED);

            // 添加文本
            String text = "Sample Text";
            int x = 50;
            int y = 50;
            g2d.drawString(text, x, y);

            // 释放资源
            g2d.dispose();

            // 保存带有文本标注的图像
            File outputImage = new File("target/output_annotated.jpg");
            ImageIO.write(image, "jpg", outputImage);
            System.out.println("文本标注已添加到图像，已保存为 output_annotated.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 使用 Graphics2D 类来在图像上添加形状或图形
     */
    @Test
    public void test_3() throws Exception {
        // 在图像上添加一个矩形
        try {
            // 加载图像
            BufferedImage image = ImageIO.read(new File(image_path));

            // 创建 Graphics2D 对象以在图像上绘制
            Graphics2D g2d = image.createGraphics();

            // 设置矩形的位置和大小
            int x = 100;
            int y = 100;
            int width = 200;
            int height = 150;

            // 设置矩形的颜色
            g2d.setColor(Color.GREEN);

            // 绘制矩形
            g2d.fillRect(x, y, width, height);

            // 释放资源
            g2d.dispose();

            // 保存带有形状标注的图像
            File outputImage = new File("target/output_shape.jpg");
            ImageIO.write(image, "jpg", outputImage);
            System.out.println("矩形已添加到图像，已保存为 output_shape.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 在图像上添加一个半透明的矩形
    @Test
    public void test_4() throws Exception {
        try {
            // 加载图像
            BufferedImage image = ImageIO.read(new File(image_path));

            // 创建 Graphics2D 对象以在图像上绘制
            Graphics2D g2d = image.createGraphics();

            // 设置矩形的位置和大小
            int x = 100;
            int y = 100;
            int width = 200;
            int height = 150;

            // 设置矩形的颜色和透明度
            Color rectColor = new Color(255, 0, 0); // 红色
            int alpha = 127; // 透明度，取值范围为 0（完全透明）到 255（不透明）
//            int alpha = 0; // 透明度，取值范围为 0（完全透明）到 255（不透明）
            Color transparentColor = new Color(rectColor.getRed(), rectColor.getGreen(), rectColor.getBlue(), alpha);
            g2d.setColor(transparentColor);

            // 设置透明度
            AlphaComposite alphaComposite = AlphaComposite.getInstance(AlphaComposite.SRC_OVER, 0.5f); // 透明度为 0.5
            g2d.setComposite(alphaComposite);

            // 绘制半透明矩形
            g2d.fillRect(x, y, width, height);

            // 释放资源
            g2d.dispose();

            // 保存带有半透明形状标注的图像
            File outputImage = new File("target/output_transparent_shape.jpg");
            ImageIO.write(image, "jpg", outputImage);
            System.out.println("半透明矩形已添加到图像，已保存为 output_transparent_shape.jpg");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    // 加载了两张不同的图像并对它们进行了一些操作，包括缩放和合成
    @Test
    public void test_5() throws Exception {
        try {
            // 加载第一张图像
            BufferedImage image1 = ImageIO.read(new File("image1.jpg"));

            // 创建一个新的 BufferedImage 用于缩放
            int newWidth = 200;
            int newHeight = 200;
            BufferedImage scaledImage = new BufferedImage(newWidth, newHeight, BufferedImage.TYPE_INT_ARGB);
            Graphics2D g2d = scaledImage.createGraphics();

            // 缩放第一张图像到指定大小
            g2d.drawImage(image1, 0, 0, newWidth, newHeight, null);
            g2d.dispose();

            // 加载第二张图像
            BufferedImage image2 = ImageIO.read(new File("image2.png"));

            // 合成第二张图像到缩放后的图像上
            Graphics2D g2dCombined = scaledImage.createGraphics();
            g2dCombined.drawImage(image2, 50, 50, null); // 在坐标 (50, 50) 处合成第二张图像
            g2dCombined.dispose();

            // 保存合成后的图像
            File outputImage = new File("combined_image.png");
            ImageIO.write(scaledImage, "png", outputImage);
            System.out.println("合成后的图像已保存为 combined_image.png");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}

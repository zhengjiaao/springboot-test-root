package com.zja.opencv.dnn;

import org.junit.Test;
import org.opencv.core.*;
import org.opencv.dnn.*;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-06-05 17:47
 */
@Deprecated // todo 未检测到内容
public class VehicleDetectionExampleONNX {

    static {
        // 加载OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Test
    public void test() {
        // 加载本地OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        String imagePath = "D:\\temp\\opencv\\Images\\bus.jpg"; // input/image.jpg
        String onnxModelPath = "D:\\temp\\opencv\\dnn\\yolov8n.onnx"; // ONNX模型路径

        // 加载模型
        Net net = Dnn.readNetFromONNX(onnxModelPath);

        // 加载图像
        Mat image = Imgcodecs.imread(imagePath);

        // 图像预处理
        // 假设模型需要输入尺寸为300x300，且输入是BGR图像
        Mat blob = Dnn.blobFromImage(image, 1.0, new Size(640, 640), new Scalar(104, 117, 123), true, false);

        // 设置输入
        net.setInput(blob);

        // 进行推理
        Mat detections = net.forward();

        // 解析输出
        List<Rect> boundingBoxes = new ArrayList<>();
        for (int i = 0; i < detections.rows(); i++) {
            float[] data = new float[7]; // 假设输出是7个元素，根据模型输出调整
            // float[] data = detections.get(i, 0).floatArray();
            detections.get(i, 0, data);
            if (data[4] > 0.3f) { // 置信度阈值
                int classId = (int) data[1];
                // double[] box = detections.get(i, new Range(0, 4))[0];
                float x = data[0];
                float y = data[1];
                float width = data[2];
                float height = data[3];

                // 转换为图像坐标
                int left = (int) Math.round(x * image.cols());
                int top = (int) Math.round(y * image.rows());
                int right = (int) Math.round((x + width) * image.cols());
                int bottom = (int) Math.round((y + height) * image.rows());

                boundingBoxes.add(new Rect(left, top, right - left, bottom - top));
            }
        }

        // 绘制边界框
        for (Rect rect : boundingBoxes) {
            Imgproc.rectangle(image, rect.tl(), rect.br(), new Scalar(0, 255, 0), 2);
        }

        // 显示结果
        Imgcodecs.imwrite("target/image_with_boxes.jpg", image);
    }
}
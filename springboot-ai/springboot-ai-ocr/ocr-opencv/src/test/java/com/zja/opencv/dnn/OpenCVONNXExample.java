package com.zja.opencv.dnn;

import org.bytedeco.javacpp.FloatPointer;
import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;

import static org.opencv.imgcodecs.Imgcodecs.imread;
import static org.opencv.imgcodecs.Imgcodecs.imwrite;
import static org.opencv.imgproc.Imgproc.*;

/**
 * @Author: zhengja
 * @Date: 2024-06-06 13:24
 */
@Deprecated // todo 未检测到内容
public class OpenCVONNXExample {

    public static void main(String[] args) {
        String onnxModelPath = "D:\\temp\\opencv\\dnn\\yolov8n.onnx"; // ONNX模型路径
        String imageInputPath = "D:\\temp\\opencv\\Images\\bus.jpg"; // input/image.jpg

        // 加载ONNX模型
        Net net = Dnn.readNetFromONNX(onnxModelPath);

        // 加载输入图像
        Mat image = imread(imageInputPath);

        if (image.empty()) {
            System.out.println("Failed to read image");
            return;
        }

        // 图像预处理
        Mat blob = Dnn.blobFromImage(image, 1.0, new Size(224, 224), new Scalar(104, 117, 123), false, false);

        // 设置输入数据
        net.setInput(blob);

        // 运行前向传播
        Mat result = net.forward();

        // 解析结果
        int numClasses = result.size(1);
        int numDetections = result.size(2);
        float confidenceThreshold = 0.5f;

        for (int i = 0; i < numDetections; ++i) {
            // FloatPointer dataPtr = new FloatPointer(result.ptr(0, 0, i));
            FloatPointer dataPtr = new FloatPointer(result.put(0, 0, i));
            float confidence = dataPtr.get(2);

            if (confidence > confidenceThreshold) {
                int classId = (int) (dataPtr.get(1) - 1);
                int left = (int) (dataPtr.get(3) * image.cols());
                int top = (int) (dataPtr.get(4) * image.rows());
                int right = (int) (dataPtr.get(5) * image.cols());
                int bottom = (int) (dataPtr.get(6) * image.rows());

                // 绘制结果标注框
                rectangle(image, new Point(left, top), new Point(right, bottom), new Scalar(0, 255, 0), 2);

                // 输出结果
                String label = "Class: " + classId + ", Confidence: " + confidence;
                putText(image, label, new Point(left, top - 10), FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0), 1);
            }
        }

        // 保存结果图像
        imwrite("output.jpg", image);
    }
}

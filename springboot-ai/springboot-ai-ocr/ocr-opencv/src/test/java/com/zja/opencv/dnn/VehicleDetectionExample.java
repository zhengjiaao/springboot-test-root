package com.zja.opencv.dnn;

import org.junit.Test;
import org.opencv.core.*;
import org.opencv.dnn.Dnn;
import org.opencv.dnn.Net;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;

/**
 * @Author: zhengja
 * @Date: 2024-06-05 16:07
 */
@Deprecated // todo 未检测到内容
public class VehicleDetectionExample {

    static {
        // 加载OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    @Test
    public void test() {

        String imagePath = "D:\\temp\\opencv\\Images\\bus.jpg"; // input/image.jpg

        // 模型文件路径
        String modelConfiguration = "D:\\temp\\opencv\\dnn\\yolov3.cfg";
        String modelWeights = "D:\\temp\\opencv\\dnn\\yolov3.weights";
        String onnxModelPath = "D:\\temp\\opencv\\dnn\\yolov8n.onnx"; // ONNX模型路径

        // 加载模型
        Net net = Dnn.readNetFromDarknet(modelConfiguration, modelWeights);
        // Net net = Dnn.readNetFromONNX(onnxModelPath);

        // 加载图像
        Mat image = Imgcodecs.imread(imagePath);

        // 图像预处理
        Mat blob = Dnn.blobFromImage(image, 1.0, new Size(640, 640), new Scalar(0, 0, 0), true, false);

        // 设置输入
        net.setInput(blob);

        // 进行推理
        Mat detections = net.forward();
        System.out.println(detections);

        // 处理结果
        System.out.println(detections.rows());
        for (int i = 0; i < detections.rows(); i++) {
            Mat detection = detections.row(i);
            float confidence = (float) detection.get(0, 4)[0];
            // if (confidence > 0.5) { // 置信度阈值
            if (confidence > 0.7) { // 置信度阈值
                // // double[] box = detections.get(i, new Range(0, 4))[0];
                // double[] box = detections.get(i, 1);
                //
                // int classId = (int) box[0];
                // int centerX = (int) (box[0] * image.cols());
                // int centerY = (int) (box[1] * image.rows());
                // int width = (int) (box[2] * image.cols());
                // int height = (int) (box[3] * image.rows());
                // int left = centerX - width / 2;
                // int top = centerY - height / 2;
                //
                // int classId2 = (int) box[1];
                // // double confidence = data[2];
                // // double left = data[3] * image.cols();
                // // double top = data[4] * image.rows();
                // // double right = data[5] * image.cols();
                // // double bottom = data[6] * image.rows();
                //
                // System.out.println(classId);
                // System.out.println(classId2);
                //
                // System.out.println();
                //
                // // 绘制边界框
                // Imgproc.rectangle(image, new Point(left, top), new Point(left + width, top + height), new Scalar(0, 255, 0), 2);
                //
                // // 绘制类别标签
                // String label = "car";
                // Imgproc.putText(image, label, new Point(left, top - 10), Imgproc.FONT_HERSHEY_SIMPLEX, 0.9, new Scalar(0, 255, 0), 2);

                int classId = (int) detections.get(i, 1)[0];
                double[] box = new double[4];

                // Retrieve the bounding box coordinates
                box[0] = detections.get(i, 0)[0];
                box[1] = detections.get(i, 1)[0];
                box[2] = detections.get(i, 2)[0];
                box[3] = detections.get(i, 3)[0];

                double centerX = box[0] * image.cols();
                double centerY = box[1] * image.rows();
                double width = box[2] * image.cols();
                double height = box[3] * image.rows();
                double left = centerX - width / 2;
                double top = centerY - height / 2;
                double right = left + width;
                double bottom = top + height;

                // 绘制边界框
                Imgproc.rectangle(image, new Point(left, top), new Point(right, bottom), new Scalar(0, 255, 0), 2);
            }
        }

        // 输出结果
        Imgcodecs.imwrite("target/image.jpg", image);

        // 显示结果
        HighGui.imshow("YOLO Detection", image);
        HighGui.waitKey();
    }
}

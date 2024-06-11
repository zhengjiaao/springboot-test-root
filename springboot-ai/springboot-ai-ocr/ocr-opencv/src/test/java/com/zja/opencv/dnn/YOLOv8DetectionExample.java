// package com.zja.opencv.dnn;
//
// import org.opencv.core.*;
// import org.opencv.dnn.*;
// import org.opencv.highgui.HighGui;
// import org.opencv.imgcodecs.Imgcodecs;
// import org.opencv.imgproc.Imgproc;
//
// import java.util.ArrayList;
// import java.util.List;
//
// /**
//  * @Author: zhengja
//  * @Date: 2024-06-05 18:12
//  */
// public class YOLOv8DetectionExample {
//     public static void main(String[] args) {
//         // Load OpenCV native library
//         System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
//
//         // Model files path
//         String modelWeights = "path/to/yolov4.weights";
//         String modelConfig = "path/to/yolov4.cfg";
//         String classLabels = "path/to/coco.names";
//
//         // Load class labels
//         List<String> labels = Utils.readFileLines(classLabels);
//
//         // Load the model
//         Net net = Dnn.readNetFromDarknet(modelConfig, modelWeights);
//
//         // Load the image
//         Mat image = Imgcodecs.imread("path/to/input/image.jpg");
//
//         // Preprocess the image
//         Mat blob = Dnn.blobFromImage(image, 1.0, new Size(416, 416), new Scalar(0, 0, 0), true, false);
//
//         // Set the input
//         net.setInput(blob);
//
//         // Perform inference
//         List<Mat> outs = new ArrayList<>();
//         List<String> outNames = net.getUnconnectedOutLayersNames();
//         net.forward(outs, outNames);
//
//         // Process the results
//         float confThreshold = 0.5f; // Confidence threshold
//         float nmsThreshold = 0.4f; // Non-maximum suppression threshold
//
//         List<Integer> classIds = new ArrayList<>();
//         List<Float> confidences = new ArrayList<>();
//         List<Rect> boxes = new ArrayList<>();
//
//         for (Mat out : outs) {
//             for (int i = 0; i < out.rows(); i++) {
//                 Mat detection = out.row(i);
//                 float confidence = (float) detection.get(0, 4)[0];
//
//                 if (confidence > confThreshold) {
//                     int classId = (int) detection.get(0, 1)[0];
//                     double[] box = detection.get(0, 0);
//
//                     int centerX = (int) (box[0] * image.cols());
//                     int centerY = (int) (box[1] * image.rows());
//                     int width = (int) (box[2] * image.cols());
//                     int height = (int) (box[3] * image.rows());
//
//                     int left = centerX - width / 2;
//                     int top = centerY - height / 2;
//
//                     classIds.add(classId);
//                     confidences.add(confidence);
//                     boxes.add(new Rect(left, top, width, height));
//                 }
//             }
//         }
//
//         // Apply non-maximum suppression
//         float[] confArr = ArrayUtils.toPrimitive(confidences.toArray(new Float[0]));
//         int[] indices = Dnn.NMSBoxes(boxes.toArray(new Rect[0]), confArr, confThreshold, nmsThreshold);
//
//         // Draw bounding boxes and labels
//         for (int idx : indices) {
//             Rect box = boxes.get(idx);
//             int classId = classIds.get(idx);
//             String label = labels.get(classId);
//             float confidence = confidences.get(idx);
//
//             Imgproc.rectangle(image, box.tl(), box.br(), new Scalar(0, 255, 0), 2);
//             Imgproc.putText(image, label + " " + confidence, box.tl(), Core.FONT_HERSHEY_SIMPLEX, 0.5, new Scalar(0, 255, 0), 2);
//         }
//
//         // Display the result
//         HighGui.imshow("YOLOv4 Detection", image);
//         HighGui.waitKey();
//     }
// }

# ocr-opencv

- [opencv.org 官网下载](https://opencv.org/releases/)
- [maven search?q=opencv](https://mvnrepository.com/search?q=opencv)
- [haarcascades Haar级联分类器的XML文件](https://github.com/opencv/opencv/blob/4.x/data/haarcascades)

**说明**

OpenCV是一个流行的计算机视觉库，它提供了许多图像处理和计算机视觉算法的功能，包括图像读取、处理、分析和显示等。

OpenCV（Open Source Computer Vision
Library）是一个开源的计算机视觉和机器学习软件库。它提供了广泛的图像和视频处理函数和算法，包括图像滤波、特征检测和提取、物体识别、相机标定等。OpenCV使用C++编写，并提供了Python和Java等多种编程语言的绑定。

以下是OpenCV的一些主要特点和功能：

1. 图像和视频输入输出：OpenCV允许您读取和写入各种格式的图像和视频，包括常见格式如JPEG、PNG和MPEG。
2. 图像处理：OpenCV提供了丰富的图像处理函数，如图像滤波（模糊、锐化等）、阈值处理、形态学操作（腐蚀、膨胀）和颜色空间转换等。
3. 特征检测和提取：OpenCV包括用于检测和提取各种图像特征的算法，如角点（Harris角点检测、Shi-Tomasi算法）、边缘（Canny边缘检测）和关键点（SIFT、SURF、ORB等）。
4. 物体识别：OpenCV支持物体识别任务，包括模板匹配、Haar级联和基于机器学习的技术，如支持向量机（SVM）和深度学习框架（如TensorFlow和PyTorch集成）。
5. 相机标定：OpenCV提供了相机标定函数，可以估计相机的内参数和外参数矩阵、畸变系数和矫正变换等。这对于三维重建和增强现实等任务非常有用。
6. 机器学习：OpenCV与TensorFlow和PyTorch等机器学习框架集成，在各种计算机视觉任务中可以利用深度学习模型，如图像分类、目标检测和语义分割。
7. GPU加速：OpenCV通过CUDA和OpenCL等框架支持GPU加速，可以在兼容的硬件上加快计算密集型任务的执行速度。
8. OpenCV拥有庞大而活跃的社区，为其开发提供支持，并提供丰富的资源，包括教程、示例代码和支持论坛。它在学术界和工业界被广泛应用于各种计算机视觉应用，如机器人技术、监控、自动驾驶车辆、医学成像等。

## OpenCV在实际应用中的一些案例

1. 人脸检测和识别：OpenCV提供了强大的人脸检测和识别功能，可以用于人脸识别门禁系统、人脸标签和表情识别应用等。
2. 物体检测和跟踪：OpenCV的物体检测和跟踪算法可以用于实时视频监控、自动驾驶中的障碍物检测、运动跟踪等应用。
3. 图像拼接和全景图生成：OpenCV的图像拼接功能可以将多张图片拼接成一张大的全景图，广泛应用于虚拟旅游、地图制作等领域。
4. 视频流分析：OpenCV可以对实时视频流进行分析，例如车牌识别、行人计数、姿态估计等，可用于交通监控、人流统计等应用。
5. 增强现实（AR）：OpenCV提供了相机标定和姿态估计等功能，可以用于增强现实应用，如虚拟物体放置、人脸滤镜等。
6. 医学图像处理：OpenCV在医学图像处理中被广泛应用，如医学图像分割、肿瘤检测、病变识别等，有助于医生进行诊断和治疗决策。
7. 视觉SLAM（同时定位与地图构建）：OpenCV可以用于视觉SLAM，实现机器人和无人机的自主导航和环境建模。
8. 虹膜识别：OpenCV可以用于虹膜图像的特征提取和匹配，用于个人身份认证和安全访问控制。
9. 自动驾驶：OpenCV在自动驾驶领域中扮演着重要的角色，用于环境感知、障碍物检测、车道线识别和跟踪等任务。
10. 机器人导航：OpenCV可用于机器人导航与定位，包括地标识别、地图构建和路径规划等。
11. 人机交互：OpenCV可以用于手势识别和追踪，从而实现基于手势的人机交互，如手势控制电脑、虚拟现实交互等。
12. 虚拟化与增强现实：OpenCV可用于虚拟现实和增强现实应用，如虚拟化试衣间、虚拟游戏角色等。
13. 图像修复与恢复：OpenCV的图像修复算法可用于修复老旧照片、去除图像噪声、恢复受损图像等。
14. 文字检测与识别：OpenCV提供了文字检测和识别的功能，可应用于自动化文档处理、光学字符识别（OCR）等。
15. 视频分析与编辑：OpenCV可以用于视频分析和编辑，如视频稳定、运动检测、视频特效等。
16. 艺术创作与图像处理：OpenCV可以用于艺术创作、图像风格迁移、图像滤镜等应用，探索图像处理的创造性应用。

## OpenCV Haar级联分类器HaarCascades

OpenCV提供了一系列的Haar级联分类器（Haar cascades），用于对象检测和识别。以下是一些常见的Haar级联分类器：

- [获取OpenCV Haar级联分类器的XML文件](https://github.com/opencv/opencv/blob/4.x/data/haarcascades)

1. 关于人体检测的Haar级联分类器：

* 人脸检测 (haarcascade_frontalface_default.xml, haarcascade_frontalface_alt.xml, haarcascade_frontalface_alt2.xml):
    * 用于检测图像或视频中的人脸。
    * 应用：人脸识别、表情分析、年龄和性别识别、情绪检测。
* 眼睛检测 (haarcascade_eye.xml, haarcascade_eye_tree_eyeglasses.xml):
    * 用于检测人脸中的眼睛位置，包括戴眼镜的情况。
    * 应用：眼部追踪、眨眼检测、疲劳驾驶预警、面部表情分析。
* 眼睛和眼镜检测 (haarcascade_eye_tree_eyeglasses.xml):
    * 专门检测佩戴眼镜的人脸中的眼睛。
    * 应用：眼镜佩戴者的人脸识别和分析。
* 眉毛检测 (haarcascade_mcs_eyebrow.xml):
    * 用于检测人脸中的眉毛。
    * 应用：面部特征识别、表情分析。
* 鼻子检测 (haarcascade_mcs_nose.xml):
    * 用于检测人脸中的鼻子位置。
    * 应用：面部特征提取、表情识别，表情分析、人机交互等领域。
* 嘴巴检测 (haarcascade_mouth.xml):
    * 用于检测人脸中的嘴巴位置。
    * 应用：表情分析、语音识别配合。
* 笑脸检测 (haarcascade_smile.xml):
    * 用于检测人脸中的微笑表情。
    * 应用：情感分析、人机交互、表情识别。
* 上半身检测 (haarcascade_upperbody.xml):
    * 用于检测图像或视频中的上半身，包括头部、肩膀和胸部等。
    * 应用：行人检测、监控系统、人体姿态估计、运动分析。
* 下半身检测 (haarcascade_lowerbody.xml):
    * 用于检测图像或视频中的下半身，如腿部。
    * 应用：行人检测、人体姿态估计、行人跟踪、运动分析。
* 全身检测 (haarcascade_fullbody.xml):
    * 用于检测图像或视频中的全身部分，包括头部、肩膀、胸部和腿部等。
    * 应用：行人检测、人体姿态估计、运动分析、行人跟踪等应用。
* 头部检测 (haarcascade_head.xml):
    * 用于检测图像或视频中的头部。
    * 应用：人脸识别、头部追踪、虚拟现实、手部检测。
* 行人检测 (haarcascade_pedestrian.xml):
    * 用于检测图像或视频中的行人。
    * 应用：智能交通监控、安全系统、人流量统计。
* 耳朵检测（haarcascade_mcs_leftear.xml和haarcascade_mcs_rightear.xml）：用于检测人脸中左右耳朵的位置。
* 人脸轮廓检测（haarcascade_profileface.xml）：用于检测人脸的侧面轮廓。

2. 关于动物检测的Haar级联分类器：

* 动物检测 (haarcascade_frontalcatface.xml, haarcascade_frontaldogface.xml):
    * 用于检测图像或视频中的猫脸和狗脸。
    * 应用：宠物识别、动物行为研究、动物保护。
* 坐着的动物检测 (haarcascade_sittingcat.xml, haarcascade_sittingdog.xml):
    * 用于检测图像或视频中坐着的猫和狗。
    * 应用：宠物行为分析、动物活动监测。
* 猫脸检测 (haarcascade_frontalcatface.xml):
    * 专门用于检测图像或视频中的猫脸。
    * 应用：猫咪识别、猫的行为研究。
* 猫眼检测 (haarcascade_eye.xml):
    * 虽然通用，但也可用于检测猫脸中的眼睛位置。
    * 应用：猫的视觉行为分析、猫的面部特征识别。
* 狗脸检测 (haarcascade_frontalface_alt2.xml):
    * 用于检测图像或视频中的狗脸。
    * 应用：狗狗识别、狗的行为研究。

3. 关于车辆检测的Haar级联分类器：

* 车辆检测 (haarcascade_car.xml):
    * 用于检测图像或视频中的车辆。
    * 应用：交通监控、智能驾驶、停车场管理、车牌识别、车辆计数。
* 车牌检测 (haarcascade_russian_plate_number.xml):
    * 专门用于检测俄罗斯车牌的位置。
    * 应用：交通管理、自动收费系统、车辆追踪。

4. 关于其他物体检测的Haar级联分类器：

* 前景检测 (haarcascade_fullbody.xml):
    * 用于检测图像或视频中的前景对象，包括行人和车辆。
    * 应用：智能安防、背景减除、物体追踪。
* 飞机检测 (haarcascade_frontalface_alt2.xml):
    * 通常用于人脸检测，但可能被改编或训练以检测飞机。
    * 应用：空中交通监控、无人机检测。
* 银行卡检测 (haarcascade_creditcard.xml):
    * 专门用于检测图像中的银行卡。
    * 应用：金融安全、自助服务终端、ATM监控。
* 其他对象检测:
    * 可根据具体场景和需求训练或获取特定对象的分类器，如自行车、摩托车、书本等。
    * 应用：智能安防、库存管理、文档识别、商品识别。

## 安装部署

- [opencv.org 官网下载 Windows](https://opencv.org/releases/)

双击`opencv-4.7.0-windows.exe`,得到目录`opencv`

配置系统环境变量(以java为例)：`E:\App\opencv-4.7.0\build\java\x64` ，主要是把opencv_java470.dll父目录配置环境变量中。

- [haarcascades Haar级联分类器的XML文件](https://github.com/opencv/opencv/blob/4.x/data/haarcascades)

## 代码示例

## 校验版本

```java
public class OpenCVExample {

    static {
        //先把 E:\App\opencv-4.7.0\build\java\x64\opencv_java470.dll 父目录添加到系统环境变量中。
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
    }

    public static void main(String[] args) {

        if ((1 == args.length) && (0 == args[0].compareTo("--build"))) {

            System.out.println(Core.getBuildInformation());
        } else if ((1 == args.length) && (0 == args[0].compareTo("--help"))) {

            System.out.println("\t--build\n\t\tprint complete build info");
            System.out.println("\t--help\n\t\tprint this help");
        } else {

            System.out.println("Welcome to OpenCV " + Core.VERSION);
        }
    }
}
```

## 简单图像监测

```java
public class OpenCVExample {
    @Test
    public void detectFace_v1() throws Exception {
        // 加载 OpenCV 库
        OpenCV.loadShared();

        // 读取图像
        String imagePath = "D:\\temp\\opencv\\Images\\people\\fuchouzhelianmeng\\1.png";  // 要检测的图片路径
        Mat image = Imgcodecs.imread(imagePath);

        // 加载人脸分类器
        //String haarFilePath = "D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_default.xml";
        //String haarFilePath = "D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt_tree.xml";
        //String haarFilePath = "D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt.xml";
        String haarFilePath = "D:\\temp\\opencv\\haarcascade\\haarcascade_frontalface_alt2.xml";
        CascadeClassifier faceDetector = new CascadeClassifier(haarFilePath);

        // 人脸检测
        MatOfRect faceDetections = new MatOfRect();
        faceDetector.detectMultiScale(image, faceDetections);

        // 绘制人脸框
        for (Rect rect : faceDetections.toArray()) {
            Imgproc.rectangle(image, new Point(rect.x, rect.y), new Point(rect.x + rect.width, rect.y + rect.height), new Scalar(0, 255, 0));
        }

        // 输出图像
        String outFile = "D:\\temp\\opencv\\Images\\people\\fuchouzhelianmeng\\1-detectFace.png";
        // 存储
        Imgcodecs.imwrite(outFile, image);

        // 显示结果
        HighGui.imshow("Detected Face", image);
        HighGui.waitKey();

        // 释放资源
        image.release();
    }

}
```












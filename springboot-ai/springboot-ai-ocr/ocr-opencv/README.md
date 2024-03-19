# ocr-opencv

- [opencv.org 官网下载](https://opencv.org/releases/)
- [maven search?q=opencv](https://mvnrepository.com/search?q=opencv)

**说明**

OpenCV是一个流行的计算机视觉库，它提供了许多图像处理和计算机视觉算法的功能，包括图像读取、处理、分析和显示等。

## 安装部署

- [opencv.org 官网下载 Windows](https://opencv.org/releases/)

双击`opencv-4.7.0-windows.exe`,得到目录`opencv`

配置系统环境变量(以java为例)：`E:\App\opencv-4.7.0\build\java\x64` ，主要是把opencv_java470.dll父目录配置环境变量中。

## 代码示例

```java
public class OpenCVExample {
    public static void main(String[] args) {
        //先把 E:\App\opencv-4.7.0\build\java\x64\opencv_java470.dll 父目录添加到系统环境变量中。

        // 加载本机OpenCV库
        System.loadLibrary(Core.NATIVE_LIBRARY_NAME);

        // 读取图像
        Mat image = Imgcodecs.imread("D:\\temp\\ocr\\input.png");

        // 显示图像
        HighGui.imshow("Original Image", image);

        // 等待按键事件
        HighGui.waitKey();

        // 关闭图像窗口
        HighGui.destroyAllWindows();
    }
}
```












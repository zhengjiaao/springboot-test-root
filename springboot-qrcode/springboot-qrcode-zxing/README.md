# springboot-qrcode-zxing

> ZXing 是一个开源的、多格式的一维/二维条码图像处理库

- [zxing 官网](https://github.com/zxing/zxing)


## 依赖引入

```xml
    <properties>
        <zxing.version>3.5.1</zxing.version>
    </properties>

        <!--zxing二维码依赖 方式二(推荐)-->
        <dependency>
            <groupId>com.google.zxing</groupId>
            <artifactId>core</artifactId>
            <version>${zxing.version}</version>
        </dependency>
        <dependency>
            <groupId>com.google.zxing</groupId>
            <artifactId>javase</artifactId>
            <version>${zxing.version}</version>
        </dependency>
```

## 简单示例

自定义工具类

```java
public class QRcodeZxingUtil {

    /**
     * 生成二维码图片
     * @param content 二维码中的数据
     * @param filePath 生成二维码的根路径   fileName文件名
     * @param deleteWhite 默认二维码边上是带有白边的，传true时会去掉白边
     */
    public static String generateQRcode(String content, String filePath, String fileName, boolean deleteWhite) throws WriterException, IOException {

        String fileAbsolutePath = filePath + fileName;

        int width = 200; // 图像宽度
        int height = 200; // 图像高度
        String format = "png";// 图像类型

        Map<EncodeHintType, Object> hints = new HashMap<EncodeHintType, Object>();
        hints.put(EncodeHintType.CHARACTER_SET, "UTF-8");
        // 生成矩阵
        BitMatrix bitMatrix = new MultiFormatWriter().encode(content,
                BarcodeFormat.QR_CODE, width, height, hints);
        //删除白边
        if (deleteWhite) {
            BufferedImage image = deleteQRcodeWhiteBorder(bitMatrix);
            File outputfile = new File(filePath + fileName);
            // 输出图像
            ImageIO.write(image, format, outputfile);
        } else {
            Path path = FileSystems.getDefault().getPath(filePath, fileName);
            // 输出图像
            MatrixToImageWriter.writeToPath(bitMatrix, format, path);
        }

        System.out.println("***********二维码生成成功！**********");
        return fileAbsolutePath;
    }

    /**
     * 读取二维码图片内容
     * @param filePath  文件的绝对路径 例如："D://zxing.png";
     */
    public static Result readQRcode(String filePath) throws NotFoundException, IOException {
        BufferedImage image = ImageIO.read(new File(filePath));
        LuminanceSource source = new BufferedImageLuminanceSource(image);
        Binarizer binarizer = new HybridBinarizer(source);
        BinaryBitmap binaryBitmap = new BinaryBitmap(binarizer);
        Map<DecodeHintType, Object> hints = new HashMap<DecodeHintType, Object>();
        hints.put(DecodeHintType.CHARACTER_SET, "UTF-8");
        // 对图像进行解码
        return new MultiFormatReader().decode(binaryBitmap, hints);
    }

    //去二维码白边
    private static BufferedImage deleteQRcodeWhiteBorder(BitMatrix matrix) {
        int[] rec = matrix.getEnclosingRectangle();
        int resWidth = rec[2] + 1;
        int resHeight = rec[3] + 1;

        BitMatrix resMatrix = new BitMatrix(resWidth, resHeight);
        resMatrix.clear();
        for (int i = 0; i < resWidth; i++) {
            for (int j = 0; j < resHeight; j++) {
                if (matrix.get(i + rec[0], j + rec[1]))
                    resMatrix.set(i, j);
            }
        }

        int width = resMatrix.getWidth();
        int height = resMatrix.getHeight();
        BufferedImage image = new BufferedImage(width, height,
                BufferedImage.TYPE_INT_RGB);
        for (int x = 0; x < width; x++) {
            for (int y = 0; y < height; y++) {
                image.setRGB(x, y, resMatrix.get(x, y) ? BLACK
                        : WHITE);
            }
        }
        return image;
    }

}
```

测试类

```java
    public static void main(String[] args) throws NotFoundException, IOException {

        //二位码 内容
        String content = "西秀区农村土地承包经营权证\n" +
                "权证编码：398881111222211J\n" +
                "发包方名称：轿子山镇大进村民委员会\n" +
                "承包方代表：杨井岗\n" +
                "确权总面积：12.33亩\n" +
                "地块总数: 13块";

        try {
            String fileName = "zxing.png";
            //1、自定义生成二维码
            QRcodeZxingUtil.generateQRcode(content, "D://", fileName, true);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (WriterException e) {
            e.printStackTrace();
        }

        //2、简单的生成二维码例子
        //String basepath= URLDecoder.decode("D:\\zxing.png","utf-8");

        //读取二位码 内容
        Result result = QRcodeZxingUtil.readQRcode("D:\\zxing.png");
        System.out.println(result.getText());
    }
```

# springboot-file-cad-gdal


### GDAL 安装(推荐)

* 下载：[下载 gisinternals](https://www.gisinternals.com/release.php) 
* 安装：[安装 gisinternals](https://blog.csdn.net/lou_csdn/article/details/137886271)

### GDAL官网安装(不推荐)

要在Java中使用GDAL，您需要按照以下步骤进行设置：

1. 官方网下载GDAL库进行安装：https://gdal.org/
2. 设置Java绑定：GDAL提供了Java绑定，以JNI（Java Native Interface）库的形式提供。在安装GDAL之后，您需要通过编译JNI库来设置Java绑定。此步骤可能因操作系统而异。
3. 创建Java项目：在您喜欢的IDE中或使用您喜欢的构建系统设置一个Java项目。确保在项目的类路径中包含GDAL Java绑定。
4. 导入GDAL类：在Java代码中，您可以导入GDAL类以访问GDAL功能。

## Java 使用 GDAL 实例

## 引入依赖

```xml

<dependencies>
    <dependency>
        <groupId>org.gdal</groupId>
        <artifactId>gdal</artifactId>
        <version>3.8.0</version>
    </dependency>
</dependencies>
```

简单示例：

```java
import org.gdal.gdal.Dataset;
import org.gdal.gdal.gdal;

public class GDALRasterReadExample {
    public static void main(String[] args) {
        // 初始化GDAL
        gdal.AllRegister();

        // 打开栅格数据文件
        String rasterFile = "path/to/raster.tif";
        Dataset rasterDataset = gdal.Open(rasterFile);

        // 获取栅格数据信息
        int width = rasterDataset.GetRasterXSize();
        int height = rasterDataset.GetRasterYSize();
        int bands = rasterDataset.getRasterCount();

        System.out.println("Width: " + width);
        System.out.println("Height: " + height);
        System.out.println("Bands: " + bands);

        // 关闭栅格数据文件
        rasterDataset.delete();
    }
}
```
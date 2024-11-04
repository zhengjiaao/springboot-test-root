# springboot-file-cad-aspose

- [aspose-cad-java 官网](https://releases.aspose.com/cad/java/)
- [aspose-cad-java github示例](https://github.com/aspose-cad/Aspose.CAD-for-Java/tree/master/Examples)

Aspose.CAD for Java 是一个用于处理 CAD 文件的 Java 库，支持读取和转换 AutoCAD DWG、DXF 和 DWF 文件。

Aspose.CAD for Java 是一种先进的CAD 转换原生 API，不依赖任何软件。它允许开发人员将 DXF、DWG、DWT、DGN、IFC、DWF、DWFX、STL、IGES(
IGS)、CF2、Collada(DAE)、PLT、OBJ、SVG、DXB、FBX、U3D、3DS、STP 文件转换为光栅图像格式和 PDF。在转换过程中，开发人员可以选择和转换特定的图层和布局，并跟踪文件转换过程。

仅输出:

固定矢量布局： PDF、EMF、WMF

光栅图像： PNG、BMP、TIFF、JPEG、JP2、PSD、DICOM、WEBP、GIF

主要功能：

* 文件读取：支持读取 DWG、DXF 和 DWF 格式的文件。
* 文件转换：可以将 CAD 文件转换为其他格式，如 PDF、JPEG、PNG、BMP 等。
* 元数据操作：可以读取和修改 CAD 文件的元数据。
* 图层操作：支持图层的显示和隐藏。
* 性能优化：提供了高效的文件处理和转换性能。

注意事项：许可证：Aspose.CAD for Java 是一个商业库，需要购买许可证才能在生产环境中使用。

## CAD 概念

关系总结

* 图像 (Image) 包含多个 图层 (Layer) 和 实体 (Entity)。
* 图层 (Layer) 可以控制实体 (Entity) 的可见性和颜色。
* 实体 (Entity) 可以属于一个 图层 (Layer)，并且可以包含 属性 (Attribute)。
* 块 (Block) 是一组 实体 (Entity) 的集合，可以通过 插入 (Insert) 操作在不同的位置重复使用。
* 属性 (Attribute) 可以附加在 实体 (Entity) 上，用于存储额外的信息。

1. 图像 (Image)
    * 在 CAD 系统中，图像通常指的是整个 CAD 文件的内容。它包含了所有的几何图形、文字、图层、块等元素。在 Aspose.CAD
      库中，CadImage 类代表了一个 CAD 文件的图像对象。这个类提供了对 CAD 文件中所有元素的访问和操作方法。
2. 实体 (Entity)
    * 实体是 CAD 文件中最基本的几何对象，如线条、圆、弧、多段线、文本等。每个实体都有其特定的属性和方法，用于定义其几何形状、位置、大小等。在
      Aspose.CAD 库中，CadEntity 是所有实体的基类，具体的实体类型（如 CadLine、CadCircle、CadText 等）继承自 CadEntity。
    * 示例实体：
    * CadLine：表示一条直线。
    * CadCircle：表示一个圆。
    * CadArc：表示一段圆弧。
    * CadPolyline：表示多段线。
    * CadText：表示文本。
3. 图层 (Layer)
    * 图层是组织和管理 CAD 文件中实体的一种方式。每个实体都属于一个图层，图层可以控制实体的可见性、颜色、线型等属性。图层的概念类似于绘图软件中的图层，可以方便地对不同类型的实体进行分组和管理。
    * 在 Aspose.CAD 库中，CadLayer 类代表了一个图层对象。每个图层都有一个唯一的名称，并且可以设置颜色、线型、是否可见等属性。
    * 示例属性：
    * Name：图层的名称。
    * ColorIndex：图层的颜色索引。
    * Linetype：图层的线型。
    * Visible：图层是否可见。
4. 属性 (Attribute)
    * 属性是附加在实体上的额外信息，用于存储与实体相关的数据。属性可以是文本、数字或其他类型的数据。在 CAD
      文件中，属性通常用于标注、注释或存储元数据。
    * 在 Aspose.CAD 库中，CadAttributeDefinition 和 CadAttribute 类分别用于定义属性和实例化属性。属性定义了属性的名称、标签、默认值等，而属性实例则包含具体的值。
    * 示例属性：
    * Tag：属性的标签。
    * Value：属性的值。
    * Prompt：属性的提示文本。
5. 块 (Block)
    * 块是 CAD 文件中的一组实体，可以作为一个整体进行插入、复制和移动。块可以包含任何类型的实体，并且可以在不同的位置多次插入。块的概念类似于模板，可以提高绘图效率和一致性。
    * 在 Aspose.CAD 库中，CadBlock 类代表了一个块对象。每个块都有一个唯一的名称，并且可以包含多个实体。
    * 示例属性：
    * Name：块的名称。
    * Entities：块中包含的实体列表。
6. 插入 (Insert)
    * 插入是指将一个块插入到 CAD 文件中的某个位置。插入可以指定块的位置、比例和旋转角度。插入操作使得块可以在不同的位置重复使用，而不需要重新绘制相同的实体。
    * 在 Aspose.CAD 库中，CadInsert 类代表了一个插入对象。插入对象包含了块的名称、插入点、比例和旋转角度等属性。
    * 示例属性：
    * Name：插入的块的名称。
    * InsertionPointX：插入点的 X 坐标。
    * InsertionPointY：插入点的 Y 坐标。
    * ScaleX：X 方向的比例。
    * ScaleY：Y 方向的比例。
    * RotationAngle：旋转角度。

## 快速开始

```xml

<dependency>
    <groupId>com.aspose</groupId>
    <artifactId>aspose-cad</artifactId>
    <!--<version>23.3</version>-->
    <version>23.9</version>
    <!--<version>24.4</version>-->
</dependency>
```

代码示例：

```java
import com.aspose.cad.Image;
import com.aspose.cad.imageoptions.JpegOptions;

public class CadToJpeg {
    public static void main(String[] args) {
        // 加载 DWG 文件
        Image image = Image.load("input.dwg");

        // 设置 JPEG 保存选项
        JpegOptions saveOptions = new JpegOptions();
        saveOptions.setQuality(100);

        // 保存为 JPEG 文件
        image.save("output.jpeg", saveOptions);
    }
}

```
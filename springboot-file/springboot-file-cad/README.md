# springboot-file-cad

CAD是计算机辅助设计（Computer Aided Design）的英文缩写，指的是利用计算机及其图形设备帮助设计人员进行设计工作的一种技术‌。CAD软件可以用于二维绘图、详细绘制、设计文档和基本三维设计，已经成为国际上广为流行的绘图工具。CAD在工程和产品设计中，可以帮助设计人员进行计算、信息存储和制图等工作，能够快速地对不同方案进行大量的计算、分析和比较，以决定最优方案。‌

CAD软件在机械、建筑、服装、电子等各个领域都有广泛的应用，成为国际制图设计的标准软件。例如，在建筑行业中，CAD软件被制图工程师用于绘制和修改建筑图纸；在机械制造中，CAD软件用于设计产品的结构和外观；在电子领域，CAD软件则用于电路设计和仿真。

## CAD文件类型

#### CAD文件的主要格式

1. DWG：由AutoCAD开发并广泛使用的CAD图纸格式，是二维CAD的标准格式，很多其他CAD软件为了兼容AutoCAD也使用DWG作为默认工作文件格式。
2. DXF：绘图交换文件，是一种标准的文本文件，主要用于与其他软件进行数据交互，可以被多种CAD软件所支持，是一种比较通用的文件格式。
3. DWF：用于网络交换的图形文件格式，可以通过互联网方便地共享和查看文件内容。
4. PDF：一种可以在各种操作系统和软件平台上查看的通用文件格式，适合与非CAD专业人士共享图纸。
5. STL：用于三维打印的文件格式，包含三角形网格表示的几何图形。
6. IGES：用于CAD数据交换的标准格式，可以在不同的CAD系统之间进行模型交换。
7. STEP：用于三维CAD模型的开放格式，可以存储几何图形、拓扑信息和属性。

#### 每种格式的特点和应用场景

1. DWG：适用于AutoCAD及其兼容软件，是二维CAD的标准格式，常用于存储详细的二维和三维图纸信息。
2. DXF：主要用于与其他软件进行数据交互，适合在多个CAD系统之间交换数据。
3. DWF：适用于通过网络共享和查看CAD图纸，无需安装CAD软件。
4. PDF：适用于与非CAD专业人士共享图纸，可以在各种操作系统和软件平台上查看。
5. STL：适用于三维打印，用于存储三维模型的三角形网格数据。
6. IGES：适用于不同CAD系统之间的数据交换，可以存储几何图形、属性和拓扑信息。
7. STEP：适用于三维CAD模型的数据交换，可以存储详细的几何、拓扑和属性信息。

## CAD 文件的结构

CAD 文件的结构复杂且层次分明，主要包括文件头、图层、实体、块、插入、属性、视图、表格和对象字典等部分。这些部分相互关联，共同构成了
CAD 文件的完整内容。理解这些结构和关系有助于更好地操作和处理 CAD 文件。

1. 文件头 (File Header)
    * 文件头包含了 CAD 文件的基本信息和元数据，如文件版本、创建日期、单位系统等。这些信息对于解析和处理 CAD 文件非常重要。
    * 主要字段：
    * ACADVER：AutoCAD 文件版本。
    * DWGCODEPAGE：字符编码页。
    * INSBASE：插入基点。
    * LIMMIN 和 LIMMAX：图纸界限。
    * UNITS：单位系统。
2. 图层 (Layers)
    * 图层是组织和管理 CAD 文件中实体的一种方式。每个实体都属于一个图层，图层可以控制实体的可见性、颜色、线型等属性。
    * 主要字段：
    * Name：图层的名称。
    * Color：图层的颜色。
    * Linetype：图层的线型。
    * Lineweight：图层的线宽。
    * PlotStyleName：图层的打印样式。
    * Visibility：图层是否可见。
3. 实体 (Entities)
    * 实体是 CAD 文件中最基本的几何对象，如线条、圆、弧、多段线、文本等。每个实体都有其特定的属性和方法，用于定义其几何形状、位置、大小等。
    * 常见实体类型：
    * LINE：直线。
    * CIRCLE：圆。
    * ARC：圆弧。
    * POLYLINE：多段线。
    * TEXT：文本。
    * DIMENSION：尺寸标注。
    * BLOCK：块。
    * INSERT：插入。
    * 实体属性：
    * Layer：所属图层。
    * Linetype：线型。
    * Color：颜色。
    * Thickness：厚度。
    * Elevation：高程。
    * StartPoint 和 EndPoint：起点和终点（适用于线）。
    * Center：中心点（适用于圆和圆弧）。
    * Radius：半径（适用于圆和圆弧）。
    * TextString：文本内容（适用于文本）。
4. 块 (Blocks)
    * 块是一组实体的集合，可以作为一个整体进行插入、复制和移动。块可以包含任何类型的实体，并且可以在不同的位置多次插入。
    * 主要字段：
    * Name：块的名称。
    * BasePoint：块的基点。
    * Entities：块中包含的实体列表。
5. 插入 (Inserts)
    * 插入是指将一个块插入到 CAD 文件中的某个位置。插入可以指定块的位置、比例和旋转角度。插入操作使得块可以在不同的位置重复使用，而不需要重新绘制相同的实体。
    * 主要字段：
    * BlockName：插入的块的名称。
    * InsertionPoint：插入点。
    * ScaleFactors：比例因子（X、Y、Z 方向）。
    * RotationAngle：旋转角度。
    * Attributes：插入的属性列表。
6. 属性 (Attributes)
    * 属性是附加在实体上的额外信息，用于存储与实体相关的数据。属性可以是文本、数字或其他类型的数据。在 CAD
      文件中，属性通常用于标注、注释或存储元数据。
    * 主要字段：
    * Tag：属性的标签。
    * Value：属性的值。
    * Prompt：属性的提示文本。
    * InsertionPoint：属性的插入点。
    * Height：属性的高度。
    * Justification：属性的对齐方式。
7. 视图 (Views)
    * 视图定义了 CAD 文件中不同视角的显示方式。视图可以包含不同的缩放比例、中心点和视图方向。
    * 主要字段：
    * Name：视图的名称。
    * ViewCenter：视图中心点。
    * ViewHeight：视图高度。
    * ViewTarget：视图目标点。
    * ViewDirection：视图方向。
8. 表格 (Tables)
    * 表格包含了各种定义和设置，如线型表、图层表、文本样式表等。这些表格用于管理和引用 CAD 文件中的各种资源。
    * 常见表格：
    * Linetypes：线型表。
    * Layers：图层表。
    * TextStyles：文本样式表。
    * Dimstyles：尺寸样式表。
9. 对象字典 (Object Dictionary)
    * 对象字典是一个特殊的表格，用于存储和管理 CAD 文件中的各种对象。对象字典中的对象可以是任何类型，如块、图层、线型等。
    * 主要字段：
    * Name：对象的名称。
    * Handle：对象的唯一标识符。
    * Type：对象的类型。

### CAD 结构示意图

```text
CAD 文件
├── 文件头 (File Header)
│   ├── ACADVER
│   ├── DWGCODEPAGE
│   ├── INSBASE
│   ├── LIMMIN
│   ├── LIMMAX
│   └── UNITS
├── 图层 (Layers)
│   ├── Layer1
│   │   ├── Name
│   │   ├── Color
│   │   ├── Linetype
│   │   ├── Lineweight
│   │   ├── PlotStyleName
│   │   └── Visibility
│   └── Layer2
│       ├── ...
├── 实体 (Entities)
│   ├── Line1
│   │   ├── Layer
│   │   ├── Linetype
│   │   ├── Color
│   │   ├── Thickness
│   │   ├── Elevation
│   │   ├── StartPoint
│   │   └── EndPoint
│   ├── Circle1
│   │   ├── ...
│   └── Text1
│       ├── ...
├── 块 (Blocks)
│   ├── Block1
│   │   ├── Name
│   │   ├── BasePoint
│   │   └── Entities
│   │       ├── Line1
│   │       ├── Circle1
│   │       └── Text1
│   └── Block2
│       ├── ...
├── 插入 (Inserts)
│   ├── Insert1
│   │   ├── BlockName
│   │   ├── InsertionPoint
│   │   ├── ScaleFactors
│   │   ├── RotationAngle
│   │   └── Attributes
│   │       ├── Attribute1
│   │       │   ├── Tag
│   │       │   ├── Value
│   │       │   ├── Prompt
│   │       │   ├── InsertionPoint
│   │       │   ├── Height
│   │       │   └── Justification
│   │       └── Attribute2
│   │           ├── ...
│   └── Insert2
│       ├── ...
├── 视图 (Views)
│   ├── View1
│   │   ├── Name
│   │   ├── ViewCenter
│   │   ├── ViewHeight
│   │   ├── ViewTarget
│   │   └── ViewDirection
│   └── View2
│       ├── ...
├── 表格 (Tables)
│   ├── Linetypes
│   │   ├── Linetype1
│   │   ├── Linetype2
│   │   └── ...
│   ├── Layers
│   │   ├── Layer1
│   │   ├── Layer2
│   │   └── ...
│   ├── TextStyles
│   │   ├── TextStyle1
│   │   ├── TextStyle2
│   │   └── ...
│   └── Dimstyles
│       ├── Dimstyle1
│       ├── Dimstyle2
│       └── ...
└── 对象字典 (Object Dictionary)
    ├── Object1
    │   ├── Name
    │   ├── Handle
    │   └── Type
    └── Object2
        ├── ...
```
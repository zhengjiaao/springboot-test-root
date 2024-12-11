# springboot-file-word-poi-tl

- [poi_tl 官网](https://deepoove.com/poi-tl/#_why_poi_tl)

POI-TL（POI-Template）是一个基于Apache POI的模板引擎，可以通过简化的方式操作Word文档。它提供了一种更便捷的方式来生成和编辑Word文档，支持模板变量替换、循环插入、条件判断等功能。

poi-tl（poi template language）是Word模板引擎，使用模板和数据**创建很棒的Word文档**。

> 在文档的任何地方做任何事情（*Do Anything Anywhere*）是poi-tl的星辰大海。

## [1. Why poi-tl](https://deepoove.com/poi-tl/#_why_poi_tl)

| 方案           | 移植性                       | 功能性                                                  | 易用性                                                       |
| :------------- | :--------------------------- | :------------------------------------------------------ | :----------------------------------------------------------- |
| **Poi-tl**     | Java跨平台                   | Word模板引擎，基于Apache POI，提供更友好的API           | 低代码，准备文档模板和数据即可                               |
| Apache POI     | Java跨平台                   | Apache项目，封装了常见的文档操作，也可以操作底层XML结构 | 文档不全，这里有一个教程：[Apache POI Word快速入门](http://deepoove.com/poi-tl/apache-poi-guide.html) |
| Freemarker     | XML跨平台                    | 仅支持文本，很大的局限性                                | 不推荐，XML结构的代码几乎无法维护                            |
| OpenOffice     | 部署OpenOffice，移植性较差   | -                                                       | 需要了解OpenOffice的API                                      |
| HTML浏览器导出 | 依赖浏览器的实现，移植性较差 | HTML不能很好的兼容Word的格式，样式糟糕                  | -                                                            |
| Jacob、winlib  | Windows平台                  | -                                                       | 复杂，完全不推荐使用                                         |

**poi-tl**是一个基于Apache POI的Word模板引擎，也是一个免费开源的Java类库，你可以非常方便的加入到你的项目中，并且拥有着让人喜悦的特性。

| Word模板引擎功能     | 描述                                                         |
| :------------------- | :----------------------------------------------------------- |
| 文本                 | 将标签渲染为文本                                             |
| 图片                 | 将标签渲染为图片                                             |
| 表格                 | 将标签渲染为表格                                             |
| 列表                 | 将标签渲染为列表                                             |
| 图表                 | 条形图（3D条形图）、柱形图（3D柱形图）、面积图（3D面积图）、折线图（3D折线图）、雷达图、饼图（3D饼图）、散点图等图表渲染 |
| If Condition判断     | 根据条件隐藏或者显示某些文档内容（包括文本、段落、图片、表格、列表、图表等） |
| Foreach Loop循环     | 根据集合循环某些文档内容（包括文本、段落、图片、表格、列表、图表等） |
| Loop表格行           | 循环复制渲染表格的某一行                                     |
| Loop表格列           | 循环复制渲染表格的某一列                                     |
| Loop有序列表         | 支持有序列表的循环，同时支持多级列表                         |
| Highlight代码高亮    | word中代码块高亮展示，支持26种语言和上百种着色样式           |
| Markdown             | 将Markdown渲染为word文档                                     |
| Word批注             | 完整的批注功能，创建批注、修改批注等                         |
| Word附件             | Word中插入附件                                               |
| SDT内容控件          | 内容控件内标签支持                                           |
| Textbox文本框        | 文本框内标签支持                                             |
| 图片替换             | 将原有图片替换成另一张图片                                   |
| 书签、锚点、超链接   | 支持设置书签，文档内锚点和超链接功能                         |
| Expression Language  | 完全支持SpringEL表达式，可以扩展更多的表达式：OGNL, MVEL…    |
| 样式                 | 模板即样式，同时代码也可以设置样式                           |
| 模板嵌套             | 模板包含子模板，子模板再包含子模板                           |
| 合并                 | Word合并Merge，也可以在指定位置进行合并                      |
| 用户自定义函数(插件) | 插件化设计，在文档任何位置执行函数                           |

## poi-tl 与 poi、jdk版本

- 1.11.x Documentation，Apache POI5.1.0+，JDK1.8+
- 1.10.x Documentation，Apache POI4.1.2，JDK1.8+
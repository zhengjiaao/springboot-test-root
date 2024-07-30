# SuperMap iObjects Java 组件帮助文档

- [SuperMap 技术资源中心](http://support.supermap.com.cn/product/iObjects.aspx)
- [SuperMap iObjects Java 组件帮助文档 - 参考开发环境配置 ](http://support.supermap.com.cn/DataWarehouse/WebDocHelp/iObjectsJava/index.html)

## 开发环境配置

### 安装环境

安装步骤：

1、下载SuperMap iObjects Java
11i ， [SuperMap 技术资源中心-软件下载-SuperMap iObjects Java 11i](http://support.supermap.com.cn/product/iObjects.aspx)

2、解压SuperMap iObjects Java 11i，解压路径 例如：D:\LocalDevEnv\SuperMap\iObjects\iObjectsJava\Bin

3、配置环境变量 PATH=C:\jdk\bin;D:\LocalDevEnv\SuperMap\iObjects\iObjectsJava\Bin

4、验证安装成功


### 模块介绍

1、获取模块

添加 JAR：用来将项目内的 jar 包加入到库引用中。首先在项目内创建lib文件夹，然后将组件产品提供的 jar 文件（位于安装目录的`iObjectsJava\Bin`文件夹内）拷贝到 lib 文件夹内，最后通过“添加 JAR”按钮将这些 jar 包加入到库引用中。

把所需的jar包安装到本地仓库：

```shell
# 把所需的jar包安装到本地仓库
mvn install:install-file -Dfile=lib/iObjects.jar -DgroupId=com.supermap.iObjects -DartifactId=iObjects -Dversion=11.1.1 -Dpackaging=jar

mvn install:install-file -Dfile='com.supermap.data.jar' -DgroupId='com.supermap.data' -DartifactId='data' -Dversion='11.1.1' -Dpackaging='jar'

mvn install:install-file -Dfile='com.supermap.data.conversion.jar' -DgroupId='com.supermap.data' -DartifactId='conversion' -Dversion='11.1.1' -Dpackaging='jar'

mvn install:install-file -Dfile='com.supermap.data.cloudlicense.jar' -DgroupId='com.supermap.data' -DartifactId='cloudlicense' -Dversion='11.1.1' -Dpackaging='jar'

mvn install:install-file -Dfile='com.supermap.data.processing.jar' -DgroupId='com.supermap.data' -DartifactId='processing' -Dversion='11.1.1' -Dpackaging='jar'

mvn install:install-file -Dfile='com.supermap.data.topology.jar' -DgroupId='com.supermap.data' -DartifactId='topology' -Dversion='11.1.1' -Dpackaging='jar'

mvn install:install-file -Dfile='com.supermap.mapping.jar' -DgroupId='com.supermap.mapping' -DartifactId='mapping' -Dversion='11.1.1' -Dpackaging='jar'

mvn install:install-file -Dfile='com.supermap.layout.jar' -DgroupId='com.supermap.layout' -DartifactId='layout' -Dversion='11.1.1' -Dpackaging='jar'

mvn install:install-file -Dfile='com.supermap.analyst.spatialanalyst.jar' -DgroupId='com.supermap.analyst' -DartifactId='spatialanalyst' -Dversion='11.1.1' -Dpackaging='jar'

mvn install:install-file -Dfile='com.supermap.analyst.networkanalyst.jar' -DgroupId='com.supermap.analyst' -DartifactId='networkanalyst' -Dversion='11.1.1' -Dpackaging='jar'

mvn install:install-file -Dfile='com.supermap.analyst.terrainanalyst.jar' -DgroupId='com.supermap.analyst' -DartifactId='terrainanalyst' -Dversion='11.1.1' -Dpackaging='jar'

mvn install:install-file -Dfile='com.supermap.analyst.addressmatching.jar' -DgroupId='com.supermap.analyst' -DartifactId='addressmatching' -Dversion='11.1.1' -Dpackaging='jar'

mvn install:install-file -Dfile='com.supermap.analyst.navigation.jar' -DgroupId='com.supermap.analyst' -DartifactId='navigation' -Dversion='11.1.1' -Dpackaging='jar'

mvn install:install-file -Dfile='com.supermap.ui.controls.jar' -DgroupId='com.supermap.ui' -DartifactId='controls' -Dversion='11.1.1' -Dpackaging='jar'

```

2、模块说明：[开发组件模块说明](./开发组件模块说明.md)

## 许可配置

- [开发指南-许可配置说明](http://support.supermap.com.cn/DataWarehouse/WebDocHelp/iObjectsJava/index.html)


1、获取许可

2、配置许可

3、配置许可文件路径


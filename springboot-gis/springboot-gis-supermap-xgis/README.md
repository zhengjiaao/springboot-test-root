# springboot-gis-supermap-xgis

- [XGIS文档目录-需数慧公司内部企业微信](https://doc.weixin.qq.com/doc/w3_AMoAjAY9AEcXqQFJjtgQyikPYv7Q2?scode=AAYALweMAA4E1a2osbAFgAjQY9AEc)
- [如何接入xgis](https://doc.weixin.qq.com/doc/w3_AMoAjAY9AEcrgxvn5soQHWhTpF6yb?scode=AAYALweMAA4gmyJrEwAFgAjQY9AEc)
- [SuperMap应用环境部署文档](https://doc.weixin.qq.com/doc/w3_AMoAjAY9AEclhUlWQ1HTKGWcNkYWS?scode=AAYALweMAA4bZvKWfUAFgAjQY9AEc)

现在统一计算服务、桌面端、模型后台以及算法全部接入了XGIS，实现多GIS平台的适配

## 一、环境要求

● 各个GIS平台的实现JAR包运行需要对应的运行环境

详情请见环境部署文档

### 1、 ArcGIS

- 只能在windows环境下运行

- 需要ArcGIS_Engine环境

详情：[ArcEngine Java 开发环境搭建](https://doc.weixin.qq.com/doc/w3_AMoAjAY9AEcInUKvxMmS967LtdPfU?scode=AAYALweMAA4TBK0QO7AMoAjAY9AEc)

### 2、SuperMap

- 可以在windows/linux环境下运行

- 需要Iobject环境以及许可

- 基础镜像：127.0.0.1/middleware/dist-jdk18:latest

详情：[SuperMap应用环境部署文档](https://doc.weixin.qq.com/doc/w3_AMoAjAY9AEclhUlWQ1HTKGWcNkYWS?scode=AAYALweMAA4TyMno1DAMoAjAY9AEc)

### 3、 开源GIS

- 可以在windows/linux环境下运行

- 如果用到gdal，则需要gdal环境

- 基础镜像：127.0.0.1/middleware/gdaljava:gdb-latest

详情：[GDAL环境部署文档](https://doc.weixin.qq.com/doc/w3_AMoAjAY9AEcV3A0Q1YPSTa7XP17L7?scode=AAYALweMAA4UopNtSHAMoAjAY9AEc)

## 二、引用对应jar包

```
<!-- xgis自定义接口模块-->
<dependency>
    <groupId>com.dist.xdata</groupId>
    <artifactId>xgis-core</artifactId>
    <version>2.0-SNAPSHOT</version>
</dependency>

<!-- 使用哪个GIS平台就引用哪个实现模块（现有arcgis、supermap、开源GIS）  -->

<!-- xgis-supermap实现模块-->
<!-- 超图实现需要额外的maven仓库
    <repositories>
        <repository>
            <id>supermap</id>
            <url>https://maven.supermap.io/</url>
            <snapshots>
                <enabled>true</enabled>
            </snapshots>
            <releases>
                <enabled>true</enabled>
            </releases>
        </repository>
    </repositories>
 -->
   
<dependency>
    <groupId>com.dist.xdata</groupId>
    <artifactId>xgis-supermap</artifactId>
    <version>2.0-SNAPSHOT</version>
</dependency>

<!-- xgis-opengis实现模块-->
<dependency>
    <groupId>com.dist.xdata</groupId>
    <artifactId>xgis-opengis</artifactId>
    <version>2.0-SNAPSHOT</version>
</dependency>

<!-- xgis-arcgis实现模块-->
<dependency>
    <groupId>com.dist.xdata</groupId>
    <artifactId>xgis-arcgis</artifactId>
    <version>2.0-SNAPSHOT</version>
</dependency>
```

● 引入之后，会自动执行各个GIS平台验证环境的方法

●
引入特定GIS版本的实现 [不同GIS版本的引入](https://doc.weixin.qq.com/doc/w3_AMoAjAY9AEc19Uet1EKS0qHCvsLO1?scode=AAYALweMAA4IyvcC2mAMoAjAY9AEc)

## 三、特殊指定配置

对应的配置（不添加配置都为默认配置）

```
gdal:
  #是否验证GDAL环境,默认为true(如果没用到gdal可以配置关闭验证)
  enabled: false
superMap:
  #是否验证超图环境
  enabled: true
  #是否连接超图云许可
  web-licence:
    enabled: true
    ip: 127.0.0.1
    port: 9183
    module: 65400
```
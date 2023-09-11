package com.zja.remote.arcgis.rest;

import com.zja.dto.arcgis.MapServerDTO;
import feign.Param;
import feign.RequestLine;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-07-29 15:03
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：Arcgis 服务调用
 */
public interface ArcgisServiceFeign {

    /**
     * 获取服务的目录
     * @RemotePath http://127.0.0.1:6080/arcgis/rest/services?f=pjson
     */
    @RequestLine("GET /rest/services?f=pjson")
    Object getFolders();

    /**
     * 获取服务信息
     *
     * @RemotePath http://127.0.0.1:6080/arcgis/rest/services/CJKSH?f=pjson
     */
    @RequestLine("GET /rest/services/{folder}?f=pjson")
    Object getServices(@Param(value = "folder") String folder);

    /**
     * 获取服务下的所有图层信息
     *
     * @param serviceName 服务名称
     * @RemotePath http://127.0.0.1:6080/arcgis/rest/services/CJKSH/CJKSH/MapServer?f=pjson
     */
    @RequestLine("GET /rest/services/{serviceName}/MapServer?f=pjson")
    Object getLayers(@Param(value = "serviceName") String serviceName);

    /**
     * 获取Arcgis服务通用接口
     *
     * @param serviceName 服务名称
     * @param layerId     图层Id
     * @RemotePath http://127.0.0.1:6080/arcgis/rest/services/CJKSH/长江经济带/MapServer/8/query?where=1=1&geometryType=esriGeometryEnvelope&spatialRel=esriSpatialRelIntersects&outFields=*&returnGeometry=true&f=pjson
     */
    @RequestLine("GET /rest/services/{serviceName}/MapServer/{layerId}/query?where=1=1&geometryType=esriGeometryEnvelope&spatialRel=esriSpatialRelIntersects&outFields=*&returnGeometry=true&f=pjson")
    MapServerDTO getMapServer(@Param(value = "serviceName") String serviceName,
                              @Param(value = "layerId") Integer layerId);

    @RequestLine("GET /rest/services/{serviceName}/MapServer/{layerId}/query?where={where}&geometryType=esriGeometryEnvelope&spatialRel=esriSpatialRelIntersects&outFields=*&returnGeometry=true&f=pjson")
    MapServerDTO getMapServer(@Param(value = "serviceName") String serviceName,
                              @Param(value = "layerId") Integer layerId,
                              @Param(value = "where") String where);

    /**
     * 获取【长江经济带】地图服务数据
     *
     * @param id
     * @RemotePath http://127.0.0.1:6080/arcgis/rest/services/CJKSH/长江经济带/MapServer/8/query?where=1=1&geometryType=esriGeometryEnvelope&spatialRel=esriSpatialRelIntersects&outFields=*&returnGeometry=true&f=pjson
     */
    @RequestLine("GET /rest/services/CJKSH/长江经济带/MapServer/{id}/query?where=1=1&geometryType=esriGeometryEnvelope&spatialRel=esriSpatialRelIntersects&outFields=*&returnGeometry=true&f=pjson")
    MapServerDTO GetMapServer(@Param(value = "id") Integer id);

    /**
     * 获取【全国】地图服务数据-长江经济带范围行政区划
     *
     * @RemotePath http://127.0.0.1:6080/arcgis/rest/services/CJKSH/XZQHXR/MapServer/1/query?where=1=1&geometryType=esriGeometryEnvelope&spatialRel=esriSpatialRelIntersects&outFields=*&returnGeometry=true&f=pjson
     */
    @RequestLine("GET /rest/services/CJKSH/XZQHXR/MapServer/1/query?where=1=1&geometryType=esriGeometryEnvelope&spatialRel=esriSpatialRelIntersects&outFields=*&returnGeometry=true&f=pjson")
    MapServerDTO GetAllMapServer();

    /**
     * 获取【全国】地图服务数据-长江经济带范围行政区划点
     *
     * @RemotePath http://127.0.0.1:6080/arcgis/rest/services/CJKSH/XZQHXR/MapServer/0/query?where=1=1&geometryType=esriGeometryEnvelope&spatialRel=esriSpatialRelIntersects&outFields=*&returnGeometry=true&f=pjson
     */
    @RequestLine("GET /rest/services/CJKSH/XZQHXR/MapServer/0/query?where=1=1&geometryType=esriGeometryEnvelope&spatialRel=esriSpatialRelIntersects&outFields=*&returnGeometry=true&f=pjson")
    MapServerDTO GetAllMapServerCenter();

//    @RequestLine("GET /rest/services/CJKSH/XZQHXR/MapServer/0/query?where={code}&geometryType=esriGeometryEnvelope&spatialRel=esriSpatialRelIntersects&outFields=*&returnGeometry=true&f=pjson")
//    MapServerDTO GetAllMapServerCenter(@Param(value = "code") String code);



}

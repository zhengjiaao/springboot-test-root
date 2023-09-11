package com.zja.remote.arcgis;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.zja.dto.arcgis.*;
import com.zja.remote.arcgis.rest.ArcgisServiceFeign;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.util.CollectionUtils;
import org.springframework.util.ObjectUtils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-07-30 11:15
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc： Arcgis服务远程接口具体操作实现类
 */
@Slf4j
@Component
public class ArcgisServiceRestImpl {

    @Autowired
    ArcgisServiceFeign arcgisServiceFeign;

    /**
     * 获取所有目录
     */
    public Object getFolders() {
        Object folders = arcgisServiceFeign.getFolders();
        JSONObject jsonObject = toJSONObject(folders);
        if (jsonObject == null) {
            return null;
        }
        return jsonObject.get("folders");
    }

    /**
     * 获取目录下所有服务信息
     *
     * @param folder 目录
     */
    public List<ServiceDTO> getServices(String folder) {

        Object services = arcgisServiceFeign.getServices(folder);
        JSONObject jsonObject = toJSONObject(services);
        if (jsonObject == null) {
            return null;
        }
        List<ServiceDTO> serviceDTOList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("services");
        if (!jsonArray.isEmpty()) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject service = jsonArray.getJSONObject(i);
                ServiceDTO serviceDTO = new ServiceDTO();
                serviceDTO.setName(service.getString("name"));
                serviceDTO.setType(service.getString("type"));
                serviceDTOList.add(serviceDTO);
            }
        }
        return serviceDTOList;
    }

    /**
     * 获取服务的所有图层信息
     *
     * @param serviceName 服务名称
     */
    public List<LayerDTO> getLayers(String serviceName) {
        Object layers = arcgisServiceFeign.getLayers(serviceName);
        JSONObject jsonObject = toJSONObject(layers);
        if (jsonObject == null) {
            return null;
        }

        List<LayerDTO> layerDTOList = new ArrayList<>();
        JSONArray jsonArray = jsonObject.getJSONArray("layers");
        if (!jsonArray.isEmpty()) {
            for (int i = 0; i < jsonArray.size(); i++) {
                JSONObject service = jsonArray.getJSONObject(i);
                LayerDTO layerDTO = new LayerDTO();
                layerDTO.setId(service.getInteger("id"));
                layerDTO.setName(service.getString("name"));
                layerDTO.setParentLayerId(service.getInteger("parentLayerId"));
                layerDTO.setDefaultVisibility(service.getBoolean("defaultVisibility"));
                layerDTOList.add(layerDTO);
            }
        }
        return layerDTOList;
    }

    /**
     * 获取Arcgis服务通用接口
     *
     * @param serviceName 服务名称
     * @param layerId     图层Id
     */
    public MapServerDTO GetMapServer(String serviceName, Integer layerId) {
        return arcgisServiceFeign.getMapServer(serviceName, layerId);
    }

    /**
     * 获取【全国】地图服务数据-长江经济带范围行政区划
     */
    public MapServerDTO GetAllMapServer() {
        return arcgisServiceFeign.GetAllMapServer();
    }

    /**
     * 获取【全国】地图服务数据-长江经济带范围行政区划点
     */
    public MapServerDTO GetAllMapServerCenter() {
        return arcgisServiceFeign.GetAllMapServerCenter();
    }

    /**
     * 取出长度最大的元素的值
     *
     * @param geometry
     */
    private List<Map> getMaxElement(GeometryDTO geometry) {

        List<List<List<Double>>> rings = geometry.getRings();
        List<List<Double>> listList = new ArrayList<>();
        if (!CollectionUtils.isEmpty(rings)) {
            //获取长度最大的元素的索引
            int mixIndex = 0;
            for (int i = 0; i < rings.size(); i++) {
                if (rings.get(i).size() > rings.get(mixIndex).size()) {
                    mixIndex = i;
                }
            }
            //取出长度最大的元素
            listList = rings.get(mixIndex);
        }
        //组装最大长度元素的数据格式
        List<Map> polygon = new ArrayList<>();
        for (List<Double> doubleList : listList) {
            Map map = new HashMap();
            map.put("x", doubleList.get(0));
            map.put("y", doubleList.get(1));
            polygon.add(map);
        }
        return polygon;
    }


    /**
     * 实体类对象转为JSONObject对象
     *
     * @param o 实体类对象
     */
    public static JSONObject toJSONObject(Object o) {
        if (ObjectUtils.isEmpty(o)) {
            return null;
        }
        String jsonString = JSON.toJSONString(o);
        return JSONObject.parseObject(jsonString);
    }
}

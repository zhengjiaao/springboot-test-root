package com.zja.dto.arcgis;

import lombok.Data;

import java.io.Serializable;

/**
 * Company: 上海数慧系统技术有限公司
 * Department: 数据中心
 * Date: 2020-09-23 15:41
 * Author: zhengja
 * Email: zhengja@dist.com.cn
 * Desc：服务
 */
@Data
public class ServicesDTO implements Serializable {

    //服务id
    private Integer serviceId;
    //服务名称
    private String serviceName;
    //服务类型 如地图服务MapServer
    private String serviceType;
    //图层id
    private Integer layerId;
    //图层名称
    private String layerName;
    //图层数据请求地址
    private String layerDataUrl;

}

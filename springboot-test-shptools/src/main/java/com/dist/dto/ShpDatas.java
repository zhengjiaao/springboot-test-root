package com.dist.dto;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**shp数据模型对象 -- 针对读
 * @author zhengja@dist.com.cn
 * @data 2019/8/28 15:31
 */
public class ShpDatas {

    private String name;
    /**
     * 属性【字段】集合
     */
    private List<Map<String,Object>> props;

    /**
     * shp文件路径地址
     */
    private String shpPath;

    public  ShpDatas(){
        props = new ArrayList<>();
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public List<Map<String, Object>> getProps() {
        return props;
    }

    public void setProps(List<Map<String, Object>> props) {
        this.props = props;
    }

    public void addProp(Map<String,Object> prop){
        this.props.add(prop);
    }

    public String getShpPath() {
        return shpPath;
    }

    public void setShpPath(String shpPath) {
        this.shpPath = shpPath;
    }

}

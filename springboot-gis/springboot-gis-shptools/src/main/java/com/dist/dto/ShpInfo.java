package com.zja.dto;


/**shp业务模型对象 -- 针对写
 * @author zhengja@dist.com.cn
 * @data 2019/8/28 15:31
 */
public class ShpInfo {


    /**
     * 图层名称
     */
    private String name;

    /**
     * 往哪个路径下写shp
     */
    private String path;

    /**
     * 几何对象WKT
     */
    private String geom;

    /**
     * 标识
     */
    private String id;

    /**
     * 描述
     */
    private String des;

    /**
     * 是否追加写
     */
    private boolean appendWrite = false;

    public ShpInfo(){
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPath() {
        return path;
    }

    public void setPath(String path) {
        this.path = path;
    }

    public String getGeom() {
        return geom;
    }

    public void setGeom(String geom) {
        this.geom = geom;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public boolean isAppendWrite() {
        return appendWrite;
    }

    public void setAppendWrite(boolean appendWrite) {
        this.appendWrite = appendWrite;
    }
}

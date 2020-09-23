package com.zja.dto;

import java.util.HashMap;
import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/8/16 16:23
 */
public class PolygonObject {
    private List<List<Double[]>> rings;
    private HashMap<String, Integer> spatialReference;

    public List<List<Double[]>> getRings() {
        return rings;
    }
    public void setRings(List<List<Double[]>> rings) {
        this.rings = rings;
    }
    public HashMap<String, Integer> getSpatialReference() {
        return spatialReference;
    }
    public void setSpatialReference(HashMap<String, Integer> spatialReference) {
        this.spatialReference = spatialReference;
    }
}

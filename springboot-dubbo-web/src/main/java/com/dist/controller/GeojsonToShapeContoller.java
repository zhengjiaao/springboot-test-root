package com.dist.controller;

import com.dist.dto.PolygonObject;
import com.google.gson.Gson;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/8/16 14:46
 */
@RequestMapping(value = "rest/geojsontoshape")
@RestController
@Api(tags = {"GeojsonToShapeContoller"}, description = "geojson转成shape文件")
public class GeojsonToShapeContoller {

    @ApiOperation(value = "测试", httpMethod = "GET")
    @RequestMapping(value = "readingfile", method = RequestMethod.GET)
    public void readingFile() {
        String wkt = "{\"rings\":[[[120.01481126800002,30.686383128000074],[120.01474083399997,30.686419206000039],[120.01481126800002,30.686383128000074]]],\"spatialReference\":{\"wkid\":4490,\"latestWkid\":4490}}";
        String wktToJson = getPOLYGONWktToJson(wkt, 1);
    }

    /**
     * 多边形 转换 JSON
     * @param wkt
     * @param wkid
     * @return
     */
    public String getPOLYGONWktToJson(String wkt, int wkid) {
        PolygonObject polygonObject = new PolygonObject();
        List<List<Double[]>> lists = new ArrayList<List<Double[]>>();
        String ToTailWkt = wkt.substring(0, wkt.length() - 1);
        String[] strHead = ToTailWkt.split("\\(", 2);
        String[] strList = strHead[1].split("\\), \\(");
        for (int i = 0; i < strList.length; i++) {
            String item = strList[i].trim();
            item = item.substring(1, item.length() - 1);
            String[] items = item.split(",");
            List<Double[]> list = new ArrayList<Double[]>();
            for (int j = 0; j < items.length; j++) {
                String jItem = items[j].trim();
                String[] jItems = jItem.split(" ");
                Double[] listResult = new Double[] {
                        Double.parseDouble(jItems[0]),
                        Double.parseDouble(jItems[1]) };
                list.add(listResult);
            }
            lists.add(list);
        }
        HashMap<String, Integer> spatialReference = new HashMap<String, Integer>();
        spatialReference.put("wkid", wkid);
        polygonObject.setRings(lists);
        polygonObject.setSpatialReference(spatialReference);
        Gson gson = new Gson();
        return gson.toJson(polygonObject);
    }


}

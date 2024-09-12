package com.zja.poitl.factory;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.zja.poitl.entity.PlanCondition;
import com.zja.poitl.util.PoiTLUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 产品 Word 生成策略实现
 *
 * @author: zhengja
 * @since: 2024/04/02 10:02
 */
@Slf4j
public class WordGenerationStrategy implements GenerationStrategy {

    @Override
    public String generate(String wordPath, String data, String template) throws IOException {
        PlanCondition planCondition = JSON.parseObject(data, PlanCondition.class);
        Map<String, Object> model = generateModel(planCondition);
        PoiTLUtil.generateWord(wordPath, model, template);

        // 使用 Word 数据源和模板生成 Word 文档的实现逻辑
        return "Generated Word Document";
    }

    // 产品
    private Map<String, Object> generateModel(PlanCondition planCondition) {
        // 获取info，通过json字符串转换一次Map，避免了深度拷贝的复杂性问题
        String infoJsonString = JSON.toJSONString(planCondition.getInfo());
        JSONObject jsonObject = JSONObject.parseObject(infoJsonString);
        Map<String, Object> model = (Map<String, Object>) jsonObject;

        // 处理附图
        Object futuObject = model.get("FUTU");
        if (ObjectUtils.isNotEmpty(futuObject)) {
            model.put("FUTU", futu(futuObject));
        } else {
            model.put("FUTU", null);
        }
        return model;
    }

    // 附图
    private Map<String, Object> futu(Object futuObject) {
        Map<String, Object> futuList = new HashMap<>((Map<String, Object>) futuObject);
        List<Map<String, Object>> children = new ArrayList<>((List<Map<String, Object>>) futuList.get("children"));
        if (ObjectUtils.isNotEmpty(children)) {
            children.forEach(futuMapObject -> {
                Object picsObject = futuMapObject.get("pics");
                if (ObjectUtils.isNotEmpty(picsObject)) {
                    List<Map<String, Object>> picsList = (List<Map<String, Object>>) picsObject;
                    picsList.forEach(map -> {
                        String id = String.valueOf(map.get("id"));
                        // 下载附图
                        if (StringUtils.isNotBlank(id)) {
                            // Pair<String, String> pair = blockResourceStorage.downloadFile(id);
                            try {
                                // map.put("image", Pictures.ofStream(Files.newInputStream(Paths.get(pair.getValue())), PictureType.PNG)
                                map.put("image", Pictures.ofStream(Files.newInputStream(Paths.get("D:\\temp\\images\\3840x2160.jpg")), PictureType.PNG)
                                        .size(500, 300).create());
                            } catch (Exception e) {
                                log.error("附图下载失败:id={},e={}", id, e);
                                // throw new RuntimeException(e);
                            }
                        }
                    });
                }
            });
        }

        return futuList;
    }
}
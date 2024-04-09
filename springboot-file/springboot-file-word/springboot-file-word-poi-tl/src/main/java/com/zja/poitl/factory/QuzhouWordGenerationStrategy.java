package com.zja.poitl.factory;

import cn.hutool.core.util.ObjectUtil;
import cn.hutool.core.util.StrUtil;
import com.alibaba.fastjson.JSON;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.zja.poitl.entity.PlanCondition;
import com.zja.poitl.entity.PlanConditionData;
import com.zja.poitl.util.PoiTLUtil;
import lombok.Getter;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;

/**
 * 衢州 Word 生成策略实现
 *
 * @author: zhengja
 * @since: 2024/04/02 10:02
 */
public class QuzhouWordGenerationStrategy implements GenerationStrategy {

    @Override
    public String generate(String wordPath, String data, String template) throws IOException {
        PlanCondition planCondition = JSON.parseObject(data, PlanCondition.class);
        Map<String, Object> model = generateModel(planCondition);
        PoiTLUtil.generateWord(wordPath, model, template);
        return "衢州 Generated Word Document";
    }

    // 来自衢州项目
    private Map<String, Object> generateModel(PlanCondition planCondition) {
        Map<String, Object> model = new HashMap<>();
        // 直接获取info
        Map info = planCondition.getInfo();
        // 获取data，区分多地块和单地块
        List<Map<String, Object>> dataList = jsonToMap(planCondition.getData());
        if (!dataList.isEmpty()) {
            String dkName = "";
            for (Map o : dataList) {
                if (ObjectUtil.isNotNull(o)) {
                    if (o.equals(dataList.get(dataList.size() - 1))) {
                        dkName += o.get("DKMC");
                    } else {
                        dkName += o.get("DKMC") + "、";
                    }
                }
            }
            // 地块名称
            model.put("DKMC", dkName);
        }
        // 用地位置
        model.put("YDWZ", info.get("YDWZ"));
        // 地块信息
        model.put("DK", dataList);
        // 日期
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy年MM月dd日");
        String date = sdf.format(new Date());
        model.put("DATE", date);
        // 年和序列
        Calendar calendar = Calendar.getInstance();
        model.put("YEAR", calendar.get(Calendar.YEAR));
        // model.put("SEQ", idUtil.next(calendar.get(Calendar.YEAR) + "seq"));
        model.put("SEQ", calendar.get(Calendar.YEAR) + "seq");
        // 用地情况
        Map<String, String> YDQK = (Map<String, String>) info.get("YDQK");
        if (ObjectUtil.isNotNull(YDQK)) {
            model.put("YDWZ", YDQK.get("YDWZ"));// 用地位置
            model.put("ZYDMJ", YDQK.get("ZYDMJ"));// 总用地面积
        }
        // 用地强度与建筑控制指标
        Map<String, String> KZZB = (Map<String, String>) info.get("KZZB");
        if (ObjectUtil.isNotNull(KZZB)) {
            model.put("ZRJL", KZZB.get("ZRJL"));// 地块整体容积率
            model.put("ZJZMD", KZZB.get("ZJZMD"));// 总建筑密度
        }
        // 规划及建筑设计要求
        Map<String, String> JZSJYQ = (Map<String, String>) info.get("JZSJYQ");
        if (ObjectUtil.isNotNull(JZSJYQ)) {
            model.put("GHDW", JZSJYQ.get("GHDW"));// 规划定位
            model.put("ZJZMJ", JZSJYQ.get("ZJZMJ"));// 总建筑面积
        }
        // 城市设计要求
        model.put("CSSJYQ", handleTitle((Map<String, String>) info.get("CSSJYQ")));
        // 公共设施、市政基础设施和自身配套设施要求
        model.put("SSYQ", handleTitle((Map<String, String>) info.get("SSYQ")));
        // 其他要求
        model.put("QTYQ", handleTitle((Map<String, String>) info.get("QTYQ")));
        // 附则内容
        model.put("FZNR", handleTitle((Map<String, String>) info.get("FZNR")));
        // 附图
        Map<String, String> imageList = (Map<String, String>) info.get("FUTU");
        if (imageList != null) {
            //
            String DKYDHXT = imageList.get("DKYDHXT");
            if (StrUtil.isNotBlank(DKYDHXT)) {
                // Pair<String, String> pair = blockResourceStorage.downloadFile(DKYDHXT);
                // try {
                //     model.put("YDHX", Pictures.ofStream(new FileInputStream(pair.getValue())));
                // } catch (FileNotFoundException e) {
                //     // throw new RuntimeException(e);
                // }
            } else {
                try {
                    model.put("YDHX", Pictures.ofStream(new FileInputStream("D:\\temp\\images\\3840x2160.jpg"), PictureType.PNG)
                            .size(400, 260).create());
                } catch (FileNotFoundException e) {
                }
            }
            //
            String XXGHT = imageList.get("XXGHT");
            if (StrUtil.isNotBlank(XXGHT)) {
                // Pair<String, String> pair = blockResourceStorage.downloadFile(XXGHT);
                // try {
                //     model.put("XXGHT", Pictures.ofStream(new FileInputStream(pair.getValue())));
                // } catch (FileNotFoundException e) {
                //     // throw new RuntimeException(e);
                // }
            } else {
                try {
                    // model.put("XXGHT", Pictures.ofStream(new FileInputStream(new File("D:\\temp\\images\\3840x2160.jpg"))));
                    model.put("XXGHT", Pictures.ofStream(new FileInputStream("D:\\temp\\images\\3840x2160.jpg"), PictureType.PNG)
                            .size(400, 260).create());
                } catch (FileNotFoundException e) {
                }
            }
            //
            String DKCBSJFA = imageList.get("DKCBSJFA");
            if (StrUtil.isNotBlank(DKCBSJFA)) {
                // Pair<String, String> pair = blockResourceStorage.downloadFile(DKCBSJFA);
                // try {
                //     model.put("CBSJFA", Pictures.ofStream(new FileInputStream(pair.getValue())));
                // } catch (FileNotFoundException e) {
                //     // throw new RuntimeException(e);
                // }
            }
        }
        return model;
    }

    public List<Map<String, Object>> jsonToMap(List<PlanConditionData> dataList) {
        List<Map<String, Object>> mapList = dataList.stream().map(o -> {
            return o.getData().getInnerMap();
        }).collect(Collectors.toList());
        return mapList;
    }

    private List<Map<String, String>> handleTitle(Map<String, String> map) {
        List<Map<String, String>> result = new ArrayList<>();
        int i = 1;
        if (ObjectUtil.isNotNull(map)) {
            for (Map.Entry<String, String> entry : map.entrySet()) {
                Map<String, String> title = new HashMap<>();
                if (StrUtil.isNotBlank(entry.getValue())) {
                    if (StrUtil.isNotBlank(TitleEnum.getDescByAbbr(entry.getKey()))) {
                        title.put("title", i + "、" + TitleEnum.getDescByAbbr(entry.getKey()) + "：");
                    } else {
                        title.put("title", i + "、");
                    }
                    title.put("content", entry.getValue());
                    result.add(title);
                    i++;
                }
            }
        }
        return result;
    }

    @Getter
    public enum TitleEnum {
        DCBSJFACR("DCBSJFACR", "带初步设计方案出让"),
        JZXS("JZXS", "建筑形式"),
        JZWQ("JZWQ", "建筑外墙"),
        WQSZYQ("WQSZYQ", "围墙设置要求"),
        XQZRK("XQZRK", "小区主入口"),
        QT("QT", "其他"),
        SYYF("SYYF", "商业用房"),
        WYGLYF("WYGLYF", "物业管理用房"),
        SQFWYF("SQFWYF", "社区服务用房"),
        JJYLFWYF("JJYLFWYF", "居家养老服务用房"),
        XQQZJSSS("SQQZJSSS", "小区群众健身设施"),
        GGWHSS("GGWHSS", "公共文化设施"),
        KDSDSS("KDSDSS", "快递送达设施"),
        RFSSYQ("RFSSYQ", "人防设施要求"),
        QTYQ("QTYQ", "其他要求"),
        RJLJS("RJLJS", "容积率计算"),
        JGLHSJYQ("JGLHSJYQ", "景观绿化设计要求"),
        JGLHSJYQ2("JGLHSJYQ2", "景观亮化设计要求"),
        ZHGXSJYQ("ZHGXSJYQ", "综合管线设计要求"),
        SXBGSJYQ("SXBGSJYQ", "竖向标高设计要求");

        final String desc;
        final String abbr;

        TitleEnum(String abbr, String desc) {
            this.abbr = abbr;
            this.desc = desc;
        }

        public static String getDescByAbbr(String abbr) {
            for (TitleEnum title : TitleEnum.values()) {
                if (StrUtil.equals(title.getAbbr(), abbr)) {
                    return title.getDesc();
                }
            }
            return "";
        }
    }
}
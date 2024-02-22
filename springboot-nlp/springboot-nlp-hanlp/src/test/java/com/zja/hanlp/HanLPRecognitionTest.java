package com.zja.hanlp;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.URLTokenizer;
import org.junit.jupiter.api.Test;

import java.util.List;

/**
 * HanLP 识别场景(中国人名识别、地名识别、机构名识别、URL地址识别)
 *
 * @author: zhengja
 * @since: 2024/02/20 16:21
 */
public class HanLPRecognitionTest {

    // 中国人名识别
    @Test
    public void ChineseNameRecognition_test() {
        String[] testCase = new String[]{
                "签约仪式前，秦光荣、李纪恒、仇和等一同会见了参加签约的企业家。",
                "陕西首富吴一坚被带走 与令计划妻子有交集",
                "凯瑟琳和露西（庐瑞媛），跟她们的哥哥们有一些不同。",
                "张浩和胡健康复员回家了",
                "王总和小丽结婚了",
                "龚学平等领导说,邓颖超生前杜绝超生",
                "请王霞进行审核",
        };
        Segment segment = HanLP.newSegment().enableNameRecognize(true);
        for (String sentence : testCase) {
            List<Term> termList = segment.seg(sentence);
            termList.forEach(t -> {
                if (Nature.nr == t.nature) {
                    System.out.println(t.word);
                    // 秦光荣
                    // 李纪恒
                    // 仇和
                    // 吴一坚
                    // 令计划
                    // 庐瑞媛
                    // 张浩
                    // 胡健康
                    // 王总
                    // 小丽
                    // 龚学平
                    // 邓颖超
                }
            });
        }
    }

    // 音译人名识别
    @Test
    public void TranslatedNameRecognition_test() {
        String[] testCase = new String[]{
                "一桶冰水当头倒下，微软的比尔盖茨、Facebook的扎克伯格跟桑德博格、亚马逊的贝索斯、苹果的库克全都不惜湿身入镜，这些硅谷的科技人，飞蛾扑火似地牺牲演出，其实全为了慈善。",
                "世界上最长的姓名是简森·乔伊·亚历山大·比基·卡利斯勒·达夫·埃利奥特·福克斯·伊维鲁莫·马尔尼·梅尔斯·帕特森·汤普森·华莱士·普雷斯顿。",
        };
        Segment segment = HanLP.newSegment().enableTranslatedNameRecognize(true);
        for (String sentence : testCase) {
            List<Term> termList = segment.seg(sentence);
            termList.forEach(t -> {
                if (Nature.nrf == t.nature) {
                    System.out.println(t.word);
                    // 比尔盖茨
                    // 扎克伯格
                    // 桑德博格
                    // 亚马逊
                    // 贝索斯
                    // 简森·乔伊·亚历山大·比基·卡利斯勒·达夫·埃利奥特·福克斯·伊维鲁莫·马尔尼·梅尔斯·帕特森·汤普森·华莱士·普雷斯顿
                }
            });
        }
    }

    // 地名识别
    @Test
    public void PlaceRecognition_test() {
        String[] testCase = new String[]{
                "蓝翔给宁夏固原市彭阳县红河镇黑牛沟村捐赠了挖掘机",
        };
        Segment segment = HanLP.newSegment().enablePlaceRecognize(true);
        for (String sentence : testCase) {
            List<Term> termList = segment.seg(sentence);
            termList.forEach(t -> {
                if (Nature.ns == t.nature) {
                    System.out.println(t.word);
                    // 宁夏
                    // 固原市
                    // 彭阳县
                    // 黑牛沟村
                }
            });
        }
    }

    // 机构名识别
    @Test
    public void OrganizationRecognition_test() {
        String[] testCase = new String[]{
                "我在上海林原科技有限公司兼职工作，",
                "我经常在台川喜宴餐厅吃饭，",
                "偶尔去开元地中海影城看电影。",
                "不用词典，福哈生态工程有限公司是动态识别的结果。",
        };
        Segment segment = HanLP.newSegment().enableCustomDictionary(false).enableOrganizationRecognize(true);
        for (String sentence : testCase) {
            List<Term> termList = segment.seg(sentence);
            termList.forEach(t -> {
                if (Nature.nt == t.nature) {
                    System.out.println(t.word);
                    // 上海林原科技有限公司
                    // 台川喜宴餐厅
                    // 开元地中海影城
                    // 福哈生态工程有限公司
                }
            });
        }
    }

    // URL 识别
    @Test
    public void URLRecognition_test() {
        String text =
                "HanLP的项目地址是https://github.com/hankcs/HanLP，" +
                        "发布地址是https://github.com/hankcs/HanLP/releases，" +
                        "我有时候会在www.hankcs.com上面发布一些消息，" +
                        "我的微博是http://weibo.com/hankcs/，会同步推送hankcs.com的新闻。";
        List<Term> termList = URLTokenizer.segment(text);
        for (Term term : termList) {
            if (term.nature == Nature.xu)
                System.out.println(term.word);
            // https://github.com/hankcs/HanLP
            // https://github.com/hankcs/HanLP/releases
            // http://weibo.com/hankcs
        }
    }

}

package com.zja.hanlp.restful;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.hankcs.hanlp.restful.CoreferenceResolutionOutput;
import com.hankcs.hanlp.restful.HanLPClient;
import com.hankcs.hanlp.restful.Span;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * @author: zhengja
 * @since: 2024/02/20 15:53
 */
public class HanLPClientTest {

    HanLPClient client;

    @BeforeEach
    void setUp() {
        client = new HanLPClient("https://hanlp.hankcs.com/api", null);
    }


    @Test
    void parseText() throws IOException {
        Map<String, List> doc = client.parse("2021年HanLPv2.1为生产环境带来次世代最先进的多语种NLP技术。英首相与特朗普通电话讨论华为与苹果公司。");
        prettyPrint(doc);
        //{
        //   "tok/fine" : [ [ "2021年", "HanLPv2.1", "为", "生产", "环境", "带来", "次世代", "最", "先进", "的", "多", "语种", "NLP", "技术", "。" ], [ "英", "首相", "与", "特朗普", "通", "电话", "讨论", "华为", "与", "苹果", "公司", "。" ] ],
        //   "tok/coarse" : [ [ "2021年", "HanLPv2.1", "为", "生产环境", "带来", "次世代", "最", "先进", "的", "多语种", "NLP", "技术", "。" ], [ "英", "首相", "与", "特朗普", "通电话", "讨论", "华为", "与", "苹果公司", "。" ] ],
        //   "pos/ctb" : [ [ "NT", "NN", "P", "NN", "NN", "VV", "NN", "AD", "JJ", "DEC", "CD", "NN", "NN", "NN", "PU" ], [ "NR", "NN", "P", "NR", "VV", "NN", "VV", "NR", "CC", "NR", "NN", "PU" ] ],
        //   "pos/pku" : [ [ "t", "nx", "p", "vn", "n", "v", "n", "d", "a", "u", "a", "n", "nx", "n", "w" ], [ "j", "n", "p", "nr", "v", "n", "v", "nz", "c", "nz", "n", "w" ] ],
        //   "pos/863" : [ [ "nt", "w", "p", "v", "n", "v", "n", "d", "a", "u", "a", "n", "w", "n", "w" ], [ "j", "n", "c", "nh", "v", "n", "v", "ni", "c", "ni", "n", "w" ] ],
        //   "ner/msra" : [ [ [ "2021年", "DATE", 0, 1 ], [ "HanLPv2.1", "ORGANIZATION", 1, 2 ] ], [ [ "英", "LOCATION", 0, 1 ], [ "特朗普", "PERSON", 3, 4 ], [ "华为", "ORGANIZATION", 7, 8 ], [ "苹果公司", "ORGANIZATION", 9, 11 ] ] ],
        //   "ner/pku" : [ [ ], [ [ "特朗普", "nr", 3, 4 ], [ "华为", "nt", 7, 8 ], [ "苹果公司", "nt", 9, 11 ] ] ],
        //   "ner/ontonotes" : [ [ [ "2021年", "DATE", 0, 1 ] ], [ [ "英", "GPE", 0, 1 ], [ "特朗普", "PERSON", 3, 4 ], [ "华为", "ORG", 7, 8 ], [ "苹果公司", "ORG", 9, 11 ] ] ],
        //   "srl" : [ [ [ [ "2021年", "ARGM-TMP", 0, 1 ], [ "HanLPv2.1", "ARG0", 1, 2 ], [ "为生产环境", "ARG2", 2, 5 ], [ "带来", "PRED", 5, 6 ], [ "次世代最先进的多语种NLP技术", "ARG1", 6, 14 ] ], [ [ "最", "ARGM-ADV", 7, 8 ], [ "先进", "PRED", 8, 9 ], [ "技术", "ARG0", 13, 14 ] ] ], [ [ [ "英首相与特朗普", "ARG0", 0, 4 ], [ "通", "PRED", 4, 5 ], [ "电话", "ARG1", 5, 6 ] ], [ [ "英首相与特朗普", "ARG0", 0, 4 ], [ "讨论", "PRED", 6, 7 ], [ "华为与苹果公司", "ARG1", 7, 11 ] ] ] ],
        //   "dep" : [ [ [ 6, "nmod:tmod" ], [ 6, "nsubj" ], [ 5, "case" ], [ 5, "compound:nn" ], [ 6, "nmod:prep" ], [ 0, "root" ], [ 14, "dep" ], [ 9, "advmod" ], [ 14, "amod" ], [ 9, "mark" ], [ 12, "dep" ], [ 14, "compound:nn" ], [ 14, "compound:nn" ], [ 6, "dobj" ], [ 6, "punct" ] ], [ [ 2, "nmod:assmod" ], [ 5, "nsubj" ], [ 4, "case" ], [ 5, "nmod:prep" ], [ 0, "root" ], [ 5, "dobj" ], [ 5, "conj" ], [ 11, "conj" ], [ 11, "cc" ], [ 11, "compound:nn" ], [ 7, "dobj" ], [ 5, "punct" ] ] ],
        //   "sdp" : [ [ [ [ 6, "Time" ] ], [ [ 6, "Exp" ] ], [ [ 5, "mPrep" ] ], [ [ 5, "Desc" ] ], [ [ 6, "Datv" ] ], [ [ 0, "Root" ] ], [ [ 14, "Time" ] ], [ [ 9, "mDegr" ] ], [ [ 14, "Desc" ] ], [ [ 9, "mAux" ] ], [ [ 12, "Quan" ] ], [ [ 13, "Desc" ], [ 14, "Desc" ] ], [ [ 14, "Nmod" ] ], [ [ 6, "Cont" ] ], [ [ 6, "mPunc" ] ] ], [ [ [ 2, "Nmod" ] ], [ [ 5, "Agt" ], [ 7, "Agt" ] ], [ [ 4, "mPrep" ] ], [ [ 7, "Agt" ] ], [ [ 1, "dMann" ] ], [ [ 5, "Cont" ] ], [ [ 0, "Root" ], [ 5, "ePurp" ] ], [ [ 7, "Cont" ] ], [ [ 11, "mConj" ] ], [ [ 11, "Nmod" ] ], [ [ 7, "Datv" ], [ 8, "eCoo" ] ], [ [ 7, "mPunc" ] ] ] ],
        //   "con" : [ [ "TOP", [ [ "IP", [ [ "NP", [ [ "NT", [ "2021年" ] ] ] ], [ "NP", [ [ "NN", [ "HanLPv2.1" ] ] ] ], [ "VP", [ [ "PP", [ [ "P", [ "为" ] ], [ "NP", [ [ "NN", [ "生产" ] ], [ "NN", [ "环境" ] ] ] ] ] ], [ "VP", [ [ "VV", [ "带来" ] ], [ "NP", [ [ "NP", [ [ "NN", [ "次世代" ] ] ] ], [ "DNP", [ [ "ADJP", [ [ "ADVP", [ [ "AD", [ "最" ] ] ] ], [ "VP", [ [ "JJ", [ "先进" ] ] ] ] ] ], [ "DEC", [ "的" ] ] ] ], [ "NP", [ [ "QP", [ [ "CD", [ "多" ] ] ] ], [ "NP", [ [ "NN", [ "语种" ] ] ] ] ] ], [ "NP", [ [ "NN", [ "NLP" ] ], [ "NN", [ "技术" ] ] ] ] ] ] ] ] ] ], [ "PU", [ "。" ] ] ] ] ] ], [ "TOP", [ [ "IP", [ [ "NP", [ [ "NP", [ [ "NR", [ "英" ] ] ] ], [ "NP", [ [ "NN", [ "首相" ] ] ] ] ] ], [ "VP", [ [ "PP", [ [ "P", [ "与" ] ], [ "NP", [ [ "NR", [ "特朗普" ] ] ] ] ] ], [ "VP", [ [ "VP", [ [ "VV", [ "通" ] ], [ "NP", [ [ "NN", [ "电话" ] ] ] ] ] ], [ "VP", [ [ "VV", [ "讨论" ] ], [ "NP", [ [ "NP", [ [ "NR", [ "华为" ] ] ] ], [ "CC", [ "与" ] ], [ "NP", [ [ "NR", [ "苹果" ] ], [ "NN", [ "公司" ] ] ] ] ] ] ] ] ] ] ] ], [ "PU", [ "。" ] ] ] ] ] ] ]
        // }
    }

    @Test
    void parseSentences() throws IOException {
        Map<String, List> doc = client.parse(new String[]{
                "2021年HanLPv2.1为生产环境带来次世代最先进的多语种NLP技术。",
                "英首相与特朗普通电话讨论华为与苹果公司。"
        });
        prettyPrint(doc);
    }

    @Test
    void parseTokens() throws IOException {
        Map<String, List> doc = client.parse(new String[][]{
                new String[]{"2021年", "HanLPv2.1", "为", "生产", "环境", "带来", "次", "世代", "最", "先进", "的", "多语种", "NLP", "技术", "。"},
                new String[]{"英", "首相", "与", "特朗普", "通", "电话", "讨论", "华为", "与", "苹果", "公司", "。"},
        });
        prettyPrint(doc);
    }

    @Test
    void parseCoarse() throws IOException {
        Map<String, List> doc = client.parse(
                "阿婆主来到北京立方庭参观自然语义科技公司。",
                new String[]{"tok/coarse", "pos", "dep"},
                new String[]{"tok/fine"});
        prettyPrint(doc);
    }

    @Test
    void tokenize() throws IOException {
        List<List<String>> fine = client.tokenize("2021年HanLPv2.1为生产环境带来次世代最先进的多语种NLP技术。阿婆主来到北京立方庭参观自然语义科技公司。");
        System.out.println(fine);
        List<List<String>> coarse = client.tokenize("2021年HanLPv2.1为生产环境带来次世代最先进的多语种NLP技术。阿婆主来到北京立方庭参观自然语义科技公司。", true);
        System.out.println(coarse);
    }

    @Test
    void textStyleTransfer() throws IOException {
        String doc = client.textStyleTransfer("国家对中石油抱有很大的期望.", "gov_doc");
        prettyPrint(doc);
    }

    @Test
    void semanticTextualSimilarity() throws IOException {
        Double similarity = client.semanticTextualSimilarity("看图猜一电影名", "看图猜电影");
        prettyPrint(similarity);
        List<Double> similarities = client.semanticTextualSimilarity(new String[][]{
                new String[]{"看图猜一电影名", "看图猜电影"},
                new String[]{"北京到上海的动车票", "上海到北京的动车票"}
        });
        for (Double similarityPerPair : similarities) {
            prettyPrint(similarityPerPair);
        }
    }

    @Test
    void coreferenceResolutionText() throws IOException {
        CoreferenceResolutionOutput clusters = client.coreferenceResolution("我姐送我她的猫。我很喜欢它。");
        prettyPrint(clusters);
    }

    @Test
    void coreferenceResolutionTokens() throws IOException {
        List<Set<Span>> clusters = client.coreferenceResolution(
                new String[][]{
                        new String[]{"我", "姐", "送", "我", "她", "的", "猫", "。"},
                        new String[]{"我", "很", "喜欢", "它", "。"}});
        prettyPrint(clusters);
    }

    @Test
    void coreferenceResolutionTokensWithSpeakers() throws IOException {
        List<Set<Span>> clusters = client.coreferenceResolution(
                new String[][]{
                        new String[]{"我", "姐", "送", "我", "她", "的", "猫", "。"},
                        new String[]{"我", "很", "喜欢", "它", "。"}},
                new String[]{"张三", "张三"});
        prettyPrint(clusters);
    }

    @Test
    void keyphraseExtraction() throws IOException {
        prettyPrint(client.keyphraseExtraction(
                "自然语言处理是一门博大精深的学科，掌握理论才能发挥出HanLP的全部性能。" +
                        "《自然语言处理入门》是一本配套HanLP的NLP入门书，助你零起点上手自然语言处理。", 3));
    }

    @Test
    void extractiveSummarization() throws IOException {
        prettyPrint(client.extractiveSummarization(
                "据DigiTimes报道，在上海疫情趋缓，防疫管控开始放松后，苹果供应商广达正在逐步恢复其中国工厂的MacBook产品生产。\n" +
                        "据供应链消息人士称，生产厂的订单拉动情况正在慢慢转强，这会提高MacBook Pro机型的供应量，并缩短苹果客户在过去几周所经历的延长交货时间。\n" +
                        "仍有许多苹果笔记本用户在等待3月和4月订购的MacBook Pro机型到货，由于苹果的供应问题，他们的发货时间被大大推迟了。\n" +
                        "据分析师郭明錤表示，广达是高端MacBook Pro的唯一供应商，自防疫封控依赖，MacBook Pro大部分型号交货时间增加了三到五周，\n" +
                        "一些高端定制型号的MacBook Pro配置要到6月底到7月初才能交货。\n" +
                        "尽管MacBook Pro的生产逐渐恢复，但供应问题预计依然影响2022年第三季度的产品销售。\n" +
                        "苹果上周表示，防疫措施和元部件短缺将继续使其难以生产足够的产品来满足消费者的强劲需求，这最终将影响苹果6月份的收入。"));
    }

    @Test
    void abstractiveSummarization() throws IOException {
        prettyPrint(client.abstractiveSummarization(
                "每经AI快讯，2月4日，长江证券研究所金属行业首席分析师王鹤涛表示，2023年海外经济衰退，美债现处于历史高位，\n" +
                        "黄金的趋势是值得关注的；在国内需求修复的过程中，看好大金属品种中的铜铝钢。\n" +
                        "此外，在细分的小品种里，建议关注两条主线，一是新能源，比如锂、钴、镍、稀土，二是专精特新主线。（央视财经）"));
    }

    @Test
    void abstractMeaningRepresentationText() throws IOException {
        prettyPrint(client.abstractMeaningRepresentation("男孩希望女孩相信他。阿婆主来到北京立方庭参观自然语义科技公司。"));
    }

    @Test
    void abstractMeaningRepresentationTokens() throws IOException {
        prettyPrint(client.abstractMeaningRepresentation(new String[][]{
                new String[]{"2021年", "HanLPv2.1", "为", "生产", "环境", "带来", "次", "世代", "最", "先进", "的", "多语种", "NLP", "技术", "。"},
                new String[]{"英", "首相", "与", "特朗普", "通", "电话", "讨论", "华为", "与", "苹果", "公司", "。"}}));
    }

    @Test
    void grammaticalErrorCorrection() throws IOException {
        prettyPrint(client.grammaticalErrorCorrection(new String[]{"每个青年都应当有远大的报复。", "有的同学对语言很兴趣。"}));
    }

    @Test
    void languageIdentification() throws IOException {
        prettyPrint(client.languageIdentification(new String[]{
                "In 2021, HanLPv2.1 delivers state-of-the-art multilingual NLP techniques to production environment.",
                "2021年、HanLPv2.1は次世代の最先端多言語NLP技術を本番環境に導入します。",
                "2021年 HanLPv2.1为生产环境带来次世代最先进的多语种NLP技术。",
        }));
    }

    @Test
    void sentimentAnalysis() throws IOException {
        prettyPrint(client.sentimentAnalysis(
                "“这是一部男人必看的电影。”人人都这么说。但单纯从性别区分，就会让这电影变狭隘。《肖申克的救赎》突破了男人电影的局限，通篇几乎充满令人难以置信的温馨基调，而电影里最伟大的主题是“希望”。 当我们无奈地遇到了如同肖申克一般囚禁了心灵自由的那种囹圄，我们是无奈的老布鲁克，灰心的瑞德，还是智慧的安迪？运用智慧，信任希望，并且勇敢面对恐惧心理，去打败它？ 经典的电影之所以经典，因为他们都在做同一件事——让你从不同的角度来欣赏希望的美好。"
        ));
    }

    void prettyPrint(Object object) throws JsonProcessingException {
        ObjectMapper mapper = new ObjectMapper();
        System.out.println(mapper.writerWithDefaultPrettyPrinter().writeValueAsString(object));
    }
}

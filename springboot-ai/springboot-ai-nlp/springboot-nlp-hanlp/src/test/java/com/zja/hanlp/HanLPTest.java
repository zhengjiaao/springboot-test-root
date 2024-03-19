package com.zja.hanlp;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.corpus.tag.Nature;
import com.hankcs.hanlp.dictionary.CustomDictionary;
import com.hankcs.hanlp.model.crf.CRFLexicalAnalyzer;
import com.hankcs.hanlp.seg.NShort.NShortSegment;
import com.hankcs.hanlp.seg.Other.AhoCorasickDoubleArrayTrieSegment;
import com.hankcs.hanlp.seg.Segment;
import com.hankcs.hanlp.seg.Viterbi.ViterbiSegment;
import com.hankcs.hanlp.seg.common.Term;
import com.hankcs.hanlp.tokenizer.*;
import com.hankcs.hanlp.utility.LexiconUtility;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

/**
 * HanLP 分词 (基础分词、极速分词、标准分词、CRF词法分词、NLP分词、索引分词、多线程并行分词、AhoCorasickDoubleArrayTrie 分词、繁体中文分词、N最短路径分词、自定义词性,以及往词典中插入自定义词性的词语)
 *
 * @author: zhengja
 * @since: 2024/02/20 14:03
 */
public class HanLPTest {

    /**
     * 基础分词 简单示例
     * 基础分词只进行基本NGram分词，不识别命名实体，不使用用户词典，分词后会带有词性
     */
    @Test
    public void BasicTokenizer_simple_test() {
        String word = "我的报销";

        List<Term> terms = HanLP.segment(word);
        terms.forEach(term -> {
            System.out.println(term.word + term.nature);
            // 我r
            // 的uj
            // 报销v
        });
    }

    // 基础分词
    @Test
    public void BasicTokenizer_test() {
        String text = "程序员(英文Programmer)是从事程序开发、维护的专业人员。" +
                "一般将程序员分为程序设计人员和程序编码人员，" +
                "但两者的界限并不非常清楚，特别是在中国。" +
                "软件从业人员分为初级程序员、高级程序员、系统" +
                "分析员和项目经理四大类。";
        System.out.println(BasicTokenizer.segment(text));
        // 测试分词速度，让大家对HanLP的性能有一个直观的认识
        long start = System.currentTimeMillis();
        int pressure = 100000;
        for (int i = 0; i < pressure; ++i) {
            BasicTokenizer.segment(text);
        }
        double costTime = (System.currentTimeMillis() - start) / (double) 1000;
        System.out.printf("BasicTokenizer分词速度：%.2f字每秒\n", text.length() * pressure / costTime);
    }


    /**
     * 极速分词
     * 基于DoubleArrayTrie实现的词典正向最长分词，适用于“高吞吐量”“精度一般”的场合
     */
    @Test
    public void HighSpeedSegment_test() {
        String text = "江西鄱阳湖干枯，中国最大淡水湖变成大草原";
        HanLP.Config.ShowTermNature = false;
        System.out.println(SpeedTokenizer.segment(text));
        long start = System.currentTimeMillis();
        int pressure = 1000000;
        for (int i = 0; i < pressure; ++i) {
            SpeedTokenizer.segment(text);
        }
        double costTime = (System.currentTimeMillis() - start) / (double) 1000;
        System.out.printf("SpeedTokenizer分词速度：%.2f字每秒\n", text.length() * pressure / costTime);
    }

    /**
     * 标准分词
     */
    @Test
    public void Segment_test() {
        String[] testCase = new String[]{
                "商品和服务",
                "当下雨天地面积水分外严重",
                "结婚的和尚未结婚的确实在干扰分词啊",
                "买水果然后来世博园最后去世博会",
                "中国的首都是北京",
                "欢迎新老师生前来就餐",
                "工信处女干事每月经过下属科室都要亲口交代24口交换机等技术性器件的安装工作",
                "随着页游兴起到现在的页游繁盛，依赖于存档进行逻辑判断的设计减少了，但这块也不能完全忽略掉。",
        };
        for (String sentence : testCase) {
            // 对StandardTokenizer.segment的包装
            List<Term> termList = HanLP.segment(sentence);
            System.out.println(termList);
        }
    }

    /**
     * CRF词法分词
     * 自1.6.6版起模型格式不兼容旧版：CRF模型为对数线性模型，通过复用结构化感知机的维特比解码算法，效率提高10倍。
     */
    @Test
    public void CRFLexicalAnalyzer_test() throws IOException {
        CRFLexicalAnalyzer analyzer = new CRFLexicalAnalyzer();
        String[] tests = new String[]{
                "商品和服务",
                "上海华安工业（集团）公司董事长谭旭光和秘书胡花蕊来到美国纽约现代艺术博物馆参观",
                "微软公司於1975年由比爾·蓋茲和保羅·艾倫創立，18年啟動以智慧雲端、前端為導向的大改組。" // 支持繁体中文
        };
        for (String sentence : tests) {
            System.out.println(analyzer.seg(sentence));
        }
    }

    /**
     * 标准分词
     * 更精准的中文分词、词性标注与命名实体识别。语料库规模决定实际效果，面向生产环境的语料库应当在千万字量级。
     * <p>
     * 词性标注可以使用 Sentence#translateLabels() 转为中文显示：
     */
    @Test
    public void NLPSegment_test() {
        NLPTokenizer.ANALYZER.enableCustomDictionary(false); // 中文分词≠词典，不用词典照样分词。
        System.out.println(NLPTokenizer.segment("我新造一个词叫幻想乡你能识别并正确标注词性吗？")); // “正确”是副形词。
        // 注意观察下面两个“希望”的词性、两个“晚霞”的词性
        System.out.println(NLPTokenizer.analyze("我的希望是希望张晚霞的背影被晚霞映红").translateLabels());
        System.out.println(NLPTokenizer.analyze("支援臺灣正體香港繁體：微软公司於1975年由比爾·蓋茲和保羅·艾倫創立。"));
    }

    /**
     * 索引分词
     */
    @Test
    public void IndexSegment_test() {
        List<Term> termList = IndexTokenizer.segment("主副食品");
        for (Term term : termList) {
            System.out.println(term + " [" + term.offset + ":" + (term.offset + term.word.length()) + "]");
        }

        System.out.println("\n最细颗粒度切分：");
        IndexTokenizer.SEGMENT.enableIndexMode(1);
        termList = IndexTokenizer.segment("主副食品");
        for (Term term : termList) {
            System.out.println(term + " [" + term.offset + ":" + (term.offset + term.word.length()) + "]");
        }
    }

    /**
     * 多线程并行分词
     * 由于HanLP的任何分词器都是线程安全的，所以用户只需调用一个配置接口就可以启用任何分词器的并行化
     */
    @Test
    public void MultithreadingSegment_test() throws IOException {
        Segment segment = new CRFLexicalAnalyzer(HanLP.Config.CRFCWSModelPath).enableCustomDictionary(false); // CRF分词器效果好，速度慢，并行化之后可以提高一些速度

        String text = "程序员(英文Programmer)是从事程序开发、维护的专业人员。" +
                "一般将程序员分为程序设计人员和程序编码人员，" +
                "但两者的界限并不非常清楚，特别是在中国。" +
                "软件从业人员分为初级程序员、高级程序员、系统" +
                "分析员和项目经理四大类。";
        HanLP.Config.ShowTermNature = false;
        System.out.println(segment.seg(text));
        int pressure = 10000;
        StringBuilder sbBigText = new StringBuilder(text.length() * pressure);
        for (int i = 0; i < pressure; i++) {
            sbBigText.append(text);
        }
        text = sbBigText.toString();
        System.gc();

        long start;
        double costTime;
        // 测个速度

        segment.enableMultithreading(false);
        start = System.currentTimeMillis();
        segment.seg(text);
        costTime = (System.currentTimeMillis() - start) / (double) 1000;
        System.out.printf("单线程分词速度：%.2f字每秒\n", text.length() / costTime);
        System.gc();

        segment.enableMultithreading(true); // 或者 segment.enableMultithreading(4);
        start = System.currentTimeMillis();
        segment.seg(text);
        costTime = (System.currentTimeMillis() - start) / (double) 1000;
        System.out.printf("多线程分词速度：%.2f字每秒\n", text.length() / costTime);
        System.gc();

        // Note:
        // 内部的并行化机制可以对1万字以上的大文本开启多线程分词
        // 另一方面，HanLP中的任何Segment本身都是线程安全的。
        // 你可以开10个线程用同一个CRFSegment对象切分任意文本，不需要任何线程同步的措施，每个线程都可以得到正确的结果。
    }

    /**
     * AhoCorasickDoubleArrayTrie 分词
     * AhoCorasickDoubleArrayTrieSegment要求用户必须提供自己的词典路径
     * 准备词典：dictionary/my-AhoCorasickDoubleArrayTrie.txt
     * 微观经济学
     * 继续教育
     * 循环经济
     */
    @Test
    public void UseAhoCorasickDoubleArrayTrieSegment_test() throws IOException {
        AhoCorasickDoubleArrayTrieSegment segment = new AhoCorasickDoubleArrayTrieSegment("dictionary/my-AhoCorasickDoubleArrayTrie.txt");
        System.out.println(segment.seg("微观经济学继续教育循环经济"));
    }

    /**
     * 繁体中文分词
     */
    @Test
    public void TraditionalChineseSegment_test() {
        List<Term> termList = TraditionalChineseTokenizer.segment("大衛貝克漢不僅僅是名著名球員，球場以外，其妻為前" +
                "辣妹合唱團成員維多利亞·碧咸，亦由於他擁有" +
                "突出外表、百變髮型及正面的形象，以至自己" +
                "品牌的男士香水等商品，及長期擔任運動品牌" +
                "Adidas的代言人，因此對大眾傳播媒介和時尚界" +
                "等方面都具很大的影響力，在足球圈外所獲得的" +
                "認受程度可謂前所未見。");
        System.out.println(termList);

        termList = TraditionalChineseTokenizer.segment("（中央社記者黃巧雯台北20日電）外需不振，影響接單動能，經濟部今天公布7月外銷訂單金額362.9億美元，年減5%，" +
                "連續4個月衰退，減幅較6月縮小。1040820\n");
        System.out.println(termList);

        termList = TraditionalChineseTokenizer.segment("中央社记者黄巧雯台北20日电");
        System.out.println(termList);
    }

    /**
     * N最短路径分词
     */
    @Test
    public void NShortSegment_test() {
        Segment nShortSegment = new NShortSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        Segment shortestSegment = new ViterbiSegment().enableCustomDictionary(false).enablePlaceRecognize(true).enableOrganizationRecognize(true);
        String[] testCase = new String[]{
                "一般将程序员分为程序设计人员和程序编码人员",
                "软件从业人员分为初级程序员、高级程序员、系统分析员和项目经理四大类。",
                "程序员(英文Programmer)是从事程序开发、维护的专业人员。",
        };
        for (String sentence : testCase) {
            System.out.println("N-最短分词：" + nShortSegment.seg(sentence) + "\n最短路分词：" + shortestSegment.seg(sentence));
        }
    }

    /**
     * 自定义词性,以及往词典中插入自定义词性的词语
     */
    @Test
    public void CustomNature_test() {
        // 对于系统中已有的词性,可以直接获取
        Nature pcNature = Nature.fromString("n");
        System.out.println(pcNature);
        // 此时系统中没有"电脑品牌"这个词性
        pcNature = Nature.fromString("电脑品牌");
        System.out.println(pcNature);
        // 我们可以动态添加一个
        pcNature = Nature.create("电脑品牌");
        System.out.println(pcNature);
        // 可以将它赋予到某个词语
        LexiconUtility.setAttribute("苹果电脑", pcNature);
        // 或者
        LexiconUtility.setAttribute("苹果电脑", "电脑品牌 1000");
        // 它们将在分词结果中生效
        List<Term> termList = HanLP.segment("苹果电脑可以运行开源阿尔法狗代码吗");
        System.out.println(termList);
        // 还可以直接插入到用户词典
        CustomDictionary.insert("阿尔法狗", "科技名词 1024");
        StandardTokenizer.SEGMENT.enablePartOfSpeechTagging(true);  // 依然支持隐马词性标注
        termList = HanLP.segment("苹果电脑可以运行开源阿尔法狗代码吗");
        System.out.println(termList);
    }

    /**
     * 自动去除停用词、自动断句的分词器
     * 准备词典：dictionary/my-stopwords.txt
     *
     */
    @Test
    public void NotionalTokenizer_test() {
        String text = "小区居民有的反对喂养流浪猫，而有的居民却赞成喂养这些小宝贝";
        // 自动去除停用词
        System.out.println(NotionalTokenizer.segment(text));    // 停用词典位于dictionary/my-stopwords.txt，可以自行修改
        // 自动断句+去除停用词
        for (List<Term> sentence : NotionalTokenizer.seg2sentence(text)) {
            System.out.println(sentence);
        }
    }
}

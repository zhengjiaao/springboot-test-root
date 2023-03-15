/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-03-15 17:32
 * @Since:
 */
package com.zja.hanlp;

import com.hankcs.hanlp.HanLP;
import com.hankcs.hanlp.seg.common.Term;

import java.util.List;

/**
 * @author: zhengja
 * @since: 2023/03/15 17:32
 */
public class HanlpTests {

    public static void main(String[] args) {
        String word ="我的报销";

        List<Term> terms = HanLP.segment(word);

        terms.forEach(term -> {

            System.out.println(term.word + term.nature);
            //我r
            //的uj
            //报销v
        });
    }

}

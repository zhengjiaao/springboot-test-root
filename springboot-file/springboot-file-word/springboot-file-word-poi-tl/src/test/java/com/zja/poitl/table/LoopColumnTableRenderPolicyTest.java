package com.zja.poitl.table;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.plugin.table.LoopColumnTableRenderPolicy;
import com.deepoove.poi.policy.RenderPolicy;
import com.zja.poitl.mdoel.Goods;
import com.zja.poitl.mdoel.Labor;
import com.zja.poitl.mdoel.PaymentHackData;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * 表-循环列
 *
 * @Author: zhengja
 * @Date: 2024-11-18 10:43
 */
@DisplayName("Example for HackLoop Table")
public class LoopColumnTableRenderPolicyTest {

    String resource = "templates/word/table/render_loopcolumn.docx";
    PaymentHackData data = new PaymentHackData();

    @BeforeEach
    public void init() {
        data.setTotal("总共：7200");

        List<Goods> goods = new ArrayList<>();
        Goods good = new Goods();
        good.setCount(4);
        good.setName("墙纸");
        good.setDesc("书房卧室");
        good.setDiscount(1500);
        good.setPrice(400);
        good.setTax(new Random().nextInt(10) + 20);
        good.setTotalPrice(1600);
        // good.setPicture(Pictures.ofLocal("src/test/resources/earth.png").size(24, 24).create());
        goods.add(good);
        goods.add(good);
        goods.add(good);
        goods.add(good);
        data.setGoods(goods);

        List<Labor> labors = new ArrayList<>();
        Labor labor = new Labor();
        labor.setCategory("油漆工");
        labor.setPeople(2);
        labor.setPrice(400);
        labor.setTotalPrice(1600);
        labors.add(labor);
        labors.add(labor);
        labors.add(labor);
        labors.add(labor);
        data.setLabors(labors);

        // same line
        data.setGoods2(goods);
        data.setLabors2(labors);

    }

    // 表格嵌套循环-循环列
    @Test
    public void testPaymentHackExample() throws Exception {
        LoopColumnTableRenderPolicy hackLoopTableRenderPolicy = new LoopColumnTableRenderPolicy();
        RenderPolicy hackLoopSameLineTableRenderPolicy = new LoopColumnTableRenderPolicy(true);
        Configure config = Configure.builder().bind("goods", hackLoopTableRenderPolicy)
                .bind("labors", hackLoopTableRenderPolicy).bind("goods2", hackLoopSameLineTableRenderPolicy)
                .bind("labors2", hackLoopSameLineTableRenderPolicy).build();
        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream(resource), config).render(data);
        template.writeToFile("target/out_render_loopcolumn.docx");
    }

    private static InputStream getResourceAsStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }

}
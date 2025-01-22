package com.zja.poitl.table.policy;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.zja.poitl.mdoel.Goods;
import com.zja.poitl.mdoel.Labor;
import com.zja.poitl.mdoel.PaymentHackData;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableCell;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @Author: zhengja
 * @Date: 2024-11-19 17:47
 */
public class CustomLoopRowTableRenderPolicyTest {

    String resource = "templates/word/table/render_hackloop.docx";
    PaymentHackData data = new PaymentHackData();

    @BeforeEach
    public void init() {
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
        data.setLabors(labors);

        data.setTotal("1024");

        // same line
        data.setGoods2(goods);
        data.setLabors2(labors);

    }

    @Test
    public void testPaymentHackExample2() throws Exception {
        CustomLoopRowTableRenderPolicy customLoopTableRenderPolicy = new CustomLoopRowTableRenderPolicy();
        CustomLoopRowTableRenderPolicy customLoopSameLineTableRenderPolicy = new CustomLoopRowTableRenderPolicy(true);
        Configure config = Configure.builder().bind("goods", customLoopTableRenderPolicy)
                .bind("labors", customLoopTableRenderPolicy).bind("goods2", customLoopSameLineTableRenderPolicy)
                .bind("labors2", customLoopSameLineTableRenderPolicy).build();
        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream(resource), config).render(data);

        // 测试输出数据
        // List<XWPFTable> tables = template.getXWPFDocument().getTables();
        // XWPFTable table = tables.get(0);
        // List<XWPFTableRow> rows = table.getRows();
        // for (XWPFTableRow xwpfTableRow : rows) {
        //     List<XWPFTableCell> tableCells = xwpfTableRow.getTableCells();
        //     for (XWPFTableCell xwpfTableCell : tableCells) {
        //         System.out.println(xwpfTableCell.getText());
        //     }
        // }

        template.writeToFile("target/out_render_looprow2.docx");
    }

    private static InputStream getResourceAsStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }
}

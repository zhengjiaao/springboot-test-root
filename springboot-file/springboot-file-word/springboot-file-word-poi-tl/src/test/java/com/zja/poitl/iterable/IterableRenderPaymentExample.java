package com.zja.poitl.iterable;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.data.Rows;
import com.deepoove.poi.data.TableRenderData;
import com.deepoove.poi.data.Tables;
import com.deepoove.poi.data.style.BorderStyle;
import com.zja.poitl.mdoel.DetailData;
import com.zja.poitl.mdoel.PaymentData;
import com.zja.poitl.table.DetailTablePolicy;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-11-18 11:36
 */
@DisplayName("Foreach table example")
public class IterableRenderPaymentExample {

    PaymentData datas = new PaymentData();

    @BeforeEach
    public void init() {
        datas.setNO("KB.6890451");
        datas.setID("ZHANG_SAN_091");
        datas.setTaitou("深圳XX家装有限公司");
        datas.setConsignee("丙丁");

        datas.setSubtotal("8000");
        datas.setTax("600");
        datas.setTransform("120");
        datas.setOther("250");
        datas.setUnpay("6600");
        datas.setTotal("总共：7200");

        RowRenderData header = Rows.of("日期", "订单编号", "销售代表", "离岸价", "发货方式", "条款", "税号").bgColor("F2F2F2").center()
                .textColor("7F7f7F").textFontFamily("Hei").textFontSize(9).create();
        RowRenderData row = Rows.of("2018-06-12", "SN18090", "李四", "5000元", "快递", "附录A", "T11090").center().create();
        BorderStyle borderStyle = new BorderStyle();
        borderStyle.setColor("A6A6A6");
        borderStyle.setSize(4);
        borderStyle.setType(XWPFTable.XWPFBorderType.SINGLE);
        TableRenderData table = Tables.ofA4MediumWidth().addRow(header).addRow(row).border(borderStyle).center()
                .create();
        datas.setOrder(table);

        DetailData detailTable = new DetailData();
        RowRenderData good = Rows.of("4", "墙纸", "书房+卧室", "1500", "/", "400", "1600").center().create();
        List<RowRenderData> goods = Arrays.asList(good, good, good);
        RowRenderData labor = Rows.of("油漆工", "2", "200", "400").center().create();
        List<RowRenderData> labors = Arrays.asList(labor, labor, labor, labor);
        detailTable.setGoods(goods);
        detailTable.setLabors(labors);
        datas.setDetailTable(detailTable);
    }

    @SuppressWarnings("serial")
    @Test
    public void testIterablePaymentExample() throws Exception {

        List<PaymentData> orders = new ArrayList<PaymentData>();
        orders.add(datas);
        orders.add(datas);

        Configure config = Configure.builder().bind("detail_table", new DetailTablePolicy()).build();
        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/iterable/iterable_payment.docx"), config);
        template.render(new HashMap<String, Object>() {
            {
                put("orders", orders);
            }
        }).writeToFile("target/out_iterable_payment.docx");
    }

    private static InputStream getResourceAsStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }

}
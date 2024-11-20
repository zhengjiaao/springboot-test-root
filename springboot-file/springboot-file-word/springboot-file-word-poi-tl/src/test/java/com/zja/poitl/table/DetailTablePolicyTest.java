package com.zja.poitl.table;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.*;
import com.deepoove.poi.data.style.BorderStyle;
import com.zja.poitl.mdoel.*;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.*;

/**
 * 动态表格填充策略-测试
 *
 * @Author: zhengja
 * @Date: 2024-11-18 10:10
 */
@DisplayName("Example for DetailTablePolicy")
public class DetailTablePolicyTest {

    String resource = "templates/word/table/payment.docx";
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
        RowRenderData good1 = Rows.of("1", "墙纸", "书房+卧室", "1500", "/", "400", "1600").center().create();
        RowRenderData good2 = Rows.of("2", "墙纸", "书房+卧室", "1500", "/", "400", "1600").center().create();
        RowRenderData good3 = Rows.of("3", "墙纸", "书房+卧室", "1500", "/", "400", "1600").center().create();
        List<RowRenderData> goods = Arrays.asList(good1, good2, good3);
        RowRenderData labor = Rows.of("油漆工", "1", "200", "400").center().create();
        List<RowRenderData> labors = Arrays.asList(labor, labor, labor);
        Collections.reverse(goods); // 反正序列
        detailTable.setGoods(goods);
        detailTable.setLabors(labors);
        datas.setDetailTable(detailTable);

        // 创建第一个表格数据
        List<RowRenderData> rows1 = new ArrayList<>();
        rows1.add(Rows.of("Row 1 Col 1", "Row 1 Col 2", "Row 1 Col 3").create());
        rows1.add(Rows.of("Row 2 Col 1", "Row 2 Col 2", "Row 2 Col 3").create());
        rows1.add(Rows.of("Row1", "Row 2 Col 2", "Row 2 Col 3").create());
        rows1.add(Rows.of("Row1", "Row 2 Col 2", "Row 2 Col 3").create());
        rows1.add(Rows.of("Row 5 Col 1", "Row 2 Col 2", "Row 2 Col 3").create());
        Collections.reverse(rows1); // 反正序列
        datas.setTable1(rows1);

        // 创建第二个表格数据
        List<RowRenderData> rows2 = new ArrayList<>();
        rows2.add(Rows.of("Row 1 Col A", "Row 1 Col B", "Row 1 Col C").create());
        rows2.add(Rows.of("Row 2 Col A", "Row 2 Col B", "Row 2 Col C").create());
        rows2.add(Rows.of("RowA", "Row 2 Col B", "Row 2 Col C").create());
        rows2.add(Rows.of("RowA", "Row 2 Col B", "Row 2 Col C").create());
        rows2.add(Rows.of("Row 5 Col A", "Row 2 Col B", "Row 2 Col C").create());
        datas.setTable2(rows2);
    }

    // 动态表格填充策略
    @Test
    public void testPaymentExample() throws Exception {
        Configure config = Configure.builder().bind("detail_table", new DetailTablePolicy()).build();
        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream(resource), config).render(datas);
        template.writeToFile("target/out_example_payment.docx");
    }

    // 动态表格填充策略2
    @Test
    public void testPaymentExample2() throws Exception {
        Configure config = Configure.builder()
                .bind("detail_table", new DetailTablePolicy2())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream(resource), config).render(datas);
        template.writeToFile("target/out_example_payment_2.docx");
    }

    // 动态表格填充策略3
    @Test
    public void testPaymentExample3() throws Exception {
        Configure config = Configure.builder()
                .bind("detail_table", new DetailTablePolicy2())
                .bind("detail_table1", new DetailTablePolicy3())
                .bind("detail_table2", new DetailTablePolicy3())
                .build();
        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/table/payment_3.docx"), config).render(datas);
        template.writeToFile("target/out_example_payment_3.docx");
    }

    private static InputStream getResourceAsStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }

}

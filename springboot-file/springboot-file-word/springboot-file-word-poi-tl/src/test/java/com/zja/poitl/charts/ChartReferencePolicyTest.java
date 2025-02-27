package com.zja.poitl.charts;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.data.ChartMultiSeriesRenderData;
import com.deepoove.poi.data.ChartSingleSeriesRenderData;
import com.deepoove.poi.data.Charts;
import org.junit.jupiter.api.Test;

import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

/**
 * 图表引用
 *
 * @Author: zhengja
 * @Date: 2024-12-17 10:07
 */
public class ChartReferencePolicyTest {

    private static InputStream getResourceAsStream(String fileName) {
        return Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName);
    }

    @Test
    public void testChart() throws Exception {
        Map<String, Object> datas = new HashMap<String, Object>();
        ChartMultiSeriesRenderData chart = createMultiSeriesChart();
        datas.put("barChart", chart);
        datas.put("VBarChart", chart);
        datas.put("3dBarChart", chart);
        datas.put("lineChart", chart);
        datas.put("redarChart", chart);
        datas.put("areaChart", chart);

        ChartMultiSeriesRenderData combChart = createComboChart();
        datas.put("combChart", combChart);

        ChartSingleSeriesRenderData pie = createSingleSeriesChart();
        datas.put("pieChart", pie);
        datas.put("doughnutChart", pie);

        ChartMultiSeriesRenderData scatter = createMultiSeriesScatterChart();
        datas.put("scatterChart", scatter);
        datas.put("relatechart", scatter);

        XWPFTemplate template = XWPFTemplate.compile(getResourceAsStream("templates/word/charts/reference_chart.docx")).render(datas);
        template.writeToFile("target/out_reference_chart.docx");
    }

    private ChartSingleSeriesRenderData createSingleSeriesChart() {
        return Charts.ofSingleSeries("ChartTitle", new String[]{"俄罗斯", "加拿大", "美国", "中国", "巴西", "澳大利亚", "印度"})
                .series("countries", new Integer[]{17098242, 9984670, 9826675, 9596961, 8514877, 7741220, 3287263})
                // .setxAsixTitle("X Title") // 可选地，数据类型存在默认轴时，不会生效，只有配置了x轴标题时，才会生效
                // .setyAsixTitle("Y Title") // 可选地，数据类型存在默认轴时，不会生效，只有配置了y轴标题时，才会生效
                .create();
    }

    private ChartMultiSeriesRenderData createMultiSeriesScatterChart() {
        return Charts.ofMultiSeries("ChartTitle", new String[]{"1", "3", "4", "6", "9", "2", "4"})
                .addSeries("A", new Integer[]{12, 4, 9, 2, 10, 5, 7})
                .addSeries("B", new Integer[]{2, 6, 3, 6, 2, 6, 9})
                .setxAsixTitle("X Title")
                .setyAsixTitle("Y Title")
                .create();
    }

    private ChartMultiSeriesRenderData createMultiSeriesChart() {
        return Charts
                .ofMultiSeries("CustomTitle",
                        new String[]{"中文", "English", "日本語", "português", "中文", "English", "日本語", "português"})
                .addSeries("countries", new Double[]{15.0, 6.0, 18.0, 231.0, 150.0, 6.0, 118.0, 31.0})
                .addSeries("speakers", new Double[]{223.0, 119.0, 154.0, 142.0, 223.0, 119.0, 54.0, 42.0})
                .addSeries("youngs", new Double[]{323.0, 89.0, 54.0, 42.0, 223.0, 119.0, 54.0, 442.0})
                // .setxAsixTitle("X Title") // todo 未生效，待解决
                // .setyAsixTitle("Y Title") // 可选地
                .create();
    }

    private ChartMultiSeriesRenderData createComboChart() {
        return Charts.ofComboSeries("ComboChartTitle", new String[]{"中文", "English", "日本語", "português"})
                .addBarSeries("countries", new Double[]{15.0, 6.0, 18.0, 231.0})
                .addBarSeries("speakers", new Double[]{223.0, 119.0, 154.0, 142.0})
                .addBarSeries("NewBar", new Double[]{223.0, 119.0, 154.0, 142.0})
                .addLineSeries("youngs", new Double[]{323.0, 89.0, 54.0, 42.0})
                .addLineSeries("NewLine", new Double[]{123.0, 59.0, 154.0, 42.0})
                // .setxAsixTitle("X Title")
                // .setyAsixTitle("Y Title")
                .create();
    }
}

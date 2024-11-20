package com.zja.poitl.table;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.policy.DynamicTableRenderPolicy;
import com.deepoove.poi.policy.TableRenderPolicy;
import com.deepoove.poi.util.TableTools;
import com.zja.poitl.mdoel.DetailData;
import org.apache.poi.xwpf.usermodel.XWPFTable;
import org.apache.poi.xwpf.usermodel.XWPFTableRow;

import java.util.List;

/**
 * 动态表格填充策略
 * <p>
 * 付款通知书 明细表格的自定义渲染策略<br/>
 * 1. 填充货品数据 <br/>
 * 2. 填充人工费数据 <br/>
 * </p>
 *
 * @Author: zhengja
 * @Date: 2024-11-18 10:49
 */
public class DetailTablePolicy2 extends DynamicTableRenderPolicy {

    private static final int LABORS_START_ROW = 5; // 假设人工费数据从第5行开始
    private static final int GOODS_START_ROW = 2; // 假设货品数据从第10行开始

    public void render(XWPFTable table, Object data) throws Exception {
        if (data == null) return;
        DetailData detailData = (DetailData) data;

        // 货品数据
        List<RowRenderData> goods = detailData.getGoods();
        if (goods != null) {
            renderData(table, goods, GOODS_START_ROW, -1, -1); // 不需要合并单元格
        }

        // 人工费数据
        List<RowRenderData> labors = detailData.getLabors();
        if (labors != null) {
            renderData(table, labors, LABORS_START_ROW, 0, 3);
        }
    }

    private void renderData(XWPFTable table, List<RowRenderData> data, int startRow, int mergeStartCol, int mergeEndCol) throws Exception {
        if (table == null || data == null) {
            throw new IllegalArgumentException("table or data cannot be null");
        }

        table.removeRow(startRow);
        for (int i = 0; i < data.size(); i++) {
            XWPFTableRow newRow = table.insertNewTableRow(startRow);
            if (newRow == null) {
                throw new IllegalStateException("Failed to create new row in table");
            }
            for (int j = 0; j < 7; j++) newRow.createCell();

            // 合并单元格 行
            if (mergeStartCol >= 0 && mergeEndCol >= 0) {
                TableTools.mergeCellsHorizonal(table, startRow, mergeStartCol, mergeEndCol);
            }

            // 合并单元格 列
            /*if (mergeStartCol >= 0 && mergeEndCol >= 0) {
                TableTools.mergeCellsVertically(table, startRow, mergeStartCol, mergeEndCol);
            }*/

            // 渲染数据
            TableRenderPolicy.Helper.renderRow(newRow, data.get(i));
        }
    }

}

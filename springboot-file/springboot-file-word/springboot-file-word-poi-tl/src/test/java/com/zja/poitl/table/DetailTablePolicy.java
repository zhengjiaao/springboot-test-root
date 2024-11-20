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
public class DetailTablePolicy extends DynamicTableRenderPolicy {

    // 初始化变量：定义了货品和人工费数据在表格中的起始行数
    // 货品填充数据所在行数
    int goodsStartRow = 2;
    // 人工费填充数据所在行数
    int laborsStartRow = 5;

    // 注意，需要从下往上插入行，否则会报异常。
    @Override
    public void render(XWPFTable table, Object data) throws Exception {
        if (null == data) return;
        DetailData detailData = (DetailData) data;

        // 人工费数据：如果 labors 不为空，移除表格中指定行，然后逐行插入新的行，并创建单元格。合并部分单元格，并渲染数据。
        List<RowRenderData> labors = detailData.getLabors();
        if (null != labors) {
            table.removeRow(laborsStartRow);
            // 循环插入行
            for (int i = 0; i < labors.size(); i++) {
                XWPFTableRow insertNewTableRow = table.insertNewTableRow(laborsStartRow);
                for (int j = 0; j < 7; j++) insertNewTableRow.createCell();

                // 合并单元格
                TableTools.mergeCellsHorizonal(table, laborsStartRow, 0, 3);
                TableRenderPolicy.Helper.renderRow(table.getRow(laborsStartRow), labors.get(i));
            }
        }

        // 货品数据：如果 goods 不为空，移除表格中指定行，然后逐行插入新的行，并创建单元格。渲染数据。
        List<RowRenderData> goods = detailData.getGoods();
        if (null != goods) {
            table.removeRow(goodsStartRow);
            for (int i = 0; i < goods.size(); i++) {
                XWPFTableRow insertNewTableRow = table.insertNewTableRow(goodsStartRow);
                for (int j = 0; j < 7; j++) insertNewTableRow.createCell();
                TableRenderPolicy.Helper.renderRow(table.getRow(goodsStartRow), goods.get(i));
            }
        }
    }

}

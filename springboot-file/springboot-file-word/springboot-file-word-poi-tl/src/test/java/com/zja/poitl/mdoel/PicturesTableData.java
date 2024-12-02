package com.zja.poitl.mdoel;

import com.deepoove.poi.data.RowRenderData;
import com.deepoove.poi.expression.Name;
import lombok.Data;

import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-11-20 11:15
 */
@Data
public class PicturesTableData {
    @Name("detail_table1")
    private List<RowRenderData> table1;
    @Name("detail_table2")
    private List<RowRenderData> table2;
}

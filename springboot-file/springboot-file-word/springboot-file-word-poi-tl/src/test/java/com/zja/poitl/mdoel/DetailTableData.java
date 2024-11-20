package com.zja.poitl.mdoel;

import com.deepoove.poi.data.RowRenderData;
import lombok.Data;

import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-11-19 20:28
 */
@Data
public class DetailTableData {
    // table1
    private List<RowRenderData> table1;

    // table2
    private List<RowRenderData> table2;
}

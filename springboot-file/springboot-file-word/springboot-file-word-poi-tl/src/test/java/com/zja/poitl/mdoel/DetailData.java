package com.zja.poitl.mdoel;

import com.deepoove.poi.data.RowRenderData;
import lombok.Data;

import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-11-18 10:48
 */
@Data
public class DetailData {
    // 货品数据
    private List<RowRenderData> goods;

    // 人工费数据
    private List<RowRenderData> labors;
}


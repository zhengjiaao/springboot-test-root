package com.zja.poitl.mdoel;

import com.deepoove.poi.data.TableRenderData;
import com.deepoove.poi.expression.Name;
import lombok.Data;

/**
 * @Author: zhengja
 * @Date: 2024-11-18 10:47
 */
@Data
public class PaymentData {
    private TableRenderData order;
    private String NO;
    private String ID;
    private String taitou;
    private String consignee;
    @Name("detail_table")
    private DetailData detailTable;
    private String subtotal;
    private String tax;
    private String transform;
    private String other;
    private String unpay;
    private String total;
}

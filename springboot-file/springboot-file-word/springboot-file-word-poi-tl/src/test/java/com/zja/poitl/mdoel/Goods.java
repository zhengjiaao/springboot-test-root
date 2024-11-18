package com.zja.poitl.mdoel;

import com.deepoove.poi.data.PictureRenderData;
import lombok.Data;

/**
 * @Author: zhengja
 * @Date: 2024-11-18 10:23
 */
@Data
public class Goods {

    private int count;
    private String name;
    private String desc;
    private int discount;
    private int tax;
    private int price;
    private int totalPrice;

    private PictureRenderData picture;
}

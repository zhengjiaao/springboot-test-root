package com.zja.poitl.mdoel;

import com.deepoove.poi.data.PictureRenderData;
import lombok.Data;

/**
 * @Author: zhengja
 * @Date: 2024-11-20 15:59
 */
@Data
public class ImageData {
    // 显示名称
    private String name;
    // 图片
    private PictureRenderData picture;
}

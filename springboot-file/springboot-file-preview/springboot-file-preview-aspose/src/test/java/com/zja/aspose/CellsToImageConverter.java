/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-17 10:05
 * @Since:
 */
package com.zja.aspose;

import com.aspose.cells.*;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

/**
 * ExcelToImage
 *
 * @author: zhengja
 * @since: 2023/11/17 10:05
 */
public class CellsToImageConverter {

    @Test
    public void cellsToImage() throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
        License license = new License();
        license.setLicense(is);

        // 加载 Excel 文件
        Workbook workbook = new Workbook("D:\\temp\\excel\\input.xlsx");

        // 获取第一个工作表
        Worksheet worksheet = workbook.getWorksheets().get(0);

        // 创建图像转换器
        ImageOrPrintOptions options = new ImageOrPrintOptions();

        // 设置转换器参数
        options.setHorizontalResolution(300); // 设置水平分辨率为 300 dpi
        options.setVerticalResolution(300); // 设置垂直分辨率为 300 dpi

        // 渲染并保存图像
        SheetRender sheetRender = new SheetRender(worksheet, options);
        for (int pageIndex = 0; pageIndex < sheetRender.getPageCount(); pageIndex++) {
            String outputFileName = String.format("D:\\temp\\excel\\to\\output-%d.png", pageIndex);

            // 渲染当前页并保存为图像
            sheetRender.toImage(pageIndex, outputFileName);
        }

        System.out.println("Excel 文件已成功转换为图像。");
    }
}

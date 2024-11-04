package com.zja.cad.aspose;

import com.aspose.cad.Image;
import com.aspose.cad.fileformats.cad.CadImage;
import com.aspose.cad.imageoptions.SvgOptions;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Paths;

/**
 * @Author: zhengja
 * @Date: 2024-10-31 10:51
 */
public class CadToSvgTest {

    String path = Paths.get("D:\\temp\\cad", "总平面规划图终核.dwg").toString();

    @Test
    public void testConvertCadToSvg() {
        // 读取 CAD 文件
        String outputFilePath;
        try (CadImage cadImage = (CadImage) Image.load(path)) {

            // 转换为 SVG 格式
            outputFilePath = "target/to_svg.svg";
            cadImage.save(outputFilePath, new SvgOptions());
        }

        // 验证输出文件存在
        assert new File(outputFilePath).exists();
    }
}

package com.zja.cad.aspose.DWGDrawings;

import com.aspose.cad.Image;
import com.aspose.cad.fileformats.cad.CadImage;
import org.junit.jupiter.api.Test;

import java.nio.file.Paths;

/**
 * @Author: zhengja
 * @Date: 2024-10-31 15:18
 */
public class DWGToDWF {

    // String cad_path = Paths.get("D:\\temp\\cad", "总平面规划图终核.dxf").toString();
    String cad_path = Paths.get("D:\\temp\\cad\\DWGDrawings", "Line.dwg").toString();

    @Test
    public void test_1() {
        try (CadImage cadImage = (CadImage) Image.load(cad_path)) {
            cadImage.save("target/Line.dwf");
        }
    }
}

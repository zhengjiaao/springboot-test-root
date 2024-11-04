package com.zja.cad.aspose;

import com.aspose.cad.Image;
import com.aspose.cad.fileformats.cad.CadImage;
import com.aspose.cad.fileformats.cad.cadobjects.CadEntityBase;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

/**
 * @Author: zhengja
 * @Date: 2024-10-31 13:42
 */
public class CadImageTest {

    // String cad_path = Paths.get("D:\\temp\\cad", "总平面规划图终核.dxf").toString();
    String cad_path = Paths.get("D:\\temp\\cad", "总平面规划图终核.dwg").toString();

    // 读取 CAD 文件
    @Test
    public void testReadCadFile() throws IOException {
        // 读取 CAD 文件
        try (CadImage cadImage = (CadImage) Image.load(cad_path)) {

            // 检查图像是否加载成功
            assert cadImage != null;
        }
    }

    // 读取并验证 CAD 文件中的对象数量
    @Test
    public void testObjectCountInCadFile() throws IOException {
        // 读取 CAD 文件
        try (CadImage cadImage = (CadImage) Image.load(cad_path)) {
            // 获取所有实体
            List<CadEntityBase> entities = cadImage.getEntities();

            int size = entities.size();
            System.out.println("实体数量：" + size);
        }
    }
}

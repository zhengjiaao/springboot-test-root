/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-17 9:30
 * @Since:
 */
package com.zja.aspose;

import com.aspose.slides.License;
import com.aspose.slides.Presentation;
import com.aspose.slides.SaveFormat;
import org.junit.jupiter.api.Test;

import java.io.InputStream;

/**
 * @author: zhengja
 * @since: 2023/11/17 9:30
 */
public class SlidesToImageConverter {

    //todo 暂时，不支持转为 png格式图像
    @Test
    public void slidesToImageTest() throws Exception {
        InputStream is = Thread.currentThread().getContextClassLoader().getResourceAsStream("license.xml");
        License license = new License();
        license.setLicense(is);

        // 加载 PowerPoint 演示文稿
        Presentation presentation = new Presentation("D:\\temp\\ppt\\input.pptx");
        presentation.save("D:\\temp\\ppt\\to\\output.gif", SaveFormat.Gif);

        System.out.println("演示文稿已成功转换为图像。");
    }
}

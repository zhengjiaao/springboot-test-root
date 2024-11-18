package com.zja.poitl.util;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.util.PoitlIOUtils;
import org.apache.poi.xwpf.usermodel.XWPFDocument;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

/**
 * @Author: zhengja
 * @Date: 2024-11-18 11:41
 */
public class XWPFTestSupport {

    public static XWPFTemplate readNewTemplate(XWPFTemplate template) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        template.write(byteArrayOutputStream);
        template.close();
        return XWPFTemplate.compile(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()),
                template.getConfig());
    }

    public static XWPFDocument readNewDocument(XWPFTemplate template) throws IOException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        template.write(byteArrayOutputStream);
        template.close();
        return new XWPFDocument(new ByteArrayInputStream(byteArrayOutputStream.toByteArray()));
    }

    public static InputStream readInputStream(XWPFDocument doc) throws IOException {
        return PoitlIOUtils.docToInputStream(doc);
    }
}

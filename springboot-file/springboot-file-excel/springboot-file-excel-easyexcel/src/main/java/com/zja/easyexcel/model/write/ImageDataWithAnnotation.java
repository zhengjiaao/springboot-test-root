package com.zja.easyexcel.model.write;

import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.converters.string.StringImageConverter;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

import java.io.File;
import java.io.InputStream;
import java.net.URL;

/**
 * 图片导出类
 *
 * @Author: zhengja
 * @Date: 2024-12-05 14:31
 */
@Getter
@Setter
@EqualsAndHashCode
@ContentRowHeight(100)
@ColumnWidth(100 / 8)
public class ImageDataWithAnnotation {
    private File file;
    private InputStream inputStream;
    /**
     * 如果string类型 必须指定转换器，string默认转换成string
     */
    @ExcelProperty(converter = StringImageConverter.class)
    private String string;
    private byte[] byteArray;
    /**
     * 根据url导出
     *
     * @since 2.1.1
     */
    private URL url;
}
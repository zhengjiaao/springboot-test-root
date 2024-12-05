package com.zja.easyexcel.model.read;

import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.converters.ReadConverterContext;
import com.alibaba.excel.converters.WriteConverterContext;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.data.WriteCellData;

/**
 * String and string converter
 *
 * @Author: zhengja
 * @Date: 2024-12-05 14:04
 */
public class CustomStringStringConverter implements Converter<String> {
    @Override
    public Class<?> supportJavaTypeKey() {
        return String.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

    /**
     * 这里读的时候会调用
     *
     * @param context
     * @return
     */
    @Override
    public String convertToJavaData(ReadConverterContext<?> context) {
        return "自定义：" + context.getReadCellData().getStringValue();
    }

    /**
     * 这里是写的时候会调用 不用管
     *
     * @return
     */
    @Override
    public WriteCellData<?> convertToExcelData(WriteConverterContext<String> context) {
        return new WriteCellData<>(context.getValue());
    }

}
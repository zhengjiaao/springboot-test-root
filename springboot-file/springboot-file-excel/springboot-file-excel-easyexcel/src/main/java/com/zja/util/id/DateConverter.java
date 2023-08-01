/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-04-25 14:26
 * @Since:
 */
package com.zja.util.id;

import cn.hutool.core.date.DateUtil;
import com.alibaba.excel.converters.Converter;
import com.alibaba.excel.enums.CellDataTypeEnum;
import com.alibaba.excel.metadata.GlobalConfiguration;
import com.alibaba.excel.metadata.data.WriteCellData;
import com.alibaba.excel.metadata.property.ExcelContentProperty;

import java.util.Date;

public class DateConverter implements Converter<Date> {

    @Override
    public Class<Date> supportJavaTypeKey() {
        return Date.class;
    }

    @Override
    public CellDataTypeEnum supportExcelTypeKey() {
        return CellDataTypeEnum.STRING;
    }

//    @Override
//    public Date convertToJavaData(ReadCellData cellData, ExcelContentProperty contentProperty,
//                                           GlobalConfiguration globalConfiguration) {
//        return DateUtil.parse(cellData.getStringValue());
//    }

    @Override
    public WriteCellData<String> convertToExcelData(Date value, ExcelContentProperty contentProperty,
                                                    GlobalConfiguration globalConfiguration) {
        return new WriteCellData(DateUtil.format(value, "yyyy/MM/dd"));
    }
}

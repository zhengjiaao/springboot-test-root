package com.zja.easyexcel.read;

import com.alibaba.excel.context.AnalysisContext;
import com.alibaba.excel.metadata.CellExtra;
import com.alibaba.excel.read.listener.ReadListener;
import com.alibaba.fastjson.JSON;
import com.zja.easyexcel.model.read.DemoExtraData;
import lombok.extern.slf4j.Slf4j;

/**
 * 读取单元格的批注
 *
 * @Author: zhengja
 * @Date: 2024-12-05 13:53
 */
@Slf4j
public class DemoExtraListener implements ReadListener<DemoExtraData> {

    @Override
    public void invoke(DemoExtraData data, AnalysisContext context) {
    }

    @Override
    public void doAfterAllAnalysed(AnalysisContext context) {
    }

    @Override
    public void extra(CellExtra extra, AnalysisContext context) {
        log.info("读取到了一条额外信息:{}", JSON.toJSONString(extra));
        switch (extra.getType()) {
            case COMMENT:
                log.info("额外信息是批注,在rowIndex:{},columnIndex;{},内容是:{}", extra.getRowIndex(),
                        extra.getColumnIndex(),
                        extra.getText());
                break;
            case HYPERLINK:
                if ("Sheet1!A1".equals(extra.getText())) {
                    log.info("额外信息是超链接,在rowIndex:{},columnIndex;{},内容是:{}", extra.getRowIndex(),
                            extra.getColumnIndex(), extra.getText());
                } else if ("Sheet2!A1".equals(extra.getText())) {
                    log.info(
                            "额外信息是超链接,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{},"
                                    + "内容是:{}",
                            extra.getFirstRowIndex(), extra.getFirstColumnIndex(), extra.getLastRowIndex(),
                            extra.getLastColumnIndex(), extra.getText());
                } else {
                   throw new RuntimeException("Unknown hyperlink!");
                }
                break;
            case MERGE:
                log.info(
                        "额外信息是合并单元格,而且覆盖了一个区间,在firstRowIndex:{},firstColumnIndex;{},lastRowIndex:{},lastColumnIndex:{}",
                        extra.getFirstRowIndex(), extra.getFirstColumnIndex(), extra.getLastRowIndex(),
                        extra.getLastColumnIndex());
                break;
            default:
        }
    }
}
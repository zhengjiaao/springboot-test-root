package com.zja.poitl.table.policy;

import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.deepoove.poi.template.ElementTemplate;
import com.deepoove.poi.template.run.RunTemplate;
import org.apache.poi.xwpf.usermodel.*;
import org.openxmlformats.schemas.wordprocessingml.x2006.main.STMerge;

/**
 * @Author: zhengja
 * @Date: 2024-11-18 19:39
 */
public class CustomLoopRowTableRenderPolicy extends LoopRowTableRenderPolicy {

    public CustomLoopRowTableRenderPolicy() {
        super();
    }

    public CustomLoopRowTableRenderPolicy(boolean onSameLine) {
        super(onSameLine);
    }

    public CustomLoopRowTableRenderPolicy(String prefix, String suffix) {
        super(prefix, suffix);
    }

    public CustomLoopRowTableRenderPolicy(String prefix, String suffix, boolean onSameLine) {
        super(prefix, suffix, onSameLine);
    }

    @Override
    public void render(ElementTemplate eleTemplate, Object data, XWPFTemplate template) {
        super.render(eleTemplate, data, template);
        XWPFTable table = this.getTableFromElement(eleTemplate);
        this.mergeCells(table);
    }

    private XWPFTable getTableFromElement(ElementTemplate eleTemplate) {
        RunTemplate runTemplate = (RunTemplate) eleTemplate;
        XWPFRun run = runTemplate.getRun();
        XWPFTableCell tagCell = (XWPFTableCell) ((XWPFParagraph) run.getParent()).getBody();
        return tagCell.getTableRow().getTable();
    }

    private void mergeCells(XWPFTable table) {
        int rowCount = table.getRows().size();
        for (int i = 0; i < rowCount - 1; i++) {
            XWPFTableRow row = table.getRow(i);
            XWPFTableCell cell = row.getCell(0); // 假设要合并第一列
            if (i == 0) {
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.RESTART);
            } else {
                cell.getCTTc().addNewTcPr().addNewVMerge().setVal(STMerge.CONTINUE);
            }
        }
    }
}
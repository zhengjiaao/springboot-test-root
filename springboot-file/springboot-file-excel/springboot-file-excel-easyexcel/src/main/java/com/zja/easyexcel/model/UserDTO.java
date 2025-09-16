package com.zja.easyexcel.model;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.*;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * @Author: zhengja
 * @Date: 2025-09-15 20:40
 */
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@HeadRowHeight(20)  // 设置表头行高为30像素
@ContentRowHeight(16)  // 设置内容行高为25像素
@HeadFontStyle(fontHeightInPoints = 12, bold = BooleanEnum.TRUE) // 设置表头字体大小和样式
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)  // 表头居中
@ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER, verticalAlignment = VerticalAlignmentEnum.CENTER)  // 内容居中
public class UserDTO implements Serializable {

    @ExcelProperty(value = "用户ID", index = 0)  // 第一列
    @ColumnWidth(value = 16) // 设置列宽为20个字符宽度
    private String userId;

    @ExcelProperty(value = "用户名称", index = 2)  // 第三列，调列顺序
    @ColumnWidth(value = 12)
    private String userName;

    @ExcelProperty(value = "用户手机", index = 1)  // 第二列，调列顺序
    @ColumnWidth(value = 16)
    @ContentStyle(horizontalAlignment = HorizontalAlignmentEnum.RIGHT, verticalAlignment = VerticalAlignmentEnum.CENTER)  // 内容靠右, 手机列内容靠右
    private String userPhone;

    @ExcelIgnore // 忽略此字段
    @ExcelProperty("用户生日")
    private String userBirthday;

    @ExcelIgnore
    // @ExcelProperty("用户地址")
    private String userAddress;

    @ExcelIgnore
    // @ExcelProperty("用户邮箱")
    private String userEmail;

    @ExcelIgnore
    // @ExcelProperty("用户性别")
    private String userSex;

    @ExcelIgnore
    // @ExcelProperty("用户状态")
    private String userStatus;
}

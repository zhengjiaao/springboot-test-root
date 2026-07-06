/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-04-07 11:08
 * @Since:
 */
package com.zja.easyexcel.db.planner;

import com.alibaba.excel.annotation.ExcelIgnore;
import com.alibaba.excel.annotation.ExcelProperty;
import com.alibaba.excel.annotation.write.style.ColumnWidth;
import com.alibaba.excel.annotation.write.style.ContentFontStyle;
import com.alibaba.excel.annotation.write.style.ContentRowHeight;
import com.alibaba.excel.annotation.write.style.ContentStyle;
import com.alibaba.excel.annotation.write.style.HeadFontStyle;
import com.alibaba.excel.annotation.write.style.HeadRowHeight;
import com.alibaba.excel.annotation.write.style.HeadStyle;
import com.alibaba.excel.enums.BooleanEnum;
import com.alibaba.excel.enums.poi.FillPatternTypeEnum;
import com.alibaba.excel.enums.poi.HorizontalAlignmentEnum;
import com.alibaba.excel.enums.poi.VerticalAlignmentEnum;
import lombok.Data;
import org.hibernate.annotations.GenericGenerator;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Table;
import java.io.Serializable;

/**
 * 规划师信息
 */
@Data
@Entity
@Table(name = "HYS_PLANNER_TEST")
// ===== 全局样式设置 =====
// 表头行高
@HeadRowHeight(25)
// 内容行高
@ContentRowHeight(20)
// 表头字体样式：加粗、字体名称、字体大小、字体颜色（白色）
@HeadFontStyle(fontName = "微软雅黑", bold = BooleanEnum.TRUE, color = 8)
// 表头样式：居中对齐、背景色（深蓝色41）
@HeadStyle(horizontalAlignment = HorizontalAlignmentEnum.CENTER,
        verticalAlignment = VerticalAlignmentEnum.CENTER,
        fillPatternType = FillPatternTypeEnum.SOLID_FOREGROUND,
        fillForegroundColor = 41)
// 内容字体样式：字体名称、字体大小
@ContentFontStyle(fontName = "微软雅黑", color = 8)
// 内容样式：垂直居中
@ContentStyle(verticalAlignment = VerticalAlignmentEnum.CENTER,
        wrapped = BooleanEnum.TRUE)
public class PlannerEntity implements Serializable {

    @Id
    @ExcelIgnore
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;

//    private Long id = IdUtil.snowFlakeId();

    @GenericGenerator(name = "id", strategy = "com.zja.easyexcel.db.planner.SnowIdGenerator")
    @GeneratedValue(generator = "id")
    private Long id;


    @ExcelProperty(value = "单位名称", index = 0)
    @ColumnWidth(30)
    private String companyName;

    @ExcelProperty(value = "姓名", index = 1)
    @ColumnWidth(15)
    private String name;

    @ExcelProperty(value = "证件号", index = 2)
    @ColumnWidth(25)
    private String licenseNumber;

    @ExcelProperty(value = "有效期", index = 3)
    @ColumnWidth(18)
    private String validityPeriod;

    @ExcelProperty(value = "发证日期", index = 4)
    @ColumnWidth(18)
    private String dateOfIssue;

    @ExcelProperty(value = "证书编号", index = 5)
    @ColumnWidth(22)
    private String certificateNumber;
}

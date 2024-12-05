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
public class PlannerEntity implements Serializable {

    @Id
    @ExcelIgnore
//    @GeneratedValue(strategy = GenerationType.AUTO)
//    private Long id;

//    private Long id = IdUtil.snowFlakeId();

    @GenericGenerator(name = "id",strategy = "com.zja.planner.SnowIdGenerator")
    @GeneratedValue(generator = "id")
    private Long id;


    @ExcelProperty("单位名称")
    private String companyName;

    @ExcelProperty("姓名")
    private String name;

    @ExcelProperty("证件号")
    private String licenseNumber;

    @ExcelProperty(value = "有效期")
    private String validityPeriod;

    @ExcelProperty("发证日期")
    private String dateOfIssue;

    @ExcelProperty("证书编号")
    private String certificateNumber;
}

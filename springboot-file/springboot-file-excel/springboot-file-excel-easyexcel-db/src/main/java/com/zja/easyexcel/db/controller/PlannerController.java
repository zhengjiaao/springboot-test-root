/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-05-19 0:38
 * @Since:
 */
package com.zja.easyexcel.db.controller;

import com.zja.easyexcel.db.planner.PlannerEntity;
import com.zja.easyexcel.db.service.PlannerEntityReadService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.List;

@Api(tags = "规划师数据管理接口")
@RestController
@RequestMapping("/planner")
public class PlannerController {

    @Autowired
    PlannerEntityReadService plannerEntityReadService;

    // ==================== 查询接口 ====================

    /**
     * 分页查询
     * http://127.0.0.1:8080/planner/page?pageNum=1&pageSize=10
     */
    @GetMapping("/page")
    @ApiOperation(value = "分页查询规划师数据", notes = "根据页码与每页条数分页返回规划师数据")
    public Page<PlannerEntity> page(
            @ApiParam(value = "页码（从 1 开始）", required = true, example = "1") @RequestParam Integer pageNum,
            @ApiParam(value = "每页条数", required = true, example = "10") @RequestParam Integer pageSize) {
        return plannerEntityReadService.findAll(pageNum, pageSize);
    }

    /**
     * 查询全部（不分页）
     * http://127.0.0.1:8080/planner/list
     */
    @GetMapping("/list")
    @ApiOperation(value = "查询全部规划师", notes = "返回全部规划师记录（不分页）")
    public List<PlannerEntity> list() {
        return plannerEntityReadService.findAllList();
    }

    /**
     * 根据 id 查询
     * http://127.0.0.1:8080/planner/1
     */
    @GetMapping("/{id}")
    @ApiOperation(value = "根据 id 查询规划师", notes = "根据主键 id 查询单条规划师记录")
    public PlannerEntity getById(
            @ApiParam(value = "规划师 id", required = true, example = "1") @PathVariable Long id) {
        return plannerEntityReadService.findById(id);
    }

    // ==================== 写入接口 ====================

    /**
     * 新增
     * POST /planner
     */
    @PostMapping
    @ApiOperation(value = "新增规划师", notes = "新增一条规划师记录并返回保存后的实体")
    public PlannerEntity create(@RequestBody PlannerEntity entity) {
        return plannerEntityReadService.save(entity);
    }

    /**
     * 更新
     * PUT /planner/1
     */
    @PutMapping("/{id}")
    @ApiOperation(value = "更新规划师", notes = "根据 id 更新规划师记录")
    public PlannerEntity update(
            @ApiParam(value = "规划师 id", required = true, example = "1") @PathVariable Long id,
            @RequestBody PlannerEntity entity) {
        return plannerEntityReadService.update(id, entity);
    }

    /**
     * 根据 id 删除
     * DELETE /planner/1
     */
    @DeleteMapping("/{id}")
    @ApiOperation(value = "根据 id 删除规划师", notes = "根据主键 id 删除单条规划师记录")
    public void deleteById(
            @ApiParam(value = "规划师 id", required = true, example = "1") @PathVariable Long id) {
        plannerEntityReadService.deleteById(id);
    }

    /**
     * 删除全部
     * DELETE /planner/all
     */
    @DeleteMapping("/all")
    @ApiOperation(value = "删除全部规划师", notes = "清空全部规划师记录")
    public void deleteAll() {
        plannerEntityReadService.deleteAll();
    }

    // ==================== 导入导出接口 ====================

    /**
     * 上传 Excel 文件导入规划师数据
     * POST /planner/import，Content-Type: multipart/form-data，参数 file=规划师信息.xlsx
     */
    @PostMapping("/import")
    @ApiOperation(value = "上传文件导入规划师数据", notes = "通过上传 Excel 文件导入规划师数据并入库")
    public String importExcel(
            @ApiParam(value = "Excel 文件（.xlsx）", required = true)
            @RequestPart("file") MultipartFile file) {
        if (file.isEmpty()) {
            return "上传文件为空";
        }
        try {
            plannerEntityReadService.importAndSave(file.getInputStream());
            return "导入成功，文件名：" + file.getOriginalFilename();
        } catch (Exception e) {
            return "导入失败：" + e.getMessage();
        }
    }

    /**
     * 筛选分页导出规划师数据
     * 示例：
     *   全量导出：http://127.0.0.1:8080/planner/export?exportAll=true
     *   分页导出：http://127.0.0.1:8080/planner/export?pageNum=0&pageSize=500
     *   筛选导出：http://127.0.0.1:8080/planner/export?name=张&companyName=上海
     */
    @GetMapping("/export")
    @ApiOperation(value = "导出规划师 Excel", notes = "支持按姓名/单位名称模糊筛选，支持全量流式导出与分页导出")
    public void exportExcel(
            HttpServletResponse response,
            @ApiParam(value = "姓名（模糊匹配）", example = "张") @RequestParam(required = false) String name,
            @ApiParam(value = "单位名称（模糊匹配）", example = "上海") @RequestParam(required = false) String companyName,
            @ApiParam(value = "页码（从 0 开始，仅 exportAll=false 时生效）", example = "0") @RequestParam(required = false, defaultValue = "0") Integer pageNum,
            @ApiParam(value = "每页条数，默认 2000", example = "2000") @RequestParam(required = false, defaultValue = "2000") Integer pageSize,
            @ApiParam(value = "true=全量导出，false=分页导出", example = "false") @RequestParam(required = false, defaultValue = "false") Boolean exportAll,
            @ApiParam(value = "自定义文件名", example = "规划师信息") @RequestParam(required = false) String fileName) throws IOException {
        plannerEntityReadService.exportExcel(response, name, companyName, pageNum, pageSize, exportAll, fileName);
    }

}

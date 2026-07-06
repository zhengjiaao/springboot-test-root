/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2022-04-22 15:58
 * @Since:
 */
package com.zja.easyexcel.db.service;

import com.alibaba.excel.EasyExcel;
import com.zja.easyexcel.db.planner.PlannerDao;
import com.zja.easyexcel.db.planner.PlannerEntity;
import org.springframework.util.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Component;

import javax.persistence.criteria.Predicate;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.InputStream;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Component
public class PlannerEntityReadService {

    @Autowired
    private PlannerDao plannerDao;

    @Autowired
    private PlannerEntityBatch plannerEntityBatch;

    /**
     * excel导入数据并完成数据存储（本地文件路径）
     */
//    @Async("taskAsyncExecutor")
    public synchronized void importAndSave(String excelPath) {
        //每次导入，都是全量的数据
        //plannerDao.deleteAllInBatch();

        //sheet()默认读取第一个sheet ,  doRead()异步无模型读(默认表头占一行,从第2行开始读)
        EasyExcel.read(excelPath, PlannerEntity.class, new PlannerEntityReadListener(plannerEntityBatch)).sheet().doRead();
    }

    /**
     * 通过上传文件流导入数据并完成数据存储
     *
     * @param inputStream 上传文件输入流
     */
    public synchronized void importAndSave(InputStream inputStream) {
        EasyExcel.read(inputStream, PlannerEntity.class, new PlannerEntityReadListener(plannerEntityBatch)).sheet().doRead();
    }

    /**
     * 分页查询
     */
    public Page<PlannerEntity> findAll(Integer pageNum, Integer pageSize) {
        Pageable pageable = PageRequest.of(pageNum - 1, pageSize);
        return plannerDao.findAll(pageable);
    }

    /**
     * 根据 id 查询
     */
    public PlannerEntity findById(Long id) {
        Optional<PlannerEntity> opt = plannerDao.findById(id);
        return opt.orElseThrow(() -> new RuntimeException("规划师不存在，id=" + id));
    }

    /**
     * 查询全部
     */
    public List<PlannerEntity> findAllList() {
        return plannerDao.findAll();
    }

    /**
     * 新增
     */
    public PlannerEntity save(PlannerEntity entity) {
        return plannerDao.save(entity);
    }

    /**
     * 更新
     */
    public PlannerEntity update(Long id, PlannerEntity entity) {
        PlannerEntity existing = findById(id);
        existing.setCompanyName(entity.getCompanyName());
        existing.setName(entity.getName());
        existing.setLicenseNumber(entity.getLicenseNumber());
        existing.setValidityPeriod(entity.getValidityPeriod());
        existing.setDateOfIssue(entity.getDateOfIssue());
        existing.setCertificateNumber(entity.getCertificateNumber());
        return plannerDao.save(existing);
    }

    /**
     * 根据 id 删除
     */
    public void deleteById(Long id) {
        if (!plannerDao.existsById(id)) {
            throw new RuntimeException("规划师不存在，id=" + id);
        }
        plannerDao.deleteById(id);
    }

    /**
     * 删除全部
     */
    public void deleteAll() {
        plannerDao.deleteAllInBatch();
    }

    /**
     * 筛选分页导出规划师数据到 Excel
     *
     * @param response    HttpServletResponse
     * @param name        姓名（模糊匹配，可为空）
     * @param companyName 单位名称（模糊匹配，可为空）
     * @param pageNum     页码（从 0 开始）
     * @param pageSize    每页条数，默认 2000
     * @param exportAll   true=全量导出（忽略分页），false=仅导出指定页
     * @param fileName    自定义文件名
     */
    public void exportExcel(HttpServletResponse response,
                            String name, String companyName,
                            Integer pageNum, Integer pageSize,
                            Boolean exportAll, String fileName) throws IOException {
        // 设置响应头
        String exportFileName = StringUtils.hasText(fileName) ? fileName : "规划师信息";
        response.setContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");
        response.setCharacterEncoding("utf-8");
        String encodedName = URLEncoder.encode(exportFileName + ".xlsx", StandardCharsets.UTF_8.name()).replaceAll("\\+", "%20");
        response.setHeader("Content-disposition", "attachment;filename*=utf-8''" + encodedName);

        if (Boolean.TRUE.equals(exportAll)) {
            // 全量流式导出：分批查询并写入，避免 OOM
            int batch = (pageSize != null && pageSize > 0) ? pageSize : 2000;
            int page = 0;
            com.alibaba.excel.ExcelWriter excelWriter = EasyExcel.write(response.getOutputStream(), PlannerEntity.class).build();
            com.alibaba.excel.write.metadata.WriteSheet writeSheet = EasyExcel.writerSheet(exportFileName).build();
            while (true) {
                Pageable pageable = PageRequest.of(page, batch);
                Page<PlannerEntity> pageData = plannerDao.findAll(buildSpecification(name, companyName), pageable);
                List<PlannerEntity> content = pageData.getContent();
                if (content.isEmpty()) {
                    break;
                }
                excelWriter.write(content, writeSheet);
                if (!pageData.hasNext()) {
                    break;
                }
                page++;
            }
            excelWriter.finish();
        } else {
            // 分页导出
            int pn = (pageNum != null) ? pageNum : 0;
            int ps = (pageSize != null && pageSize > 0) ? pageSize : 2000;
            Pageable pageable = PageRequest.of(pn, ps);
            Page<PlannerEntity> entityPage = plannerDao.findAll(buildSpecification(name, companyName), pageable);
            EasyExcel.write(response.getOutputStream(), PlannerEntity.class)
                    .sheet(exportFileName)
                    .doWrite(entityPage.getContent());
        }
    }

    /**
     * 构建动态查询条件
     */
    private org.springframework.data.jpa.domain.Specification<PlannerEntity> buildSpecification(String name, String companyName) {
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            if (StringUtils.hasText(name)) {
                predicates.add(cb.like(root.get("name"), "%" + name + "%"));
            }
            if (StringUtils.hasText(companyName)) {
                predicates.add(cb.like(root.get("companyName"), "%" + companyName + "%"));
            }
            return predicates.isEmpty() ? cb.conjunction() : cb.and(predicates.toArray(new Predicate[0]));
        };
    }

}

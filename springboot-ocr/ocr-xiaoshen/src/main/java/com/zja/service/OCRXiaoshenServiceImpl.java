/**
 * @Company: 上海数慧系统技术有限公司
 * @Department: 数据中心
 * @Author: 郑家骜[ào]
 * @Email: zhengja@dist.com.cn
 * @Date: 2023-11-08 10:35
 * @Since:
 */
package com.zja.service;

import com.zja.define.ImageEnum;
import com.zja.util.OCRApacheTikaUtil;
import com.zja.util.OCRTesseractUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import org.springframework.validation.annotation.Validated;

import java.io.File;

/**
 * OCR 服务
 *
 * @author: zhengja
 * @since: 2023/11/08 10:35
 */
@Slf4j
@Validated
@Service
public class OCRXiaoshenServiceImpl implements OCRXiaoshenService {

    @Override
    public String autoExtractContent(String inputFilePath) {
        File inputFile = new File(inputFilePath);
        if (!inputFile.exists()) {
            throw new RuntimeException("文件不存在：" + inputFilePath);
        }

        String name = inputFile.getName();

        String result = "";

        String fileExtension = FilenameUtils.getExtension(name).toLowerCase();
        if (isImage(fileExtension)) {
            result = OCRTesseractUtil.ocrImage(inputFilePath);
        } /*else if (isPDF(fileExtension)) {
            result = OCRmyPDFUtil.ocrPdf(inputFilePath);
        } */else {
            result = OCRApacheTikaUtil.autoExtractedContent(inputFilePath);
        }

        return result;
    }

    private boolean isImage(String fileExtension) {
        ImageEnum imageEnum = ImageEnum.get(fileExtension.toLowerCase());

        if (ObjectUtils.isEmpty(imageEnum)) {
            return Boolean.FALSE;
        }

        return Boolean.TRUE;
    }

    private boolean isPDF(String fileExtension) {
        return "pdf".equalsIgnoreCase(fileExtension) ? Boolean.TRUE : Boolean.FALSE;
    }

/*
    @Autowired
    OCRXiaoshenRepo repo;

    @Autowired
    OCRXiaoshenMapper mapper;


    @Override
    public OCRXiaoshenDTO findById(String id) {
        OCRXiaoshen entity = repo.findById(id).orElseThrow(() -> new ArgumentNotValidException("传入的 id 有误！"));
        return mapper.map(entity);
    }

    @Override
    public PageData<OCRXiaoshenDTO> pageList(OCRXiaoshenPageSearchRequest request) {
        int page = request.getPage();
        int size = request.getSize();
        Sort sort = Sort.by(Sort.Direction.DESC, "createTime");

        //查询条件
        Specification<OCRXiaoshen> spec = buildQuery(request);
        //分页查询
        Page<OCRXiaoshen> sourcePage = repo.findAll(spec, PageRequest.of(page, size, sort));

        return PageData.of(mapper.mapList(sourcePage.getContent()), page, size, sourcePage.getTotalElements());
    }

    private Specification<OCRXiaoshen> buildQuery(OCRXiaoshenPageSearchRequest request) {
        // 构建查询条件
        return (root, query, cb) -> {
            List<Predicate> predicates = new ArrayList<>();
            // 关键词
            if (!StringUtils.isEmpty(request.getName())) {
                predicates.add(cb.equal(root.get("name"), request.getName()));
            }
            // 将条件连接在一起
            return query.where(predicates.toArray(new Predicate[0])).getRestriction();
        };
    }

    @Override
    public OCRXiaoshenDTO add(OCRXiaoshenRequest request) {
        OCRXiaoshen entity = mapper.map(request);
        repo.save(entity);
        return mapper.map(entity);
    }

    @Override
    public OCRXiaoshenDTO update(String id, OCRXiaoshenUpdateRequest request) {
        OCRXiaoshen entity = repo.findById(id).orElseThrow(() -> new ArgumentNotValidException("传入的 id 有误！"));
        //更新属性
        BeanUtils.copyProperties(request, entity);
        entity = repo.save(entity);
        return mapper.map(entity);
    }

    @Override
    public void deleteById(String id) {
        if (!repo.findById(id).isPresent()) {
            return;
        }
        repo.deleteById(id);
    }
*/

}
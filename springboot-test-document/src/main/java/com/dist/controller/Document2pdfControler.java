package com.dist.controller;

import com.dist.util.office.OfficeToPdfUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhengja@dist.com.cn
 * @data 2019/7/16 10:41
 */
@Api(tags = {"DocumentControler"},description = "文档转换操作")
@RestController
@RequestMapping(value = "/document")
public class Document2pdfControler {

    private static org.slf4j.Logger log = LoggerFactory.getLogger(Document2pdfControler.class);

    @ApiOperation(value = "excel2PDF",httpMethod = "GET")
    @RequestMapping(value = "/excel2pdf",method = RequestMethod.GET)
    public Object excel2PDF(){
        boolean excel2PDF = OfficeToPdfUtil.convert2PDF("D:\\doc\\台账分类标准.xlsx", "D:\\doc\\台账分类标准.pdf");
        log.info("转换是否成功="+excel2PDF);
        return excel2PDF;
    }

    @ApiOperation(value = "word2PDF",httpMethod = "GET")
    @RequestMapping(value = "/word2pdf",method = RequestMethod.GET)
    public Object word2PDF(){
        boolean word2PDF = OfficeToPdfUtil.convert2PDF("D:\\doc\\qwe.doc", "D:\\doc\\qwea.pdf");
        log.info("转换是否成功="+word2PDF);
        return word2PDF;
    }

    @ApiOperation(value = "ppt2PDF",httpMethod = "GET")
    @RequestMapping(value = "/ppt2pdf",method = RequestMethod.GET)
    public Object ppt2PDF(){
        boolean ppt2PDF = OfficeToPdfUtil.convert2PDF("D:\\doc\\2019-郑家骜-年中总结.ppt", "D:\\doc\\2019-郑家骜-年中总结.pdf");
        log.info("转换是否成功="+ppt2PDF);
        return ppt2PDF;
    }
}

// package com.zja.tree.dirtree.service;
//
// import com.zja.tree.dirtree.entity.enums.TemplateType;
// import com.zja.tree.dirtree.model.response.InitTreeResult;
// import lombok.extern.slf4j.Slf4j;
// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.stereotype.Service;
//
// import java.io.IOException;
//
// /**
//  * @Author: zhengja
//  * @Date: 2025-11-06 11:22
//  */
// @Slf4j
// @Service
// public class BusinessInitializationService {
//
//     @Autowired
//     private JsonTreeInitializationService jsonTreeInitializationService;
//
//     public void initializeDefaultStructures() {
//         try {
//             // 使用模板初始化商品分类
//             InitTreeResult productResult = jsonTreeInitializationService
//                     .initializeFromTemplate(TemplateType.PRODUCT_CATEGORY, "MY_PRODUCT_CAT", "system");
//
//             // 使用模板初始化文档结构
//             InitTreeResult documentResult = jsonTreeInitializationService
//                     .initializeFromTemplate(TemplateType.DOCUMENT_STRUCTURE, "COMPANY_DOCS", "admin");
//
//             log.info("初始化完成: 商品分类 {} 节点, 文档结构 {} 节点",
//                     productResult.getTotalNodes(), documentResult.getTotalNodes());
//
//         } catch (IOException e) {
//             log.error("初始化默认结构失败", e);
//         }
//     }
//
//     public void initializeFromCustomJson() {
//         try {
//             String jsonString = """
//                     {
//                       "name": "自定义目录树",
//                       "businessType": "CUSTOM_TYPE",
//                       "createdBy": "user123",
//                       "overwrite": true,
//                       "nodes": [
//                         {
//                           "name": "根节点",
//                           "nodeType": "DIRECTORY",
//                           "children": [
//                             {
//                               "name": "子节点1",
//                               "nodeType": "DIRECTORY"
//                             }
//                           ]
//                         }
//                       ]
//                     }
//                     """;
//
//             InitTreeResult result = jsonTreeInitializationService.initializeFromJsonString(jsonString);
//             log.info("自定义JSON初始化完成: {} 节点", result.getTotalNodes());
//
//         } catch (IOException e) {
//             log.error("自定义JSON初始化失败", e);
//         }
//     }
// }
# springboot-file-excel

**说明**

Excel 读取数据、写入数据、模版数据填充、规则计算等操作。


## spring-boot 集成(组件)示例

> 以下是已经完成的示例模块

- [springboot-file-excel-easyexcel 较推荐](./springboot-file-excel-easyexcel)
- [springboot-file-excel-easyexcel-db 较推荐](./springboot-file-excel-easyexcel-db)
- [springboot-file-excel-easypoi 不推荐，开源版已经不维护了](./springboot-file-excel-easypoi)
- [springboot-file-excel-jxls 推荐](./springboot-file-excel-jxls)

## easyexcel、jxls、poi 对比 

- easyexcel 简单好用、效率高、社区活跃(从2024年11月份开始，已经不维护，这里指不新增需求，但可以修复bug)
- jxls 模板，稍微复杂，但可以应对更多场景，例如：模版填充数据、合并单元格、设置样式、公式计算等
- poi 复杂，但功能强大，例如：图片导出、公式计算等

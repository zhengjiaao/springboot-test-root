package com.zja.controller;

import com.zja.entity.UserEntity;
import com.zja.entity.UserEntityDto;
import com.zja.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.solr.core.SolrTemplate;
import org.springframework.data.solr.core.query.Query;
import org.springframework.data.solr.core.query.SimpleQuery;
import org.springframework.data.solr.core.query.result.ScoredPage;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

/**全文检索 Solr
 * @author zhengja@dist.com.cn
 * @data 2019/8/20 15:43
 */
//@EnableScheduling  //开启定时任务，项目启动自动执行定时任务
@RequestMapping(value = "rest/solr/v2")
@RestController
@Api(tags = {"SolrTemplateController"},description = "Solr 全文检索 SolrTemplate")
public class SolrTemplateController {

    //方式二：自动装配
    @Autowired
    private SolrTemplate solrTemplate;

    @Autowired
    private UserService userService;

    @ApiOperation(value = "core库-添加文档内容-方式二",notes = "向文档中添加域，必须有id域，域的名称必须在scheme.xml中定义",httpMethod = "POST")
    @RequestMapping(value = "v2/core/insert",method = RequestMethod.POST)
    public Object validator2(@ApiParam(value = "具体参考：Model") @RequestBody UserEntityDto userEntityDto,
                             @ApiParam(value = "默 core 库" ,defaultValue = "core") @RequestParam String collection){
        try {
            UpdateResponse response = this.solrTemplate.saveBean(collection, userEntityDto);
            solrTemplate.commit(collection);
            return response;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    @ApiOperation(value = "core库-更新文档-方式二",notes = "更新文档内容，跟添加的区别是：id不能变，其他的可以变",httpMethod = "PUT")
    @RequestMapping(value = "v2/core/updateDocument",method = RequestMethod.PUT)
    public Object updateDocument2(@ApiParam(value = "具体参考：Model") @RequestBody UserEntityDto userEntityDto,
                                  @ApiParam(value = "默 core 库" ,defaultValue = "core") @RequestParam String collection) throws Exception{
        UpdateResponse updateResponse = this.solrTemplate.saveBean(collection, userEntityDto);
        this.solrTemplate.commit(collection);
        return updateResponse;
    }

    @ApiOperation(value = "core库-批量更新文档-方式二",notes = "批量更新文档",httpMethod = "GET")
    @RequestMapping(value = "v3/core/updateDocument",method = RequestMethod.GET)
    public Object updateDocument3(@ApiParam(value = "默 core 库" ,defaultValue = "core") @RequestParam String collection){

        UserEntityDto document = new UserEntityDto();
        document.setId(1L);
        document.setAge("20");
        document.setGuid("123");
        //少name字段

        UserEntityDto document2 = new UserEntityDto();
        document2.setId(2L);
        document2.setAge("21");
        document2.setName("List2");
        document2.setGuid("1234");  //多guid字段

        UserEntityDto document3 = new UserEntityDto();
        document3.setId(3L);
        document3.setAge("22");
        document3.setName("List3");
        //正常字段

        UpdateResponse response = solrTemplate.saveBeans(collection, Arrays.asList(document, document2,document3));
        solrTemplate.commit(collection);

        return response;
    }


    @ApiOperation(value = "查询文档内容-方式二",notes = "复杂(高级)查询",httpMethod = "GET")
    @RequestMapping(value = "v2/queryDocument",method = RequestMethod.GET)
    public Object queryDocument2(@ApiParam(value = "条件",defaultValue = "*:*") @RequestParam String condition,
                                 @ApiParam(value = "corename/默 core 库",defaultValue = "core") @RequestParam String collection,
                                 @ApiParam(value = "分页起始 默 0",defaultValue = "0") @RequestParam Long pageStart,
                                 @ApiParam(value = "分页结束 默 10",defaultValue = "10") @RequestParam Integer pageEnd) throws Exception {

        Query query=new SimpleQuery(condition);
        query.setOffset(pageStart);  //开始索引（默认0）start:(page-1)*rows
        query.setRows(pageEnd);      //每页记录数(默认10)//rows:rows
        if ("core".equals(collection)){
            ScoredPage<UserEntityDto> userEntityDtos = this.solrTemplate.queryForPage(collection, query, UserEntityDto.class);
            this.solrTemplate.commit(collection);
            return userEntityDtos.iterator();
        }else {
            ScoredPage<UserEntity> userEntities = this.solrTemplate.queryForPage(collection, query, UserEntity.class);
            this.solrTemplate.commit(collection);
            return userEntities.iterator();
        }
    }

    @ApiOperation(value = "根据id查询文档内容-方式二",notes = "根据id查询文档内容",httpMethod = "GET")
    @RequestMapping(value = "v3/queryDocument",method = RequestMethod.GET)
    public Object queryDocument3(@ApiParam(value = "条件id") @RequestParam String id,
                                 @ApiParam(value = "corename/默 core 库",defaultValue = "core") @RequestParam String collection){

        if ("core".equals(collection)){
            Optional<UserEntityDto> userEntityDto = this.solrTemplate.getById(collection, id, UserEntityDto.class);
            System.out.println("userEntityDto "+userEntityDto);
            this.solrTemplate.commit(collection);
            return userEntityDto.get();
        }else {
            Optional<UserEntity> userEntity = this.solrTemplate.getById(collection, id, UserEntity.class);
            System.out.println("userEntity "+userEntity);
            this.solrTemplate.commit(collection);
            return userEntity.get();
        }
    }

    @ApiOperation(value = "corename库-将数据库的数据导入solr索引库-方式二",notes = "将数据库的数据导入solr索引库",httpMethod = "GET")
    @RequestMapping(value = "v2/dataToSolr",method = RequestMethod.GET)
    public Object dataToSolr2(@ApiParam(value = "默 corename 库" ,defaultValue = "corename") @RequestParam String collection) throws Exception {
        //先去数据库查数据
        List<UserEntity> userList = this.userService.getUserList();

        //同时，解决了属性值 null 报错的问题
        UpdateResponse updateResponse = this.solrTemplate.saveBeans(collection, userList);
        System.out.println("成功保存数据到corename库："+userList);
        this.solrTemplate.commit(collection);
        return updateResponse;
    }

    @ApiOperation(value = "corename库-增量/默认全量更新-数据库-方式二",notes = "java操作Solr的全量或增量更新，可以结合定时任务做定时全量或增量更新",httpMethod = "GET")
    @RequestMapping(value = "updateSolrData",method = RequestMethod.GET)
    public Object updateSolrData(@ApiParam(value = "默 corename 库",defaultValue = "corename") @RequestParam String collection,
                                 @ApiParam(value = "默 全量更新",defaultValue = "true") @RequestParam String is) throws Exception {
        //获取当前时间
        LocalDateTime localDateTime = LocalDateTime.now();
        //输出当前时间
        System.out.println("当前时间为:" + localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));

        if ("true".equals(is)){
            //调用删除索引的方法，实现全量更新-不加的，多次调用就是增量更新
            deteleAllDocument2(collection,"*:*");
            System.out.println("============测试全量更新============");
        }else {
            System.out.println("============测试增量更新============");
        }
        //调用数据新增到索引库的方法二
        Object toSolr2 = dataToSolr2(collection);

        return toSolr2;
    }

    @ApiOperation(value = "根据条件删除文档",notes = "默认删除所有文档",httpMethod = "DELETE")
    @RequestMapping(value = "v2/deteleAllDocument",method = RequestMethod.DELETE)
    public Object deteleAllDocument2(@ApiParam(value = "core/默 corename 库" ,defaultValue = "corename") @RequestParam String collection,
                                    @ApiParam(value = "条件",defaultValue = "*:*") @RequestParam String condition) throws Exception {

        // 根据条件删除
        // httpSolrServer.deleteByQuery("");
        Query query=new SimpleQuery(condition);
        // 删除所有文档
        UpdateResponse response = this.solrTemplate.delete(collection,query);
        // 提交
        this.solrTemplate.commit(collection);

        return "删除文档-成功！";
    }

    /**
     * 需要在类上 加开启定时任务注解  @EnableScheduling
     * 全量更新 -成功
     * @throws Exception
     */
    @Scheduled(cron = "0 */1 * * * ?")
    public void timer2() throws Exception {
        //获取当前时间
        LocalDateTime localDateTime = LocalDateTime.now();
        //输出当前时间
        System.out.println("当前时间为:" +
                localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("============测试二============");
        //调用删除索引的方法
        deteleAllDocument2("corename","*:*");
        Thread.sleep(5000);
        //调用数据新增到索引库的方法二
        dataToSolr2("corename");
    }
}

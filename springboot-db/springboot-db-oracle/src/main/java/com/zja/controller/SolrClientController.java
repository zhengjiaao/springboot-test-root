package com.zja.controller;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.zja.entity.UserEntity;
import com.zja.service.UserService;
import com.zja.util.PropertyNullToStrUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.apache.solr.client.solrj.SolrClient;
import org.apache.solr.client.solrj.SolrQuery;
import org.apache.solr.client.solrj.SolrServerException;
import org.apache.solr.client.solrj.response.QueryResponse;
import org.apache.solr.client.solrj.response.UpdateResponse;
import org.apache.solr.common.SolrDocumentList;
import org.apache.solr.common.SolrInputDocument;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**全文检索 Solr
 * @author zhengja@dist.com.cn
 * @data 2019/8/20 15:43
 */
//@EnableScheduling  //开启定时任务，项目启动自动执行定时任务
@RequestMapping(value = "rest/solr/v1")
@RestController
@Api(tags = {"SolrClientController"},description = "Solr 全文检索 SolrClient")
public class SolrClientController {

    //方式一
    @Autowired
    private SolrClient client;

    //数据库获取数据接口
    @Autowired
    private UserService userService;


    @ApiOperation(value = "core库-添加文档内容-方式一",notes = "向文档中添加域，必须有id域，域的名称必须在scheme.xml中定义",httpMethod = "GET")
    @RequestMapping(value = "core/insert",method = RequestMethod.GET)
    public Object validator(@ApiParam(value = "name") @RequestParam String name,
                            @ApiParam(value = "age") @RequestParam String age,
                            @ApiParam(value = "默 core 库" ,defaultValue = "core") @RequestParam String collection){
        try {
            String idStr = String.valueOf(System.currentTimeMillis());

            SolrInputDocument document = new SolrInputDocument();
            document.setField("id", idStr);
            document.setField("name", name);
            document.setField("age",age);

            //1.可以用addBean 添加对象写入索引库-(推荐使用)-不存在null指针异常
            /*UserEntityDto userEntityDto = new UserEntityDto();
            userEntityDto.setId(10L);
            userEntityDto.setName("2"+name);
            userEntityDto.setCreateTime(new Date());
            client.addBean(collection,userEntityDto);*/

            //2.也可以把文档对象写入索引库
            client.add(collection,document);//如果配置文件中没有指定core，这个方法的第一个参数就需要指定core名称,比如client.add("core", doc);
            client.commit(collection);//如果配置文件中没有指定core，这个方法的第一个参数就需要指定core名称client.commit("core");
            return idStr;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "error";
    }

    /**怎么保证是修改还是新增呢？
     * 这里主要是根据id来判断，这个id类似db中的唯一主键，当我们没有指定id时，会随机生成一个id
     *如果存在相同的id，则修改文档；如果不存在，则新增文档
     */
    @ApiOperation(value = "core库-更新文档-方式一",notes = "更新文档内容，跟添加的区别是：id不能变，其他的可以变",httpMethod = "GET")
    @RequestMapping(value = "core/updateDocument",method = RequestMethod.GET)
    public Object updateDocument(@ApiParam(value = "idStr") @RequestParam String idStr,
                               @ApiParam(value = "name") @RequestParam String name,
                               @ApiParam(value = "age") @RequestParam String age,
                                 @ApiParam(value = "sex2") @RequestParam String sex2,
                                 @ApiParam(value = "默 core 库" ,defaultValue = "core") @RequestParam String collection) throws Exception{
        // 创建一个文档对象, 向文档中添加域，必须有id域，域的名称必须在scheme.xml中定义
        SolrInputDocument document = new SolrInputDocument();
        document.setField("id", idStr);
        document.setField("name", name);
        document.setField("age",age);
        document.setField("sex2",sex2);
        // 把文档对象写入索引库
        client.add(collection,document);
        // 提交
        client.commit(collection);
        return document;
    }


    @ApiOperation(value = "查询文档内容-方式一",notes = "复杂查询",httpMethod = "GET")
    @RequestMapping(value = "queryDocument",method = RequestMethod.GET)
    public Object queryDocument(@ApiParam(value = "条件",defaultValue = "*:*") @RequestParam String condition,
                              @ApiParam(value = "core/默 corename 库",defaultValue = "corename") @RequestParam String collection,
                              @ApiParam(value = "分页起始 默 0",defaultValue = "0") @RequestParam Integer pageStart,
                              @ApiParam(value = "分页结束 默 10",defaultValue = "10") @RequestParam Integer pageEnd) throws Exception {
        // 创建一个查询条件
        SolrQuery solrQuery = new SolrQuery();
        // 设置查询条件
        solrQuery.setQuery(condition);
        // 设置分页
        solrQuery.setStart(pageStart);
        solrQuery.setRows(pageEnd);
        //排序
        solrQuery.setSort("id",SolrQuery.ORDER.asc);

        /*// df 代表默认的查询字段
        solrQuery.set("name", "关键字");
        //   指的是你查询完毕之后要返回的字段
        solrQuery.set("name", "id,name");
        //高亮
        //打开开关
        solrQuery.setHighlight(false);
        solrQuery.addHighlightField("name"); // 高亮字段

        //设置前缀
        solrQuery.setHighlightSimplePre("<font color=\"red\">");
        //设置后缀
        solrQuery.setHighlightSimplePost("</font>");*/

        // 执行查询
        QueryResponse query = client.query(collection,solrQuery);
        // 取查询结果
        SolrDocumentList solrDocumentList = query.getResults();

        System.out.println("总记录数：" + solrDocumentList.getNumFound());
        client.commit(collection);
        return solrDocumentList;
    }


    @ApiOperation(value = "根据id删除文档--方式一",notes = "根据id删除单个文档",httpMethod = "DELETE")
    @RequestMapping(value = "deteleDocument",method = RequestMethod.DELETE)
    public Object deteleDocument(@ApiParam(value = "core/默 corename 库" ,defaultValue = "corename") @RequestParam String collection,
                               @ApiParam(value = "idStr") @RequestParam String idStr) throws Exception {

        // 根据条件删除
        // httpSolrServer.deleteByQuery("");
        // 根据id删除
        UpdateResponse response = client.deleteById(collection, idStr);
        // 提交
        client.commit(collection);
        return response;
    }

    @ApiOperation(value = "根据条件删除文档-方式一",notes = "默认删除所有文档",httpMethod = "DELETE")
    @RequestMapping(value = "deteleAllDocument",method = RequestMethod.DELETE)
    public Object deteleAllDocument(@ApiParam(value = "core/默 corename 库" ,defaultValue = "corename") @RequestParam String collection,
                                    @ApiParam(value = "条件",defaultValue = "*:*") @RequestParam String condition) throws Exception {

        // 根据条件删除
        // httpSolrServer.deleteByQuery("");

        // 删除所有文档
        UpdateResponse response = client.deleteByQuery(collection,condition);
        // 提交
        client.commit(collection);

        return "删除所有文档-成功！";
    }

    @ApiOperation(value = "corename库-添加文档内容-方式一",notes = "向文档中添加域，必须有id域，域的名称必须在scheme.xml中定义",httpMethod = "GET")
    @RequestMapping(value = "corename/insert",method = RequestMethod.GET)
    public Object validator2(@ApiParam(value = "数据库数据id") @RequestParam Long id,
                             @ApiParam(value = "默 corename 库" ,defaultValue = "corename") @RequestParam String collection) throws IOException, SolrServerException {
        Optional<UserEntity> userEntity = this.userService.getUserById(id);
        UserEntity user = userEntity.get();

        //方式一(推荐使用)：把数据对象写入索引库  -不存在null指针异常
        //client.addBean(collection,user);

        System.out.println("user "+user);

        //方式二：把文档对象写入索引库
        SolrInputDocument document = new SolrInputDocument();

        //原始添加方式：这种方式也可以，不过类属性字段，不能有null值存在，如 age=null，出错：null指针异常
        /*document.addField("user_id",user.getId().toString());
        document.addField("user_age",user.getAge().toString());
        document.addField("user_lastUpdateTime",new SimpleDateFormat("yyyy-MM-dd HH:MM:SS").format(new Date()));
        document.addField("user_name",user.getName().toString());*/

        //解决属性值 null 指针异常
        //把 类 中所有属性为 null 的转为 ""
        String str = JSON.toJSONString(user,SerializerFeature.WriteMapNullValue);
        //json对象转string
        JSONObject object = new JSONObject(str);
        JSONObject jsonObject = PropertyNullToStrUtil.filterNull(object);
        System.out.println("jsonObject"+jsonObject);

        document.addField("id",jsonObject.get("id"));
        document.addField("user_age",jsonObject.get("age"));
        document.addField("user_lastUpdateTime",new SimpleDateFormat("yyyy-MM-dd HH:MM:SS").format(new Date()));
        document.addField("user_name",jsonObject.get("name"));

        //方式二：把文档对象写入索引库
        client.add(collection,document);//如果配置文件中没有指定core，这个方法的第一个参数就需要指定core名称,比如client.add("corename", doc);
        client.commit(collection);//如果配置文件中没有指定core，这个方法的第一个参数就需要指定core名称client.commit("core");
        return document;
    }


    @ApiOperation(value = "corename库-更新文档内容-方式一",notes = "更新文档内容，跟添加的区别是：id不能变，其他的可以变",httpMethod = "GET")
    @RequestMapping(value = "corename/updateDocument",method = RequestMethod.GET)
    public Object updateDocument2(@ApiParam(value = "数据库数据id") @RequestParam Long id,
                                  @ApiParam(value = "Solr数据库id") @RequestParam String solrid,
                                  @ApiParam(value = "默 corename 库" ,defaultValue = "corename") @RequestParam String collection) throws Exception{
        Optional<UserEntity> userEntity = this.userService.getUserById(id);
        UserEntity user = userEntity.get();
        // 创建一个文档对象, 向文档中添加域，必须有id域，域的名称必须在scheme.xml中定义
        SolrInputDocument document = new SolrInputDocument();

        //把 类 中所有属性为 null 的转为 ""
        String str = JSON.toJSONString(user,SerializerFeature.WriteMapNullValue);
        //json对象转string
        JSONObject object = new JSONObject(str);
        JSONObject jsonObject = PropertyNullToStrUtil.filterNull(object);
        //System.out.println("jsonObject"+jsonObject);
        document.addField("id",jsonObject.get("id"));
        document.addField("user_age",jsonObject.get("age"));
        document.addField("user_lastUpdateTime",jsonObject.get("lastUpdateTime"));
        document.addField("user_name",jsonObject.get("name"));

        // 把文档对象写入索引库
        client.add(collection,document);
        // 提交
        client.commit(collection);
        return document;
    }

    @ApiOperation(value = "corename库-将数据库的数据导入solr索引库-方式一",notes = "将数据库的数据导入solr索引库",httpMethod = "GET")
    @RequestMapping(value = "dataToSolr",method = RequestMethod.GET)
    public void dataToSolr(@ApiParam(value = "默 corename 库" ,defaultValue = "corename") @RequestParam String collection) throws Exception {
        //先去数据库查数据
        List<UserEntity> userList = this.userService.getUserList();
        //循环遍历查询
        for (UserEntity user : userList){

            SolrInputDocument document = new SolrInputDocument();
            //创建文档对象
            //添加域
            //System.out.println("user= "+user);

            //把 类 中所有属性为 null 的转为 ""
            String str = JSON.toJSONString(user,SerializerFeature.WriteMapNullValue);
            //json对象转string
            JSONObject object = new JSONObject(str);
            JSONObject jsonObject = PropertyNullToStrUtil.filterNull(object);
            //System.out.println("jsonObject"+jsonObject);
            document.addField("id",jsonObject.get("id"));
            document.addField("user_age",jsonObject.get("age"));
            document.addField("user_lastUpdateTime",jsonObject.get("lastUpdateTime"));
            document.addField("user_name",jsonObject.get("name"));
            //写入
            System.out.println("document="+document);
            client.add(collection,document);
        }
        //提交
        client.commit(collection);
        System.out.println("成功保存数据到corename库："+userList);
    }

    /**需要在类上 加开启定时任务注解  @EnableScheduling
     * 定时器  - 全量更新 -成功
     * cron代表的是时间  如下代表（每隔2分钟执行一次）
     * @throws Exception
     */
    @Scheduled(cron = "0 */2 * * * ?")
    public void timer() throws Exception {
        //获取当前时间
        LocalDateTime localDateTime = LocalDateTime.now();
        //输出当前时间
        System.out.println("当前时间为:" +
                localDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")));
        System.out.println("============测试一============");
        //调用删除索引的方法
        deteleAllDocument("corename","*:*");
        Thread.sleep(5000);
        //调用数据新增到索引库的方法
        dataToSolr("corename");
    }


}

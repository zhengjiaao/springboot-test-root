# springboot-test-elasticsearch

# Springboot Elasticsearch

## 一、版本选择

#### Spring Data Elasticsearch

> 参考：https://docs.spring.io/spring-data/elasticsearch/docs/3.2.0.RC3/reference/html/#preface.versions

Spring Data Elasticsearch、Elasticsearch 对应版本

| Spring Data Elasticsearch | Elasticsearch |
| ------------------------- | ------------- |
| 3.2.x                     | 6.8.1         |
| 3.1.x                     | 6.2.2         |
| 3.0.x                     | 5.5.0         |
| 2.1.x                     | 2.4.0         |
| 2.0.x                     | 2.2.0         |
| 1.3.x                     | 1.5.2         |

Spring Boot、Spring Data Elasticsearch、Elasticsearch 对应版本

|                  Spring Data Release Train                   |                  Spring Data Elasticsearch                   | Elasticsearch |                         Spring Boot                          |
| :----------------------------------------------------------: | :----------------------------------------------------------: | :-----------: | :----------------------------------------------------------: |
| Neumann[[1](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#_footnotedef_1)] | 4.0.x[[1](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#_footnotedef_1)] |     7.6.2     | 2.3.x[[1](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#_footnotedef_1)] |
|                            Moore                             |                            3.2.x                             |     6.8.6     |                            2.2.x                             |
|                           Lovelace                           |                            3.1.x                             |     6.2.2     |                            2.1.x                             |
| Kay[[2](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#_footnotedef_2)] | 3.0.x[[2](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#_footnotedef_2)] |     5.5.0     | 2.0.x[[2](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#_footnotedef_2)] |
| Ingalls[[2](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#_footnotedef_2)] | 2.1.x[[2](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#_footnotedef_2)] |     2.4.0     | 1.5.x[[2](https://docs.spring.io/spring-data/elasticsearch/docs/current/reference/html/#_footnotedef_2)] |

dome参考：https://github.com/spring-projects/spring-data-examples/tree/master/elasticsearch

#### Java Elasticsearch 客户端

官方推荐：https://www.elastic.co/guide/en/elasticsearch/client/index.html

- **Transport Client**  不推荐
- **[Java High Level REST Client](https://www.elastic.co/guide/en/elasticsearch/client/java-rest/6.8/java-rest-high.html)**  推荐
- **Reactive REST Client** 不推荐



## 二、Spring Data Elasticsearch
> 默认 High Level REST Client

pom.xml
```xml
<dependency>
    <groupId>org.springframework.data</groupId>
    <artifactId>spring-data-elasticsearch</artifactId>
    <version>3.2.5.RELEASE</version>
</dependency>
<dependency>
    <groupId>org.elasticsearch</groupId>
    <artifactId>elasticsearch</artifactId>
    <version>6.8.5</version>
</dependency>
```
*.yml
```yaml
spring:
  data:
    elasticsearch:
      repositories:
        enabled: true # 启用 ElasticsearchTemplate
      properties:
        path:
          logs: ./elasticsearch/log   # ES日志存储目录
          data: ./elasticsearch/data  # ES数据存储目录
      cluster-name: my-cluster        # ES集群名称，在elasticsearch.yml中配置 默 elasticsearch
      cluster-nodes: 127.0.0.1:9300   # ES集群节点 用逗号分隔 , java连接默9300，http连接默9200
```
实体类
```java
@Data
@Document(indexName = "book_index", type = "book_type", indexStoreType = "fs", shards = 5, replicas = 1, refreshInterval = "-1")
public class BookEntity {
    @Id
    private String id;
    private String title;
    private String content;
    private int userId;
    private int weight;
}
```
Repository接口
```java
@Component
public interface BookRepository extends ElasticsearchRepository<BookEntity, String> {
}
```
controller层
```java
package com.dist.controller;

import com.dist.dao.BookRepository;
import com.dist.entity.BookEntity;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.elasticsearch.index.query.MultiMatchQueryBuilder;
import org.elasticsearch.index.query.QueryBuilders;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.elasticsearch.core.ElasticsearchTemplate;
import org.springframework.data.elasticsearch.core.query.IndexQuery;
import org.springframework.data.elasticsearch.core.query.IndexQueryBuilder;
import org.springframework.data.elasticsearch.core.query.NativeSearchQueryBuilder;
import org.springframework.data.elasticsearch.core.query.SearchQuery;
import org.springframework.web.bind.annotation.*;

import static org.elasticsearch.index.query.QueryBuilders.*;

/**
 * @author zhengja@dist.com.cn
 * @data 2020/07/17 11:41
 */
@RestController
@Api(tags = {"BookElasticsearchController"}, description = "全文检索")
public class BookElasticsearchController {

    @Autowired
    ElasticsearchTemplate elasticsearchTemplate;

    @Autowired
    BookRepository bookRepository;

    @ApiOperation(value = "添加索引", httpMethod = "POST")
    @PostMapping(value = "v1/book/add/{id}")
    public Object addbook(@ApiParam(defaultValue = "50") @PathVariable String id) {
        //创建索引
        // 1、直接用名称创建索引
        //boolean indexRes = elasticsearchTemplate.createIndex("book_es");
        // 2、填入class对象创建索引
        //boolean indexRes = elasticsearchTemplate.createIndex(BookEntity.class);
        //System.out.println("======创建索引结果：" + indexRes + "=========");

        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(id);
        bookEntity.setTitle("浣溪沙");
        bookEntity.setContent("宋·李清照 莫许杯深琥珀浓，未成沉醉意先融。疏钟已应晚来风。瑞脑香消魂梦断");
        bookEntity.setUserId(51);
        bookEntity.setWeight(100);

        IndexQuery indexQuery = new IndexQueryBuilder()
                .withId(bookEntity.getId())
                .withObject(bookEntity)
                .build();
        String index = elasticsearchTemplate.index(indexQuery);
        //BookEntity document = bookRepository.save(bookEntity);
        return index;
    }

    @ApiOperation(value = "查询全部内容", httpMethod = "GET")
    @GetMapping(value = "v1/book/srarch/all")
    public Object srarch() {
        return bookRepository.findAll();
    }

    @ApiOperation(value = "根据id查询内容", httpMethod = "GET")
    @GetMapping(value = "v1/book/srarch/id")
    public Object srarch(@ApiParam(defaultValue = "50") @RequestParam String id) {
        return bookRepository.findById(id);
    }

    @ApiOperation(value = "根据id更新内容", httpMethod = "PUT")
    @PutMapping(value = "v1/book/srarch/updete")
    public Object updete(@ApiParam(defaultValue = "50") @RequestParam String id) {
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(id);
        bookEntity.setTitle("浣溪沙2");
        bookEntity.setContent("宋·李清照 莫许杯深琥珀浓，未成沉醉意先融。疏钟已应晚来风。瑞脑香消魂梦断");
        bookEntity.setUserId(55);
        bookEntity.setWeight(102);
        return bookRepository.save(bookEntity);
    }

    @ApiOperation(value = "单字符串全文模糊查询Ex", httpMethod = "GET")
    @GetMapping(value = "v1/book/srarch/ex")
    public Object srarchEx(@ApiParam(value = "查询内容", defaultValue = "浣溪沙") @RequestParam String content,
                         @ApiParam(value = "当前页", defaultValue = "0") @RequestParam int page,
                         @ApiParam(value = "显示多少条", defaultValue = "20") @RequestParam int size) {

        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.queryStringQuery(content)).withPageable(PageRequest.of(page, size)).build();
        return bookRepository.search(searchQuery);
    }

    @ApiOperation(value = "多字段的匹配查询Ex", httpMethod = "GET")
    @GetMapping(value = "v1/book/srarch/queryBuilder")
    public Object srarchQueryBuilder(@ApiParam(value = "查询内容", defaultValue = "浣溪沙") @RequestParam String title) {

        MultiMatchQueryBuilder queryBuilder = multiMatchQuery(title, "title", "content");
        return bookRepository.search(queryBuilder);
    }

    @ApiOperation(value = "单字符串全文模糊查询", notes = "查询全部字段内容", httpMethod = "GET")
    @GetMapping(value = "v1/book/srarch")
    public Object srarch(@ApiParam(value = "查询内容", defaultValue = "浣溪沙") @RequestParam String content,
                         @ApiParam(value = "当前页", defaultValue = "0") @RequestParam int page,
                         @ApiParam(value = "显示多少条", defaultValue = "20") @RequestParam int size) {
        //使用queryStringQuery完成单字符串查询
        //单字符串默认模糊查询，默认排序。将从所有字段中查找包含传来的content分词后字符串的数据集
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.queryStringQuery(content)).withPageable(PageRequest.of(page, size)).build();
        return elasticsearchTemplate.queryForList(searchQuery, BookEntity.class);
    }

    @ApiOperation(value = "某字段字符串模糊查询", notes = "将从所有字段中查找包含传来的content分词后字符串的数据集", httpMethod = "GET")
    @GetMapping(value = "v1/book/srarch/matchQuery")
    public Object srarchMatchQuery(@ApiParam(value = "查询内容", defaultValue = "浣溪沙") @RequestParam String content,
                                   @ApiParam(value = "当前页", defaultValue = "0") @RequestParam int page,
                                   @ApiParam(value = "显示多少条", defaultValue = "20") @RequestParam int size) {
        //某字段(content)字符串模糊查询
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(matchQuery("content", content)).withPageable(PageRequest.of(page, size)).build();
        return elasticsearchTemplate.queryForList(searchQuery, BookEntity.class);
    }

    @ApiOperation(value = "短语匹配", notes = "短语必须是连续的 不知归路 不归路", httpMethod = "GET")
    @GetMapping(value = "v1/book/srarch/PhraseQuery")
    public Object srarchPhraseQuery(@ApiParam("查询内容") @RequestParam String content,
                                    @ApiParam(value = "当前页", defaultValue = "0") @RequestParam int page,
                                    @ApiParam(value = "显示多少条", defaultValue = "20") @RequestParam int size) {

        // PhraseMatch查询，短语匹配，单字段对某短语进行匹配查询，短语分词的顺序会影响结果
        //类似于数据库里的 %落日熔金%
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.matchPhraseQuery("content", content)).withPageable(PageRequest.of(page, size)).build();
        return elasticsearchTemplate.queryForList(searchQuery, BookEntity.class);
    }

    @ApiOperation(value = "完全匹配查询", notes = "最严格的匹配,不进行分词", httpMethod = "GET")
    @GetMapping(value = "v1/book/srarch/TermQuery")
    public Object srarchTermQuery(@ApiParam(value = "id", defaultValue = "5") @RequestParam int userId,
                                  @ApiParam(value = "当前页", defaultValue = "0") @RequestParam int page,
                                  @ApiParam(value = "显示多少条", defaultValue = "10") @RequestParam int size) {
        //term一般适用于做过滤器filter的情况，譬如我们去查询title中包含“浣溪沙”且userId=1时，那么就可以用termQuery(“userId”, 1)作为查询的filter
        //不对传来的值分词，去找完全匹配的
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(termQuery("userId", userId)).withPageable(PageRequest.of(page, size)).build();
        return elasticsearchTemplate.queryForList(searchQuery, BookEntity.class);
    }

    @ApiOperation(value = "多字段的匹配查询", httpMethod = "GET")
    @GetMapping(value = "v1/book/srarch/MultiMatchQuery")
    public Object srarchMultiMatchQuery(@ApiParam("查询内容") @RequestParam String title,
                                        @ApiParam(value = "当前页", defaultValue = "0") @RequestParam int page,
                                        @ApiParam(value = "显示多少条", defaultValue = "10") @RequestParam int size) {

        //MultiMatchQuery: 多字段匹配 "title","content" ,只要任何一个字段包括该字符串即可
        SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(QueryBuilders.multiMatchQuery(title, "title", "content"))
                .withPageable(PageRequest.of(page, size))
                .build();

        //MultiMatchQueryBuilder multiMatchQuery = QueryBuilders.multiMatchQuery(title, "title", "content");
        //构建查询对象
        /*SearchQuery searchQuery = new NativeSearchQueryBuilder()
                .withQuery(multiMatchQuery)
                .withIndices("book_es")  //索引名
                .withPageable(PageRequest.of(page, size, new Sort(Sort.Direction.DESC, "id")))
                .build();*/
        //执行查询
        return elasticsearchTemplate.queryForList(searchQuery, BookEntity.class);
    }

    @ApiOperation(value = "多字段合并查询", httpMethod = "GET")
    @GetMapping(value = "v1/book/srarch/BoolQuery")
    public Object srarchBoolQuery(@ApiParam(value = "userId", defaultValue = "2") @RequestParam String userId,
                                  @ApiParam(value = "weight", defaultValue = "14") @RequestParam String weight,
                                  @ApiParam(value = "title", defaultValue = "浣溪沙") @RequestParam String title) {
        //boolQuery 可以设置多个条件的查询方式,用来组合多个Query,组合方式有四种：must，mustnot，filter，should
        /*must代表返回的文档必须满足must子句的条件，会参与计算分值；
        filter代表返回的文档必须满足filter子句的条件，但不会参与计算分值；
        should代表返回的文档可能满足should子句的条件，也可能不满足，有多个should时满足任何一个就可以，通过minimum_should_match设置至少满足几个。
        mustnot代表必须不满足子句的条件。*/
        SearchQuery searchQuery = new NativeSearchQueryBuilder().withQuery(QueryBuilders.boolQuery().must(termQuery("userId", userId))
                .should(rangeQuery("weight").lt(weight)).must(matchQuery("title", title))).build();
        return elasticsearchTemplate.queryForList(searchQuery, BookEntity.class);
    }

    @ApiOperation(value = "删除索引", httpMethod = "DELETE")
    @DeleteMapping(value = "v1/book/delete")
    public void deleteIndex(@ApiParam("id") @RequestParam String id) {

        //删除索引
        BookEntity bookEntity = new BookEntity();
        bookEntity.setId(id);

        //bookRepository.delete(bookEntity);
        //根据id删除
        bookRepository.deleteById(id);
        //删除所有
        //bookRepository.deleteAll();
    }

    @ApiOperation(value = "删除所有索引", httpMethod = "DELETE")
    @DeleteMapping(value = "v1/book/delete/all")
    public void deleteAllIndex() {
        //删除所有
        bookRepository.deleteAll();
    }

}
```
InitBookData 初始化数据
```java
/**
 * Email: zhengja@dist.com.cn
 * Desc：只初始化一次测试数据
 */
@Component
public class InitBookData {

    @Autowired
    BookRepository bookRepository;

    @PostConstruct
    public void init() {
        //只初始化一次
        Iterable<BookEntity> posts = bookRepository.findAll();
        if (posts.iterator().hasNext()) {
            return;
        }
        for (int i = 0; i < 40; i++) {
            BookEntity book = new BookEntity();
            book.setTitle(getTitle().get(i));
            book.setContent(getContent().get(i));
            book.setWeight(i);
            book.setUserId(i % 10);
            bookRepository.save(book);
        }
    }

    private List<String> getTitle() {
        List<String> list = new ArrayList<>();
        list.add("《如梦令·常记溪亭日暮》");
        list.add("《醉花阴·薄雾浓云愁永昼》");
        list.add("《声声慢·寻寻觅觅》");
        list.add("《永遇乐·落日熔金》");
        list.add("《如梦令·昨夜雨疏风骤》");
        list.add("《渔家傲·雪里已知春信至》");
        list.add("《点绛唇·蹴[1]罢秋千》");
        list.add("《点绛唇·寂寞深闺》");
        list.add("《蝶恋花·泪湿罗衣脂粉满》");
        list.add("《蝶恋花 离情》");
        list.add("《浣溪沙》");
        list.add("《浣溪沙》");
        list.add("《浣溪沙》");
        list.add("《浣溪沙》");
        list.add("《浣溪沙》");
        list.add("《减字木兰花·卖花担上》");
        list.add("《临江仙·欧阳公作《蝶恋花》");
        list.add("《临江仙·庭院深深深几许》");
        list.add("《念奴娇·萧条庭院》");
        list.add("《菩萨蛮·风柔日薄春犹早》");
        list.add("《菩萨蛮·归鸿声断残云碧》");
        list.add("《武陵春·风住尘香花已尽》");
        list.add("《一剪梅·红藕香残玉蕈秋》");
        list.add("《渔家傲·天接云涛连晓雾》");
        list.add("《鹧鸪天·暗淡轻黄体性柔》");
        list.add("《鹧鸪天·寒日萧萧上锁窗》");
        list.add("《一剪梅·红藕香残玉簟秋》");
        list.add("《如梦令·常记溪亭日暮》");
        list.add("《浣溪沙》");
        list.add("《浣溪沙》");
        list.add("《浣溪沙》");
        list.add("《蝶恋花·泪湿罗衣脂粉满》");
        list.add("《蝶恋花·暖日晴风初破冻》");
        list.add("《鹧鸪天·寒日萧萧上锁窗》");
        list.add("《醉花阴·薄雾浓云愁永昼》");
        list.add("《鹧鸪天·暗淡轻黄体性柔》");
        list.add("《蝶恋花·永夜恹恹欢意少》");
        list.add("《浣溪沙》");
        list.add("《浣溪沙》");
        list.add("《如梦令·谁伴明窗独坐》");
        return list;
    }

    private List<String> getContent() {
        List<String> list = new ArrayList<>();
        list.add("初中 宋·李清照 常记溪亭日暮，沉醉不知归路，兴尽晚回舟，误入藕花深处。争渡，争渡");
        list.add("重阳节 宋·李清照 薄雾浓云愁永昼，瑞脑消金兽。佳节又重阳，玉枕纱厨，半夜凉初透。东");
        list.add("闺怨诗 宋·李清照 寻寻觅觅，冷冷清清，凄凄惨惨戚戚。乍暖还寒时候，最难将息。三杯两");
        list.add("元宵节 宋·李清照 落日熔金，暮云合璧，人在何处。染柳烟浓，吹梅笛怨，春意知几许。元");
        list.add("婉约诗 宋·李清照 昨夜雨疏风骤，浓睡不消残酒，试问卷帘人，却道海棠依旧。知否，知否");
        list.add("描写梅花 宋·李清照 雪里已知春信至，寒梅点缀琼枝腻，香脸半开娇旖旎，当庭际，玉人浴出");
        list.add(" 宋·李清照 蹴罢秋千，起来慵整纤纤手。露浓花瘦，薄汗轻衣透。见客入来，袜刬金");
        list.add("闺怨诗 宋·李清照 寂寞深闺，柔肠一寸愁千缕。惜春春去。几点催花雨。倚遍阑干，只是无");
        list.add("婉约诗 宋·李清照 泪湿罗衣脂粉满。四叠阳关，唱到千千遍。人道山长水又断。萧萧微雨闻");
        list.add("描写春天 宋·李清照 暖雨晴风初破冻，柳眼梅腮，已觉春心动。酒意诗情谁与共？泪融残粉花");
        list.add("寒食节 宋·李清照 淡荡春光寒食天，玉炉沈水袅残烟，梦回山枕隐花钿。海燕未来人斗草，");
        list.add(" 宋·李清照 髻子伤春慵更梳，晚风庭院落梅初，淡云来往月疏疏，玉鸭薰炉闲瑞脑，");
        list.add(" 宋·李清照 莫许杯深琥珀浓，未成沉醉意先融。疏钟已应晚来风。瑞脑香消魂梦断，");
        list.add("闺怨诗 宋·李清照 小院闲窗春已深，重帘未卷影沉沉。倚楼无语理瑶琴，远岫出山催薄暮。");
        list.add("爱情诗 宋·李清照 绣幕芙蓉一笑开，斜偎宝鸭亲香腮，眼波才动被人猜。一面风情深有韵，");
        list.add("描写春天 宋·李清照 卖花担上，买得一枝春欲放。泪染轻匀，犹带彤霞晓露痕。怕郎猜道，奴");
        list.add("》 宋·李清照 欧阳公作《蝶恋花》，有“深深深几许”之句，予酷爱之。用其语作“庭");
        list.add("描写梅花 宋·李清照 庭院深深深几许，云窗雾阁春迟，为谁憔悴损芳姿。夜来清梦好，应是发");
        list.add("寒食节 宋·李清照 萧条庭院，又斜风细雨，重门须闭。宠柳娇花寒食近，种种恼人天气。险");
        list.add("思乡诗 宋·李清照 风柔日薄春犹早，夹衫乍著心情好。睡起觉微寒，梅花鬓上残。故乡何处");
        list.add("描写春天 宋·李清照 归鸿声断残云碧，背窗雪落炉烟直。烛底凤钗明，钗头人胜轻。角声催晓");
        list.add("闺怨诗 宋·李清照 风住尘香花已尽，日晚倦梳头。物是人非事事休，欲语泪先流。闻说双溪");
        list.add(" 宋·李清照 红藕香残玉蕈秋，轻解罗裳，独上兰舟。云中谁寄锦书来？雁字回时，月");
        list.add("豪放诗 宋·李清照 天接云涛连晓雾。星河欲转千帆舞。仿佛梦魂归帝所。闻天语。殷勤问我");
        list.add("描写花 宋·李清照 暗淡轻黄体性柔。情疏迹远只香留。何须浅碧深红色，自是花中第一流。");
        list.add("描写秋天 宋·李清照 寒日萧萧上琐窗，梧桐应恨夜来霜。酒阑更喜团茶苦，梦断偏宜瑞脑香。");
        list.add("闺怨诗 宋·李清照 红藕香残玉簟秋。轻解罗裳，独上兰舟。云中谁寄锦书来？雁字回时，月");
        list.add(" 宋·李清照 常记溪亭日暮。沈醉不知归路。兴尽晚回舟，误入藕花深处。争渡。争渡");
        list.add(" 宋·李清照 莫许杯深琥珀浓。未成沈醉意先融。已应晚来风。瑞脑香消魂梦断，");
        list.add(" 宋·李清照 小院闲窗春色深。重帘未卷影沈沈。倚楼无语理瑶琴。远岫出山催薄暮，");
        list.add(" 宋·李清照 淡荡春光寒食天。玉炉沈水袅残烟。梦回山枕隐花钿。海燕未来人斗草，");
        list.add(" 宋·李清照 泪湿罗衣脂粉满。四叠阳关，唱到千千遍。人道山长山又断。萧萧微雨闻");
        list.add(" 宋·李清照 暖日晴风初破冻。柳眼眉腮，已觉春心动。酒意诗情谁与共。泪融残粉花");
        list.add(" 宋·李清照 寒日萧萧上锁窗。梧桐应恨夜来霜。酒阑更喜团茶苦，梦断偏宜瑞脑香。");
        list.add(" 宋·李清照 薄雾浓云愁永昼。瑞脑消金兽。佳节又重阳，玉枕纱厨，半夜凉初透。东");
        list.add(" 宋·李清照 暗淡轻黄体性柔。情疏迹远只香留。何须浅碧深红色，自是花中第一流。");
        list.add(" 宋·李清照 永夜恹恹欢意少。空梦长安，认取长安道。为报今年春色好。花光月影宜");
        list.add(" 宋·李清照 髻子伤春慵更梳。晚风庭院落梅初。淡云来往月疏疏。玉鸭熏炉闲瑞脑，");
        list.add(" 宋·李清照 绣面芙蓉一笑开。斜飞宝鸭衬香腮。眼波才动被人猜。一面风情深有韵，");
        list.add(" 宋·李清照 谁伴明窗独坐，我共影儿俩个。灯尽欲眠时，影也把人抛躲。无那，无那");
        return list;
    }

}

```
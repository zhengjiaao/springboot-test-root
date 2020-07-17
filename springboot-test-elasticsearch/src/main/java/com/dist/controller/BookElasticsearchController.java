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
 * @data 2020/07/16 11:41
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

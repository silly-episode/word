package com.boot.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.boot.common.result.Result;
import com.boot.entity.Article;
import com.boot.utils.JsonUtils;
import com.boot.utils.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/3/17 16:19
 * @FileName: ArticleController
 * @Description: 文章
 */
@RestController
@RequestMapping("article")
@Slf4j
@RequiresAuthentication
public class ArticleController {

    @Resource
    private ElasticsearchClient elasticsearchClient;

    /**
     * @param article:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 创建文章
     * @Date: 2023/3/17 16:27
     */
    @PostMapping("article")
    public Result article(@RequestBody Article article) throws IOException {

        article.setArticleId(String.valueOf(SnowFlakeUtil.getNextId()))
                .setArticleCreateTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli());

        //这是一个同步请求，请求会卡在这里
        IndexResponse response = elasticsearchClient
                .index(i -> i.index("article")
                        .document(article)
                        .id(article.getArticleId()));
        log.info(response.result().jsonValue());
        if ("created".equals(response.result().jsonValue())) {
            return Result.success("插入成功");
        } else {
            return Result.error("插入失败");
        }

    }


    /**
     * @param articleId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 删除一个文章
     * @Date: 2023/3/17 17:09
     */
    @DeleteMapping("article/{articleId}")
    public Result article(@PathVariable String articleId) throws IOException {
        DeleteRequest deleteRequest = DeleteRequest.of(s -> s
                .index("article")
                .id(articleId));
        DeleteResponse response = elasticsearchClient.delete(deleteRequest);
        if ("deleted".equals(response.result().jsonValue())) {
            return Result.success("删除成功");
        } else {
            return Result.error("删除失败");
        }
    }

    /**
     * @param article:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 修改文章
     * @Date: 2023/3/20 11:44
     */
    @PutMapping("article")
    public Result articleChange(@RequestBody Map<String, String> article) throws IOException {
        log.info(JsonUtils.getBeanToJson(article));
        // 构建修改文档的请求
        UpdateResponse<Article> response = elasticsearchClient
                .update(e -> e.index("article")
                        .id(article.get("articleId"))
                        .doc(article), Article.class);

        // 打印请求结果
        System.out.println(response.result());
        if ("updated".equals(response.result().jsonValue())) {
            return Result.success("修改成功");
        } else {
            return Result.error("修改失败");
        }
    }

    /**
     * @param pageNum:
     * @param pageSize:
     * @param countUp:
     * @param countLow:
     * @param title:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 文章查询，如果countLow和up没有写值，就传99999和0
     * @Date: 2023/3/20 17:14
     */
    @GetMapping("article/{pageNum}/{pageSize}/{countUp}/{countLow}/{title}")
    public Result article(
            @PathVariable Integer pageNum,
            @PathVariable Integer pageSize,
            @PathVariable(required = false) Integer countUp,
            @PathVariable(required = false) Integer countLow,
            @PathVariable(required = false) String title) throws IOException {
//        log.info(title);
//        log.info(String.valueOf(countLow));
//        log.info(String.valueOf(countUp));
//        log.info(String.valueOf(pageSize));
//        log.info(String.valueOf(pageNum));
        SearchRequest request = new SearchRequest.Builder()
                //去哪个索引里搜索
                .index("article")
                .query(
                        QueryBuilders.bool().filter(
                                        QueryBuilders
                                                .wildcard()
                                                .field("articleTitle")
                                                .value(String.format("*%s*", title))
                                                .build()._toQuery(),
                                        QueryBuilders
                                                .range()
                                                .field("wordNumber")
                                                .gte(JsonData.fromJson(String.valueOf(countLow)))
                                                .lte(JsonData.fromJson(String.valueOf(countUp)))
                                                .build()._toQuery()
                                )
                                .build()
                                ._toQuery()
                )
                .from((pageNum - 1) * pageSize)
                .size(pageSize)
                .build();
        List<Article> list = new ArrayList<>();
        List<Hit<Article>> hits = elasticsearchClient.search(request, Article.class).hits().hits();
        for (Hit<Article> Hit : hits) {
            list.add(Hit.source());
        }
        System.out.println(JsonUtils.getBeanToJson(list));
        return Result.success(list);
    }
}

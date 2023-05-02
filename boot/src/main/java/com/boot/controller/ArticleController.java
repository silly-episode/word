package com.boot.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.SortOptionsBuilders;
import co.elastic.clients.elasticsearch._types.SortOrder;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.*;
import co.elastic.clients.elasticsearch.core.search.Hit;
import co.elastic.clients.json.JsonData;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.boot.common.result.Result;
import com.boot.dto.ArticleSearchDto;
import com.boot.entity.Article;
import com.boot.utils.ActionLogUtils;
import com.boot.utils.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

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
//@RequiresAuthentication
public class ArticleController {

    @Resource
    private ElasticsearchClient elasticsearchClient;
    @Resource
    private ActionLogUtils actionLogUtils;

    /**
     * @param article:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: 发布文章
     * @Date: 2023/3/17 16:27
     */
    @PostMapping("article")
    @RequiresAuthentication
    public Result article(@RequestBody Article article, HttpServletRequest request) throws IOException {

        article.setArticleId(String.valueOf(SnowFlakeUtil.getNextId()))
                .setArticleCreateTime(LocalDateTime.now().toInstant(ZoneOffset.of("+8")).toEpochMilli())
                .setWordNumber(article.getContent().length())
                .setArticleStudyNumber(0);

        //这是一个同步请求，请求会卡在这里
        IndexResponse response = elasticsearchClient
                .index(i -> i.index("article")
                        .document(article)
                        .id(article.getArticleId()));
        if ("created".equals(response.result().jsonValue())) {
            actionLogUtils.saveActionLog(request, actionLogUtils.INSERT, "发布了 《" + article.getArticleTitle() + "》 的文章");
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
    @RequiresAuthentication
    public Result article(@PathVariable String articleId, HttpServletRequest request) throws IOException {
        /*判断是否存在*/
        SearchRequest searchRequest = new SearchRequest.Builder()
                //去哪个索引里搜索
                .index("article")
                .query(
                        QueryBuilders
                                .term()
                                .field("articleId")
                                .value(articleId)
                                .build()._toQuery()
                )
                .size(1)
                .build();
        List<Hit<Article>> hits = elasticsearchClient.search(searchRequest, Article.class).hits().hits();
        Article article = 1 == hits.size() ? hits.get(0).source() : null;
        if (article != null) {
            /*开始删除*/
            DeleteRequest deleteRequest = DeleteRequest.of(s -> s
                    .index("article")
                    .id(articleId));
            DeleteResponse response = elasticsearchClient.delete(deleteRequest);
            if ("deleted".equals(response.result().jsonValue())) {
                /*记录操作日志*/
                actionLogUtils.saveActionLog(request, actionLogUtils.DELETE, "删除了 《" + article.getArticleTitle() + "》 的文章");
                return Result.success("删除成功");
            } else {
                return Result.error("删除失败");
            }
        } else {
            return Result.error("该文章不存在");
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
    @RequiresAuthentication
    public Result articleChange(@RequestBody Article article, HttpServletRequest request) throws IOException {

        // 构建修改文档的请求
        UpdateResponse<Article> response = elasticsearchClient
                .update(e -> e.index("article")
                        .id(article.getArticleId())
                        .doc(article), Article.class);
        if ("updated".equals(response.result().jsonValue())) {
            actionLogUtils.saveActionLog(request, actionLogUtils.UPDATE, "修改了 《" + article.getArticleTitle() + "》 的文章");
            return Result.success("修改成功");
        } else {
            return Result.error("修改失败");
        }
    }

    /**
     * @param articleSearchDto:
     * @Return: null
     * @Author: DengYinzhe
     * @Description: TODO 文章分页查询
     * @Date: 2023/3/26 19:22
     */
    @PostMapping("articleSearch")
    public Result<Page<Article>> article(@RequestBody ArticleSearchDto articleSearchDto) throws IOException {
        log.info(articleSearchDto.toString());
        Integer pageNum = articleSearchDto.getPageNum();
        Integer pageSize = articleSearchDto.getPageSize();
        SearchRequest request = new SearchRequest.Builder()
                //去哪个索引里搜索
                .index("article")
                .query(
                        QueryBuilders.bool().filter(
                                        QueryBuilders.bool().should(
                                                QueryBuilders
                                                        .term()
                                                        .field("articleTitle")
                                                        .value(String.format("%s", articleSearchDto.getSearch()))
                                                        .build()._toQuery(),
                                                QueryBuilders
                                                        .wildcard()
                                                        .field("articleTitle")
                                                        .value(String.format("*%s*", articleSearchDto.getSearch()))
                                                        .build()._toQuery(),
                                                QueryBuilders
                                                        .wildcard()
                                                        .field("articleAuthor")
                                                        .value(String.format("*%s*", articleSearchDto.getSearch()))
                                                        .build()._toQuery()
                                        ).build()._toQuery(),
                                        QueryBuilders
                                                .range()
                                                .field("wordNumber")
                                                .gte(JsonData.fromJson(String.valueOf(articleSearchDto.getCountLow())))
                                                .lte(JsonData.fromJson(String.valueOf(articleSearchDto.getCountUp())))
                                                .build()._toQuery(),
                                        QueryBuilders
                                                .range()
                                                .field("articleCreateTime")
                                                .gte(JsonData.fromJson(String.valueOf(articleSearchDto.getBeginTime())))
                                                .lte(JsonData.fromJson(String.valueOf(articleSearchDto.getEndTime())))
                                                .build()._toQuery()
                                )
                                .build()
                                ._toQuery()
                )
                .sort(
                        SortOptionsBuilders.field(f -> f.field("articleCreateTime").order(SortOrder.Desc)),
                        SortOptionsBuilders.field(f -> f.field("wordNumber").order(SortOrder.Desc)))
//                .source(sourceConfigBuilders ->
//                        sourceConfigBuilders
//                                .filter(sourceFilterBuilder ->
//                                        sourceFilterBuilder
//                                                .excludes("content")
//                                )
//                )
                .from((pageNum - 1) * pageSize)
                .size(pageSize)
                .build();
        List<Article> list = new ArrayList<>();
        List<Hit<Article>> hits = elasticsearchClient.search(request, Article.class).hits().hits();
        for (Hit<Article> hit : hits) {
            list.add(hit.source());
        }
        long total = Objects.requireNonNull(elasticsearchClient.search(request, Article.class).hits().total()).value();
        Page<Article> pageInfo = new Page<>(pageNum, pageSize, total);
        pageInfo.setRecords(list);
        return Result.success(pageInfo);
    }

    /**
     * @param articleId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 根据文章id获取文章
     * @Date: 2023/3/29 0:21
     */
    @GetMapping("article/{articleId}")
    public Result<Article> article(@PathVariable Long articleId) throws IOException {
        SearchRequest request = new SearchRequest.Builder()
                //去哪个索引里搜索
                .index("article")
                .query(
                        QueryBuilders
                                .term()
                                .field("articleId")
                                .value(articleId)
                                .build()._toQuery()
                )
                .size(1)
                .build();
        List<Hit<Article>> hits = elasticsearchClient.search(request, Article.class).hits().hits();
        Article article = 1 == hits.size() ? hits.get(0).source() : null;

        if (article == null) {
            return Result.error("该文章不存在");
        } else {
            /*修改文章练习人数*/
            article.setArticleStudyNumber(article.getArticleStudyNumber() + 1);
            elasticsearchClient
                    .update(e -> e.index("article")
                            .id(article.getArticleId())
                            .doc(article), Article.class);
            return Result.success(article);
        }


    }

}

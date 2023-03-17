package com.boot.controller;

import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch.core.IndexResponse;
import com.boot.common.result.Result;
import com.boot.entity.Article;
import com.boot.utils.SnowFlakeUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import java.io.IOException;
import java.time.LocalDateTime;

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
public class ArticleController {

    @Resource
    private ElasticsearchClient elasticsearchClient;

    /**
     * @param article:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 创建文章
     * @Date: 2023/3/17 16:27
     */
    @PostMapping("article")
    public Result article(@RequestBody Article article) throws IOException {

        article.setArticleId(SnowFlakeUtil.getNextId()).setArticleCreateTime(LocalDateTime.now());

        //这是一个同步请求，请求会卡在这里
        IndexResponse response = elasticsearchClient.index(i -> i.index("article").document(article).id(String.valueOf(article.getArticleId())));
        log.info(response.result().jsonValue());
        if ("created".equals(response.result().jsonValue())) {
            return Result.success("插入成功");
        } else {
            return Result.error("插入失败");
        }

    }


    @DeleteMapping("article/{articleId}")
    public Result article(@PathVariable Long articleId) {

        return Result.success();
    }

    @PutMapping("article")
    public Result articleChange(@RequestBody Article article) throws IOException {

        return Result.success();
    }

    @GetMapping("article/{pageNum}/{pageSize}/{countKind}/{title}")
    public Result article(
            @PathVariable Integer pageNum,
            @PathVariable Integer pageSize,
            @PathVariable(required = false) Integer countKind,
            @PathVariable(required = false) String title) {

        return Result.success();
    }
}

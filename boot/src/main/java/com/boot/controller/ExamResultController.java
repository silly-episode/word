package com.boot.controller;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.boot.bo.WordPlan;
import com.boot.common.result.Result;
import com.boot.dto.Question;
import com.boot.entity.ExamResult;
import com.boot.service.ExamResultService;
import com.boot.utils.JwtUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.*;


/**
 * (ExamResult)表控制层
 *
 * @author makejava
 * @since 2023-02-20 14:22:15
 */
@RestController
@RequestMapping("examResult")
//@RequiresAuthentication
public class ExamResultController {
    /**
     * 服务对象
     */
    @Resource
    private ExamResultService examResultService;
    @Resource
    private ElasticsearchClient elasticsearchClient;
    @Resource
    private JwtUtils jwtUtils;

    /**
     * @param examResult:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 录入成绩，传入grade和planId
     * @Date: 2023/2/20 14:38
     */
    @PostMapping("examResult")
    public Result examResult(@RequestBody ExamResult examResult, HttpServletRequest request) {
        Long userId = jwtUtils.getUserIdFromRequest(request);
        if (userId == null) {
            return null;
        }
        examResult
                .setExamTime(LocalDateTime.now())
                .setUserId(userId);
        if (examResultService.save(examResult)) {
            return Result.success("录入成绩成功");
        } else {
            return Result.error("录入成绩失败");
        }
    }

    /**
     * @param userId:
     * @param beginTime:
     * @param endTime:
     * @param planId:
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 查询用户成绩，时间条件，计划id （平均分，最高分，最低分）（模块名称，考试时间，考试成绩，是否合格）
     * @Date: 2023/2/23 20:29
     */
    @GetMapping("examResult/{userId}/{beginTime}/{endTime}/{planId}")
    public Result examResult(
            @PathVariable() Long userId,
            @PathVariable(required = false) LocalDate beginTime,
            @PathVariable(required = false) LocalDate endTime,
            @PathVariable(required = false) Long planId) {


        return Result.success();
    }


    /**
     * @Return:
     * @Author: DengYinzhe
     * @Description: TODO 生成考试题库
     * @Date: 2023/4/21 15:58
     */
    @PostMapping("questionBank")
    public Result questionBank(@RequestBody WordPlan wordPlan) throws IOException {

        /*确认选词的序列范围*/
        int bankTotal = wordPlan.getAllWord();
        int choiceCount = 4;
        int start = wordPlan.getFinishedWord() + 1;
        int end = start + wordPlan.getDayWord() - 1;
        if (end > bankTotal) {
            end = bankTotal;
        }

        /*返回结果*/
        List<Question> questionList = new ArrayList<>(wordPlan.getDayWord());
        for (int i = start; i <= end; i++) {
            /*生成题目随机数*/
            Set<Integer> set = new HashSet<>(choiceCount - 1);
            Random ran = new Random();
            while (set.size() != choiceCount - 1) {
                int r = ran.nextInt(Math.toIntExact(bankTotal)) + 1;
                if (r != i) {
                    set.add(r);
                }
            }

            /*查找非正确答案选项信息*/
            Integer answer = ran.nextInt(Math.toIntExact(4));
            List<FieldValue> list = new ArrayList<FieldValue>();
            for (Integer integer : set) {
                list.add(FieldValue.of(String.valueOf(integer)));
            }
            Question question = new Question(choiceCount, answer);
            SearchRequest request = new SearchRequest.Builder()
                    .index(wordPlan.getEsIndex())
                    .query(
                            QueryBuilders.bool().filter(
                                    QueryBuilders
                                            .terms()
                                            .field("wordRank")
                                            .terms(builder -> builder.value(list))
                                            .build()._toQuery()
                            ).build()._toQuery()
                    )
                    .size(3)
                    .build();
            List<Hit<ObjectNode>> hits = elasticsearchClient.search(request, ObjectNode.class).hits().hits();
            for (Hit<ObjectNode> hit : hits) {
                question.getChoice().add(hit.source());
            }

            /*查找正确答案选项信息*/
            SearchRequest requestAnswer = new SearchRequest.Builder()
                    .index(wordPlan.getEsIndex())
                    .query(
                            QueryBuilders.bool().filter(
                                    QueryBuilders
                                            .term()
                                            .field("wordRank")
                                            .value(i)
                                            .build()._toQuery()
                            ).build()._toQuery()
                    )
                    .size(1)
                    .build();
            List<Hit<ObjectNode>> answerHit = elasticsearchClient.search(requestAnswer, ObjectNode.class).hits().hits();
            for (Hit<ObjectNode> hit : answerHit) {
                question.getChoice().add(answer, hit.source());
            }
            questionList.add(question);
        }


        return Result.success(questionList);
    }
}


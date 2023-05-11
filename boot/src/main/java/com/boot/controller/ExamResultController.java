package com.boot.controller;


import co.elastic.clients.elasticsearch.ElasticsearchClient;
import co.elastic.clients.elasticsearch._types.FieldValue;
import co.elastic.clients.elasticsearch._types.query_dsl.QueryBuilders;
import co.elastic.clients.elasticsearch.core.SearchRequest;
import co.elastic.clients.elasticsearch.core.search.Hit;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.boot.bo.WordPlan;
import com.boot.common.result.Result;
import com.boot.dto.ArticleResult;
import com.boot.dto.GameResult;
import com.boot.dto.PlanResult;
import com.boot.dto.Question;
import com.boot.entity.ExamResult;
import com.boot.entity.Plan;
import com.boot.entity.WordModule;
import com.boot.service.ExamResultService;
import com.boot.service.PlanService;
import com.boot.service.WordModuleService;
import com.boot.utils.BeanDtoVoUtils;
import com.boot.utils.JwtUtils;
import com.fasterxml.jackson.databind.node.ObjectNode;
import org.apache.shiro.authz.annotation.RequiresAuthentication;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
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
@RequiresAuthentication
public class ExamResultController {
    /**
     * 服务对象
     */
    @Resource
    private ExamResultService examResultService;
    @Resource
    private ElasticsearchClient elasticsearchClient;
    @Resource
    private PlanService planService;
    @Resource
    private WordModuleService wordModuleService;
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
     * @Return: Result
     * @Author: DengYinzhe
     * @Description: TODO 查询用户成绩，时间条件，计划id （平均分，最高分，最低分）（模块名称，考试时间，考试成绩，是否合格）
     * @Date: 2023/2/23 20:29
     */
    @GetMapping("examResult")
    public Result examResult(HttpServletRequest request) {
        long userId = jwtUtils.getUserIdFromRequest(request);
        LambdaQueryWrapper<ExamResult> queryWrapper = new LambdaQueryWrapper<>();
        int recordSize = 50;
        int[] recordIndex = new int[recordSize];
        String lastSql = " limit " + recordSize;
        List<ExamResult> oldList;
        List<PlanResult> planResults = new ArrayList<>(recordSize);
        List<ArticleResult> articleResults = new ArrayList<>(recordSize);
        List<GameResult> gameResults = new ArrayList<>(recordSize);

        /*横轴*/
        for (int i = 1; i <= recordSize; i++) {
            recordIndex[i - 1] = i;
        }

        /*考试*/
        queryWrapper
                .eq(ExamResult::getUserId, userId)
                .eq(ExamResult::getResultType, "0")
                .orderByDesc(ExamResult::getExamTime)
                .last(lastSql);
        oldList = examResultService.list(queryWrapper);
        for (ExamResult result : oldList) {
            String planName = "";
            String moduleName = "";
            PlanResult planResult = BeanDtoVoUtils.convert(result, PlanResult.class);
            Plan plan = planService.getById(result.getPlanId());
            if (plan != null) {
                planName = plan.getPlanName();
                WordModule wordModule = wordModuleService.getById(plan.getModuleId());
                if (wordModule != null) {
                    moduleName = wordModule.getModuleName();
                }
            }
            planResult
                    .setValue(Double.valueOf(result.getGrade()))
                    .setPlanName(planName)
                    .setModuleName(moduleName);
            planResults.add(planResult);
        }
        Collections.reverse(planResults);

        /*文章*/
        queryWrapper.clear();
        queryWrapper
                .eq(ExamResult::getUserId, userId)
                .eq(ExamResult::getResultType, "1")
                .orderByDesc(ExamResult::getExamTime)
                .last(lastSql);
        oldList = examResultService.list(queryWrapper);
        for (ExamResult result : oldList) {
            ArticleResult articleResult = BeanDtoVoUtils.convert(result, ArticleResult.class);
            articleResult.setValue(result.getVelocity());
            articleResults.add(articleResult);
        }
        Collections.reverse(articleResults);

        /*游戏*/
        queryWrapper.clear();
        queryWrapper
                .eq(ExamResult::getUserId, userId)
                .eq(ExamResult::getResultType, "2")
                .orderByDesc(ExamResult::getExamTime)
                .last(lastSql);
        oldList = examResultService.list(queryWrapper);
        for (ExamResult result : oldList) {
            GameResult gameResult = BeanDtoVoUtils.convert(result, GameResult.class);
            gameResult.setValue(Double.valueOf(result.getScore()));
            gameResults.add(gameResult);
        }
        Collections.reverse(gameResults);

        Map<String, Object> map = new HashMap<>(3);
        map.put("planResults", planResults);
        map.put("articleResults", articleResults);
        map.put("gameResults", gameResults);
        map.put("recordIndex", recordIndex);
        map.put("endIndex", Math.max(planResults.size(), Math.max(gameResults.size(), articleResults.size())));
        return Result.success(map);
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
            int answer = ran.nextInt(Math.toIntExact(4));
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


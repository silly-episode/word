package com.boot.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/14 16:07
 * @FileName: Question
 * @Description: 题目
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
@SuppressWarnings("all")
public class Question {

    private int quesionIndex;

    private String word;

    private List<String> questionList;

    private String answer;
}

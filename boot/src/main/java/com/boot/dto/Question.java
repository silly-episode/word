package com.boot.dto;

import com.fasterxml.jackson.databind.node.ObjectNode;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

/**
 * @Project: word
 * @Author: DengYinzhe
 * @Date: 2023/4/21 16:10
 * @FileName: QuestionList
 * @Description: 词义选择的List
 */
@Data
@AllArgsConstructor
@NoArgsConstructor
public class Question {

    private Integer answer;

    private List<ObjectNode> choice;

    public Question(int size, Integer answer) {
        this.choice = new ArrayList<ObjectNode>(size);
        this.answer = answer;
    }

}

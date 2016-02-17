package com.applicationcourse.mobile.graphmaster;

import java.util.List;

/**
 * Created by teresa on 16/02/16.
 */
public class CGQuestion {
    private int id;
    private String question;
    private String type;
    private List<String> xValue;
    private List<String> yValue;
    private List<CGSubQuestion> subQuestionList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public List<String> getyValue() {
        return yValue;
    }

    public void setyValue(List<String> yValue) {
        this.yValue = yValue;
    }

    public List<String> getxValue() {
        return xValue;
    }

    public void setxValue(List<String> xValue) {
        this.xValue = xValue;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public List<CGSubQuestion> getSubQuestionList() {
        return subQuestionList;
    }

    public void setSubQuestionList(List<CGSubQuestion> subQuestionList) {
        this.subQuestionList = subQuestionList;
    }
}
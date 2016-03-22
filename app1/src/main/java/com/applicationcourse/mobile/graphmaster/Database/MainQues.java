package com.applicationcourse.mobile.graphmaster.Database;

import java.util.List;

/**
 * Created by teresa on 16/02/16.
 */
public class MainQues {
    private long mqId;
    private String question;
    private String type;
    private String function;
    private String grade;
    private List<MainQuesHeading> mainQuesHeadList;
    private List<SubQuestion> subQuestionList;

    //get value from database
    public MainQues(long id, String question, String type, String function, String grade) {
        this.mqId = id;
        this.question = question;
        this.type = type;
        this.function = function;
        this.grade = grade;
    }
    //save into database
    public MainQues(String question, String type, String function, String grade){
        this.question = question;
        this.type = type;
        this.function = function;
        this.grade = grade;
    }
    //empty constructor
    public MainQues(){

    }

    public long getMqId() {
        return mqId;
    }

    public void setMqId(long mqId) {
        this.mqId = mqId;
    }

    public String getQuestion() {
        return question;
    }

    public void setQuestion(String question) {
        this.question = question;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getFunction() {
        return function;
    }

    public void setFunction(String function) {
        this.function = function;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }

    public List<MainQuesHeading> getMainQuesHeadList() {
        return mainQuesHeadList;
    }

    public void setMainQuesHeadList(List<MainQuesHeading> mainQuesHeadList) {
        this.mainQuesHeadList = mainQuesHeadList;
    }
    public List<SubQuestion> getSubQuestionList() {
        return subQuestionList;
    }

    public void setSubQuestionList(List<SubQuestion> subQuestionList) {
        this.subQuestionList = subQuestionList;
    }

}
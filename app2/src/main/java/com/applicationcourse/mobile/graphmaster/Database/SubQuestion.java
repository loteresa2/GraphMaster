package com.applicationcourse.mobile.graphmaster.Database;

import java.util.List;

/**
 * Created by teresa on 17/02/16.
 */
public class SubQuestion {
    private long subQuesId;
    private String type;
    private String function;
    private String subQuestion;
    private String optionType;
    private List<Options> optionsList;
    private List<String> answerList;
    private List<String> explainList;

    //get from database
    public SubQuestion(long id, String type, String function,String subQuestion, String optionType) {
        this.subQuesId = id;
        this.subQuestion = subQuestion;
        this.type = type;
        this.function = function;
        this.optionType = optionType;
    }

    //save into database
    public SubQuestion( String type, String function,String subQuestion, String optionType){
        this.subQuestion = subQuestion;
        this.type = type;
        this.function = function;
        this.optionType = optionType;
    }

    //empty constructor
    public SubQuestion(){

    }

    public long getSubQuesId() {
        return subQuesId;
    }

    public void setSubQuesId(long subQuesId) {
        this.subQuesId = subQuesId;
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

    public String getSubQuestion() {
        return subQuestion;
    }

    public void setSubQuestion(String subQuestion) {
        this.subQuestion = subQuestion;
    }

    public String getOptionType() {
        return optionType;
    }

    public void setOptionType(String optionType) {
        this.optionType = optionType;
    }

    public List<Options> getOptionsList() {
        return optionsList;
    }

    public void setOptionsList(List<Options> optionsList) {
        this.optionsList = optionsList;
    }

    public List<String> getAnswerList() {
        return answerList;
    }

    public void setAnswerList(List<String> answerList) {
        this.answerList = answerList;
    }

    public List<String> getExplainList() {
        return explainList;
    }

    public void setExplainList(List<String> explainList) {
        this.explainList = explainList;
    }
}

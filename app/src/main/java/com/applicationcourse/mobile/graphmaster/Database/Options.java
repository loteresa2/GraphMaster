package com.applicationcourse.mobile.graphmaster.Database;

/**
 * Created by teresa on 17/02/16.
 */
public class Options {
    long mqId;
    long subQuesId;
    long optionId;
    String optionValue;
    String answer;
    String explanation;

    //get from database
    public Options(long mqId, long subQuesId, long OptionId, String optionValue, String answer, String explanation) {
        this.mqId = mqId;
        this.subQuesId = subQuesId;
        this.optionId = OptionId;
        this.optionValue = optionValue;
        this.answer = answer;
        this.explanation = explanation;
    }

    //save into database
    public Options(long mqId, long subQuesId, String optionValue, String answer, String explanation){
        this.mqId = mqId;
        this.subQuesId = subQuesId;
        this.optionValue = optionValue;
        this.answer = answer;
        this.explanation = explanation;
    }

    //EMPTY constructor
    public Options(){

    }

    public String getAnswer() {
        return answer;
    }

    public String getExplanation() {
        return explanation;
    }

    public long getMqId() {
        return mqId;
    }

    public long getOptionId() {
        return optionId;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public long getSubQuesId() {
        return subQuesId;
    }

    public void setAnswer(String answer) {
        this.answer = answer;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }

    public void setMqId(long mqId) {
        this.mqId = mqId;
    }

    public void setOptionId(long optionId) {
        this.optionId = optionId;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public void setSubQuesId(long subQuesId) {
        this.subQuesId = subQuesId;
    }

    @Override
    public String toString() {
        return "Options{" +
                "mqId=" + mqId +
                ", subQuesId=" + subQuesId +
                ", optionId=" + optionId +
                ", optionValue='" + optionValue + '\'' +
                ", answer='" + answer + '\'' +
                ", explanation='" + explanation + '\'' +
                '}';
    }
}

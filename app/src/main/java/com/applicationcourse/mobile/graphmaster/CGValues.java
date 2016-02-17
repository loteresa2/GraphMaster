package com.applicationcourse.mobile.graphmaster;

/**
 * Created by teresa on 17/02/16.
 */
public class CGValues {
    int mainQuestionNo;
    int subQuestionNo;
    String optionValue;
    Boolean answer;
    String explanation;

    public int getMainQuestionNo() {
        return mainQuestionNo;
    }

    public void setMainQuestionNo(int mainQuestionNo) {
        this.mainQuestionNo = mainQuestionNo;
    }

    public int getSubQuestionNo() {
        return subQuestionNo;
    }

    public void setSubQuestionNo(int subQuestionNo) {
        this.subQuestionNo = subQuestionNo;
    }

    public String getOptionValue() {
        return optionValue;
    }

    public void setOptionValue(String optionValue) {
        this.optionValue = optionValue;
    }

    public Boolean getAnswer() {
        return answer;
    }

    public void setAnswer(Boolean answer) {
        this.answer = answer;
    }

    public String getExplanation() {
        return explanation;
    }

    public void setExplanation(String explanation) {
        this.explanation = explanation;
    }
}

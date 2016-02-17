package com.applicationcourse.mobile.graphmaster;

import java.util.List;

/**
 * Created by teresa on 17/02/16.
 */
public class CGSubQuestion {
    private int id;
    private String subQuestion;
    private List<CGValues> cgValuesList;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getSubQuestion() {
        return subQuestion;
    }

    public void setSubQuestion(String subQuestion) {
        this.subQuestion = subQuestion;
    }

    public List<CGValues> getCgValuesList() {
        return cgValuesList;
    }

    public void setCgValuesList(List<CGValues> cgValuesList) {
        this.cgValuesList = cgValuesList;
    }
}

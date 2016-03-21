package com.applicationcourse.mobile.graphmaster.Database;

/**
 * Created by teresa on 04/03/16.
 */
public class Progress {
    String dateTime;
    int studId;
    String funcType;
    int level;
    int attempCount;
    String timeTaken;
    float no_wrong_ans;

    public Progress(String dateTime, int studId, String funcType, int level, String timeTaken, float no_wrong_ans) {
        this.dateTime = dateTime;
        this.studId = studId;
        this.funcType = funcType;
        this.level = level;
        this.timeTaken = timeTaken;
        this.no_wrong_ans = no_wrong_ans;
    }
    public Progress(String dateTime, int studId, String funcType, int level, int attempCount, String timeTaken, float no_wrong_ans) {
        this.dateTime = dateTime;
        this.studId = studId;
        this.funcType = funcType;
        this.level = level;
        this.timeTaken = timeTaken;
        this.no_wrong_ans = no_wrong_ans;
        this.attempCount = attempCount;
    }

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public int getStudId() {
        return studId;
    }

    public void setStudId(int studId) {
        this.studId = studId;
    }

    public String getFuncType() {
        return funcType;
    }

    public void setFuncType(String funcType) {
        this.funcType = funcType;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public int getAttempCount() {
        return attempCount;
    }

    public void setAttempCount(int attempCount) {
        this.attempCount = attempCount;
    }

    public String getTimeTaken() {
        return timeTaken;
    }

    public void setTimeTaken(String timeTaken) {
        this.timeTaken = timeTaken;
    }

    public float getNo_wrong_ans() {
        return no_wrong_ans;
    }

    public void setNo_wrong_ans(int no_wrong_ans) {
        this.no_wrong_ans = no_wrong_ans;
    }

    @Override
    public String toString() {
        return "Progress{" +
                "dateTime='" + dateTime + '\'' +
                ", studId=" + studId +
                ", funcType='" + funcType + '\'' +
                ", level=" + level +
                ", attempCount=" + attempCount +
                ", timeTaken='" + timeTaken + '\'' +
                ", no_wrong_ans=" + no_wrong_ans +
                '}';
    }
}

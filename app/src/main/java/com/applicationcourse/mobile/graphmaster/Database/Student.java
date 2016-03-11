package com.applicationcourse.mobile.graphmaster.Database;

/**
 * Created by teresa on 04/03/16.
 */
public class Student {
    int studId;
    String name;
    int teachId;
    String functType;
    int levelReached;

    public Student(int studId, String name, int teachId, String functType, int levelReached ) {
        this.studId = studId;
        this.levelReached = levelReached;
        this.name = name;
        this.teachId = teachId;
        this.functType = functType;
    }

    public Student(String name, int teachId, String functType, int levelReached) {
        this.name = name;
        this.teachId = teachId;
        this.functType = functType;
        this.levelReached = levelReached;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getStudId() {
        return studId;
    }

    public void setStudId(int studId) {
        this.studId = studId;
    }

    public int getTeachId() {
        return teachId;
    }

    public void setTeachId(int teachId) {
        this.teachId = teachId;
    }

    public String getFunctType() {
        return functType;
    }

    public void setFunctType(String functType) {
        this.functType = functType;
    }

    public int getLevelReached() {
        return levelReached;
    }

    public void setLevelReached(int levelReached) {
        this.levelReached = levelReached;
    }
}

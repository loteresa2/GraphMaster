package com.applicationcourse.mobile.graphmaster.Database;

/**
 * Created by teresa on 20/03/16.
 */
public class Marks {
    long subQId;
    float weight;

    public Marks(float weight) {
        this.weight = weight;
    }

    public Marks(long subQId, float weight) {
        this.subQId = subQId;
        this.weight = weight;
    }

    public long getSubQId() {
        return subQId;
    }

    public void setSubQId(long subQId) {
        this.subQId = subQId;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}

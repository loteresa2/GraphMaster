package com.applicationcourse.mobile.graphmaster.Database;

/**
 * Created by teresa on 23/02/16.
 */
public class MainQuesHData {
    private long mqId;
    private long hId;
    private long ordering;
    private String data;

    //get from database
    public MainQuesHData(long MainID, long HeadingID, long Order, String Data){
        this.mqId = MainID;
        this.hId = HeadingID;
        this.ordering = Order;
        this.data = Data;
    }

    //save into database
    public MainQuesHData( String Data){
        this.data = Data;
    }

    //empty constructor
    public MainQuesHData(){

    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public long gethId() {
        return hId;
    }

    public void sethId(long hId) {
        this.hId = hId;
    }

    public long getOrdering() {
        return ordering;
    }

    public void setOrdering(long ordering) {
        this.ordering = ordering;
    }

    public long getMqId() {
        return mqId;
    }

    public void setMqId(long mqId) {
        this.mqId = mqId;
    }
}

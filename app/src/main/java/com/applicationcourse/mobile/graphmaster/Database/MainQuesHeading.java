package com.applicationcourse.mobile.graphmaster.Database;

import java.util.List;

/**
 * Created by teresa on 23/02/16.
 */
public class MainQuesHeading {
    private long mqId;
    private long hId;
    String axis;
    String heading;
    List<MainQuesHData> mainQuesHDataList;
    //get from database
    public MainQuesHeading(long MainID, String axis, String Heading){
        this.axis = axis;
        this.mqId = MainID;
        this.heading = Heading;
    }
    public MainQuesHeading(long MainID,long hId, String axis, String Heading){
        this.axis = axis;
        this.mqId = MainID;
        this.hId = hId;
        this.heading = Heading;
    }
    //empty constructor
    public MainQuesHeading(){

    }

    public List<MainQuesHData> getMainQuesHDataList() {
        return mainQuesHDataList;
    }

    public void setMainQuesHDataList(List<MainQuesHData> mainQuesHDataList) {
        this.mainQuesHDataList = mainQuesHDataList;
    }

    public long getMqId() {
        return mqId;
    }

    public void setMqId(long mqId) {
        this.mqId = mqId;
    }

    public long gethId() {
        return hId;
    }

    public void sethId(long hId) {
        this.hId = hId;
    }

    public String getAxis() {
        return axis;
    }

    public void setAxis(String axis) {
        this.axis = axis;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }
}

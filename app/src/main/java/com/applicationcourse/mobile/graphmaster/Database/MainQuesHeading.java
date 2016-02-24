package com.applicationcourse.mobile.graphmaster.Database;

import java.util.List;

/**
 * Created by teresa on 23/02/16.
 */
public class MainQuesHeading {
    private long hId;
    private long mqId;
    String heading;
    List<MainQuesHData> mainQuesHDataList;
    //get from database
    public MainQuesHeading(long HeadingID, long MainID, String Heading){
        this.hId = HeadingID;
        this.mqId = MainID;
        this.heading = Heading;
    }

    //put into database
    public MainQuesHeading( long MainID, String Heading){
        this.mqId = MainID;
        this.heading = Heading;
    }

    //empty constructor
    public MainQuesHeading(){

    }

    public long gethId() {
        return hId;
    }

    public void sethId(long hId) {
        this.hId = hId;
    }

    public String getHeading() {
        return heading;
    }

    public void setHeading(String heading) {
        this.heading = heading;
    }

    public long getMqId() {
        return mqId;
    }

    public void setMqId(long mqId) {
        this.mqId = mqId;
    }

    public List<MainQuesHData> getMainQuesHDataList() {
        return mainQuesHDataList;
    }

    public void setMainQuesHDataList(List<MainQuesHData> mainQuesHDataList) {
        this.mainQuesHDataList = mainQuesHDataList;
    }
}

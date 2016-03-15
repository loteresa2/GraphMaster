package com.applicationcourse.mobile.graphmaster.Database;

/**
 * Created by Aiping Xiao on 2016-03-14.
 */
public class Help {
    long mid;
    long sid;
    String type;
    String value;
    byte[] image;

    public Help(){

    }

    public Help(long mid,long sid,String type,String value,byte[] image){
        this.mid = mid;
        this.sid = sid;
        this.type = type;
        this.value = value;
        this.image = image;
    }

    public byte[] getImage() {
        return image;
    }

    public long getMid() {
        return mid;
    }

    public long getSid() {
        return sid;
    }

    public String getType() {
        return type;
    }

    public String getValue() {
        return value;
    }

    public void setImage(byte[] image) {
        this.image = image;
    }

    public void setMid(long mid) {
        this.mid = mid;
    }

    public void setSid(long sid) {
        this.sid = sid;
    }

    public void setType(String type) {
        this.type = type;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
package com.example.hu.a20180126;

import org.litepal.annotation.Column;
import org.litepal.crud.DataSupport;

/**
 * Created by hu on 2018/1/26.
 */

public class Message extends DataSupport{
    @Column(nullable = false)
    String phoneNumber;
    @Column(nullable = false)
    String content;
    String tag;
    String place;

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }
}

package com.company.project.bean;

import androidx.annotation.NonNull;

import java.util.Locale;

/**
 * @author Tinlone
 * @date 2018/3/26.
 */

public class AdvertisementBean {

    private int id;
    private String picture;
    private int millisecond;
    private String app;
    private int type;
    private int status;
    private String title;
    private String url;
    private String createdTime;
    private String startTime;
    private String endTime;

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public int getMillisecond() {
        return millisecond;
    }

    public void setMillisecond(int millisecond) {
        this.millisecond = millisecond;
    }

    public String getApp() {
        return app;
    }

    public void setApp(String app) {
        this.app = app;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCreatedTime() {
        return createdTime;
    }

    public void setCreatedTime(String createdTime) {
        this.createdTime = createdTime;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @NonNull
    @Override
    public String toString() {
        return String.format(Locale.CHINA, "AdvertisementBean{id=%d, picture='%s', millisecond=%d, app='%s', type=%d, status=%d, title='%s', url='%s', createdTime='%s', startTime='%s', endTime='%s'}", id, picture, millisecond, app, type, status, title, url, createdTime, startTime, endTime);
    }
}

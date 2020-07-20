package com.company.project.adapter.base.bean;

public class BaseListBean<T> {
    public String type;
    public String subType;
    public T data;

    public String getType() {
        return type;
    }

    public String getSubType() {
        return subType;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}

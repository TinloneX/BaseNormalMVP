package com.company.project.adapter.base.bean;

import com.company.project.adapter.TemplateType;

/**
 *
 * @param <T>
 */
public class BaseListBean<T> {
    @TemplateType.Type
    public String type;
    @TemplateType.SubType
    public String subType;
    public T data;

    public void setData(T data) {
        this.data = data;
    }
}

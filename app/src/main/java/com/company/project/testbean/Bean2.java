package com.company.project.testbean;

import com.company.project.adapter.base.bean.IBean;

public class Bean2 implements IBean {

    public String name;

    public int color;

    public Bean2(String name, int color) {
        this.name = name;
        this.color = color;
    }

}

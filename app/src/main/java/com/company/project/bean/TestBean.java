package com.company.project.bean;

/**
 * @author EDZ
 * @date 2018/3/23.
 */

public class TestBean {

    private String title;
    private String lrcContent;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLrcContent() {
        return lrcContent;
    }

    public void setLrcContent(String lrcContent) {
        this.lrcContent = lrcContent;
    }

    @Override
    public String toString() {
        return "TestBean{" +
                "title='" + title + '\'' +
                ", lrcContent='" + lrcContent + '\'' +
                '}';
    }
}

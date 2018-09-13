package com.company.project.base;

import java.util.List;

public class ListResponse<ITEM> extends BaseResponse<List<ITEM>> {

    private int totalCount;

    public int getTotalCount() {
        return totalCount;
    }

    public void setTotalCount(int totalCount) {
        this.totalCount = totalCount;
    }
}

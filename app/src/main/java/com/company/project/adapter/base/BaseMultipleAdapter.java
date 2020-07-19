package com.company.project.adapter.base;

import com.company.project.adapter.base.bean.BaseListBean;

/**
 *  非第一原作者
 *  MultipleRecyclerViewAdapter抽象类
 */
public abstract class BaseMultipleAdapter<T extends BaseListBean> extends BaseRecyclerViewAdapter<T> {
    @Override
    public boolean hasViewType() {
        return true;
    }

    @Override
    public int multipleView(int position) {
        return getMultipleView(position);
    }

    public abstract int getMultipleView(int position);
}

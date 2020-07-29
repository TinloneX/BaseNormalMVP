package com.company.project.adapter.temp;

import android.content.Context;

import com.company.project.R;
import com.company.project.adapter.base.CommonViewHolder;
import com.company.project.adapter.base.bean.BaseListBean;


/**
 * 数据错误是的默认模板
 */
public class TemplateDefaultAdapter<T> extends BaseTemplateAdapter<T> {
    public TemplateDefaultAdapter(Context context) {
        super(context);
    }

    /**
     * 返回item布局id
     */
    @Override
    protected int getItemLayoutId(BaseListBean<T> info) {
        return R.layout.template_default_item;
    }

    @Override
    protected void onInitView(CommonViewHolder holder, BaseListBean<T> item) {
    }

    @Override
    protected void setDataToHolder(CommonViewHolder holder, BaseListBean<T> item) {
    }
}
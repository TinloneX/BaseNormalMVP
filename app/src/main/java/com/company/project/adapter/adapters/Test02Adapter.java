package com.company.project.adapter.adapters;

import android.content.Context;
import android.widget.TextView;

import com.company.project.R;
import com.company.project.adapter.base.CommonViewHolder;
import com.company.project.adapter.base.bean.BaseListBean;
import com.company.project.adapter.temp.BaseTemplateAdapter;
import com.company.project.testbean.Bean2;

public class Test02Adapter extends BaseTemplateAdapter<Bean2> {

    private TextView tvTitle;

    public Test02Adapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutId(BaseListBean<Bean2> item) {
        return R.layout.layout_test_type_0102;
    }

    @Override
    protected void onInitView(CommonViewHolder holder, BaseListBean<Bean2> item) {
        if (item == null){
            return;
        }
        tvTitle = holder.getView(R.id.tv_title);
        tvTitle.setTextColor(item.data.color);
    }

    @Override
    protected void setDataToHolder(CommonViewHolder holder, BaseListBean<Bean2> item) {
        if (item == null){
            return;
        }
        TextView tvContent = holder.getView(R.id.tv_content);
        tvContent.setText(item.data.name);
    }
}

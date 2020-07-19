package com.company.project.adapter.adapters;

import android.content.Context;
import android.widget.TextView;

import com.company.project.R;
import com.company.project.adapter.base.CommonViewHolder;
import com.company.project.adapter.base.bean.BaseListBean;
import com.company.project.adapter.temp.BaseTemplateAdapter;
import com.company.project.testbean.Bean1;

public class Test0101Adapter  extends BaseTemplateAdapter<Bean1> {

    private TextView tvTitle;

    public Test0101Adapter(Context context, String type) {
        super(context, type);
    }

    @Override
    protected int getItemLayoutId(BaseListBean<Bean1> item) {
        return R.layout.layout_test_type_0101;
    }

    @Override
    protected void onInitView(CommonViewHolder holder, BaseListBean<Bean1> item) {
        tvTitle = holder.getView(R.id.tv_title);
    }

    @Override
    protected void setDataToHolder(CommonViewHolder holder, BaseListBean<Bean1> item) {
        TextView tvContent = holder.getView(R.id.tv_content);
        tvContent.setBackgroundColor(item.data.color);
        tvContent.setText(item.data.name);
    }
}

package com.company.project.adapter.adapters;

import android.content.Context;
import android.view.ViewGroup;

import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.company.project.R;
import com.company.project.adapter.base.CommonViewHolder;
import com.company.project.adapter.base.bean.BaseListBean;
import com.company.project.adapter.temp.BaseTemplateAdapter;
import com.company.project.testbean.Bean2;
import com.company.project.util.DensityUtil;

import java.util.List;

public class Test03Adapter extends BaseTemplateAdapter<List<Bean2>> {

    public Test03Adapter(Context context) {
        super(context);
    }

    @Override
    protected int getItemLayoutId(BaseListBean<List<Bean2>> item) {
        return R.layout.layout_item_03;
    }

    @Override
    protected void onInitView(CommonViewHolder holder, BaseListBean<List<Bean2>> item) {
        if (item == null){
            return;
        }
        RecyclerView recyclerView = holder.getView(R.id.rv_template_sub_list);
        LinearLayoutManager lm = new LinearLayoutManager(mContext);
        lm.setOrientation(LinearLayoutManager.VERTICAL);
        recyclerView.setLayoutManager(lm);
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        recyclerView.setHasFixedSize(true);
        ViewGroup.LayoutParams params = recyclerView.getLayoutParams();
        int totalHeight = DensityUtil.dip2px(mContext, 85) * item.data.size();
        if (params == null) {
            params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, totalHeight);
        }
        params.height = totalHeight;
        recyclerView.setLayoutParams(params);
        Test0301Adapter adapter = new Test0301Adapter(mContext, item.data);
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void setDataToHolder(CommonViewHolder holder, BaseListBean<List<Bean2>> item) {
        if (item == null){
            return;
        }
        RecyclerView recyclerView = holder.getView(R.id.rv_template_sub_list);
        Test0301Adapter adapter = (Test0301Adapter) recyclerView.getAdapter();
        if (adapter != null) {
            adapter.notifyData(item.data);
        } else {
            adapter = new Test0301Adapter(mContext, item.data);
            adapter.notifyData(item.data);
            recyclerView.setAdapter(adapter);
        }
    }
}

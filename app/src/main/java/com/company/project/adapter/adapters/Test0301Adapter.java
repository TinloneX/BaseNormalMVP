package com.company.project.adapter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.ViewGroup;
import android.widget.TextView;

import com.company.project.R;
import com.company.project.adapter.base.BaseRecyclerViewAdapter;
import com.company.project.adapter.base.CommonViewHolder;
import com.company.project.testbean.Bean2;

import java.util.ArrayList;
import java.util.List;

public class Test0301Adapter extends BaseRecyclerViewAdapter<Bean2> {
    Context mContext;

    public Test0301Adapter(Context context, List<Bean2> data) {
        mContext = context;
        mList.clear();
        mList.addAll(data);
    }

    public void notifyData(List<Bean2> data) {
        if (mList == null) {
            mList = new ArrayList<>();
        }
        mList.clear();
        mList.addAll(data);
        notifyDataSetChanged();
    }

    @Override
    public void onBindViewHolderEvent(CommonViewHolder holder, int position) {
        if (mList == null || mList.size() <= position || mList.get(position) == null) {
            return;
        }
        Bean2 item = mList.get(position);
        holder.itemView.setBackgroundColor(item.color);
        TextView tvTitle = holder.getView(R.id.tv_title);
        TextView tvContent = holder.getView(R.id.tv_content);
        tvTitle.setText(R.string.item_in_sub_recyclerview);
        tvContent.setText(item.name);

    }

    @Override
    public CommonViewHolder onCreateViewHolderEvent(ViewGroup parent, int position) {
        return new CommonViewHolder(mContext, LayoutInflater.from(mContext).inflate(R.layout.layout_test_type_0102, parent, false));
    }

}

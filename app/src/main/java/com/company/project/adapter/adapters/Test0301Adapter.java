package com.company.project.adapter.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.company.project.R;
import com.company.project.testbean.Bean2;

import java.util.ArrayList;
import java.util.List;

public class Test0301Adapter extends RecyclerView.Adapter<Test0301Adapter.VH> {
    Context mContext;
    private List<Bean2> mData = new ArrayList<>();

    public Test0301Adapter(Context context, List<Bean2> data) {
        mContext = context;
        mData.clear();
        mData.addAll(data);
    }

    public void notifyData(List<Bean2> data) {
        if (mData == null) {
            mData = new ArrayList<>();
        }
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public VH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return new VH(LayoutInflater.from(mContext).inflate(R.layout.layout_test_type_0102, parent, false));
    }

    @Override
    public void onBindViewHolder(@NonNull VH holder, int position) {
        if (mData == null || mData.size() <= position || mData.get(position) == null) {
            return;
        }
        Bean2 item = mData.get(position);
        holder.itemView.setBackgroundColor(item.color);
        holder.tvTitle.setText(R.string.item_in_sub_recyclerview);
        holder.tvContent.setText(item.name);
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public static class VH extends RecyclerView.ViewHolder {
        private TextView tvTitle;
        private TextView tvContent;

        public VH(@NonNull View itemView) {
            super(itemView);
            tvTitle = itemView.findViewById(R.id.tv_title);
            tvContent = itemView.findViewById(R.id.tv_content);
        }

    }

}

package com.company.project.adapter;

import android.content.Intent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.company.project.R;
import com.company.project.activity.MainActivity;

import java.util.List;

/**
 * @author Tinlone
 * @date 2018/3/27.
 */

public class GuideAdapter extends BaseQuickAdapter<Integer, BaseViewHolder> {


    public GuideAdapter(@Nullable List<Integer> data) {
        super(R.layout.item_guide_image, data);
    }

    private int getItemPosition(Integer item) {
        return item != null && mData != null && !mData.isEmpty() ? mData.indexOf(item) : -1;
    }

    @Override
    protected void convert(BaseViewHolder helper, Integer item) {
        ImageView imageView = helper.getView(R.id.ivGuide);

        TextView tvToUse = helper.getView(R.id.tvToUse);
        if (getItemPosition(item) == mData.size() - 1) {
            tvToUse.setVisibility(View.VISIBLE);
            tvToUse.setOnClickListener(v -> mContext.startActivity(new Intent(mContext, MainActivity.class)));
        }
        imageView.setImageResource(item);
    }

}

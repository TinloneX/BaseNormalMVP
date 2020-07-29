package com.company.project.adapter.temp;

import android.content.Context;
import android.view.ViewGroup;

import com.company.project.R;
import com.company.project.adapter.base.BaseMultipleAdapter;
import com.company.project.adapter.base.CommonViewHolder;
import com.company.project.adapter.base.bean.BaseListBean;
import com.company.project.adapter.factorys.SubAdapterFactory;

import java.util.ArrayList;
import java.util.List;

/**
 * 通用模板Adapter
 * 修订，通过BaseListBean做数据封装约定类型，以支持多种数据类型
 */
public class TemplateAdapter extends BaseMultipleAdapter<BaseListBean> {
    private Context mContext;

    public TemplateAdapter(Context context) {
        mContext = context;
    }

    /**
     * 初始化item的view
     */
    @Override
    public CommonViewHolder onCreateViewHolderEvent(ViewGroup parent, int position) {
        BaseListBean item = getItem(position);
        BaseTemplateAdapter adapter = null;
        try {
            adapter = SubAdapterFactory.newTemplateAdapter(mContext, item);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (adapter != null) {
            return adapter.onCreateViewHolder(parent, item);
        } else {
            return new CommonViewHolder(mContext, parent);
        }
    }

    /**
     * 更新数据到item上
     */
    @Override
    public void onBindViewHolderEvent(CommonViewHolder holder, int position) {
        try {
            BaseTemplateAdapter adapter = (BaseTemplateAdapter) holder.getConvertView().getTag(R.id.template_group_adapter);
            adapter.onBindViewHolder(holder, mList.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void setDataList(List<BaseListBean> dataList) {
        if (null != dataList) {
            if (mList == null) {
                mList = new ArrayList<>();
            }
            this.mList.clear();
            this.mList.addAll(dataList);
            try {
                this.notifyDataSetChanged();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public int getMultipleView(int position) {
        return position;
    }
}

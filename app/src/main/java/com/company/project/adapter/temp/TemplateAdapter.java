package com.company.project.adapter.temp;

import android.content.Context;
import android.text.TextUtils;
import android.view.ViewGroup;

import com.company.project.R;
import com.company.project.adapter.base.BaseMultipleAdapter;
import com.company.project.adapter.base.CommonViewHolder;
import com.company.project.adapter.base.bean.BaseListBean;
import com.company.project.adapter.factorys.SubAdapterFactory;
import com.company.project.util.TLog;

import java.util.List;

/**
 * @author ynet
 * 通用模板Adapter
 */
public class TemplateAdapter<T> extends BaseMultipleAdapter<BaseListBean<T>> {
    private Context mContext;

    public TemplateAdapter(Context context) {
        mContext = context;
    }

    /**
     * 初始化item的view
     */
    @Override
    public CommonViewHolder onCreateViewHolderEvent(ViewGroup parent, int position) {
        BaseListBean<T> item = getItem(position);
        BaseTemplateAdapter<T> adapter = null;
        try {
            String groupType = item.getType();
            if (TextUtils.isEmpty(groupType)) {
                //无对应模板类型
                groupType = "-1";
            }
            adapter = SubAdapterFactory.newTemplateAdapter(mContext, item);
        } catch (Exception e) {
            TLog.fLog(e);
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
            BaseTemplateAdapter<T> adapter = (BaseTemplateAdapter) holder.getConvertView().getTag(R.id.template_group_adapter);
            adapter.onBindViewHolder(holder, mList.get(position));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * @param dataList 数据列表
     *                 设置数据
     */
    public void setDataList(List<BaseListBean<T>> dataList) {
        try {
            mList = dataList;
            notifyDataSetChanged();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public synchronized void setDateList(List<BaseListBean<T>> dataList) {
        try {
            if (null != dataList) {
                if (null != this.mList) {
                    this.mList.clear();
                    this.mList.addAll(dataList);
                    try {
                        this.notifyDataSetChanged();
                    } catch (Exception e) {
                        TLog.fLog(e);
                    }
                }
            }
        } catch (Exception e) {
            TLog.fLog(e);
        }
    }

    @Override
    public int getMultipleView(int position) {
        return position;
    }
}

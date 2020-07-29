package com.company.project.adapter.temp;

import android.content.Context;
import android.view.ViewGroup;

import com.company.project.R;
import com.company.project.adapter.base.CommonViewHolder;
import com.company.project.adapter.base.bean.BaseListBean;
import com.company.project.util.TLog;


/**
 * 模板大类别Adapter，供各模板大类别Adapter继承
 *
 * 此adapter非RecyclerView的Adapter，可以理解为 列表项ViewHolder中的 View生成器 以及 数据分发器
 */
public abstract class BaseTemplateAdapter<T> {
    protected Context mContext;

    public BaseTemplateAdapter(Context context) {
        mContext = context;
    }

    /**
     * 初始化布局与CommonViewHolder
     */
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, BaseListBean<T> item) {
        CommonViewHolder holder = CommonViewHolder.createViewHolder(mContext, parent, getItemLayoutId(item));
        holder.getConvertView().setTag(R.id.template_group_adapter, this);
        try {
            TLog.w("BTA onCreateViewHolder item = " + TLog.valueOf(item));
            onInitView(holder, item);
        } catch (Exception e) {
            TLog.e(e);
        }
        return holder;
    }

    /**
     * 更新数据到adapter
     */
    public void onBindViewHolder(CommonViewHolder holder, BaseListBean<T> item) {
        try {
            TLog.w("BTA onBindViewHolder item = " + TLog.valueOf(item));
            setDataToHolder(holder, item);
        } catch (Exception e) {
            TLog.e(e);
        }
    }

    /**
     * 不同的模板传不同的layout的id
     */
    protected abstract int getItemLayoutId(BaseListBean<T> item);

    /**
     * 一些布局初始化，比如设置宽高
     */
    protected abstract void onInitView(CommonViewHolder holder, BaseListBean<T> item) throws Exception;

    /**
     * 将数据设置在布局上
     */
    protected abstract void setDataToHolder(CommonViewHolder holder, BaseListBean<T> item) throws Exception;
}
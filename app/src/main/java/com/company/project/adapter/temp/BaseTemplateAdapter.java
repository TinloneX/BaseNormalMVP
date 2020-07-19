package com.company.project.adapter.temp;

import android.content.Context;
import android.view.ViewGroup;

import com.company.project.R;
import com.company.project.adapter.base.CommonViewHolder;
import com.company.project.adapter.base.bean.BaseListBean;
import com.company.project.adapter.base.bean.IBean;
import com.company.project.util.TLog;


/**
 *  模板大类别Adapter，供各模板大类别Adapter继承
 */
public abstract class BaseTemplateAdapter<T extends IBean> {
    protected Context mContext;
    /**
     * 子类别，例: TemplateType.ChildType.TYPE_1
     */
    protected String mType;

    public BaseTemplateAdapter(Context context, String type) {
        mContext = context;
        mType = type;
    }

    /**
     *  初始化布局与CommonViewHolder
     */
    public CommonViewHolder onCreateViewHolder(ViewGroup parent, BaseListBean<T> item) {
        CommonViewHolder holder = CommonViewHolder.createViewHolder(mContext, parent, getItemLayoutId(item));
        holder.getConvertView().setTag(R.id.template_group_adapter, this);
        try {
            onInitView(holder, item);
        } catch (Exception e) {
            TLog.fLog(e);
        }
        return holder;
    }

    /**
     *  更新数据到adapter
     */
    public void onBindViewHolder(CommonViewHolder holder, BaseListBean<T> item) {
        try {
            setDataToHolder(holder, item);
        } catch (Exception e) {
            TLog.fLog(e);
        }
    }

    /**
     *  不同的模板传不同的layout的id
     */
    protected abstract int getItemLayoutId(BaseListBean<T> item);

    /**
     * 一些布局初始化，比如设置宽高
     */
    protected abstract void onInitView(CommonViewHolder holder, BaseListBean<T> item) throws Exception;

    /**
     *  将数据设置在布局上
     */
    protected abstract void setDataToHolder(CommonViewHolder holder, BaseListBean<T> item) throws Exception;
}

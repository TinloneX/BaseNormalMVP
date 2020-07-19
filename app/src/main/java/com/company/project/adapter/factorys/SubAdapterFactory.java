package com.company.project.adapter.factorys;

import android.content.Context;

import androidx.annotation.NonNull;

import com.company.project.adapter.adapters.Test0101Adapter;
import com.company.project.adapter.adapters.Test0102Adapter;
import com.company.project.adapter.adapters.Test02Adapter;
import com.company.project.adapter.base.bean.BaseListBean;
import com.company.project.adapter.temp.TemplateDefaultAdapter;
import com.company.project.adapter.temp.BaseTemplateAdapter;
import com.company.project.testbean.Bean2;
import com.company.project.util.TLog;

import java.lang.reflect.Constructor;

public class SubAdapterFactory {

    /**
     * @param item 实体
     * @return adapter实例
     * 创建模板adapter
     */
    public static BaseTemplateAdapter newTemplateAdapter(Context context, @NonNull BaseListBean item) {
        return newInstance(SubAdapterFactory.getClassByChildType(item), context, item.getSubType());
    }

    /**
     * 创建实例
     */
    private static BaseTemplateAdapter newInstance(Class<? extends BaseTemplateAdapter> clazz, Context context, String childType) {
        if (clazz == null) {
            return new TemplateDefaultAdapter(context, childType);
        }
        try {
            Class clzz = Class.forName(clazz.getName());
            Class[] parameterTypes = {Context.class, String.class};
            Constructor constructor = clzz.getConstructor(parameterTypes);
            Object[] parameters = {context, childType};
            return (BaseTemplateAdapter) constructor.newInstance(parameters);
        } catch (Exception e) {
            TLog.fLog(e);
        }
        return new TemplateDefaultAdapter(context, childType);
    }

    /**
     * 维护实例映射
     * @param item 对象
     * @return  模板适配器
     */
    public static Class<? extends BaseTemplateAdapter> getClassByChildType(@NonNull BaseListBean item) {
        if (item.data!=null){
            if (item.data instanceof Bean2){
                return Test02Adapter.class;
            }else {
                switch (item.subType) {
                    case "1":
                        return Test0101Adapter.class;
                    default:
                        return Test0102Adapter.class;
                }
            }
        }
        return TemplateDefaultAdapter.class;
    }
}

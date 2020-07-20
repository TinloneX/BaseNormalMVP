package com.company.project.adapter.factorys;

import android.content.Context;
import android.text.TextUtils;

import androidx.annotation.NonNull;

import com.company.project.adapter.adapters.Test0101Adapter;
import com.company.project.adapter.adapters.Test0102Adapter;
import com.company.project.adapter.adapters.Test02Adapter;
import com.company.project.adapter.base.bean.BaseListBean;
import com.company.project.adapter.temp.BaseTemplateAdapter;
import com.company.project.adapter.temp.TemplateDefaultAdapter;
import com.company.project.util.TLog;

import java.lang.reflect.Constructor;

/**
 * @author Administrator
 * 子类adapter工厂
 */
public class SubAdapterFactory {

    /**
     * @param item 实体
     * @return adapter实例
     * 创建模板adapter
     */
    public static <T> BaseTemplateAdapter<T> newTemplateAdapter(Context context, @NonNull BaseListBean<T> item) {
        return newInstance(SubAdapterFactory.getClassByChildType(item), context, item.getSubType());
    }

    /**
     * 创建实例
     */
    private static<T> BaseTemplateAdapter<T> newInstance(Class<? extends BaseTemplateAdapter> clazz, Context context, String childType) {
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
     * 此方法建议单独拆解出去维护为一个类，相当于 类别-适配器 的配置项
     * @param item 对象
     * @return 模板适配器
     * 由于泛型检查问题，此处需要优化
     */
    public static<T> Class<? extends BaseTemplateAdapter> getClassByChildType(@NonNull BaseListBean<T> item) {
        String type = item.type;
        String subType = item.subType;
        if (type == null) {
            return TemplateDefaultAdapter.class;
        }
        switch (type) {
            case "1":
                return typeOneSubAdapterClass(subType);
            default:
                return typeTwoSubAdapterClass(subType);

        }
    }

    /**
     * 单独处理小类型subType
     *
     * @param subType 子类型
     * @return 子类型对应adapter
     */
    private static Class<? extends BaseTemplateAdapter> typeTwoSubAdapterClass(String subType) {
        if (TextUtils.isEmpty(subType)) {
            return TemplateDefaultAdapter.class;
        }
        return Test02Adapter.class;
    }

    private static Class<? extends BaseTemplateAdapter> typeOneSubAdapterClass(String subType) {
        switch (subType) {
            case "1":
                return Test0101Adapter.class;
            default:
                return Test0102Adapter.class;
        }
    }

}

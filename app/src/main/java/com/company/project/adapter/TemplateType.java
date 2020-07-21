package com.company.project.adapter;

import androidx.annotation.StringDef;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

public interface TemplateType {
    /**
     * 此处定义唯一可信类别常量
     */
    @StringDef({Type.TYPE_01, Type.TYPE_02, Type.TYPE_03})
    @Retention(RetentionPolicy.SOURCE)
    @interface Type {

        String TYPE_01 = "1";
        String TYPE_02 = "2";
        String TYPE_03 = "3";

    }

    /**
     * 此处定义唯一可信子类别常量
     */
    @StringDef({SubType.TYPE_01, SubType.TYPE_02})
    @Retention(RetentionPolicy.SOURCE)
    @interface SubType {

        String TYPE_01 = "1";
        String TYPE_02 = "2";
        String TYPE_03 = "3";

    }


}

package com.company.project.testbean;

import android.graphics.Color;

import com.company.project.adapter.base.bean.BaseListBean;
import com.company.project.adapter.base.bean.IBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestDataJSON {

    public static List<BaseListBean<IBean>> getJson() {
        ArrayList<BaseListBean<IBean>> beans = new ArrayList<>();
        int type;
        int subType;
        for (int j = 0; j < 15; j++) {
            type = randomInt(2);
            subType = randomInt(2);
            BaseListBean<IBean> wrapper = new BaseListBean<>();
            wrapper.type = type + "";
            wrapper.subType = subType + "";
            if (j % 2 == 0) {
                Bean1 bean1 = new Bean1(String.format("bean1 我是type[%s],sunType[%s]的子项", type, subType), randomColor());
                wrapper.setData(bean1);
            } else {
                Bean2 bean2 = new Bean2(String.format("bean2 我是type[%s],sunType[%s]的子项", type, subType), randomColor());
                wrapper.setData(bean2);
            }
            beans.add(wrapper);
        }
        return beans;
    }

    public static int randomInt(int max) {
        return new Random().nextInt(max) + 1;
    }

    public static int randomColor() {

        return Color.rgb(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));

    }


}

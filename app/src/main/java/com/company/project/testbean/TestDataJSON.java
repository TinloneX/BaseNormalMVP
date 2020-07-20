package com.company.project.testbean;

import android.graphics.Color;

import com.company.project.adapter.base.bean.BaseListBean;
import com.company.project.adapter.base.bean.IBean;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class TestDataJSON {

    public static List<BaseListBean> getJson() {
        ArrayList<BaseListBean> beans = new ArrayList<>();
        int type;
        int subType;
        for (int j = 0; j < 15; j++) {
            type = randomInt(2);
            subType = randomInt(2);
            BaseListBean<IBean> wrapper = new BaseListBean<>();
            wrapper.subType = subType + "";
            if (j % 2 == 0) {
                Bean1 bean1 = new Bean1(String.format("bean1 我是type[%s-%s]", type, subType), randomColor());
                wrapper.type = "1";
                wrapper.setData(bean1);
            } else {
                Bean2 bean2 = new Bean2(String.format("bean2 我是type[%s-%s]的项", type, subType), randomColor());
                wrapper.type = "2";
                wrapper.setData(bean2);
            }
            beans.add(wrapper);
        }
        ArrayList<Bean2> t3Bean = new ArrayList<Bean2>() {{
            for (int i = 0; i < 4; i++) {
                add(new Bean2(String.format("bean2 我是type[%s-%s]的项", "3", "1"), randomColor()));
            }
        }};

        return beans;
    }

    public static int randomInt(int max) {
        return new Random().nextInt(max) + 1;
    }

    public static int randomColor() {

        return Color.rgb(new Random().nextInt(255), new Random().nextInt(255), new Random().nextInt(255));

    }


}

package com.company.project.fragment;


import com.company.project.R;
import com.company.project.base.BaseFragment;

/**
 * **************************************************
 * 文件名称 : FragmentOne
 * 作    者 : Created by ydy
 * 创建时间 : 2018/5/28 18:26
 * 文件描述 :
 * 注意事项 :
 * 修改历史 : 2018/5/28 1.00 初始版本
 * **************************************************
 */
public class FragmentOne extends BaseFragment {

    public static FragmentOne newInstance() {
        return new FragmentOne();
    }

    @Override
    public int layoutId() {
        return R.layout.fragment_one;
    }
}

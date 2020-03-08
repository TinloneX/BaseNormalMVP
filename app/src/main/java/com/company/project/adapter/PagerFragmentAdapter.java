package com.company.project.adapter;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import java.util.List;

public class PagerFragmentAdapter extends FragmentPagerAdapter {

    private List<Fragment> mFragments;
    private List<CharSequence> mTitles;

    public PagerFragmentAdapter(FragmentManager fm, List<Fragment> fragments) {
        this(fm, fragments, null);
    }

    public PagerFragmentAdapter(FragmentManager fm, List<Fragment> fragments, List<CharSequence> titles) {
        super(fm);
        mFragments = fragments;
        mTitles = titles;
    }


    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        if (mTitles == null) {
            return "";
        }
        return mTitles.get(position);
    }
}

package com.android.base.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;

import java.util.List;

/**
 * 主页面架构适配器
 * Created by 15161 on 2017/7/13.
 */

public class MainTabAdapter extends FragmentPagerAdapter {
    private Context mContext;
    private List<Fragment> mFragments;

    public MainTabAdapter(Context mContext, FragmentManager fm, List<Fragment> fragmentArray) {
        super(fm);
        this.mContext = mContext;
        this.mFragments = fragmentArray;
    }

    @Override
    public int getCount() {
        return mFragments.size();
    }

    @Override
    public Fragment getItem(int position) {
        return mFragments.get(position);
    }
}
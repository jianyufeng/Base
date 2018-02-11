package com.android.base.adapter;

import android.content.Context;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.view.ViewGroup;

import com.fpi.mobile.view.viewpager.LazyFragmentPagerAdapter;

/**
 * 主页面架构适配器
 * Created by 15161 on 2017/7/13.
 */

public class MaintabAdapter extends LazyFragmentPagerAdapter {
    private Context mContext;
    private Class fragmentArray[];

    public MaintabAdapter(Context mContext, FragmentManager fm, Class[] fragmentArray) {
        super(fm);
        this.mContext = mContext;
        this.fragmentArray = fragmentArray;
    }

    @Override
    public int getCount() {
        return fragmentArray.length;
    }

    @Override
    protected Fragment getItem(ViewGroup container, int position) {
        return Fragment.instantiate(mContext, fragmentArray[position].getName());
    }


}
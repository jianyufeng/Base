package com.android.base.fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.android.base.R;

/**
 * 资料库
 * Created by 14165 on 2017/8/12.
 */

public class DatabaseFragment extends LazyFragment {
    private View mView;

    @Override
    public void preData() {

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        if (null == mView) {
            initView(inflater, container);
        }
        ViewGroup group = (ViewGroup) mView.getParent();
        if (group != null) {
            group.removeAllViewsInLayout();
        }
        return mView;
    }

    private void initView(LayoutInflater inflater, ViewGroup container) {
        mView = inflater.inflate(R.layout.frag_home, container, false);
    }


    @Override
    protected void lazyLoad() {
        showToast("资料库");

    }
}

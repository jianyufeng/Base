package com.android.base.fragment;

import com.fpi.mobile.base.BaseFragment;

/**
 * Created by 16896 on 2018/2/28.
 */

public abstract class LazyFragment extends BaseFragment {
    /**
     * Fragment当前状态是否可见
     */
    protected boolean isVisible;

    /**
     * 调用在OnCreate 之前的方法
     *
     * @param isVisibleToUser 是否可见
     */
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        // 获得是否可见
        if (getUserVisibleHint()) {
            isVisible = true;
            // 可见的
            onVisible();
        } else {
            // 不可见的
            isVisible = false;
            onInvisible();
        }

    }

    /**
     * 可见
     */
    protected void onVisible() {
        lazyLoad();
    }

    /**
     * 不可见
     */
    protected  void onInvisible(){

    }

    /**
     * 延迟加载
     * 子类必须重写此方法
     */
    protected abstract void lazyLoad();
}

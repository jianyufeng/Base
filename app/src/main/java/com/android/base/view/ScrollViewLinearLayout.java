package com.android.base.view;

import android.content.Context;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.Scroller;

/**
 * Created by Administrator on 2018/2/27/027.
 */

public class ScrollViewLinearLayout extends LinearLayout implements View.OnTouchListener {
    private Scroller mScroller;
    private boolean isFirst=true;
    private ScrollView sv;
    private float y2;
    private float y1;

    public ScrollViewLinearLayout(Context context) {
        super(context);
    }

    public ScrollViewLinearLayout(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        setClickable(true);
        setLongClickable(true);
        mScroller = new Scroller(context);
    }

    public ScrollViewLinearLayout(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    protected void smoothScrollBy(int dx, int dy) {
        mScroller.startScroll(0, mScroller.getFinalY(), 0, dy);
        invalidate();
    }

    protected void smoothScrollTo(int fx, int fy) {
        int dx = fx - mScroller.getFinalX();
        int dy = fy - mScroller.getFinalY();
        smoothScrollBy(0, dy);
    }

    @Override
    protected void onLayout(boolean changed, int l, int t, int r, int b) {
        super.onLayout(changed, l, t, r, b);
        if (changed && isFirst) {
            sv = (ScrollView) getChildAt(0);
            sv.setOverScrollMode(View.OVER_SCROLL_NEVER);
            sv.setOnTouchListener(this);
            isFirst = false;
        }
    }

    @Override
    public void computeScroll() {
        if (mScroller.computeScrollOffset()) {
            scrollTo(mScroller.getCurrX(), mScroller.getCurrY());
            postInvalidate();

        }
        super.computeScroll();
    }

    @Override
    public boolean onTouch(View v, MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                y1 = event.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                y2 = event.getY();
                int scrollY = v.getScrollY();
                int height = v.getHeight();
                int scrollViewMeasureHeight = sv.getChildAt(0).getMeasuredHeight();
                if (y2 - y1 > 0 && v.getScaleY() <= 0) {
                    smoothScrollTo(0, -(int) (y2 - y1) / 2);
                    return false;
                }
                if ((y2 - y1) < 0 && (scrollY + height) == scrollViewMeasureHeight) {
                    smoothScrollTo(0,-(int)(y2-y1)/2);
                    return false;
                }
                break;
            case MotionEvent.ACTION_UP:
                smoothScrollTo(0,0);
                break;
            default:break;
        }
        return false;
    }
}

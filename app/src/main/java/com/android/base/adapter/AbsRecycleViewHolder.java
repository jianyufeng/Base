package com.android.base.adapter;

import android.support.v7.widget.RecyclerView;
import android.util.SparseArray;
import android.view.View;

/**
 * Created by 16896 on 2017/12/18.
 */

public class AbsRecycleViewHolder extends RecyclerView.ViewHolder implements View.OnLongClickListener, View.OnClickListener {

    /*用于存储各类控件的容器*/
    private SparseArray<View> mViews;

    private ItemClickListener mListener;
    private ItemLongClickListener mLongClickListener;

    public AbsRecycleViewHolder(View itemView, ItemClickListener listener, ItemLongClickListener longClickListener) {
        super(itemView);
        mViews = new SparseArray<View>();
        itemView.setTag(this);
        this.mListener = listener;
        this.mLongClickListener = longClickListener;
        itemView.setOnClickListener(this);
        itemView.setOnLongClickListener(this);
    }

    @Override
    public boolean onLongClick(View view) {
        if (mLongClickListener != null) {
            mLongClickListener.onItemLongClick(view, (Integer) view.getTag());
        }
        return true;
    }

    @Override
    public void onClick(View view) {
        if (mListener != null) {
            mListener.onItemClick(view, (Integer) view.getTag());
        }
    }


    //通过ViewID在容器中获取控件，若在容器中没有该控件就通过ID获取，然后存储到容器中
    public <T extends View> T getView(int ViewID) {
        View view = mViews.get(ViewID);
        if (view == null) {
            view = itemView.findViewById(ViewID);
            if (view != null) {
                mViews.put(ViewID, view);
            }
        }
        return (T) view;
    }

    //为RecycleView添加点击事件
    public interface ItemClickListener {
        void onItemClick(View view, int postion);
    }

    //为RecycleView添加长按事件
    public interface ItemLongClickListener {
        void onItemLongClick(View view, int position);
    }

}
package com.android.base.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.util.List;

/**
 * Created by Administrator on 2016/7/6.
 */
public abstract class AbsRecycleAdapter<T> extends RecyclerView.Adapter<AbsRecycleViewHolder> {

    private AbsRecycleViewHolder.ItemClickListener mItemClickListener;
    private AbsRecycleViewHolder.ItemLongClickListener mLongClickListener;

    /**
     * 设置Item点击监听
     *
     * @param listener
     */
    public void setOnItemClickListener(AbsRecycleViewHolder.ItemClickListener listener) {
        this.mItemClickListener = listener;
    }

    /**
     * 设置Item长按监听
     *
     * @param listener
     */
    public void setOnItemLongClickListener(AbsRecycleViewHolder.ItemLongClickListener listener) {
        this.mLongClickListener = listener;
    }

    /**
     * 设置Item长按监听
     *
     * @param parent  RecycleView
     * @param viewType 布局类型
     */
    @Override
    public AbsRecycleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        AbsRecycleViewHolder holder = new AbsRecycleViewHolder(LayoutInflater.from(context).inflate(LayoutID, parent, false), mItemClickListener, mLongClickListener);
        return holder;
    }

    @Override
    public void onBindViewHolder(AbsRecycleViewHolder holder, int position) {
        holder.itemView.setTag(position);
        bindViewHolder(holder, datas.get(position));
    }

    public abstract void bindViewHolder(AbsRecycleViewHolder holder, T t);

    @Override
    public int getItemCount() {
        return datas.size();
    }

    public T getItem(int positoion) {
        return datas.get(positoion);
    }

    private Context context;
    private List<T> datas;
    private int LayoutID;

    public AbsRecycleAdapter(Context context, List<T> datas, int LayoutID) {
        this.context = context;
        this.datas = datas;
        this.LayoutID = LayoutID;

    }

    public void addData(T data) {
        int position = datas.size();
        datas.add(position, data);
        notifyItemInserted(position);
    }

    public void refreshData(List<T> datas) {
        this.datas = datas;
        notifyDataSetChanged();
    }

    public void remove(int position) {

        datas.remove(position);
        notifyItemRemoved(position);
        if (position != datas.size()) {
            notifyItemRangeChanged(position, datas.size() - position);
        }
    }

    public void remove(T t) {
        int i = datas.indexOf(t);
        datas.remove(t);
        if (i == -1) return;
        notifyItemRemoved(i);
        if (i != datas.size()) {
            notifyItemRangeChanged(i, datas.size() - i);
        }
    }

    public void clearData() {
        this.datas.clear();
        notifyDataSetChanged();
    }


    @Override
    public long getItemId(int position) {
        return position;
    }
}
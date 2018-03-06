package com.hyphenate.easeui.widget.emojicon;

import android.content.Context;
import android.support.v4.view.ViewCompat;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.HorizontalScrollView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.hyphenate.easeui.R;
import com.hyphenate.util.DensityUtil;

import java.util.ArrayList;
import java.util.List;

/**
 * 表情底部导航
 */
public class EaseEmojiconScrollTabBar extends RelativeLayout{

    private Context context;
    private HorizontalScrollView scrollView;
    private LinearLayout tabContainer;
    
    private List<ImageView> tabList = new ArrayList<ImageView>();
    private EaseScrollTabBarItemClickListener itemClickListener;

    public EaseEmojiconScrollTabBar(Context context) {
        this(context, null);
    }

    public EaseEmojiconScrollTabBar(Context context, AttributeSet attrs, int defStyle) {
        this(context, attrs);
    }

    public EaseEmojiconScrollTabBar(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }
    
    private void init(Context context, AttributeSet attrs){
        this.context = context;
        LayoutInflater.from(context).inflate(R.layout.ease_widget_emojicon_tab_bar, this);
        
        scrollView = (HorizontalScrollView) findViewById(R.id.scroll_view);
        tabContainer = (LinearLayout) findViewById(R.id.tab_container);
    }
    
    /**
     * add tab
     * @param icon
     */
    public void addTab(int icon){ //创建表情底部的导航快捷切换表情类型
        View tabView = View.inflate(context, R.layout.ease_scroll_tab_item, null);
        ImageView imageView = (ImageView) tabView.findViewById(R.id.iv_icon);
        imageView.setImageResource(icon);
        int tabWidth = 60;
        LinearLayout.LayoutParams imgParams = new LinearLayout.LayoutParams(DensityUtil.dip2px(context, tabWidth), LayoutParams.MATCH_PARENT);
        imageView.setLayoutParams(imgParams);
        tabContainer.addView(tabView);
        tabList.add(imageView);
        final int position = tabList.size() -1;
        imageView.setOnClickListener(new OnClickListener() {
            
            @Override
            public void onClick(View v) {
                if(itemClickListener != null){
                    itemClickListener.onItemClick(position);
                }
            }
        });
    }
    
    /**
     * remove tab
     * @param position
     */
    public void removeTab(int position){ //移除
        tabContainer.removeViewAt(position);
        tabList.remove(position);
    }
    
    public void selectedTo(int position){
        scrollTo(position);  //处理底部导航较多时滑动显示
        for (int i = 0; i < tabList.size(); i++) {//设置底部导航的选中
            if (position == i) {
                tabList.get(i).setBackgroundColor(getResources().getColor(R.color.emojicon_tab_selected));
            } else {
                tabList.get(i).setBackgroundColor(getResources().getColor(R.color.emojicon_tab_nomal));
            }
        }
    }
    
    private void scrollTo(final int position){ //滑动
        int childCount = tabContainer.getChildCount();
        if(position < childCount){
            scrollView.post(new Runnable() {
                @Override
                public void run() {
                    int mScrollX = tabContainer.getScrollX();
                    int childX = (int)ViewCompat.getX(tabContainer.getChildAt(position));

                    if(childX < mScrollX){
                        scrollView.scrollTo(childX,0);
                        return;
                    }

                    int childWidth = (int)tabContainer.getChildAt(position).getWidth();
                    int hsvWidth = scrollView.getWidth();
                    int childRight = childX + childWidth;
                    int scrollRight = mScrollX + hsvWidth;

                    if(childRight > scrollRight){
                        scrollView.scrollTo(childRight - scrollRight,0);
                        return;
                    }
                }
            });
        }
    }
    
    
    public void setTabBarItemClickListener(EaseScrollTabBarItemClickListener itemClickListener){
        this.itemClickListener = itemClickListener;
    }
    
    
    public interface EaseScrollTabBarItemClickListener{
        void onItemClick(int position);
    }

}

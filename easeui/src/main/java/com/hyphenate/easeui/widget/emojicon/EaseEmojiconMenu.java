package com.hyphenate.easeui.widget.emojicon;

import android.annotation.TargetApi;
import android.content.Context;
import android.content.res.TypedArray;
import android.os.Build;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconPagerView.EaseEmojiconPagerViewListener;
import com.hyphenate.easeui.widget.emojicon.EaseEmojiconScrollTabBar.EaseScrollTabBarItemClickListener;

import java.util.ArrayList;
import java.util.List;

/**
 * Emojicon menu
 */
public class EaseEmojiconMenu extends EaseEmojiconMenuBase{
	
	private int emojiconColumns;
	private int bigEmojiconColumns;
    private EaseEmojiconScrollTabBar tabBar;
    private EaseEmojiconIndicatorView indicatorView;
    private EaseEmojiconPagerView pagerView;
    
    private List<EaseEmojiconGroupEntity> emojiconGroupList = new ArrayList<EaseEmojiconGroupEntity>();
	
	
	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public EaseEmojiconMenu(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init(context, attrs);
	}

	public EaseEmojiconMenu(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
	}

	public EaseEmojiconMenu(Context context) {
		super(context);
		init(context, null);
	}
	
	private void init(Context context, AttributeSet attrs){ //初始化表情视图
		LayoutInflater.from(context).inflate(R.layout.ease_widget_emojicon, this);
		TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.EaseEmojiconMenu);
        int defaultColumns = 7;
        emojiconColumns = ta.getInt(R.styleable.EaseEmojiconMenu_emojiconColumns, defaultColumns);
        int defaultBigColumns = 4;
        bigEmojiconColumns = ta.getInt(R.styleable.EaseEmojiconMenu_bigEmojiconRows, defaultBigColumns);
		ta.recycle();
		//表情承载容器
		pagerView = (EaseEmojiconPagerView) findViewById(R.id.pager_view);
        //表情页指示器
		indicatorView = (EaseEmojiconIndicatorView) findViewById(R.id.indicator_view);
        //表情类型
		tabBar = (EaseEmojiconScrollTabBar) findViewById(R.id.tab_bar);
		
	}
	
	public void init(List<EaseEmojiconGroupEntity> groupEntities){
	    if(groupEntities == null || groupEntities.size() == 0){
	        return;
	    }
	    for(EaseEmojiconGroupEntity groupEntity : groupEntities){
	        emojiconGroupList.add(groupEntity);
	        tabBar.addTab(groupEntity.getIcon()); //添加底部 表情导航
	    }
        //设置viewpage滑动变化时内部的组的 指示器变化
        pagerView.setPagerViewListener(new EmojiconPagerViewListener());
        pagerView.init(emojiconGroupList, emojiconColumns,bigEmojiconColumns); //根据表情组 初始化表情页
        
        tabBar.setTabBarItemClickListener(new EaseScrollTabBarItemClickListener() {
            
            @Override
            public void onItemClick(int position) { //底部导航点击
                pagerView.setGroupPostion(position); //表情组切换
            }
        });
	    
	}
	
	
	/**
     * add emojicon group
     * @param groupEntity
     */
    public void addEmojiconGroup(EaseEmojiconGroupEntity groupEntity){
        emojiconGroupList.add(groupEntity);  //添加数据
        pagerView.addEmojiconGroup(groupEntity, true); //添加表情到 表情容器
        tabBar.addTab(groupEntity.getIcon());   //添加 表情导航
    }
    
    /**
     * add emojicon group list
     * @param groupEntitieList 添加好多组 表情
     */
    public void addEmojiconGroup(List<EaseEmojiconGroupEntity> groupEntitieList){
        for(int i= 0; i < groupEntitieList.size(); i++){
            EaseEmojiconGroupEntity groupEntity = groupEntitieList.get(i);
            emojiconGroupList.add(groupEntity);
            pagerView.addEmojiconGroup(groupEntity, i == groupEntitieList.size()-1 ? true : false);
            tabBar.addTab(groupEntity.getIcon());
        }
        
    }
    
    /**
     * remove emojicon group
     * @param position
     */
    public void removeEmojiconGroup(int position){  //移除表情
        emojiconGroupList.remove(position);  //移除数据
        pagerView.removeEmojiconGroup(position); //因为使用同一个数据引用 所以 适配器刷新就行
        tabBar.removeTab(position); //地导航移除视图
    }
    
    public void setTabBarVisibility(boolean isVisible){ //隐藏表情的底部导航
        if(!isVisible){
            tabBar.setVisibility(View.GONE);
        }else{
            tabBar.setVisibility(View.VISIBLE);
        }
    }
	
	
	private class EmojiconPagerViewListener implements EaseEmojiconPagerViewListener{

        @Override
        public void onPagerViewInited(int groupMaxPageSize, int firstGroupPageSize) {
            indicatorView.init(groupMaxPageSize);  //初始化 指示器
            indicatorView.updateIndicator(firstGroupPageSize); //设置显示器的数量
            tabBar.selectedTo(0);
        }

        @Override
        public void onGroupPositionChanged(int groupPosition, int pagerSizeOfGroup) {
            indicatorView.updateIndicator(pagerSizeOfGroup); //更新数量
            tabBar.selectedTo(groupPosition);  //设置选中
        }

        @Override
        public void onGroupInnerPagePostionChanged(int oldPosition, int newPosition) {
            indicatorView.selectTo(oldPosition, newPosition); //设置指示器的选中
        }

        @Override
        public void onGroupPagePostionChangedTo(int position) {
            indicatorView.selectTo(position); //设置指示器的选中
        }

        @Override
        public void onGroupMaxPageSizeChanged(int maxCount) {
            indicatorView.updateIndicator(maxCount); //添加表情组后  相应添加指示器
        }

        @Override
        public void onDeleteImageClicked() {
            if(listener != null){
                listener.onDeleteImageClicked(); //表情中的删除点击
            }
        }

        @Override
        public void onExpressionClicked(EaseEmojicon emojicon) {
            if(listener != null){
                listener.onExpressionClicked(emojicon);  //表情点击
            }
        }
	    
	}
	
}

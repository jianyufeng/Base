package com.hyphenate.easeui.widget.emojicon;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.EmojiconGridAdapter;
import com.hyphenate.easeui.adapter.EmojiconPagerAdapter;
import com.hyphenate.easeui.domain.EaseEmojicon;
import com.hyphenate.easeui.domain.EaseEmojicon.Type;
import com.hyphenate.easeui.domain.EaseEmojiconGroupEntity;
import com.hyphenate.easeui.utils.EaseSmileUtils;


public class EaseEmojiconPagerView extends ViewPager {

    private Context context;
    private List<EaseEmojiconGroupEntity> groupEntities;

    private PagerAdapter pagerAdapter;

    private int emojiconRows = 3;
    private int emojiconColumns = 7;

    private int bigEmojiconRows = 2;
    private int bigEmojiconColumns = 4;

    private int firstGroupPageSize;

    private int maxPageCount;
    private int previousPagerPosition;
    private EaseEmojiconPagerViewListener pagerViewListener;
    private List<View> viewpages;

    public EaseEmojiconPagerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.context = context;
    }

    public EaseEmojiconPagerView(Context context) {
        this(context, null);
    }


    public void init(List<EaseEmojiconGroupEntity> emojiconGroupList, int emijiconColumns, int bigEmojiconColumns) {
        if (emojiconGroupList == null) {
            throw new RuntimeException("emojiconGroupList is null");
        }
        this.groupEntities = emojiconGroupList;
        this.emojiconColumns = emijiconColumns;
        this.bigEmojiconColumns = bigEmojiconColumns;
        viewpages = new ArrayList<View>();
        //创建Viewpage的页
        for (int i = 0; i < groupEntities.size(); i++) {
            EaseEmojiconGroupEntity group = groupEntities.get(i);
            List<EaseEmojicon> groupEmojicons = group.getEmojiconList();
            List<View> gridViews = getGroupGridViews(group); //创建表情视图
            if (i == 0) {
                firstGroupPageSize = gridViews.size();//第一中表情 需要的页数
            }
            maxPageCount = Math.max(gridViews.size(), maxPageCount);
            viewpages.addAll(gridViews);  //添加视图
        }

        pagerAdapter = new EmojiconPagerAdapter(viewpages); //创建viewpage的适配器
        setAdapter(pagerAdapter);
        addOnPageChangeListener(new EmojiPagerChangeListener()); //设置滑动监听
        if (pagerViewListener != null) { //回调表情类型 的页数
            pagerViewListener.onPagerViewInited(maxPageCount, firstGroupPageSize);
        }
    }

    public void setPagerViewListener(EaseEmojiconPagerViewListener pagerViewListener) {
        this.pagerViewListener = pagerViewListener;
    }


    /**
     * set emojicon group position
     *
     * @param position
     */
    public void setGroupPostion(int position) { //切换表情组
        if (getAdapter() != null && position >= 0 && position < groupEntities.size()) {
            int count = 0;
            for (int i = 0; i < position; i++) {  //计算组所在的页
                count += getPageSize(groupEntities.get(i));
            }
            setCurrentItem(count); //设置页
        }
    }

    /**
     * get emojicon group gridview list
     *
     * @param groupEntity  创建表情页
     * @return
     */
    public List<View> getGroupGridViews(EaseEmojiconGroupEntity groupEntity) {
        List<EaseEmojicon> emojiconList = groupEntity.getEmojiconList();
        int itemSize = emojiconColumns * emojiconRows - 1; //处理每行数据显示
        int totalSize = emojiconList.size();
        Type emojiType = groupEntity.getType();
        if (emojiType == Type.BIG_EXPRESSION) {
            itemSize = bigEmojiconColumns * bigEmojiconRows; //每页显示多少个
        }
        //计算多少页
        int pageSize = totalSize % itemSize == 0 ? totalSize / itemSize : totalSize / itemSize + 1;
        List<View> views = new ArrayList<View>();
        for (int i = 0; i < pageSize; i++) {
            View view = View.inflate(context, R.layout.ease_expression_gridview, null);
            GridView gv = (GridView) view.findViewById(R.id.gridview);
            if (emojiType == Type.BIG_EXPRESSION) {
                gv.setNumColumns(bigEmojiconColumns); //设置列数
            } else {
                gv.setNumColumns(emojiconColumns);  //设置列数
            }
            List<EaseEmojicon> list = new ArrayList<EaseEmojicon>();
            if (i != pageSize - 1) {   //截取每页显示的数据
                list.addAll(emojiconList.subList(i * itemSize, (i + 1) * itemSize));
            } else {  //最后一页显示的数据
                list.addAll(emojiconList.subList(i * itemSize, totalSize));
            }
            if (emojiType != Type.BIG_EXPRESSION) { //每页数据后添加删除图标
                EaseEmojicon deleteIcon = new EaseEmojicon();
                deleteIcon.setEmojiText(EaseSmileUtils.DELETE_KEY);
                list.add(deleteIcon);
            }
            //表情适配器
            final EmojiconGridAdapter gridAdapter = new EmojiconGridAdapter(context, 1, list, emojiType);
            gv.setAdapter(gridAdapter);
            gv.setOnItemClickListener(new OnItemClickListener() {

                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    EaseEmojicon emojicon = gridAdapter.getItem(position);
                    if (pagerViewListener != null) {  //表情点击事件回调
                        String emojiText = emojicon.getEmojiText();
                        if (emojiText != null && emojiText.equals(EaseSmileUtils.DELETE_KEY)) {
                            pagerViewListener.onDeleteImageClicked();
                        } else {
                            pagerViewListener.onExpressionClicked(emojicon);
                        }

                    }

                }
            });

            views.add(view);
        }
        return views;
    }


    /**
     * add emojicon group
     *  添加表情
     * @param groupEntity
     */
    public void addEmojiconGroup(EaseEmojiconGroupEntity groupEntity, boolean notifyDataChange) {
        int pageSize = getPageSize(groupEntity);  //添加表情后页发生变化
        if (pageSize > maxPageCount) {
            maxPageCount = pageSize;
            if (pagerViewListener != null && pagerAdapter != null) {
                pagerViewListener.onGroupMaxPageSizeChanged(maxPageCount);
            }
        }
        viewpages.addAll(getGroupGridViews(groupEntity));
        if (pagerAdapter != null && notifyDataChange) {
            pagerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * remove emojicon group
     * 移除表情
     * @param position
     */
    public void removeEmojiconGroup(int position) {
        if (position > groupEntities.size() - 1) {
            return;
        }
        if (pagerAdapter != null) {
            pagerAdapter.notifyDataSetChanged();
        }
    }

    /**
     * get size of pages
     *
     * @param emojiconList
     * @return
     */
    private int getPageSize(EaseEmojiconGroupEntity groupEntity) {
        List<EaseEmojicon> emojiconList = groupEntity.getEmojiconList();
        int itemSize = emojiconColumns * emojiconRows - 1;
        int totalSize = emojiconList.size();
        Type emojiType = groupEntity.getType();
        if (emojiType == Type.BIG_EXPRESSION) {
            itemSize = bigEmojiconColumns * bigEmojiconRows;
        }
        int pageSize = totalSize % itemSize == 0 ? totalSize / itemSize : totalSize / itemSize + 1;
        return pageSize;
    }

    private class EmojiPagerChangeListener implements OnPageChangeListener {
        @Override
        public void onPageSelected(int position) {
            int endSize = 0; //多少页
            int groupPosition = 0; //多少组
            for (EaseEmojiconGroupEntity groupEntity : groupEntities) {
                int groupPageSize = getPageSize(groupEntity);  //根据数据获取页数
                //if the position is in current group
                if (endSize + groupPageSize > position) { //选择所在表情组处理  筛选
                    //this is means user swipe to here from previous page
                    if (previousPagerPosition - endSize < 0) { //当前页是从上个页滑过来的
                        if (pagerViewListener != null) {     //所以选择的是当前组的 第一页
                            pagerViewListener.onGroupPositionChanged(groupPosition, groupPageSize); //组发生变化
                            pagerViewListener.onGroupPagePostionChangedTo(0);  //选中当前组的第一页
                        }
                        break;
                    }
                    //this is means user swipe to here from back page
                    if (previousPagerPosition - endSize >= groupPageSize) {  //当前页是从下个页滑过来的
                        if (pagerViewListener != null) {   //组发生变化
                            pagerViewListener.onGroupPositionChanged(groupPosition, groupPageSize);
                            pagerViewListener.onGroupPagePostionChangedTo(position - endSize);//
                        }
                        break;
                    }

                    //page changed
                    if (pagerViewListener != null) {  //组未发生变化
                        pagerViewListener.onGroupInnerPagePostionChanged(previousPagerPosition - endSize, position - endSize);
                    }
                    break;

                }
                groupPosition++;
                endSize += groupPageSize;
            }

            previousPagerPosition = position;
        }

        @Override
        public void onPageScrollStateChanged(int arg0) {
        }

        @Override
        public void onPageScrolled(int arg0, float arg1, int arg2) {
        }
    }


    public interface EaseEmojiconPagerViewListener {
        /**
         * pagerview initialized
         *
         * @param groupMaxPageSize     --max pages size
         * @param firstGroupPageSize-- size of first group pages
         */
        void onPagerViewInited(int groupMaxPageSize, int firstGroupPageSize);

        /**
         * group position changed
         *
         * @param groupPosition--group   position
         * @param pagerSizeOfGroup--page size of group
         */
        void onGroupPositionChanged(int groupPosition, int pagerSizeOfGroup);

        /**
         * page position changed
         *
         * @param oldPosition
         * @param newPosition
         */
        void onGroupInnerPagePostionChanged(int oldPosition, int newPosition);

        /**
         * group page position changed
         *
         * @param position
         */
        void onGroupPagePostionChangedTo(int position);

        /**
         * max page size changed
         *
         * @param maxCount
         */
        void onGroupMaxPageSizeChanged(int maxCount);

        void onDeleteImageClicked();

        void onExpressionClicked(EaseEmojicon emojicon);

    }

}

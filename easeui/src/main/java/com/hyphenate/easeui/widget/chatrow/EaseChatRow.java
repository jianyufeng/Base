package com.hyphenate.easeui.widget.chatrow;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMMessage.Direct;
import com.hyphenate.easeui.EaseUI;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.adapter.EaseMessageAdapter;
import com.hyphenate.easeui.domain.EaseAvatarOptions;
import com.hyphenate.easeui.model.styles.EaseMessageListItemStyle;
import com.hyphenate.easeui.utils.EaseUserUtils;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.EaseChatMessageList.MessageListItemClickListener;
import com.hyphenate.easeui.widget.EaseImageView;
import com.hyphenate.util.DateUtils;

import java.util.Date;

/**
 * 聊天类型显示基类
 */
public abstract class EaseChatRow extends LinearLayout {
    public interface EaseChatRowActionCallback {
        void onResendClick(EMMessage message);

        void onBubbleClick(EMMessage message);

        void onDetachedFromWindow();
    }

    protected static final String TAG = EaseChatRow.class.getSimpleName();

    protected LayoutInflater inflater;
    protected Context context;
    protected BaseAdapter adapter;
    protected EMMessage message;
    protected int position;

    protected TextView timeStampView;
    protected ImageView userAvatarView;
    protected View bubbleLayout;
    protected TextView usernickView;

    protected TextView percentageView;
    protected ProgressBar progressBar;
    protected ImageView statusView;
    protected Activity activity;

    protected TextView ackedView;
    protected TextView deliveredView;

    protected MessageListItemClickListener itemClickListener;
    protected EaseMessageListItemStyle itemStyle;

    private EaseChatRowActionCallback itemActionCallback;

    public EaseChatRow(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context);
        this.context = context;
        this.message = message;
        this.position = position;
        this.adapter = adapter;
        this.activity = (Activity) context;
        inflater = LayoutInflater.from(context);

        initView();
    }

    @Override
    protected void onDetachedFromWindow() {
        itemActionCallback.onDetachedFromWindow();
        super.onDetachedFromWindow();
    }

    public void updateView(final EMMessage msg) {
        activity.runOnUiThread(new Runnable() {
            @Override
            public void run() {
                onViewUpdate(msg);  //更新消息状态视图
            }
        });
    }

    private void initView() {
        onInflateView();  //加载布局
        timeStampView = (TextView) findViewById(R.id.timestamp); //时间
        userAvatarView = (ImageView) findViewById(R.id.iv_userhead); //用户图像
        bubbleLayout = findViewById(R.id.bubble);  //聊天气泡
        usernickView = (TextView) findViewById(R.id.tv_userid); //昵称

        progressBar = (ProgressBar) findViewById(R.id.progress_bar); //发送进度
        statusView = (ImageView) findViewById(R.id.msg_status);  //发送状态
        ackedView = (TextView) findViewById(R.id.tv_ack);    //消息已读
        deliveredView = (TextView) findViewById(R.id.tv_delivered); //消息送达

        onFindViewById();    //查找空间控件
    }

    /**
     * set property according message and postion
     * 
     * @param message
     * @param position
     */
    public void setUpView(EMMessage message, int position,
            EaseChatMessageList.MessageListItemClickListener itemClickListener,
                          EaseChatRowActionCallback itemActionCallback,
                          EaseMessageListItemStyle itemStyle) {
        this.message = message;
        this.position = position;
        this.itemClickListener = itemClickListener;
        this.itemActionCallback = itemActionCallback;
        this.itemStyle = itemStyle;

        setUpBaseView();  //设置基本信息  数据填充
        onSetUpView();  //扩展数据填充
        setClickListener(); //设置监听事件
    }

    private void setUpBaseView() {
    	// set nickname, avatar and background of bubble
        TextView timestamp = (TextView) findViewById(R.id.timestamp);
        if (timestamp != null) {   //显示时间
            if (position == 0) {  //第一条item
                timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                timestamp.setVisibility(View.VISIBLE);
            } else {
            	// show time stamp if interval with last message is > 30 seconds
                EMMessage prevMessage = (EMMessage) adapter.getItem(position - 1); //比较相邻消息的时间是否大于30秒 显示
                if (prevMessage != null && DateUtils.isCloseEnough(message.getMsgTime(), prevMessage.getMsgTime())) {
                    timestamp.setVisibility(View.GONE);
                } else {
                    timestamp.setText(DateUtils.getTimestampString(new Date(message.getMsgTime())));
                    timestamp.setVisibility(View.VISIBLE);
                }
            }
        }
        if(userAvatarView != null) {  //设置图像用户
            //set nickname and avatar
            if (message.direct() == Direct.SEND) {
                EaseUserUtils.setUserAvatar(context, EMClient.getInstance().getCurrentUser(), userAvatarView);
            } else {
                EaseUserUtils.setUserAvatar(context, message.getFrom(), userAvatarView);
                EaseUserUtils.setUserNick(message.getFrom(), usernickView);
            }
        }
        if (EMClient.getInstance().getOptions().getRequireDeliveryAck()) {  //消息送达提醒
            if(deliveredView != null){
                if (message.isDelivered()) {
                    deliveredView.setVisibility(View.VISIBLE);
                } else {
                    deliveredView.setVisibility(View.INVISIBLE);
                }
            }
        }
        if (EMClient.getInstance().getOptions().getRequireAck()) { //消息已读提醒
            if (ackedView != null) {
                if (message.isAcked()) {
                    if (deliveredView != null) {
                        deliveredView.setVisibility(View.INVISIBLE);
                    }
                    ackedView.setVisibility(View.VISIBLE);
                } else {
                    ackedView.setVisibility(View.INVISIBLE);
                }
            }
        }

        if (itemStyle != null) {
            if (userAvatarView != null) {    //根据配置设置图像形状
                if (itemStyle.isShowAvatar()) {
                    userAvatarView.setVisibility(View.VISIBLE);
                    EaseAvatarOptions avatarOptions = EaseUI.getInstance().getAvatarOptions();
                    if(avatarOptions != null && userAvatarView instanceof EaseImageView){
                        EaseImageView avatarView = ((EaseImageView)userAvatarView);
                        if(avatarOptions.getAvatarShape() != 0)
                            avatarView.setShapeType(avatarOptions.getAvatarShape());
                        if(avatarOptions.getAvatarBorderWidth() != 0)
                            avatarView.setBorderWidth(avatarOptions.getAvatarBorderWidth());
                        if(avatarOptions.getAvatarBorderColor() != 0)
                            avatarView.setBorderColor(avatarOptions.getAvatarBorderColor());
                        if(avatarOptions.getAvatarRadius() != 0)
                            avatarView.setRadius(avatarOptions.getAvatarRadius());
                    }
                } else {
                    userAvatarView.setVisibility(View.GONE);
                }
            }
            if (usernickView != null) {    //根据配置设置昵称是否显示
                if (itemStyle.isShowUserNick())
                    usernickView.setVisibility(View.VISIBLE);
                else
                    usernickView.setVisibility(View.GONE);
            }
            if (bubbleLayout != null) {     //根据配置设置聊天气泡
                if (message.direct() == Direct.SEND) {
                    if (itemStyle.getMyBubbleBg() != null) {
                        bubbleLayout.setBackgroundDrawable(((EaseMessageAdapter) adapter).getMyBubbleBg());
                    }
                } else if (message.direct() == Direct.RECEIVE) {
                    if (itemStyle.getOtherBubbleBg() != null) {
                        bubbleLayout.setBackgroundDrawable(((EaseMessageAdapter) adapter).getOtherBubbleBg());
                    }
                }
            }
        }

    }

    private void setClickListener() {
        if(bubbleLayout != null){  //气泡点击事件
            bubbleLayout.setOnClickListener(new OnClickListener() {
    
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null && itemClickListener.onBubbleClick(message)){
                        return;
                    }
                    if (itemActionCallback != null) {
                        itemActionCallback.onBubbleClick(message);
                    }
                }
            });
    
            bubbleLayout.setOnLongClickListener(new OnLongClickListener() {//气泡长按事件
    
                @Override
                public boolean onLongClick(View v) {
                    if (itemClickListener != null) {
                        itemClickListener.onBubbleLongClick(message);
                    }
                    return true;
                }
            });
        }

        if (statusView != null) {   //重新发送点击事件
            statusView.setOnClickListener(new OnClickListener() {

                @Override
                public void onClick(View v) {
                    if (itemActionCallback != null) {
                        itemActionCallback.onResendClick(message);
                    }
                }
            });
        }

        if(userAvatarView != null){   //用户图像点击事件
            userAvatarView.setOnClickListener(new OnClickListener() {
    
                @Override
                public void onClick(View v) {
                    if (itemClickListener != null) {
                        if (message.direct() == Direct.SEND) { //自己
                            itemClickListener.onUserAvatarClick(EMClient.getInstance().getCurrentUser());
                        } else { //发送方
                            itemClickListener.onUserAvatarClick(message.getFrom());
                        }
                    }
                }
            });
            userAvatarView.setOnLongClickListener(new OnLongClickListener() {  //用户图像长按事件
                
                @Override
                public boolean onLongClick(View v) {
                    if(itemClickListener != null){
                        if (message.direct() == Direct.SEND) {
                            itemClickListener.onUserAvatarLongClick(EMClient.getInstance().getCurrentUser());
                        } else {
                            itemClickListener.onUserAvatarLongClick(message.getFrom());
                        }
                        return true;
                    }
                    return false;
                }
            });
        }
    }

    protected abstract void onInflateView();

    /**
     * find view by id
     */
    protected abstract void onFindViewById();

    /**
     * refresh view when message status change
     */
    protected abstract void onViewUpdate(EMMessage msg);

    /**
     * setup view
     * 
     */
    protected abstract void onSetUpView();
}

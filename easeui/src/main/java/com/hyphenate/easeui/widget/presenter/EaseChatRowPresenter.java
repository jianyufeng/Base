package com.hyphenate.easeui.widget.presenter;

import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.BaseAdapter;

import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.model.styles.EaseMessageListItemStyle;
import com.hyphenate.easeui.widget.EaseAlertDialog;
import com.hyphenate.easeui.widget.EaseChatMessageList;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;

/**
 * Created by zhangsong on 17-10-12.
 * 消息类型视图提供者基类 代理
 */

public abstract class EaseChatRowPresenter implements EaseChatRow.EaseChatRowActionCallback {
    private EaseChatRow chatRow; //布局视图处理

    private Context context;
    private BaseAdapter adapter; //适配器
    private EMMessage message;  //消息
    private int position;   //位置

    @Override
    public void onResendClick(final EMMessage message) {
        new EaseAlertDialog(getContext(), R.string.resend, R.string.confirm_resend, null, new EaseAlertDialog.AlertDialogUser() {
            @Override
            public void onResult(boolean confirmed, Bundle bundle) {
                if (!confirmed) { //重新发送消息
                    return;
                }
                message.setStatus(EMMessage.Status.CREATE); //消息状态 创建成功待发送
                handleSendMessage(message);   //发送
            }
        }, true).show();
    }

    @Override
    public void onBubbleClick(EMMessage message) { //气泡点击事件
    }

    @Override
    public void onDetachedFromWindow() { //视图分离
    }

    public EaseChatRow createChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        this.context = cxt;
        this.adapter = adapter;
        chatRow = onCreateChatRow(cxt, message, position, adapter); //创建聊天视图
        return chatRow;
    }

    public void setup(EMMessage msg, int position,
                      EaseChatMessageList.MessageListItemClickListener itemClickListener,
                      EaseMessageListItemStyle itemStyle) {
        this.message = msg;   //消息
        this.position = position; //位置

        chatRow.setUpView(message, position, itemClickListener, this, itemStyle); //填充数据及设置监听

        handleMessage();
    }
    //发送消息更新消息状态
    protected void handleSendMessage(final EMMessage message) {
        EMMessage.Status status = message.status();

        // Update the view according to the message current status.
        getChatRow().updateView(message);  //根据消息状态更新视图

        if (status == EMMessage.Status.SUCCESS || status == EMMessage.Status.FAIL) {
            return;
        }
        //设置发送消息回调状态
        message.setMessageStatusCallback(new EMCallBack() {
            @Override
            public void onSuccess() {
                getChatRow().updateView(message);
            }

            @Override
            public void onError(int code, String error) {
                Log.i("EaseChatRowPresenter", "onError: " + code + ", error: " + error);
                getChatRow().updateView(message);
            }

            @Override
            public void onProgress(int progress, String status) {
                getChatRow().updateView(message);
            }
        });

        // Already in progress, do not send again
        if (status == EMMessage.Status.INPROGRESS) { //消息发送中
            return;
        }

        // Send the message  发送消息
        EMClient.getInstance().chatManager().sendMessage(message);
    }

    protected void handleReceiveMessage(EMMessage message) {
    }

    protected abstract EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter);

    protected EaseChatRow getChatRow() {
        return chatRow;
    }

    protected Context getContext() {
        return context;
    }

    protected BaseAdapter getAdapter() {
        return adapter;
    }

    protected EMMessage getMessage() {
        return message;
    }

    protected int getPosition() {
        return position;
    }

    private void handleMessage() {
        if (message.direct() == EMMessage.Direct.SEND) {
            handleSendMessage(message); //处理发送消息的状态
        } else if (message.direct() == EMMessage.Direct.RECEIVE) {
            handleReceiveMessage(message); //处理收到消息的状态
        }
    }
}

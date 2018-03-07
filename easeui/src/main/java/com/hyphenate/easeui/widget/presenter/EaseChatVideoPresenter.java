package com.hyphenate.easeui.widget.presenter;

import android.content.Context;
import android.content.Intent;
import android.widget.BaseAdapter;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.easeui.ui.EaseShowVideoActivity;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowVideo;
import com.hyphenate.util.EMLog;

/**
 * Created by zhangsong on 17-10-12.
 * 视频视图提供者
 */

public class EaseChatVideoPresenter extends EaseChatFilePresenter {
    private static final String TAG = "EaseChatVideoPresenter";

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        return new EaseChatRowVideo(cxt, message, position, adapter);//创建视频视图
    }

    @Override
    public void onBubbleClick(EMMessage message) {  //气泡点击事件
        EMVideoMessageBody videoBody = (EMVideoMessageBody) message.getBody();
        EMLog.d(TAG, "video view is on click");
        if(EMClient.getInstance().getOptions().getAutodownloadThumbnail()) {

        }else{
            if(videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING ||
                        videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED){
                // retry download with click event of user
                EMClient.getInstance().chatManager().downloadThumbnail(message); //下载附件
                return;
            }
        }
        Intent intent = new Intent(getContext(), EaseShowVideoActivity.class);
        intent.putExtra("msg", message);
        if (message != null && message.direct() == EMMessage.Direct.RECEIVE && !message.isAcked()
                && message.getChatType() == EMMessage.ChatType.Chat) {//设置已读回执
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        getContext().startActivity(intent);//启动播放视频
    }
}

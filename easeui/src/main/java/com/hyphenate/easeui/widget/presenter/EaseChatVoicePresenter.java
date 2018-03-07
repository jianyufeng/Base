package com.hyphenate.easeui.widget.presenter;

import android.content.Context;
import android.media.MediaPlayer;
import android.os.AsyncTask;
import android.widget.BaseAdapter;
import android.widget.Toast;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVoiceMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.widget.chatrow.EaseChatRow;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowVoice;
import com.hyphenate.easeui.widget.chatrow.EaseChatRowVoicePlayer;
import com.hyphenate.exceptions.HyphenateException;
import com.hyphenate.util.EMLog;

import java.io.File;

/**
 * Created by zhangsong on 17-10-12.
 * 语音类型视图提供者
 */

public class EaseChatVoicePresenter extends EaseChatFilePresenter {
    private static final String TAG = "EaseChatVoicePresenter";

    private EaseChatRowVoicePlayer voicePlayer;

    @Override
    protected EaseChatRow onCreateChatRow(Context cxt, EMMessage message, int position, BaseAdapter adapter) {
        voicePlayer = EaseChatRowVoicePlayer.getInstance(cxt);
        return new EaseChatRowVoice(cxt, message, position, adapter); //创建语音视图
    }

    @Override
    public void onBubbleClick(final EMMessage message) { //气泡点击事件
        String msgId = message.getMsgId();

        if (voicePlayer.isPlaying()) {  //如果正在播放则停止播放
            // Stop the voice play first, no matter the playing voice item is this or others.
            voicePlayer.stop();
            // Stop the voice play animation.
            ((EaseChatRowVoice) getChatRow()).stopVoicePlayAnimation();

            // If the playing voice item is this item, only need stop play.
            String playingId = voicePlayer.getCurrentPlayingId();
            if (msgId.equals(playingId)) {
                return;
            }
        }

        if (message.direct() == EMMessage.Direct.SEND) { //播放发送的语音
            // Play the voice
            String localPath = ((EMVoiceMessageBody) message.getBody()).getLocalUrl();
            File file = new File(localPath);
            if (file.exists() && file.isFile()) {
                playVoice(message);
                // Start the voice play animation.
                ((EaseChatRowVoice) getChatRow()).startVoicePlayAnimation(); //启动动画
            } else {
                asyncDownloadVoice(message); //下载语音
            }
        } else { //接受消息
            final String st = getContext().getResources().getString(R.string.Is_download_voice_click_later);
            if (message.status() == EMMessage.Status.SUCCESS) {
                if (EMClient.getInstance().getOptions().getAutodownloadThumbnail()) {
                    play(message); //收到的语音消息 设置 收听，及状态显示
                } else {
                    EMVoiceMessageBody voiceBody = (EMVoiceMessageBody) message.getBody();
                    EMLog.i(TAG, "Voice body download status: " + voiceBody.downloadStatus());
                    switch (voiceBody.downloadStatus()) { //语音下载状态
                        case PENDING:// Download not begin
                        case FAILED:// Download failed
                            getChatRow().updateView(getMessage()); //下载失败
                            asyncDownloadVoice(message);//重新下载
                            break;
                        case DOWNLOADING:// During downloading
                            Toast.makeText(getContext(), st, Toast.LENGTH_SHORT).show();
                            break;
                        case SUCCESSED:// Download success
                            play(message);//收到的语音消息 设置 收听，及状态显示·
                        break;
                    }
                }
            } else if (message.status() == EMMessage.Status.INPROGRESS) {
                Toast.makeText(getContext(), st, Toast.LENGTH_SHORT).show();
            } else if (message.status() == EMMessage.Status.FAIL) {
                Toast.makeText(getContext(), st, Toast.LENGTH_SHORT).show();
                asyncDownloadVoice(message); //重新下载
            }
        }
    }

    @Override
    public void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (voicePlayer.isPlaying()) { //停止播放
            voicePlayer.stop();
        }
    }

    private void asyncDownloadVoice(final EMMessage message) {
        new AsyncTask<Void, Void, Void>() {
            @Override
            protected Void doInBackground(Void... params) {
                //下载附件  语音下载
                EMClient.getInstance().chatManager().downloadAttachment(message);
                return null;
            }

            @Override
            protected void onPostExecute(Void result) {
                super.onPostExecute(result);
                getChatRow().updateView(getMessage());//完成更新视图状态
            }
        }.execute();
    }

    private void play(EMMessage message) { //播放前的检查处理
        String localPath = ((EMVoiceMessageBody) message.getBody()).getLocalUrl();
        File file = new File(localPath);
        if (file.exists() && file.isFile()) {
            ackMessage(message); //设置 已读  及 收听状态
            playVoice(message); //播放
            // Start the voice play animation.
            ((EaseChatRowVoice) getChatRow()).startVoicePlayAnimation();
        } else {
            EMLog.e(TAG, "file not exist");
        }
    }

    private void ackMessage(EMMessage message) {
        EMMessage.ChatType chatType = message.getChatType();
        if (!message.isAcked() && chatType == EMMessage.ChatType.Chat) { //设置已读回执
            try {
                EMClient.getInstance().chatManager().ackMessageRead(message.getFrom(), message.getMsgId());
            } catch (HyphenateException e) {
                e.printStackTrace();
            }
        }
        if (!message.isListened()) { //设置已经收听
            EMClient.getInstance().chatManager().setVoiceMessageListened(message);
        }
    }

    private void playVoice(EMMessage msg) { //播放
        voicePlayer.play(msg, new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) { //播放完成回调 停止动画
                // Stop the voice play animation.
                ((EaseChatRowVoice) getChatRow()).stopVoicePlayAnimation();
            }
        });
    }
}

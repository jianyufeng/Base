package com.hyphenate.easeui.widget.chatrow;

import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.view.View;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMFileMessageBody;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.chat.EMVideoMessageBody;
import com.hyphenate.easeui.R;
import com.hyphenate.easeui.model.EaseImageCache;
import com.hyphenate.easeui.utils.EaseCommonUtils;
import com.hyphenate.util.DateUtils;
import com.hyphenate.util.EMLog;
import com.hyphenate.util.ImageUtils;
import com.hyphenate.util.TextFormater;

import java.io.File;

public class EaseChatRowVideo extends EaseChatRowFile{
    private static final String TAG = "EaseChatRowVideo";

    private ImageView imageView;
    private TextView sizeView;
    private TextView timeLengthView;

    public EaseChatRowVideo(Context context, EMMessage message, int position, BaseAdapter adapter) {
        super(context, message, position, adapter);
    }

	@Override
	protected void onInflateView() { //视频视图
		inflater.inflate(message.direct() == EMMessage.Direct.RECEIVE ?
				R.layout.ease_row_received_video : R.layout.ease_row_sent_video, this);
	}

	@Override
	protected void onFindViewById() {
	    imageView = ((ImageView) findViewById(R.id.chatting_content_iv));
        sizeView = (TextView) findViewById(R.id.chatting_size_iv);  //大小
        timeLengthView = (TextView) findViewById(R.id.chatting_length_iv); //播放时长
        ImageView playView = (ImageView) findViewById(R.id.chatting_status_btn); //播放按钮
        percentageView = (TextView) findViewById(R.id.percentage); //发送的百分比
	}

	@Override
	protected void onSetUpView() {
	    EMVideoMessageBody videoBody = (EMVideoMessageBody) message.getBody();
        String localThumb = videoBody.getLocalThumb();

        if (localThumb != null) {
            //显示视频图片
            showVideoThumbView(localThumb, imageView, videoBody.getThumbnailUrl(), message);
        }
        if (videoBody.getDuration() > 0) {
            String time = DateUtils.toTime(videoBody.getDuration());//设置时长
            timeLengthView.setText(time);
        }

        if (message.direct() == EMMessage.Direct.RECEIVE) { //设置收到的文件大小
            if (videoBody.getVideoFileLength() > 0) {
                String size = TextFormater.getDataSize(videoBody.getVideoFileLength());
                sizeView.setText(size);
            }
        } else {
            if (videoBody.getLocalUrl() != null && new File(videoBody.getLocalUrl()).exists()) {//设置发送的文件大小
                String size = TextFormater.getDataSize(new File(videoBody.getLocalUrl()).length());
                sizeView.setText(size);
            }
        }

        EMLog.d(TAG,  "video thumbnailStatus:" + videoBody.thumbnailDownloadStatus());
        if (message.direct() == EMMessage.Direct.RECEIVE) { //设置
            if (videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING) {
                imageView.setImageResource(R.drawable.ease_default_image);
            } else {
                // System.err.println("!!!! not back receive, show image directly");
                imageView.setImageResource(R.drawable.ease_default_image);
                if (localThumb != null) {
                    showVideoThumbView(localThumb, imageView, videoBody.getThumbnailUrl(), message);
                }
            }
            return;
        }else{ //发送视频设置状态
            if (videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.DOWNLOADING ||
                    videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.PENDING ||
                        videoBody.thumbnailDownloadStatus() == EMFileMessageBody.EMDownloadStatus.FAILED) {
                progressBar.setVisibility(View.INVISIBLE);
                percentageView.setVisibility(View.INVISIBLE);
                imageView.setImageResource(R.drawable.ease_default_image);
            } else {
                progressBar.setVisibility(View.GONE);
                percentageView.setVisibility(View.GONE);
                imageView.setImageResource(R.drawable.ease_default_image);
                showVideoThumbView(localThumb, imageView, videoBody.getThumbnailUrl(), message);
            }
        }
	}



	/**
     * show video thumbnails
     * 
     * @param localThumb
     *            local path for thumbnail
     * @param iv
     * @param thumbnailUrl
     *            Url on server for thumbnails
     * @param message  设置视频缩略图
     */
    private void showVideoThumbView(final String localThumb, final ImageView iv, String thumbnailUrl, final EMMessage message) {
        // first check if the thumbnail image already loaded into cache
        Bitmap bitmap = EaseImageCache.getInstance().get(localThumb);
        if (bitmap != null) {
            // thumbnail image is already loaded, reuse the drawable
            iv.setImageBitmap(bitmap);
        } else {
            imageView.setImageResource(R.drawable.ease_default_image);
            new AsyncTask<Void, Void, Bitmap>() {

                @Override
                protected Bitmap doInBackground(Void... params) {
                    if (new File(localThumb).exists()) {
                        return ImageUtils.decodeScaleImage(localThumb, 160, 160);
                    } else {
                        return null;
                    }
                }
                
                @Override
                protected void onPostExecute(Bitmap result) {
                    super.onPostExecute(result);
                    if (result != null) {
                        EaseImageCache.getInstance().put(localThumb, result);
                        iv.setImageBitmap(result);

                    } else {
                        if (message.status() == EMMessage.Status.FAIL) {
                            if (EaseCommonUtils.isNetWorkConnected(activity)) {
                                EMClient.getInstance().chatManager().downloadThumbnail(message);
                            }
                        }

                    }
                }
            }.execute();
        }
        
    }

}

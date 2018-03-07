package com.hyphenate.easeui.widget;

import android.content.Context;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.hyphenate.easeui.R;
import com.hyphenate.util.EMLog;

/**
 * primary menu
 */
public class EaseChatPrimaryMenu extends EaseChatPrimaryMenuBase implements OnClickListener {
    private EditText editText;
    private View buttonSetModeKeyboard;
    private RelativeLayout edittext_layout;
    private View buttonSetModeVoice;
    private View buttonSend;
    private View buttonPressToSpeak;
    private ImageView faceNormal;
    private ImageView faceChecked;
    private Button buttonMore;
    private boolean ctrlPress = false;

    public EaseChatPrimaryMenu(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context, attrs);
    }

    public EaseChatPrimaryMenu(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public EaseChatPrimaryMenu(Context context) {
        super(context);
        init(context, null);
    }

    private void init(final Context context, AttributeSet attrs) {
        Context context1 = context;
        LayoutInflater.from(context).inflate(R.layout.ease_widget_chat_primary_menu, this);
        editText = (EditText) findViewById(R.id.et_sendmessage);
        buttonSetModeKeyboard = findViewById(R.id.btn_set_mode_keyboard);
        edittext_layout = (RelativeLayout) findViewById(R.id.edittext_layout);
        buttonSetModeVoice = findViewById(R.id.btn_set_mode_voice);
        buttonSend = findViewById(R.id.btn_send);
        buttonPressToSpeak = findViewById(R.id.btn_press_to_speak);
        faceNormal = (ImageView) findViewById(R.id.iv_face_normal);
        faceChecked = (ImageView) findViewById(R.id.iv_face_checked);
        RelativeLayout faceLayout = (RelativeLayout) findViewById(R.id.rl_face);
        buttonMore = (Button) findViewById(R.id.btn_more);
        edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_normal);

        buttonSend.setOnClickListener(this);
        buttonSetModeKeyboard.setOnClickListener(this);
        buttonSetModeVoice.setOnClickListener(this);
        buttonMore.setOnClickListener(this);
        faceLayout.setOnClickListener(this);
        editText.setOnClickListener(this);
        editText.requestFocus();
        editText.setOnTouchListener(new OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.requestFocus();
                return false;
            }
        });
        //输入框 焦点变化时的 状态改变
        editText.setOnFocusChangeListener(new OnFocusChangeListener() {

            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if (hasFocus) {
                    edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_active);
                } else {
                    edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_normal);
                }

            }
        });
        // listen the text change  输入框文本变化时显示发送按钮 或 加号
        editText.addTextChangedListener(new TextWatcher() {

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!TextUtils.isEmpty(s)) {
                    buttonMore.setVisibility(View.GONE);
                    buttonSend.setVisibility(View.VISIBLE);
                } else {
                    buttonMore.setVisibility(View.VISIBLE);
                    buttonSend.setVisibility(View.GONE);
                }
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        editText.setOnKeyListener(new OnKeyListener() {
            @Override
            public boolean onKey(View v, int keyCode, KeyEvent event) {
                EMLog.d("key", "keyCode:" + keyCode + " action:" + event.getAction());

                // test on Mac virtual machine: ctrl map to KEYCODE_UNKNOWN
                if (keyCode == KeyEvent.KEYCODE_UNKNOWN) {
                    if (event.getAction() == KeyEvent.ACTION_DOWN) {
                        ctrlPress = true;
                    } else if (event.getAction() == KeyEvent.ACTION_UP) {
                        ctrlPress = false;
                    }
                }
                return false;
            }
        });

        editText.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                EMLog.d("key", "keyCode:" + event.getKeyCode() + " action" + event.getAction() + " ctrl:" + ctrlPress);
                if (actionId == EditorInfo.IME_ACTION_SEND ||
                        (event.getKeyCode() == KeyEvent.KEYCODE_ENTER &&
                                event.getAction() == KeyEvent.ACTION_DOWN &&
                                ctrlPress == true)) {
                    String s = editText.getText().toString();
                    editText.setText("");
                    listener.onSendBtnClicked(s);
                    return true;
                } else {
                    return false;
                }
            }
        });

        //按住说活
        buttonPressToSpeak.setOnTouchListener(new OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (listener != null) {  //触摸点击  按住说话
                    return listener.onPressToSpeakBtnTouch(v, event);
                }
                return false;
            }
        });
    }

    /**
     * set recorder view when speak icon is touched
     *
     * @param voiceRecorderView
     */
    public void setPressToSpeakRecorderView(EaseVoiceRecorderView voiceRecorderView) {
        EaseVoiceRecorderView voiceRecorderView1 = voiceRecorderView;
    }

    /**
     * append emoji icon to editText
     * 添加表情
     * @param emojiContent
     */
    public void onEmojiconInputEvent(CharSequence emojiContent) {
        editText.append(emojiContent);
    }

    /**
     * delete emojicon
     * 删除表情
     */
    @Override
    public void onEmojiconDeleteEvent() {
        if (!TextUtils.isEmpty(editText.getText())) {
            KeyEvent event = new KeyEvent(0, 0, 0, KeyEvent.KEYCODE_DEL, 0, 0, 0, 0, KeyEvent.KEYCODE_ENDCALL);
            editText.dispatchKeyEvent(event);
        }
    }

    /**
     * on clicked event
     *
     * @param view 输入及其相关控件的点击事件回调
     */
    @Override
    public void onClick(View view) {
        int id = view.getId();
        if (id == R.id.btn_send) { //发送按钮
            if (listener != null) {  //发送回调
                String s = editText.getText().toString();
                editText.setText("");
                listener.onSendBtnClicked(s);
            }
        } else if (id == R.id.btn_set_mode_voice) { //点击语音按钮
            setModeVoice();  //点击语音按钮逻辑显示处理
            showNormalFaceImage();   //重置表情按钮状态
            if (listener != null)  //点击进入语音录入状态
                listener.onToggleVoiceBtnClicked();
        } else if (id == R.id.btn_set_mode_keyboard) { //点击小键盘按钮
            setModeKeyboard();  //显示输入框
            showNormalFaceImage(); //重置表情按钮状态
            if (listener != null)  // 点击退出语音录入状态
                listener.onToggleVoiceBtnClicked();
        } else if (id == R.id.btn_more) {  //点击 加号 按钮
            buttonSetModeVoice.setVisibility(View.VISIBLE); //显示语音按钮
            buttonSetModeKeyboard.setVisibility(View.GONE); //隐藏小键盘按钮
            edittext_layout.setVisibility(View.VISIBLE); //显示输入框容器
            buttonPressToSpeak.setVisibility(View.GONE); //隐藏 按住说话 容器
            edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_normal);
            showNormalFaceImage();   //重置表情按钮状态
            if (listener != null)   //点击加号 回调  切换显示扩展容器
                listener.onToggleExtendClicked();
        } else if (id == R.id.et_sendmessage) {  //点击输入框
            edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_active);
            showNormalFaceImage();  //重置表情按钮状态
            if (listener != null)  //点击输入框的回调
                listener.onEditTextClicked();
        } else if (id == R.id.rl_face) {   //点击表情显示切换
            toggleFaceImage();
            edittext_layout.setBackgroundResource(R.drawable.ease_input_bar_bg_normal);
            if (listener != null) {//表情切换回调
                listener.onToggleEmojiconClicked();
            }
        } else {
        }
    }


    /**
     * show voice icon when speak bar is touched
     * 点击语音按钮
     */
    protected void setModeVoice() {
        hideKeyboard();
        edittext_layout.setVisibility(View.GONE);  //隐藏输入框容器
        buttonSetModeVoice.setVisibility(View.GONE);  //隐藏语音按钮
        buttonSetModeKeyboard.setVisibility(View.VISIBLE); //显示键盘按钮
        buttonSend.setVisibility(View.GONE);       // 发送按钮
        buttonMore.setVisibility(View.VISIBLE);   //显示加号按钮
        buttonPressToSpeak.setVisibility(View.VISIBLE); //显示 按住说话容器
    }

    /**
     * show keyboard
     */
    protected void setModeKeyboard() {
        edittext_layout.setVisibility(View.VISIBLE); //显示输入框容器
        buttonSetModeKeyboard.setVisibility(View.GONE); //隐藏键盘按钮
        buttonSetModeVoice.setVisibility(View.VISIBLE); //显示语音按钮
        // mEditTextContent.setVisibility(View.VISIBLE);
        editText.requestFocus();                        //输入聚焦
        // buttonSend.setVisibility(View.VISIBLE);
        buttonPressToSpeak.setVisibility(View.GONE);  //隐藏 按住说话
        if (TextUtils.isEmpty(editText.getText())) {  //处理显示发送按钮
            buttonMore.setVisibility(View.VISIBLE);
            buttonSend.setVisibility(View.GONE);
        } else {
            buttonMore.setVisibility(View.GONE);
            buttonSend.setVisibility(View.VISIBLE);
        }

    }


    protected void toggleFaceImage() { //反转表情是否点击选中
        if (faceNormal.getVisibility() == View.VISIBLE) {
            showSelectedFaceImage();
        } else {
            showNormalFaceImage();
        }
    }

    private void showNormalFaceImage() { //显示表情未选中
        faceNormal.setVisibility(View.VISIBLE);
        faceChecked.setVisibility(View.INVISIBLE);
    }

    private void showSelectedFaceImage() {//显示表情选中
        faceNormal.setVisibility(View.INVISIBLE);
        faceChecked.setVisibility(View.VISIBLE);
    }


    @Override
    public void onExtendMenuContainerHide() {
        showNormalFaceImage();//显示表情未选中逻辑
    }

    @Override
    public void onTextInsert(CharSequence text) {
        int start = editText.getSelectionStart();
        Editable editable = editText.getEditableText();
        editable.insert(start, text);
        setModeKeyboard();
    }

    @Override
    public EditText getEditText() {
        return editText;
    }

}

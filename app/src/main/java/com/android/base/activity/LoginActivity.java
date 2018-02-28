package com.android.base.activity;

import android.content.Intent;
import android.graphics.Rect;
import android.os.Bundle;
import android.view.View;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.base.R;
import com.fpi.mobile.app.ActivityLifeManager;
import com.fpi.mobile.base.BaseActivity;
import com.fpi.mobile.view.ClearEditText;
import com.hyphenate.EMCallBack;
import com.hyphenate.chat.EMClient;

/**
 * Created by 16896 on 2018/2/28.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener {
    /**
     * 用户名
     */
    private ClearEditText mEditUse;
    /**
     * 密码
     */
    private ClearEditText mEditPassword;
    /**
     * 登录
     */
    private Button mBtnLogin;
    private ImageView mIvIcon;
    private RelativeLayout mLayoutIcon;
    private RelativeLayout mLayoutContent;

    @Override
    public void preData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        initView();
        addLayoutListener(mLayoutContent, mBtnLogin);
        ActivityLifeManager.getInstance().finishAllActivityExcept(this);


        if (EMClient.getInstance().isLoggedInBefore()) {
            //enter to main activity directly if you logged in before.
            goActivityAndFinish(MainTabActivity.class);
        }


    }

    private void initView() {
        mEditUse = (ClearEditText) findViewById(R.id.edit_use);
        mEditPassword = (ClearEditText) findViewById(R.id.edit_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        mIvIcon = (ImageView) findViewById(R.id.iv_icon);
        mLayoutIcon = (RelativeLayout) findViewById(R.id.layout_icon);
        mLayoutContent = (RelativeLayout) findViewById(R.id.layout_content);
    }

    /**
     * 1、获取main在窗体的可视区域
     * 2、获取main在窗体的不可视区域高度
     * 3、判断不可视区域高度
     * 1、大于150：键盘显示  获取Scroll的窗体坐标
     * 算出main需要滚动的高度，使scroll显示。
     * 2、小于150：键盘隐藏
     *
     * @param main   根布局
     * @param scroll 需要显示的最下方View
     */
    public void addLayoutListener(final View main, final View scroll) {
        main.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                main.getWindowVisibleDisplayFrame(rect);
                int mainInvisibleHeight = main.getRootView().getHeight() - rect.bottom;
                if (mainInvisibleHeight > 150) {
                    int[] location = new int[2];
                    scroll.getLocationInWindow(location);
                    int srollHeight = (location[1] + scroll.getHeight()) - rect.bottom;
                    if (srollHeight > 0) {
                        main.scrollTo(0, srollHeight);
                    }
                } else {
                    main.scrollTo(0, 0);
                }
            }
        });
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_login:
                //login
                EMClient.getInstance().login(mEditUse.getText().toString(), mEditPassword.getText().toString(), new EMCallBack() {
                    @Override
                    public void onSuccess() {
                        goActivityAndFinish(MainTabActivity.class);
                    }

                    @Override
                    public void onProgress(int progress, String status) {

                    }

                    @Override
                    public void onError(int code, String error) {
                        runOnUiThread(new Runnable() {
                            public void run() {
                                Toast.makeText(getApplicationContext(), "login failed", 0).show();
                            }
                        });
                    }
                });
                break;
        }
    }
}

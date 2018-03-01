package com.android.base.activity.login;

import android.graphics.Rect;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewTreeObserver;
import android.view.inputmethod.EditorInfo;
import android.widget.Button;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.base.R;
import com.android.base.activity.MainTabActivity;
import com.android.base.activity.login.p.LoginPresent;
import com.android.base.activity.login.v.ILoginView;
import com.fpi.mobile.app.ActivityLifeManager;
import com.fpi.mobile.base.BaseActivity;
import com.fpi.mobile.view.ClearEditText;
import com.hyphenate.chat.EMClient;

/**
 * Created by 16896 on 2018/2/28.
 */

public class LoginActivity extends BaseActivity implements View.OnClickListener, ILoginView {
    LoginPresent loginPresent;
    /**
     * 用户名
     */
    private ClearEditText mEditUser;
    /**
     * 密码
     */
    private ClearEditText mEditPassword;
    /**
     * 登录
     */
    private Button mBtnLogin;
    private RelativeLayout mLayoutContent;

    @Override
    public void preData() {
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (EMClient.getInstance().isLoggedInBefore()) {
            //enter to main activity directly if you logged in before.
            goActivityAndFinish(MainTabActivity.class);
            return;
        }
        loginPresent = new LoginPresent(this);
        setContentView(R.layout.act_login);
        initView();
        addLayoutListener(mLayoutContent, mBtnLogin);
        ActivityLifeManager.getInstance().finishAllActivityExcept(this);
    }

    private void initView() {
        mEditUser = (ClearEditText) findViewById(R.id.edit_user);
        mEditPassword = (ClearEditText) findViewById(R.id.edit_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
        mLayoutContent = (RelativeLayout) findViewById(R.id.layout_content);
        // if user changed, clear the password
        mEditUser.addTextChangedListener(new TextWatcher() {
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                mEditPassword.setText(null);
            }

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
        mEditPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_DONE || ((event.getKeyCode() == KeyEvent.KEYCODE_ENTER) && (event.getAction() == KeyEvent.ACTION_DOWN))) {
                  loginPresent.login();
                    return true;
                } else {
                    return false;
                }
            }
        });
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
            case R.id.btn_login:
                //登录
                loginPresent.login();
                break;
        }
    }

    @Override
    public void showLoading() {
        showProgress();
    }

    @Override
    public String getUserName() {
        return mEditUser.getText().toString().trim();
    }

    @Override
    public String getPassword() {
        return mEditPassword.getText().toString().trim();
    }

    @Override
    public void dismissLoading() {
        dismissProgress();
    }

    @Override
    public void goMainTabActivity() {
        goActivityAndFinish(MainTabActivity.class);
    }

    @Override
    public void alert(String s) {
        showToast(s);
    }
}

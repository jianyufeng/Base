package com.android.base.activity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.android.base.R;
import com.fpi.mobile.base.BaseActivity;
import com.fpi.mobile.view.ClearEditText;

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

    @Override
    public void preData() {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_login);
        initView();

    }

    private void initView() {
        mEditUse = (ClearEditText) findViewById(R.id.edit_use);
        mEditPassword = (ClearEditText) findViewById(R.id.edit_password);
        mBtnLogin = (Button) findViewById(R.id.btn_login);
        mBtnLogin.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            default:
                break;
            case R.id.btn_login:
                goActivityAndFinish(MainTabActivity.class);
                break;
        }
    }
}

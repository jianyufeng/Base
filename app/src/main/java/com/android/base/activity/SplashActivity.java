package com.android.base.activity;

import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.support.annotation.NonNull;

import com.android.base.constant.Constant;
import com.android.base.util.CheckAndTransportFile;
import com.fpi.mobile.base.BaseActivity;
import com.fpi.mobile.permission.DangerousPermissions;
import com.fpi.mobile.permission.PermissionsHelper;

import java.io.File;

import static com.fpi.mobile.permission.DangerousPermissions.PERMISSIONS;


/**
 * Created by 14165 on 2017/5/17.
 */

public class SplashActivity extends BaseActivity {
    //欢迎界面展示800ms
    private int splashTime = 1000;


    private void goMainActivity() {
        checkFile();
        long absTime = System.currentTimeMillis() - tempTime;
        long holdTime = 0;
        if (absTime <= splashTime) {
            holdTime = splashTime - absTime;
        }
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Intent intent = new Intent(SplashActivity.this, LoginActivity.class);
                startActivity(intent);
                finish();
            }
        }, holdTime);
    }

    private long tempTime;

    @Override
    public void preData() {
        tempTime = System.currentTimeMillis();
        if (Build.VERSION.SDK_INT >= 23) {
            checkPermissions();
        } else {
            goMainActivity();
        }
    }
    /**
     * 百度地图配置文件是否存在
     */
    private void checkFile() {
        File file = new File(Constant.FILE_DIR, Constant.BAIDU_MAP_NAME);
        if (!file.exists()) {
            new Thread(new CheckAndTransportFile(this)).start();
        }
    }

    private PermissionsHelper permissionsHelper;

    /**
     * 6.0以上系统需要检查权限
     */
    private void checkPermissions() {
        //危险权限需要根据实际情况调整
        DangerousPermissions.PERMISSIONS = new String[]{
                DangerousPermissions.STORAGE};
        permissionsHelper = new PermissionsHelper(this, PERMISSIONS);
        if (permissionsHelper.checkAllPermissions(PERMISSIONS)) {
            permissionsHelper.onDestroy();
            goMainActivity();
        } else {
            //申请权限
            permissionsHelper.startRequestNeedPermissions();
        }
        permissionsHelper.setonAllNeedPermissionsGrantedListener(new PermissionsHelper.onAllNeedPermissionsGrantedListener() {
            @Override
            public void onAllNeedPermissionsGranted() {
                goMainActivity();
            }

            @Override
            public void onPermissionsDenied() {
                finish();
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        permissionsHelper.onRequestPermissionsResult(requestCode, permissions, grantResults);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        permissionsHelper.onActivityResult(requestCode, resultCode, data);
    }

}

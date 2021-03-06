package com.android.base.app;


import com.android.base.BuildConfig;
import com.android.base.R;
import com.android.base.activity.login.bean.ModelUserInfo;
import com.android.base.constant.Constant;
import com.fpi.mobile.base.BaseApplication;
import com.fpi.mobile.bean.ModelUserBase;
import com.fpi.mobile.constant.ServerUrl;
import com.fpi.mobile.utils.StringTool;
import com.hyphenate.easeui.EaseUI;
import com.socks.library.KLog;



/**
 * Created by 14165 on 2017/3/10.
 */

public class MainApplication extends BaseApplication {
    @Override
    protected void init() {
        //APP名称
        Constant.APP_NAME = "DWSQ";
        colorStatusBar = R.color.colorPrimaryDark;
        //服务器地址
        ServerUrl.BASE_URL = "http://219.233.250.162:8090";
        //百度地图
//        SDKInitializer.initialize(getApplicationContext());
        KLog.init(BuildConfig.LOG_DEBUG);

        EaseUI.getInstance().init(this, null);
    }

    @Override
    public boolean isLogin() {
        ModelUserBase currUser = getCurrUser();
        return (currUser instanceof ModelUserInfo && !StringTool.isEmpty(currUser.getPassword()));
    }


    @Override
    protected void setCrashHandler() {
      /*  //是否保存crash日志
        CrashConstant.SAVE = true;
        //  日志存储文件夹名、路径
        CrashConstant.CRASH_DIR = "DwsqCrash";
        CrashConstant.CRASH_PATH = CrashConstant.CRASH_ROOT + CrashConstant.CRASH_DIR;
        //  crash监听
        CrashHandler.getInstance().setCrashHanler(getApplicationContext());*/
    }
}

package com.android.base.crash;

import com.android.base.activity.MainTabActivity;
import com.fpi.mobile.crash.CrashHandlerBase;

/**
 * Created by 14165 on 2017/3/10.
 */

public class CrashHandler extends CrashHandlerBase {
    private static CrashHandler mInstance = new CrashHandler();

    public static CrashHandler getInstance() {
        return mInstance;
    }

    @Override
    protected Class<?> getRestartActivity() {
        return MainTabActivity.class;
    }
}

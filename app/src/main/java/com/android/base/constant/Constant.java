package com.android.base.constant;

import android.os.Environment;

import com.fpi.mobile.constant.ConstantBase;

/**
 * Created by 14165 on 2017/3/10.
 */

public class Constant extends ConstantBase {

    public static String FILE_DIR = Environment.getExternalStorageDirectory().getPath() + "/FpiDwsq/";

    public static final String BAIDU_MAP_NAME = "custom_config_roadcolor.txt";
    //主页tab点击事件
    public static final String CLICK_TAB_HOME = "click_tab_home";
    public static final String CLICK_TAB_STATISTICS = "click_tab_home";
    public static final String CLICK_TAB_ALARM = "click_tab_alarm";

}

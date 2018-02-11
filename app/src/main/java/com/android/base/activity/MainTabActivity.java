package com.android.base.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;

import com.android.base.R;
import com.android.base.activity.present.UserPresenter;
import com.android.base.adapter.MaintabAdapter;
import com.android.base.constant.Constant;
import com.android.base.fragment.AlarmFragment;
import com.android.base.fragment.HomeFragment;
import com.android.base.fragment.MineFragment;
import com.android.base.fragment.StatisticsFragment;
import com.android.base.model.user.ModelUserInfo;
import com.android.base.view.TabView;
import com.fpi.mobile.base.BaseActivity;
import com.fpi.mobile.bean.ModelBase;
import com.fpi.mobile.network.BaseNetworkInterface;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import org.greenrobot.eventbus.EventBus;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageBottomTabLayout;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

public class MainTabActivity extends BaseActivity implements BaseNetworkInterface {
    private NavigationController navigationController;
    private UserPresenter userPresenter;

    @Override
    public void preData() {
//        userPresenter = new UserPresenter(this);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initView();
        updateAPP(); //检查更新
    }

    private void initView() {
        PageBottomTabLayout tab = (PageBottomTabLayout) findViewById(R.id.tab_main);
        navigationController = tab.custom()
                .addItem(newItem(R.mipmap.ic_menu1_1, R.mipmap.ic_menu1_2, "首页"))
                .addItem(newItem(R.mipmap.ic_menu2_1, R.mipmap.ic_menu2_2, "统计"))
                .addItem(newItem(R.mipmap.ic_menu3_1, R.mipmap.ic_menu3_2, "报警"))
                .addItem(newItem(R.mipmap.ic_menu4_1, R.mipmap.ic_menu4_2, "我的"))
                .build();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(4);
        Class fragmentArray[] = {HomeFragment.class, StatisticsFragment.class, AlarmFragment.class, MineFragment.class};
        viewPager.setAdapter(new MaintabAdapter(mContext, fragmentManager, fragmentArray));
        //自动适配ViewPager页面切换
        navigationController.setupWithViewPager(viewPager);
        navigationController.addTabItemSelectedListener(new OnTabItemSelectedListener() {
            @Override
            public void onSelected(int index, int old) {
                dealTabSelect(index); //底部导航点击事件

            }


            @Override
            public void onRepeat(int index) {

            }
        });

    }

    //设置报警的小红点
    private void setUnRead(boolean unReadread) {
        navigationController.setHasMessage(2, unReadread);
    }

    private void dealTabSelect(int index) {
        ModelBase modelBase = new ModelBase();
        switch (index) {
            case 0:
                modelBase.setName(Constant.CLICK_TAB_HOME);
                break;
            case 1:
                modelBase.setName(Constant.CLICK_TAB_STATISTICS);
                break;
            case 2:
                if (isFirst) {
                    isFirst = false;

                }
                setUnRead(false);//处理未读消息
                modelBase.setName(Constant.CLICK_TAB_ALARM);
                break;
        }
        EventBus.getDefault().post(modelBase);

    }

    private Boolean isFirst = true;

    private BaseTabItem newItem(int drawable, int checkedDrawable, String text) {
        TabView mainTab = new TabView(this);
        mainTab.initialize(drawable, checkedDrawable, text);
        mainTab.setTextDefaultColor(0xff9E9E9E);
        mainTab.setTextCheckedColor(0xff2196F3);
        return mainTab;
    }

    long firstTime;

    /**
     * 返回键退出系统
     */
    @Override
    public void onBackPressed() {
        long secondTime = System.currentTimeMillis();
        if (secondTime - firstTime > 2000) {
            // 如果两次按键时间间隔大于2秒，则不退出
            showToast("再按一次退出程序");
            // 更新firstTime
            firstTime = secondTime;
        } else {
            // 两次按键小于2秒时，退出应用
            finish();
        }
    }

    @Override
    public void loadding() {

    }

    @Override
    public void loaddingFinish() {

    }

    @Override
    public void requestSuccess(Object object) {
        // 1 登陆成功  返回数据
        if (object instanceof ModelUserInfo) {
            ModelUserInfo modelUser = (ModelUserInfo) object;
            if (modelUser != null) {
            }
        }

    }

    @Override
    public void requestError(String s) {

    }

    private void updateAPP() {
        PgyUpdateManager.register(this,
                new UpdateManagerListener() {
                    @Override
                    public void onUpdateAvailable(final String result) {
                        // 将新版本信息封装到AppBean中
                        final AppBean appBean = getAppBeanFromString(result);
                        new AlertDialog.Builder(MainTabActivity.this)
                                .setTitle("新版本：V" + appBean.getVersionName())
                                .setMessage(appBean.getReleaseNote())
                                .setNegativeButton("稍后再说", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        dialog.dismiss();
                                    }
                                })
                                .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog, int which) {
                                        startDownloadTask(MainTabActivity.this, appBean.getDownloadURL());
                                    }
                                }).show();
                    }

                    @Override
                    public void onNoUpdateAvailable() {
                    }
                });
    }
}

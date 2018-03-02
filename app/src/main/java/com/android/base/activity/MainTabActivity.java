package com.android.base.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;

import com.android.base.R;
import com.android.base.activity.login.bean.ModelUserInfo;
import com.android.base.activity.present.UserPresenter;
import com.android.base.adapter.MainTabAdapter;
import com.android.base.constant.Constant;
import com.android.base.fragment.DatabaseFragment;
import com.android.base.fragment.HomeFragment;
import com.android.base.view.TabView;
import com.fpi.mobile.base.BaseActivity;
import com.fpi.mobile.bean.ModelBase;
import com.fpi.mobile.network.BaseNetworkInterface;
import com.hyphenate.chat.EMConversation;
import com.hyphenate.easeui.EaseConstant;
import com.hyphenate.easeui.domain.EaseUser;
import com.hyphenate.easeui.ui.EaseContactListFragment;
import com.hyphenate.easeui.ui.EaseConversationListFragment;
import com.pgyersdk.javabean.AppBean;
import com.pgyersdk.update.PgyUpdateManager;
import com.pgyersdk.update.UpdateManagerListener;

import org.greenrobot.eventbus.EventBus;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import me.majiajie.pagerbottomtabstrip.NavigationController;
import me.majiajie.pagerbottomtabstrip.PageNavigationView;
import me.majiajie.pagerbottomtabstrip.item.BaseTabItem;
import me.majiajie.pagerbottomtabstrip.listener.OnTabItemSelectedListener;

public class MainTabActivity extends BaseActivity implements BaseNetworkInterface {
    private NavigationController navigationController;
    private UserPresenter userPresenter;
    private EaseConversationListFragment conversationListFragment;
    private EaseContactListFragment contactListFragment;
    @Override
    public void preData() {
//        userPresenter = new UserRequest(this);

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.act_main);
        initView();
        updateAPP(); //检查更新
    }

    private void initView() {
        PageNavigationView tab = (PageNavigationView) findViewById(R.id.tab_main);
        navigationController = tab.custom()
                .addItem(newItem(R.mipmap.ic_menu1_1, R.mipmap.ic_menu1_2, "首页"))
                .addItem(newItem(R.mipmap.ic_menu2_1, R.mipmap.ic_menu2_2, "资料库"))
                .addItem(newItem(R.mipmap.ic_menu3_1, R.mipmap.ic_menu3_2, "通讯录"))
                .addItem(newItem(R.mipmap.ic_menu4_1, R.mipmap.ic_menu4_2, "我的"))
                .build();
        ViewPager viewPager = (ViewPager) findViewById(R.id.viewPager);
        viewPager.setOffscreenPageLimit(4);
        ArrayList<Fragment> fragments = new ArrayList<>();
        conversationListFragment = new HomeFragment();
        fragments.add( conversationListFragment);
        fragments.add(new DatabaseFragment());
        contactListFragment = new EaseContactListFragment();
        contactListFragment.setContactsMap(getContacts());
        fragments.add(contactListFragment);
//        fragments.add(new HomeFragment());
//        fragments.add(new ContactsFragment());
        SettingsFragment settingFragment = new SettingsFragment();
        fragments.add(settingFragment);
//        fragments.add(new MineFragment());

        conversationListFragment.setConversationListItemClickListener(new EaseConversationListFragment.EaseConversationListItemClickListener() {

            @Override
            public void onListItemClicked(EMConversation conversation) {
                startActivity(new Intent(MainTabActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, conversation.conversationId()));
            }
        });
        contactListFragment.setContactListItemClickListener(new EaseContactListFragment.EaseContactListItemClickListener() {

            @Override
            public void onListItemClicked(EaseUser user) {
                startActivity(new Intent(MainTabActivity.this, ChatActivity.class).putExtra(EaseConstant.EXTRA_USER_ID, user.getUsername()));
            }
        });


        viewPager.setAdapter(new MainTabAdapter(mContext, fragmentManager, fragments));
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
    /**
     * prepared users, password is "123456"
     * you can use these user to test
     * @return
     */
    private Map<String, EaseUser> getContacts(){
        Map<String, EaseUser> contacts = new HashMap<String, EaseUser>();
        for(int i = 1; i <= 10; i++){
            EaseUser user = new EaseUser("easeuitest" + i);
            contacts.put("easeuitest" + i, user);
        }
        return contacts;
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

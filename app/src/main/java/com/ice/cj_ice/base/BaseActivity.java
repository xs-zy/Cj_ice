package com.ice.cj_ice.base;

import android.content.IntentFilter;
import android.net.ConnectivityManager;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;

import com.ice.cj_ice.leyaoyao.SimpleMsgHandler;
import com.ice.cj_ice.util.ArmUtil;
import com.ice.cj_ice.util.NetWorkChangReceiver;
import com.ice.cj_ice.util.Params;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import NDKLoader.HitbotNDKLoader;
import cn.lyy.netty.client.NettyClient;


/**
 * Created by Administrator on 2019/4/4.
 */

public abstract class BaseActivity extends App {
    private boolean isRegistered = false;
    private NetWorkChangReceiver netWorkChangReceiver;
    private HitbotNDKLoader armUtil;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //注册网络状态监听广播
        netWorkChangReceiver = new NetWorkChangReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.WIFI_STATE_CHANGED_ACTION);
        filter.addAction(WifiManager.NETWORK_STATE_CHANGED_ACTION);
        filter.addAction(ConnectivityManager.CONNECTIVITY_ACTION);
        registerReceiver(netWorkChangReceiver, filter);
        armUtil = ArmUtil.getArmUtil();
        isRegistered = true;
        App.activity = this;
        layoutId();
        initView();
        initData();
        initListener();
        loadData();
    }



    //布局绑定
    protected abstract void layoutId();
    //初始化View
    protected abstract void initView();
    //初始化数据
    protected abstract void initData();
    //初始化监听
    protected abstract void initListener();
    //加载数据
    protected abstract void loadData();
    //跳转
    protected abstract void in(Class tClass);


    //查看机械臂是否连接
    public boolean is_connect(){
        boolean tag;
        int connect = armUtil.is_connect();
        if(connect == 1){
            tag = true;
        }else {
            tag = false;
        }
        return tag;
    }

    //回退
    public void out(){
        finish();
    }
    //BACK键
    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if ((keyCode == KeyEvent.KEYCODE_BACK)) {
            out();
            return false;
        }else {
            return super.onKeyDown(keyCode, event);
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //解绑
        if (isRegistered) {
            unregisterReceiver(netWorkChangReceiver);
        }
    }
}

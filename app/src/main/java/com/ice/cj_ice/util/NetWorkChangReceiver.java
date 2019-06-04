package com.ice.cj_ice.util;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import com.ice.cj_ice.leyaoyao.eventbus.QcodeEvent;
import com.ice.cj_ice.leyaoyao.eventbus.WifiStateEvent;

import org.greenrobot.eventbus.EventBus;

/**
 * Created by Administrator on 2019/4/16.
 */

public class NetWorkChangReceiver extends BroadcastReceiver {

   /**
     * 获取连接类型
     * @param type
     * @return
     */
    private String getConnectionType(int type) {

        String connType = "";
        if (type == ConnectivityManager.TYPE_MOBILE) {
            connType = "4G网络数据";
        } else if (type == ConnectivityManager.TYPE_WIFI) {
            connType = "WIFI网络";
        }else if(type == ConnectivityManager.TYPE_ETHERNET){
            connType = "有线网络数据";
        }
        return connType;

    }

    @Override
    public void onReceive(Context context, Intent intent) {

        if (WifiManager.WIFI_STATE_CHANGED_ACTION.equals(intent.getAction())) {// 监听wifi的打开与关闭，与wifi的连接无关
            int wifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, 0);
            Log.e("TAG", "wifiState:" + wifiState);
            //语音提示   wifiState = 3    无网络连接
            switch (wifiState) {
                case WifiManager.WIFI_STATE_DISABLED:
                    break;
                case WifiManager.WIFI_STATE_DISABLING:
                    break;
            }
        }

        // 监听网络连接，包括wifi和移动数据的打开和关闭,以及连接上可用的连接都会接到监听
        if (ConnectivityManager.CONNECTIVITY_ACTION.equals(intent.getAction())) {
            //获取联网状态的NetworkInfo对象
            NetworkInfo info = intent.getParcelableExtra(ConnectivityManager.EXTRA_NETWORK_INFO);
            if (info != null) {
                //如果当前的网络连接成功并且网络连接可用
                if (NetworkInfo.State.CONNECTED == info.getState() && info.isAvailable()) {
                    if (info.getType() == ConnectivityManager.TYPE_WIFI || info.getType() == ConnectivityManager.TYPE_MOBILE || info.getType() == ConnectivityManager.TYPE_ETHERNET) {
                        Log.i("TAG", getConnectionType(info.getType()) + "连上");
                        EventBus.getDefault().post(new WifiStateEvent(1));
                    }
                } else {
                    Log.i("TAG", getConnectionType(info.getType()) + "断开");
                    EventBus.getDefault().post(new WifiStateEvent(0));
                }
            }
        }

    }
}

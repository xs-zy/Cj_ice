package com.ice.cj_ice.util;

import android.content.Intent;
import android.os.Handler;
import android.os.Message;

import com.ice.cj_ice.base.App;

import java.util.Timer;
import java.util.TimerTask;

/**
 * Created by Administrator on 2019/5/22.
 */

public class TimerUtil {
    Timer mTimer;
    int recLen;
    public TimerTask mTimerTask;

    public void initsTimer() {
        //计数器，每次减一秒。
        mTimerTask = new TimerTask() {
            @Override
            public void run() {
                if (recLen == 0) {
                    recLen = 90;
                } else {
                    //计数器，每次减一秒。
                    recLen -= 1;
                }
                Message message = new Message();
                message.what = 0;
                message.obj = recLen;
                mHandler.sendMessage(message);
            }
        };
        mTimer = new Timer();
    }


    //实现更新主线程UI
    Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 0:

                    break;
            }
        }
    };


}

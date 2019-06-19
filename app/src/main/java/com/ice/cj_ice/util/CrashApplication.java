package com.ice.cj_ice.util;

import android.app.Application;

/**
 * Created by Administrator on 2019/6/13.
 */

public class CrashApplication extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        CrashHandler.getInstance().init(this);
    }
}

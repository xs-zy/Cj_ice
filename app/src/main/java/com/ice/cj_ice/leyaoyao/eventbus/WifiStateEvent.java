package com.ice.cj_ice.leyaoyao.eventbus;

/**
 * Created by Administrator on 2019/6/4.
 */

public class WifiStateEvent {
    public int state;

    public WifiStateEvent(int state){
        this.state = state;
    }

    public int getState() {
        return state;
    }

    public void setState(int state) {
        this.state = state;
    }
}

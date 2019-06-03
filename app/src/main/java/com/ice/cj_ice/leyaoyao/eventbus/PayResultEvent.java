package com.ice.cj_ice.leyaoyao.eventbus;

/**
 * Created by Administrator on 2019/5/10.
 */

public class PayResultEvent {
    public String result;

    public PayResultEvent(String result){
        this.result = result;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}

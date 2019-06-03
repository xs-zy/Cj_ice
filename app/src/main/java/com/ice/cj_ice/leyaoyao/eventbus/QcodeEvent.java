package com.ice.cj_ice.leyaoyao.eventbus;

/**
 * Created by Administrator on 2019/5/10.
 */

public class QcodeEvent {
    public String qcode;

    public QcodeEvent(String qcode){
        this.qcode = qcode;
    }

    public String getQcode() {
        return qcode;
    }

    public void setQcode(String qcode) {
        this.qcode = qcode;
    }
}

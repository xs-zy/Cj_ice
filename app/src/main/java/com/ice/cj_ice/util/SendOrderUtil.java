package com.ice.cj_ice.util;


import com.ice.cj_ice.base.App;

/**
 * Created by Administrator on 2019/1/18.
 */

public class SendOrderUtil {

    private static SendOrderUtil sendOrderUtil = null;
    private SendOrderUtil(){}
    public static SendOrderUtil sendOrderUtil(){
        if (sendOrderUtil == null) {
            synchronized (App.activity){
                if (sendOrderUtil == null) {
                    sendOrderUtil = new SendOrderUtil();
                }
            }
        }
        return sendOrderUtil;
    }

    public static byte[] getSendOrderUtil(byte... args){
        int length = args.length;
        byte[] buf = new byte[length];
        for (int i = 0;i<length;i++){
            buf[i] = args[i];
        }
        return buf;
    }


}

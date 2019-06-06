package com.ice.cj_ice.util;

import com.ice.cj_ice.leyaoyao.SimpleMsgHandler;
import cn.lyy.netty.client.NettyClient;

/**
 * Created by Administrator on 2019/6/6.
 */

public class LeyaoyaoUtil {
    private static NettyClient nettyClient = null;
    public static synchronized NettyClient getLeyaoyaoUtil(){
        if(nettyClient == null)
            synchronized (LeyaoyaoUtil.class){
                if(nettyClient == null){
                    nettyClient = new NettyClient(Params.appid,Params.appSecret,Params.host,Params.port,Params.uuid,"",new SimpleMsgHandler());
                    nettyClient.connect();
                }
            }
        return nettyClient;
    }
}

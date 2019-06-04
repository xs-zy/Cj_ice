package com.ice.cj_ice.leyaoyao;


import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.PixelFormat;
import android.graphics.drawable.Drawable;
import android.util.Base64;

import java.io.ByteArrayOutputStream;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import cn.lyy.netty.client.NettyClientProperties;
import cn.lyy.netty.common.constant.ClientConstants;
import cn.lyy.netty.common.param.AddStockParam;
import cn.lyy.netty.common.param.ApplyOpenPositionParam;
import cn.lyy.netty.common.param.EqStartResultParam;
import cn.lyy.netty.common.param.EquipmentParam;
import cn.lyy.netty.common.param.GetAppVersionParam;
import cn.lyy.netty.common.param.GetEquipmentParamParam;
import cn.lyy.netty.common.param.GetPayCodeParam;
import cn.lyy.netty.common.param.GetPhoneParam;
import cn.lyy.netty.common.param.GetUploadFileUrlParam;
import cn.lyy.netty.common.param.ICCIDParam;
import cn.lyy.netty.common.param.ReportEquipmentFailureParam;
import cn.lyy.netty.common.param.ReportEquipmentParamParam;
import cn.lyy.netty.common.param.ReportGiftInfoParam;
import cn.lyy.netty.common.param.ReportPositionInfoParam;
import cn.lyy.netty.common.param.UploadGiftParam;
import cn.lyy.netty.common.param.UploadMainboardInfoParam;
import cn.lyy.netty.msg.MsgProducer;
import cn.lyy.netty.msg.handler.AbstractLogHandler;

/**
 * Created by Administrator on 2019/3/18.
 */

public class OpenNettyDemo {
    public static  String tempKey = null;

    public static boolean getPayCode(List<String> list) {
        System.out.println("获取支付二维码......");
        GetPayCodeParam param = new GetPayCodeParam();
        param.setUniqueKey(UUID.randomUUID().toString());
        //仓位   数量   总金额
       // Arrays.asList("0,1,1;3,1,1");
        param.setSelectChannels(list);
        param.setIsGaming(false);
        return MsgProducer.getPayCode(param);
    }

    //   冰淇淋机启动结果上传
    public static boolean eqStartResult(String key,boolean result,String info) {
        tempKey = key;
        System.out.println("设备启动结果上传......");
        EqStartResultParam param = new EqStartResultParam();
        param.setUniqueCode(key);
        param.setIsSuccess(result);
        List<String> strs = new ArrayList<>();
        strs.add(info);
        param.setChannels(strs);
        MsgProducer.eqStartResult(param);
        return true;
    }

    //上传货道信息
    public static boolean uploadMainboard(UploadMainboardInfoParam param) {
        System.out.println("上传仓位信息......到：");
        //k
        String key = UUID.randomUUID().toString();
        param.setUniqueCode(key);
        //现有库存   cu
        param.setStock(param.getStock());
        //货道容量   ca
        param.setCapture(param.getCapture());
        //商品获得概率   c
        param.setProbability(param.getProbability());
        //商品售卖价格   p
        param.setBuyPrice(param.getBuyPrice());
        //游戏单价   g
        param.setGamePrice(param.getGamePrice());
        //商品名    n
        //param.setProductName(param.getProductName());
        //货到序号   仓位编号   i
        param.setMainPosition(param.getMainPosition());
        //产品成本  co
        param.setProductCost(param.getProductCost());
        //产品图片
        // param.setPicture(param.getPicture());
        //货道位置   po
        param.setPositionName(param.getPositionName());
        //货道状态  1开启    0关闭   ce
        param.setPositionStatus(param.getPositionStatus());
        MsgProducer.uploadMainboardInfo(param);
        return true;
    }



    //购买商品上传服务器
    public static boolean uploadGift(String giftTotal,String giftInc,String mainPosition) {
        System.out.println("上传礼品信息......");
        String key;
        UploadGiftParam param;
        key = UUID.randomUUID().toString();
        param = new UploadGiftParam();
        //k
        param.setUniqueCode(key);
        //退礼总量
        param.setGiftTotal(giftTotal);
        //推理增量
        param.setGiftInc(giftInc);
        //仓位编号
        param.setMainPosition(mainPosition);
        MsgProducer.uploadGift(param);
        return true;
    }

    //上传版本号
    public static boolean getAppVersion() {
        System.out.println("获取app版本......");
        GetAppVersionParam param = new GetAppVersionParam();
        param.setType("1");
        param.setVersion("1.3.4");
        param.setUniqueCode(UUID.randomUUID().toString());
        MsgProducer.getAppVersion(param);
        return true;
    }


    public static boolean getEquipmentParam() {
        System.out.println("获取设备参数设置......");
        GetEquipmentParamParam param = new GetEquipmentParamParam();
        param.setUniqueCode(UUID.randomUUID().toString());
        MsgProducer.getEquipmentParam(param);
        return true;
    }

    public static boolean getUploadUrl() {
        System.out.println("获取图片上传链接......");
        GetUploadFileUrlParam param = new GetUploadFileUrlParam();
        param.setSuffix("png");
        param.setAttach("10001");
        param.setUniqueCode(uuid());
        return MsgProducer.getUploadFileUrl(param);
    }

    public static boolean reportEquipmentFailure() {
        ReportEquipmentFailureParam param = new ReportEquipmentFailureParam();
        param.setErrorCode("234");
        param.setErrorDesc("设备故障上报测试");
        param.setErrorStatus("1");
        param.setUniqueCode(uuid());
        return MsgProducer.reportEquipmentFailure(param);
    }

    //补货
    public static boolean addStock() {
        AddStockParam param = new AddStockParam();
        param.setUniqueCode(uuid());
        return MsgProducer.addStock(param);
    }


    public static boolean uploadEP() {
        List<EquipmentParam> resps = new ArrayList<>();
        EquipmentParam sp = new EquipmentParam();
        sp.setN("bType");
        sp.setV("3");
        EquipmentParam sp1 = new EquipmentParam();
        sp1.setN("isBuy");
        sp1.setV("1");
        EquipmentParam sp2 = new EquipmentParam();
        sp2.setN("isMulti");
        sp2.setV("1");
        EquipmentParam sp3 = new EquipmentParam();
        sp3.setN("gameModel");
        sp3.setV("0");
        EquipmentParam sp4 = new EquipmentParam();
        sp4.setN("isAttempt");
        sp4.setV("1");
        EquipmentParam sp5 = new EquipmentParam();
        sp5.setN("iceMaking");
        sp5.setV("1");
        EquipmentParam sp6 = new EquipmentParam();
        sp6.setN("buyLimit");
        sp6.setV("10");
        resps.add(sp);
        resps.add(sp1);
        resps.add(sp2);
        resps.add(sp3);
        resps.add(sp4);
        resps.add(sp5);
        resps.add(sp6);
        ReportEquipmentParamParam param = new ReportEquipmentParamParam();
        param.setUniqueCode(uuid());
        param.setDetails(resps);
        return MsgProducer.reportEquipmentParam(param);
    }

   /* public static boolean reversePay(String authcode) {
        ReportReversePayInfoParam param = new ReportReversePayInfoParam();
        param.setIsGaming(false);
        param.setQrCode(authcode);
        param.setUniqueKey(UUID.randomUUID().toString());
        List<PayInfoChannelParam> payInfoChannelParams = new ArrayList<>();
        PayInfoChannelParam payInfoChannelParam = new PayInfoChannelParam();
        payInfoChannelParam.setAmount("2");
        payInfoChannelParam.setChannelNo("4");
        payInfoChannelParam.setCount("2");
        PayInfoChannelParam payInfoChannelParam1 = new PayInfoChannelParam();
        payInfoChannelParam1.setAmount("1");
        payInfoChannelParam1.setChannelNo("3");
        payInfoChannelParam1.setCount("1");
        payInfoChannelParams.add(payInfoChannelParam1);
        payInfoChannelParams.add(payInfoChannelParam);
        param.setDetails(payInfoChannelParams);
        return MsgProducer.reportReversePayInfo(param);
    }*/

    private static boolean iccidReport(String iccid) {
        System.out.println("上传iccid。。。。" + iccid);
        ICCIDParam param = new ICCIDParam();
        param.setIccid(iccid);
        param.setUniqueCode(uuid());
        return MsgProducer.reportICCID(param);
    }

    private static boolean queryPhoneNo() {
        System.out.println("查询客服电话。。。。");
        GetPhoneParam param = new GetPhoneParam();
        param.setUniqueCode(uuid());
        return MsgProducer.getPhone(param);
    }

    //设置仓位信息    ---------------   将商品与货道绑定
    public static boolean reportPositionInfo(String i,String n,String p,String s,String si) {
        System.out.println("上传/查询 仓位信息");
        ReportPositionInfoParam param = new ReportPositionInfoParam();
        //货道编号
        param.setI(i);
        //货道名字
        param.setN(n);
        //设置
        param.setO("put");
        //商品售卖价格
        param.setP(p);
        //商品库存
        param.setS(s);
        //商品ID
        param.setSi(si);
        //k
        param.setUniqueCode(uuid());
        return MsgProducer.reportPositionInfo(param);
    }

    //上传礼品信息   ---------------  上传商品
    public static boolean reportGiftInfo(String ci,String i,String n,String p){
        System.out.println("上传礼品信息");
        ReportGiftInfoParam param = new ReportGiftInfoParam();
        param.setO("put");
        //商品ID
        param.setCi(ci);
        //图片地址
        param.setI(i);
        //商品名称
        param.setN(n);
        //商品价格
        param.setP(p);
        //k
        param.setUniqueCode(uuid());
        return MsgProducer.reportGiftInfo(param);
    }

    public static boolean reportOpenPosition(String o) {
        System.out.println("货道开启关闭上传，查询");
        String key = UUID.randomUUID().toString();
        ApplyOpenPositionParam param = new ApplyOpenPositionParam();
        param.setI("2");
        param.setO(o);
        param.setS("1");
        param.setK(key);
        return MsgProducer.applyOpenPosition(param);
    }

    private static class MyLogHandler extends AbstractLogHandler {
        @Override
        public void handlerLog(String msg, ClientConstants.LogLevel level) {
            if (level.value() >= NettyClientProperties.logLevel)
                System.out.println(String.format("Date: %s, LogLevel: %s, Message: %s", new Date(), level.name(), msg));
        }
    }

    private static String uuid() {
        return UUID.randomUUID().toString();
    }


    public static String convertIconToString(Bitmap bitmap) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();// outputstream
        bitmap.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] bytes = baos.toByteArray();// 转为byte数组
        return Base64.encodeToString(bytes, Base64.DEFAULT);
    }
}

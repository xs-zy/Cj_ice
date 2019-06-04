package com.ice.cj_ice.leyaoyao;

import android.content.SharedPreferences;
import android.util.Log;

import com.google.gson.Gson;
import com.ice.cj_ice.base.App;
import com.ice.cj_ice.leyaoyao.eventbus.PayResultEvent;
import com.ice.cj_ice.leyaoyao.eventbus.QcodeEvent;
import com.ice.cj_ice.model.db.DBManager;
import com.ice.cj_ice.model.entity.ShopInfoReturenBean;
import com.ice.cj_ice.util.Params;

import org.greenrobot.eventbus.EventBus;
import java.io.File;
import java.util.ArrayList;
import java.util.List;

import cn.lyy.netty.client.NettyClient;
import cn.lyy.netty.common.constant.ClientConstants;
import cn.lyy.netty.common.param.RemoteDivisionParam;
import cn.lyy.netty.common.param.ReportCustomizeEquipmentParamParam;
import cn.lyy.netty.common.response.AddOrCleanStockResp;
import cn.lyy.netty.common.response.BatchSetMainboardResp;
import cn.lyy.netty.common.response.GetPhoneResultResp;
import cn.lyy.netty.common.response.GetUploadFileUrlResp;
import cn.lyy.netty.common.response.GiftInfoNoticeResp;
import cn.lyy.netty.common.response.GiftInfoReportResultResp;
import cn.lyy.netty.common.response.LoginResp;
import cn.lyy.netty.common.response.MotorTestNoticeResp;
import cn.lyy.netty.common.response.OpenDoorResp;
import cn.lyy.netty.common.response.OpenPositionNoticeResp;
import cn.lyy.netty.common.response.OpenPositionResultResp;
import cn.lyy.netty.common.response.PayCodeResp;
import cn.lyy.netty.common.response.PhoneNoticeResp;
import cn.lyy.netty.common.response.PositionInfoNoticeResp;
import cn.lyy.netty.common.response.PositionInfoReportResultResp;
import cn.lyy.netty.common.response.QrCodeResp;
import cn.lyy.netty.common.response.RemoteDivisionResp;
import cn.lyy.netty.common.response.ResponseParam;
import cn.lyy.netty.common.response.SetEquipmentParamResp;
import cn.lyy.netty.common.response.SetMainboardResp;
import cn.lyy.netty.msg.MsgProducer;
import cn.lyy.netty.msg.handler.AbstractMsgHandler;
import cn.lyy.netty.util.HttpUtil;
import cn.lyy.netty.util.LogUtil;

import static android.content.Context.MODE_PRIVATE;

/**
 * insert description here
 * @author liuxiaohua
 * @since 2018/10/30 17:39
 */
public class SimpleMsgHandler extends AbstractMsgHandler {

    private SharedPreferences.Editor editor;
    private SharedPreferences sharedPreferences;
    private DBManager dbManager = new DBManager(App.activity);


    @Override
    protected boolean loginHandler(ResponseParam responseParam) {
        sharedPreferences = App.activity.getSharedPreferences("loginState", MODE_PRIVATE);
        editor = sharedPreferences.edit();
        LoginResp resp = getResp(responseParam, LoginResp.class);
        if(resp.getD() == "0"){
            //已绑定
            editor.putString("D",resp.getD());
            editor.putString("V",resp.getV());
            editor.commit();
        }else {
            editor.putString("D",resp.getD());
            editor.putString("V",resp.getV());
            editor.putString("Q",resp.getQ());
            editor.commit();
        }
        System.out.println(String.format("get 设备登录结果为：%s", gson.toJson(resp)));
        return true;
    }


    @Override
    protected boolean qrCodeHandler(ResponseParam responseParam) {
        QrCodeResp resp = getResp(responseParam, QrCodeResp.class);
        System.out.println(String.format("get 设备注册二维码为：%s", gson.toJson(resp)));
        return true;
    }

    @Override
    protected boolean eqBindNoticeHandler(ResponseParam responseParam) {
        //b
        editor.putString("A",responseParam.getA());
        editor.commit();
        System.out.println(String.format("get 收到设备绑定通知: %s", gson.toJson(responseParam)));
        return true;
    }

    @Override
    protected boolean eqUnBindNoticeHandler(ResponseParam responseParam) {
        //ub
        editor.putString("A",responseParam.getA());
        editor.commit();
        System.out.println(String.format("get 收到设备解绑通知: %s", gson.toJson(responseParam)));
        return true;
    }

    @Override
    protected boolean payCodeHandler(ResponseParam responseParam) {
        PayCodeResp resp = getResp(responseParam, PayCodeResp.class);
        // get 设备支付二维码为：{"d":"https://sm.leyaoyao.com/customer/shj/pay/00000000824103385458328"}
        String Qcode = resp.getD();
        EventBus.getDefault().post(new QcodeEvent(Qcode));
        //MsgProducer.
        System.out.println(String.format("get 设备支付二维码为：%s", gson.toJson(resp)));
        return true;
    }

    @Override
    protected boolean payResultHandler(ResponseParam responseParam) {
        //{"a":"pr","k":"ee830e1c-92f4-4707-bd51-be7bdcd93fd8","p":{"uid":"33174018"}}
        System.out.println(String.format("set 设备支付结果为：%s", gson.toJson(responseParam)));
        Log.d("zhiyuan",responseParam.getK());
        MsgProducer.payResultResult(Params.appSecret,responseParam.getK());
        // TODO 支付成功或者失败后的逻辑处理：不管处理成功或者失败都会返回结果。
        // MsgProducer.eqStartResult(NettyClient.appSecret, true, responseParam.getK());
        // OpenNettyDemo.eqStartResult(responseParam.getK());
        // OpenNettyDemo.uploadGift();
        Log.d("qianqian","次数。。。。");
        EventBus.getDefault().post(new PayResultEvent(responseParam.getK()));
        return true;
    }

    @Override
    protected boolean eqStartResultReHandler(ResponseParam responseParam) {
        System.out.println(String.format("get 设备启动结果上传返回结果为：%s", gson.toJson(responseParam)));
        return true;
    }

    //服务器返回给客户端的货道信息
    @Override
    protected boolean uploadMainboartResultHandler(ResponseParam responseParam) {
        //{"a":"pir","k":"37d9da72-7cde-4d2b-af54-863ee643dbe9"}
        System.out.println(String.format("get 上传仓位信息返回结果为：%s", gson.toJson(responseParam)));
        return true;
    }


    //手机微信端设置货道信息后下发的
    @Override
    protected boolean setMainboardHandler(ResponseParam responseParam) {
        SetMainboardResp resp = getResp(responseParam, SetMainboardResp.class);
        System.out.println(String.format("set 设置仓位信息：%s", gson.toJson(resp)));
        return true;
    }

    //手机微信端批量设置货道信息后下发的    需要
    @Override
    protected boolean batchSetMainboardHandler(ResponseParam responseParam) {
        BatchSetMainboardResp resp = getResp(responseParam, BatchSetMainboardResp.class);
        List<String> cargoNum = resp.getI();
        for(String num:cargoNum){
            dbManager.queryInfoSetCargo(num,resp.getN(),resp.getCu(),resp.getP(),resp.getCo(),resp.getG(),resp.getC(),resp.getPi());
        }
        System.out.println(String.format("set 批量设置仓位信息：%s", gson.toJson(resp)));
        return true;
    }

    @Override
    protected boolean uploadCoinsResultHandler(ResponseParam responseParam) {
        System.out.println(String.format("get 上传投币信息返回结果为：%s", gson.toJson(responseParam)));
        return true;
    }

    //上传购买商品信息的返回数据
    @Override
    protected boolean uploadGiftResultHandler(ResponseParam responseParam) {
        //  {"i":5.0,"n":"核桃仁","pi":"","g":1.0,"p":1.0,"co":1.0,"c":1.0,"cu":20.0}
        ShopInfoReturenBean resp = getResp(responseParam,ShopInfoReturenBean.class);
        int i = resp.getI();
        int cu = resp.getCu();
        String pi = resp.getPi();
        String n = resp.getN();
        Log.d("xuehziyuan",i+"=="+cu+"=="+pi+"=="+n);
        // dbManager.queryInfoSetCargo();
        System.out.println(String.format("get 上传退礼信息返回结果为：%s", gson.toJson(responseParam)));
        return true;
    }

    //机台参数设置列表
    @Override
    protected boolean setEquipmentParamHandler(ResponseParam responseParam) {
        System.out.println(String.format("set 参数设置通知：%s", gson.toJson(responseParam)));
        List<SetEquipmentParamResp> resps = getRespList(responseParam, SetEquipmentParamResp.class);
        System.out.println(String.format("解析参数为：%s", gson.toJson(resps)));
        return true;
    }

    @Override
    protected boolean openDoorHandler(ResponseParam responseParam) {
        System.out.println(String.format("set 开门通知：%s", gson.toJson(responseParam)));
        List<OpenDoorResp> resps = getRespList(responseParam, OpenDoorResp.class);
        System.out.println(String.format("解析参数为：%s", gson.toJson(resps)));
        return true;
    }

    @Override
    protected boolean addOrCleanStockHandler(ResponseParam responseParam) {
        System.out.println(String.format("set 补货/清货通知：%s", gson.toJson(responseParam)));
        List<AddOrCleanStockResp> resps = getRespList(responseParam, AddOrCleanStockResp.class);
        int size = resps.size();
        for(int s = 0;s<size;s++){
            String n = resps.get(s).getN();
            dbManager.updateDatabaseNum(n,resps.get(s).getV());
        }
        System.out.println(String.format("解析参数为：%s", gson.toJson(resps)));
        return true;
    }

    @Override
    protected boolean getAppVersionHandler(ResponseParam responseParam) {
        System.out.println(String.format("get 获取版本结果：%s", gson.toJson(responseParam)));
        return true;
    }

    @Override
    protected boolean appVersionNoticeHandler(ResponseParam responseParam) {
        System.out.println(String.format("set 系统版本更新通知：%s", gson.toJson(responseParam)));
        return true;
    }

    @Override
    protected boolean getEquipmentParamHandler(ResponseParam responseParam) {
        System.out.println(String.format("获取设备参数设置结果返回：%s", gson.toJson(responseParam)));
        return true;
    }

    @Override
    protected boolean queryCustomizeEquipmentParamHandler(ResponseParam responseParam) {
        System.out.println(String.format("获取设备自定义参数设置：%s", gson.toJson(responseParam)));
        ReportCustomizeEquipmentParamParam param = new ReportCustomizeEquipmentParamParam();
        param.setDetails(new ArrayList());
        param.setUniqueCode(responseParam.getK());
        return MsgProducer.reportCustomizeEquipmentParam(param);
    }

    @Override
    protected boolean setCustomizeEquipmentParamHandler(ResponseParam responseParam) {
        System.out.println(String.format("设置设备自定义参数：%s", gson.toJson(responseParam)));
        return true;
    }

    @Override
    protected boolean reportEquipmentFailureResultHandler(ResponseParam responseParam) {
        System.out.println(String.format("上报设备错误信息 结果返回：%s", gson.toJson(responseParam)));
        return true;
    }

    @Override
    protected boolean getUploadFileUrlResultHandler(ResponseParam responseParam) {
        try {
            GetUploadFileUrlResp resp = getResp(responseParam, GetUploadFileUrlResp.class);
            String url = resp.getU();
            String path = "D:\\Documents\\images\\al.png";
            HttpUtil.uploadFile("http://lyy-public.oss-cn-shenzhen.aliyuncs.com/vending/hello.png?Expires=1545618485&OSSAccessKeyId=KX0Gz9qNxkaaBryM&Signature=TgGPrJfBPqUupScvhP1%2BPn75JQM%3D", new File(path));
//            UploadUtil.post(url, new File(path), null, null);
//            UploadUtil.putASync("http://lyy-public.oss-cn-shenzhen.aliyuncs.com/15444959508477y.png?Expires=1544496250&OSSAccessKeyId=KX0Gz9qNxkaaBryM&Signature=cwqMFi%2BatrCc0las4y4y0pNCAtE%3D", new File(path));
//            UploadUtil.putASync(url, new File(path));
            System.out.println(url);
            System.out.println(String.format("获取上传文件链接 结果返回：%s;", gson.toJson(responseParam)));
        } catch (Exception e) {
            e.printStackTrace();
        }
        return true;
    }

    @Override
    protected boolean addStockResultHandler(ResponseParam responseParam) {
        System.out.println(String.format("一键补货 结果返回：%s", gson.toJson(responseParam)));
        return true;
    }

    @Override
    protected boolean reportEquipmentParamResultHandler(ResponseParam responseParam) {
        return super.reportEquipmentParamResultHandler(responseParam);
    }

    @Override
    protected boolean reportReversePayInfoResultHandler(ResponseParam responseParam) {
        return super.reportReversePayInfoResultHandler(responseParam);
    }

    @Override
    protected boolean remoteDivisionHandler(ResponseParam responseParam) {
        LogUtil.printLog("接收到远程上分消息", ClientConstants.LogLevel.INFO);
        RemoteDivisionResp resp = getResp(responseParam, RemoteDivisionResp.class);
        RemoteDivisionParam param = new RemoteDivisionParam();
        param.setI(resp.getI());
        param.setT(resp.getT());
        param.setUniqueCode(responseParam.getK());
        //OpenNettyDemo.uploadGift();
//        return MsgProducer.reportRemoteDivision(param);
        return true;
    }

    @Override
    protected boolean reportICCIDResultHandler(ResponseParam responseParam) {
        System.out.println("Iccid 上报结果返回");
        return super.reportICCIDResultHandler(responseParam);
    }

    @Override
    protected boolean getPhoneResultHandler(ResponseParam responseParam) {
        System.out.println("获取客服电话结果返回");
        GetPhoneResultResp resp = getResp(responseParam, GetPhoneResultResp.class);
        return super.getPhoneResultHandler(responseParam);
    }

    @Override
    protected boolean phoneNoticeHandler(ResponseParam responseParam) {
        System.out.println("客服电话下发");
        PhoneNoticeResp resp = getResp(responseParam, PhoneNoticeResp.class);
        return super.phoneNoticeHandler(responseParam);
    }

    @Override
    protected boolean openPositionNoticeHandler(ResponseParam responseParam) {
        System.out.println("货道开启关闭通知");
        OpenPositionNoticeResp resp = getResp(responseParam, OpenPositionNoticeResp.class);
        return super.openPositionNoticeHandler(responseParam);
    }

    @Override
    protected boolean openPositionResultHandler(ResponseParam responseParam) {
        System.out.println("货道开启关闭查询/设置");
        OpenPositionResultResp resp = getResp(responseParam, OpenPositionResultResp.class);
        return super.openPositionResultHandler(responseParam);
    }

    //3.2.26下发商品信息
    @Override
    protected boolean giftInfoReportResultHandler(ResponseParam responseParam) {
        System.out.println("礼品信息上传结果返回");
        GiftInfoReportResultResp resp = getResp(responseParam, GiftInfoReportResultResp.class);
        dbManager.queryUpDataInfo(resp.getCi(),resp.getN(),resp.getI(),resp.getP(),resp.getSi());
        return super.giftInfoReportResultHandler(responseParam);
    }


    //下发商品信息
    @Override
    protected boolean giftInfoNoticeHandler(ResponseParam responseParam) {
        System.out.println("礼品设置");
        GiftInfoNoticeResp resp = getResp(responseParam, GiftInfoNoticeResp.class);
        dbManager.queryshopSet(resp.getP(),resp.getI(),resp.getN(),resp.getSi(),resp.getCi());
        return super.giftInfoNoticeHandler(responseParam);
    }

    //3.2.27   上传货道设置
    @Override
    protected boolean positionInfoReportResultHandler(ResponseParam responseParam) {
        System.out.println("仓位设置上传");
        PositionInfoReportResultResp resp = getResp(responseParam, PositionInfoReportResultResp.class);
        dbManager.queryInfoSet(resp.getI(),resp.getN(),resp.getS(),resp.getP(),resp.getSi(),resp.getCi());
        return super.positionInfoReportResultHandler(responseParam);
    }

    //3.2.28   下发货道设置
    @Override
    protected boolean positionInfoNoticeHandler(ResponseParam responseParam) {
        System.out.println("仓位设置下发");
        PositionInfoNoticeResp resp = getResp(responseParam, PositionInfoNoticeResp.class);
        List<String> cargoNum = resp.getI();
        for(String num:cargoNum){
            dbManager.querySetCargo(num,resp.getGn(),resp.getS(),resp.getP(),resp.getGp(),resp.getN(),resp.getGi(),resp.getSi(),resp.getCi());
        }
        return super.positionInfoNoticeHandler(responseParam);
    }

    @Override
    protected boolean motorTestHandler(ResponseParam responseParam) {
        System.out.println("电机测试");
        MotorTestNoticeResp resp = getResp(responseParam, MotorTestNoticeResp.class);
        return super.motorTestHandler(responseParam);
    }

}

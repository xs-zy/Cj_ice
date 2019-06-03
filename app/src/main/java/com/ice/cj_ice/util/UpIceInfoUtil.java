package com.ice.cj_ice.util;


import android.util.Log;
import com.ice.cj_ice.base.App;
import com.ice.cj_ice.leyaoyao.OpenNettyDemo;
import com.ice.cj_ice.model.db.DBManager;
import com.ice.cj_ice.model.entity.CargoInfoBean;


import cn.lyy.netty.common.param.UploadMainboardInfoParam;

/**
 * Created by Administrator on 2019/4/23.
 */

public class UpIceInfoUtil {

    private UploadMainboardInfoParam umip;
    private static UpIceInfoUtil instance = null;
    private DBManager dbManager = new DBManager(App.activity);
    CargoInfoBean cargoInfoBean = new CargoInfoBean();
    private UpIceInfoUtil(){}

    public static UpIceInfoUtil getInstance(){
        if(instance == null){
            synchronized (UpIceInfoUtil.class){
                if(instance == null){
                    instance = new UpIceInfoUtil();
                }
            }
        }
        return instance;
    }

    //上传仓位信息
    private void upCargoInfoLeYaoyao(int i,String cost,String buyPrice,String gamePrice,String probability,
                             String capture,String stock){
        Log.d("xuezhiyuan","上传完成"+i+"货道");
        cargoInfoBean.setId(i);
        //货道状态
        cargoInfoBean.setCargo_state("1");
        //货道编号
        cargoInfoBean.setCargo_num(i+"");
        //货道位置
        cargoInfoBean.setCargo_location(i+"");
        //成本价
        cargoInfoBean.setCost_price(cost);
        //售卖价
        cargoInfoBean.setShop_price(buyPrice);
        //游戏价
        cargoInfoBean.setGame_price(gamePrice);
        //中奖概率
        cargoInfoBean.setWinning_probability(probability);
        //货道容量
        cargoInfoBean.setCargo_capacity(capture);
        //货道剩余量
        cargoInfoBean.setChannel_surplus(stock);
        dbManager.insertCargo(cargoInfoBean);
        //上传服务器
        umip = new UploadMainboardInfoParam();
        //货道状态
        umip.setPositionStatus("1");
        //货道编号
        umip.setMainPosition(i+"");
        //货道位置
        umip.setPositionName(i+"");
        //成本价
        umip.setProductCost(cost);
        //售卖价
        umip.setBuyPrice(buyPrice);
        //游戏价
        umip.setGamePrice(gamePrice);
        //中奖概率
        umip.setProbability(probability);
        //货道容量
        umip.setCapture(capture);
        //货道剩余量
        umip.setStock(stock);
        OpenNettyDemo.uploadMainboard(umip);
    }

    private void upShopInfoLeYaoyao(int id,String ci,String i,String n,String p){
        dbManager.queryShopInfo(ci,n,i,p,id);
        //上传
        OpenNettyDemo.reportGiftInfo(ci,i,n,p);
    }

    private void upCargoSetLeYaoyao(String i,String n,String p,String s,String si){
        // //上传
        dbManager.queryShopInfoSet(s,n,si,p,i);
        OpenNettyDemo.reportPositionInfo(i,n,p,s,si);
    }


    //上传仓位信息
    public void upCargoInfo(){
        for(int i = 1;i<10;i++){
            upCargoInfoLeYaoyao(i,"1","1","1","1","20","20");
        }
    }


    //上传商品信息
    public void upShopInfo(){
        //                   商品ID
        upShopInfoLeYaoyao(1,"101","","原味","1");
        upShopInfoLeYaoyao(2,"102","","草莓","1");
        upShopInfoLeYaoyao(3,"103","","混合","1");
        upShopInfoLeYaoyao(4,"104","","葡萄干","1");
        upShopInfoLeYaoyao(5,"105","","核桃仁","1");
        upShopInfoLeYaoyao(6,"106","","巧克力","1");
        upShopInfoLeYaoyao(7,"107","","麦片","1");
        upShopInfoLeYaoyao(8,"108","","瓜子","1");
        upShopInfoLeYaoyao(9,"109","","杯子","1");
    }


    //上传货道设置信息
    public void upCargoSet(){
        //{"ci":"1004","i":"tupian","n":"葡萄干","o":"put","p":"1","si":"1033023"}}
        //货道编号
        upCargoSetLeYaoyao("1","主料1","1","20",dbManager.queryData("1"));
        upCargoSetLeYaoyao("2","主料2","1","20",dbManager.queryData("2"));
        upCargoSetLeYaoyao("3","主料3","1","20",dbManager.queryData("3"));
        upCargoSetLeYaoyao("4","配料1","1","20",dbManager.queryData("4"));
        upCargoSetLeYaoyao("5","配料2","1","20",dbManager.queryData("5"));
        upCargoSetLeYaoyao("6","配料3","1","20",dbManager.queryData("6"));
        upCargoSetLeYaoyao("7","配料4","1","20",dbManager.queryData("7"));
        upCargoSetLeYaoyao("8","配料5","1","20",dbManager.queryData("8"));
        upCargoSetLeYaoyao("9","杯子","1","20",dbManager.queryData("9"));
    }

}

package com.ice.cj_ice.model.db;

/**
 * 需要用到的数据
 * Created by 17710890509 on 2018/8/13.
 */

public class DatabaseStatic {

    //数据库名
    public final static String DATABASE_NAME = "ice.db";
    //数据库版本号
    public final static int DATABASE_VERSION = 1;
    public final static String TABLE_NAME_CARGO = "cargoList";
    //主键
    public static final String ID = "_id";   //id
    public static final String SHOP_ID = "shop_id";   //商品ID
    public static final String SHOP_ID_SERVER = "shop_id_ser";   //服务器段礼品ID
    public static final String CARGO_NUM = "cargo_num";//货道编号
    public static final String CARGO_NAME = "cargo_name";//货道名
    public static final String COST_PRICE = "cost_price";//成本价
    public static final String SHOP_PRICE = "shop_price";//售卖价
    public static final String GAME_PRICE = "game_price";//游戏单价
    public static final String WINNING_PROBABILITY = "winning_probability";//中奖概率
    public static final String CARGO_CAPACITY = "cargo_capacity";//货道容量
    public static final String CHANNEL_SURPLUS = "channel_surplus";//货道剩余
    public static final String CARGO_LOCATION = "cargo_location";//货道位置
    public static final String CARGO_STATE = "cargo_state";//货道状态
    public static final String SHOP_NAME = "shop_name"; //商品名
    public static final String SHOP_PICTURE = "shop_picture";//商品图片

}

package com.ice.cj_ice.model.entity;


/**
 * Created by Administrator on 2019/4/9.
 */

public class CargoInfoBean  {

    //主键
    private int id;
    //货道编号
    private String cargo_num;
    //成本价
    private String cost_price;
    //售卖价
    private String shop_price;
    //游戏价
    private String game_price;
    //中奖概率
    private String winning_probability;
    //货道容量
    private String cargo_capacity;
    //货道剩余量
    private String channel_surplus;
    //货道位置
    private String cargo_location;
    //货道状态
    private String cargo_state;
    //商品ID
    private String shop_id;
    //服务器商品ID
    private String shopId_server;
    //商品名
    private String shop_name;
    //商品图片
    private String picture;
    //货道名
    private String cargo_name;


    public CargoInfoBean(){}

    public CargoInfoBean(int id, String cargo_num, String cost_price, String shop_price, String game_price, String winning_probability, String cargo_capacity, String channel_surplus, String cargo_location, String cargo_state, String shop_id, String shopId_server, String shop_name, String picture, String cargo_name) {
        this.id = id;
        this.cargo_num = cargo_num;
        this.cost_price = cost_price;
        this.shop_price = shop_price;
        this.game_price = game_price;
        this.winning_probability = winning_probability;
        this.cargo_capacity = cargo_capacity;
        this.channel_surplus = channel_surplus;
        this.cargo_location = cargo_location;
        this.cargo_state = cargo_state;
        this.shop_id = shop_id;
        this.shopId_server = shopId_server;
        this.shop_name = shop_name;
        this.picture = picture;
        this.cargo_name = cargo_name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getCargo_num() {
        return cargo_num;
    }

    public void setCargo_num(String cargo_num) {
        this.cargo_num = cargo_num;
    }

    public String getCost_price() {
        return cost_price;
    }

    public void setCost_price(String cost_price) {
        this.cost_price = cost_price;
    }

    public String getShop_price() {
        return shop_price;
    }

    public void setShop_price(String shop_price) {
        this.shop_price = shop_price;
    }

    public String getGame_price() {
        return game_price;
    }

    public void setGame_price(String game_price) {
        this.game_price = game_price;
    }

    public String getWinning_probability() {
        return winning_probability;
    }

    public void setWinning_probability(String winning_probability) {
        this.winning_probability = winning_probability;
    }

    public String getCargo_capacity() {
        return cargo_capacity;
    }

    public void setCargo_capacity(String cargo_capacity) {
        this.cargo_capacity = cargo_capacity;
    }

    public String getChannel_surplus() {
        return channel_surplus;
    }

    public void setChannel_surplus(String channel_surplus) {
        this.channel_surplus = channel_surplus;
    }

    public String getCargo_location() {
        return cargo_location;
    }

    public void setCargo_location(String cargo_location) {
        this.cargo_location = cargo_location;
    }

    public String getCargo_state() {
        return cargo_state;
    }

    public void setCargo_state(String cargo_state) {
        this.cargo_state = cargo_state;
    }

    public String getShop_id() {
        return shop_id;
    }

    public void setShop_id(String shop_id) {
        this.shop_id = shop_id;
    }

    public String getShopId_server() {
        return shopId_server;
    }

    public void setShopId_server(String shopId_server) {
        this.shopId_server = shopId_server;
    }

    public String getShop_name() {
        return shop_name;
    }

    public void setShop_name(String shop_name) {
        this.shop_name = shop_name;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public String getCargo_name() {
        return cargo_name;
    }

    public void setCargo_name(String cargo_name) {
        this.cargo_name = cargo_name;
    }

    @Override
    public String toString() {
        return "CargoInfoBean{" +
                "id=" + id +
                ", cargo_num='" + cargo_num + '\'' +
                ", cost_price='" + cost_price + '\'' +
                ", shop_price='" + shop_price + '\'' +
                ", game_price='" + game_price + '\'' +
                ", winning_probability='" + winning_probability + '\'' +
                ", cargo_capacity='" + cargo_capacity + '\'' +
                ", channel_surplus='" + channel_surplus + '\'' +
                ", cargo_location='" + cargo_location + '\'' +
                ", cargo_state='" + cargo_state + '\'' +
                ", shop_id='" + shop_id + '\'' +
                ", shopId_server='" + shopId_server + '\'' +
                ", shop_name='" + shop_name + '\'' +
                ", picture='" + picture + '\'' +
                ", cargo_name='" + cargo_name + '\'' +
                '}';
    }
}

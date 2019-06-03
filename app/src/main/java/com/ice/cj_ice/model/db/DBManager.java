package com.ice.cj_ice.model.db;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.widget.Toast;


import com.ice.cj_ice.model.entity.CargoInfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by 17710890509 on 2018/8/13.
 */

public class DBManager {

    private SQLiteDatabase db;
    private CargoHelper helper;
    private Context context;

    public DBManager(Context context){
        this.context = context;
        createDatabase();
    }

    /*
     * 调用getWritabelDatabase方法或者
     * getReadableDatabase方法时，如果数据库文
     * 件中不存在（注意一个数据库中可以存在多个表格），
     * 那么会回调MyHelper类的onCreate方法新建一个数据库文
     * 件并且在这个数据库文件中新建一
     * 个book表格
     */
    public void createDatabase() {
        helper = new CargoHelper(context);
        db = helper.getWritableDatabase();
    }


    // 向数据库中插入货道数据
    public synchronized <T> void insertCargo(T bean) {
        if(helper == null) {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        try {
            if (bean != null && bean instanceof CargoInfoBean) {
                CargoInfoBean cargoInfoBean = (CargoInfoBean) bean;
                ContentValues cv = new ContentValues();
                cv.put(DatabaseStatic.ID, cargoInfoBean.getId());
                cv.put(DatabaseStatic.CARGO_NUM, cargoInfoBean.getCargo_num());
                cv.put(DatabaseStatic.COST_PRICE, cargoInfoBean.getCost_price());
                cv.put(DatabaseStatic.SHOP_PRICE, cargoInfoBean.getShop_price());
                cv.put(DatabaseStatic.GAME_PRICE, cargoInfoBean.getGame_price());
                cv.put(DatabaseStatic.WINNING_PROBABILITY, cargoInfoBean.getWinning_probability());
                cv.put(DatabaseStatic.CARGO_CAPACITY, cargoInfoBean.getCargo_capacity());
                cv.put(DatabaseStatic.CHANNEL_SURPLUS, cargoInfoBean.getChannel_surplus());
                cv.put(DatabaseStatic.CARGO_LOCATION, cargoInfoBean.getCargo_location());
                cv.put(DatabaseStatic.CARGO_STATE, cargoInfoBean.getCargo_state());
                cv.put(DatabaseStatic.SHOP_ID, cargoInfoBean.getShop_id());
                cv.put(DatabaseStatic.SHOP_NAME, cargoInfoBean.getShop_name());
                cv.put(DatabaseStatic.SHOP_PICTURE, cargoInfoBean.getPicture());
                cv.put(DatabaseStatic.SHOP_ID_SERVER, cargoInfoBean.getShopId_server());
                cv.put(DatabaseStatic.CARGO_NAME, cargoInfoBean.getCargo_name());
                db.insert(DatabaseStatic.TABLE_NAME_CARGO, null, cv);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }







    // 向数据库中更新商品数据
    public void queryShopInfo(String shopId,String shopName,String shop_picture,String shop_cost,int id) {
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(DatabaseStatic.SHOP_ID, shopId);
        cV.put(DatabaseStatic.SHOP_NAME, shopName);
        cV.put(DatabaseStatic.SHOP_PICTURE, shop_picture);
        cV.put(DatabaseStatic.COST_PRICE, shop_cost);
        db.update(DatabaseStatic.TABLE_NAME_CARGO, cV, DatabaseStatic.ID + "= ?", new String[]{String.valueOf(id)});
    }


    //仓位与商品绑定设置更新
    public void queryShopInfoSet(String cs,String cargoName,String shopIdSer,String shopPrice,String cargoNum) {
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(DatabaseStatic.CARGO_NAME, cargoName);
        cV.put(DatabaseStatic.CHANNEL_SURPLUS, cs);
        cV.put(DatabaseStatic.SHOP_ID_SERVER, shopIdSer);
        cV.put(DatabaseStatic.SHOP_PRICE, shopPrice);
        db.update(DatabaseStatic.TABLE_NAME_CARGO, cV, DatabaseStatic.CARGO_NUM + "= ?", new String[]{String.valueOf(cargoNum)});
    }

    //根据商品信息 服务器返回的信息更新数据库
    public void queryUpDataInfo(String shopId,String shopName,String shop_picture,String shop_cost,String shop_id_ser) {
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(DatabaseStatic.SHOP_ID_SERVER, shop_id_ser);
        cV.put(DatabaseStatic.SHOP_NAME, shopName);
        cV.put(DatabaseStatic.SHOP_PICTURE, shop_picture);
        cV.put(DatabaseStatic.COST_PRICE, shop_cost);
        db.update(DatabaseStatic.TABLE_NAME_CARGO, cV, DatabaseStatic.SHOP_ID + "= ?", new String[]{String.valueOf(shopId)});
    }

    //根据仓位设置 服务器返回的信息更新数据库
    public void queryInfoSet(String cargoNum,String cargoName,String cs,String shopPrice,String shop_id_ser,String shopId) {
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(DatabaseStatic.SHOP_ID_SERVER, shop_id_ser);
        cV.put(DatabaseStatic.SHOP_ID, shopId);
        cV.put(DatabaseStatic.CARGO_NAME, cargoName);
        cV.put(DatabaseStatic.CHANNEL_SURPLUS, cs);
        cV.put(DatabaseStatic.SHOP_PRICE, shopPrice);
        db.update(DatabaseStatic.TABLE_NAME_CARGO, cV, DatabaseStatic.CARGO_NUM + "= ?", new String[]{String.valueOf(cargoNum)});
    }


    //批量设置仓位信息 服务器返回的信息更新数据库     上传推理数据返回结果
    public void queryInfoSetCargo(String cargoNum,String shopName,String cs,String shopPrice,String cost,String game_price,String winning,String shop_picture) {
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        ContentValues cV = new ContentValues();
        //中奖概率
        cV.put(DatabaseStatic.WINNING_PROBABILITY, winning);
        //成本价
        cV.put(DatabaseStatic.COST_PRICE, cost);
        //游戏价
        cV.put(DatabaseStatic.GAME_PRICE, game_price);
        //商品名
        cV.put(DatabaseStatic.SHOP_NAME, shopName);
        //库存
        cV.put(DatabaseStatic.CHANNEL_SURPLUS, cs);
        //售卖价
        cV.put(DatabaseStatic.SHOP_PRICE, shopPrice);
        //商品图片
        cV.put(DatabaseStatic.SHOP_PICTURE, shop_picture);
        db.update(DatabaseStatic.TABLE_NAME_CARGO, cV, DatabaseStatic.CARGO_NUM + "= ?", new String[]{String.valueOf(cargoNum)});
    }


    //设置仓位下发信息 服务器返回的信息更新数据库
    public void querySetCargo(String cargoNum,String shopName,String cs,String shopPrice,String cost,String cargoName,String shop_picture,String shop_id_ser,String shopId) {
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        ContentValues cV = new ContentValues();
        //货道名称
        cV.put(DatabaseStatic.CARGO_NAME, cargoName);
        //成本价
        cV.put(DatabaseStatic.COST_PRICE, cost);
        //商品名
        cV.put(DatabaseStatic.SHOP_NAME, shopName);
        //库存
        cV.put(DatabaseStatic.CHANNEL_SURPLUS, cs);
        //售卖价
        cV.put(DatabaseStatic.SHOP_PRICE, shopPrice);
        //商品图片
        cV.put(DatabaseStatic.SHOP_PICTURE, shop_picture);
        //服务器  商品ID
        cV.put(DatabaseStatic.SHOP_ID_SERVER, shop_id_ser);
        //设备   商品ID
        cV.put(DatabaseStatic.SHOP_ID, shopId);
        db.update(DatabaseStatic.TABLE_NAME_CARGO, cV, DatabaseStatic.CARGO_NUM + "= ?", new String[]{String.valueOf(cargoNum)});
    }


    //根据仓位设置 服务器返回的信息更新数据库
    public void queryshopSet(String cost,String shop_picture,String shopName,String shop_id_ser,String shopId) {
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        ContentValues cV = new ContentValues();
        //成本价
        cV.put(DatabaseStatic.COST_PRICE, cost);
        //设备ID
        cV.put(DatabaseStatic.SHOP_ID, shopId);
        //商品图片
        cV.put(DatabaseStatic.SHOP_PICTURE, shop_picture);
        //商品名
        cV.put(DatabaseStatic.SHOP_NAME, shopName);
        db.update(DatabaseStatic.TABLE_NAME_CARGO, cV, DatabaseStatic.CARGO_NUM + "= ?", new String[]{String.valueOf(shop_id_ser)});
    }


    //修改货道状态   根据shopId修改number
    public void updateDatabase(String state,String num,int id) {
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(DatabaseStatic.CARGO_STATE, state);
        cV.put(DatabaseStatic.CARGO_NUM, num);
        db.update(DatabaseStatic.TABLE_NAME_CARGO, cV, DatabaseStatic.ID + "= ?", new String[]{String.valueOf(id)});
        //Toast.makeText(context, "数据更新成功", Toast.LENGTH_SHORT).show();
    }

    //修改库存
    public void updateDatabaseNum(String num,String cs) {
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(DatabaseStatic.CHANNEL_SURPLUS, cs);
        db.update(DatabaseStatic.TABLE_NAME_CARGO, cV, DatabaseStatic.CARGO_NUM + "= ?", new String[]{String.valueOf(num)});
        //Toast.makeText(context, "数据更新成功", Toast.LENGTH_SHORT).show();
    }

    //修改货道管理
    public void updataDatabaseBox(String ca,String state,String num,int id){
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        // Log.d("xuezhiyuan","容量："+ca+"=="+"货道状态："+ce+"=="+"货道编号："+cn+"批量设置："+s);
        db = helper.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(DatabaseStatic.CARGO_CAPACITY, ca);
        cV.put(DatabaseStatic.CARGO_STATE, state);
        cV.put(DatabaseStatic.CARGO_NUM, num);
        db.update(DatabaseStatic.TABLE_NAME_CARGO, cV, DatabaseStatic.ID + "= ?", new String[]{String.valueOf(id)});
    }


    //修改全部数据   根据shopId修改number
    public void updateDatabaseAll(CargoInfoBean cargoInfoBean,int id) {
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        ContentValues cV = new ContentValues();
        cV.put(DatabaseStatic.CARGO_NUM, cargoInfoBean.getCargo_num());
        cV.put(DatabaseStatic.COST_PRICE, cargoInfoBean.getCost_price());
        cV.put(DatabaseStatic.SHOP_PRICE, cargoInfoBean.getShop_price());
        cV.put(DatabaseStatic.GAME_PRICE, cargoInfoBean.getGame_price());
        cV.put(DatabaseStatic.WINNING_PROBABILITY, cargoInfoBean.getWinning_probability());
        cV.put(DatabaseStatic.CARGO_CAPACITY, cargoInfoBean.getCargo_capacity());
        cV.put(DatabaseStatic.CHANNEL_SURPLUS, cargoInfoBean.getChannel_surplus());
        db.update(DatabaseStatic.TABLE_NAME_CARGO, cV, DatabaseStatic.ID + "= ?", new String[]{String.valueOf(id)});
        Toast.makeText(context, "数据更新成功", Toast.LENGTH_SHORT).show();
    }


    /**
     * 删除表中所有的数据
     */
    public synchronized void clearAll() {
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        String sql = "delete from " + DatabaseStatic.TABLE_NAME_CARGO;
        try {
            db.execSQL(sql);
            Toast.makeText(context, "数据删除成功", Toast.LENGTH_SHORT).show();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            db.close();
        }
    }


    //数据库中删除某条数据  根据货道编号条件
    public void deleteDatabase(String cargoNum) {
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        db.delete(DatabaseStatic.TABLE_NAME_CARGO, DatabaseStatic.CARGO_NUM + " = ? ",
                new String[]{cargoNum});
        Toast.makeText(context, "数据删除成功", Toast.LENGTH_SHORT).show();
    }



    /**
     * 查询所有数据
     *
     * @return List
     */
    public synchronized <T> List<T> query() {
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        List<T> list = new ArrayList<>();
        String querySql = "select * from " + DatabaseStatic.TABLE_NAME_CARGO;
        Cursor cursor = null;
        try {
            cursor = db.rawQuery(querySql, null);
            while (cursor.moveToNext()) {
                CargoInfoBean cargoInfoBean = new CargoInfoBean();
                cargoInfoBean.setId(cursor.getInt(cursor.getColumnIndex(DatabaseStatic.ID)));
                cargoInfoBean.setCargo_num(cursor.getString(cursor.getColumnIndex(DatabaseStatic.CARGO_NUM)));
                cargoInfoBean.setCost_price(cursor.getString(cursor.getColumnIndex(DatabaseStatic.COST_PRICE)));
                cargoInfoBean.setShop_price(cursor.getString(cursor.getColumnIndex(DatabaseStatic.SHOP_PRICE)));
                cargoInfoBean.setGame_price(cursor.getString(cursor.getColumnIndex(DatabaseStatic.GAME_PRICE)));
                cargoInfoBean.setWinning_probability(cursor.getString(cursor.getColumnIndex(DatabaseStatic.WINNING_PROBABILITY)));
                cargoInfoBean.setCargo_capacity(cursor.getString(cursor.getColumnIndex(DatabaseStatic.CARGO_CAPACITY)));
                cargoInfoBean.setChannel_surplus(cursor.getString(cursor.getColumnIndex(DatabaseStatic.CHANNEL_SURPLUS)));
                cargoInfoBean.setCargo_location(cursor.getString(cursor.getColumnIndex(DatabaseStatic.CARGO_LOCATION)));
                cargoInfoBean.setCargo_state(cursor.getString(cursor.getColumnIndex(DatabaseStatic.CARGO_STATE)));
                cargoInfoBean.setShop_name(cursor.getString(cursor.getColumnIndex(DatabaseStatic.SHOP_NAME)));
                cargoInfoBean.setPicture(cursor.getString(cursor.getColumnIndex(DatabaseStatic.SHOP_PICTURE)));
                list.add((T) cargoInfoBean);
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return list;
    }

    //根据货道编号查询数据库   商品信息
    public synchronized String queryData(String cargoNum){
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        String rawQuerySql = "select * from "+DatabaseStatic.TABLE_NAME_CARGO+" where "+DatabaseStatic.CARGO_NUM +" =?";
        Cursor cursor = null;
        String si = null;
        try {
            cursor = db.rawQuery(rawQuerySql, new String[]{cargoNum});
            if(cursor.moveToFirst()){
                si = cursor.getString(cursor.getColumnIndex(DatabaseStatic.SHOP_ID_SERVER));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return si;
    }


    //根据货道编号查询数据库   库存信息
    public synchronized String queryDataCs(String cargoNum){
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        String rawQuerySql = "select * from "+DatabaseStatic.TABLE_NAME_CARGO+" where "+DatabaseStatic.CARGO_NUM +" =?";
        Cursor cursor = null;
        String cs = null;
        try {
            cursor = db.rawQuery(rawQuerySql, new String[]{cargoNum});
            if(cursor.moveToFirst()){
                cs = cursor.getString(cursor.getColumnIndex(DatabaseStatic.CHANNEL_SURPLUS));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return cs;
    }


    //根据货道编号查询数据库   库存信息
    public synchronized String queryPrice(String cargoNum){
        if(helper == null)
        {
            helper = new CargoHelper(context);
        }
        db = helper.getWritableDatabase();
        String rawQuerySql = "select * from "+DatabaseStatic.TABLE_NAME_CARGO+" where "+DatabaseStatic.CARGO_NUM +" =?";
        Cursor cursor = null;
        String price = null;
        try {
            cursor = db.rawQuery(rawQuerySql, new String[]{cargoNum});
            if(cursor.moveToFirst()){
                price = cursor.getString(cursor.getColumnIndex(DatabaseStatic.SHOP_PRICE));
            }
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (cursor != null) {
                cursor.close();
            }
            if (db != null) {
                db.close();
            }
        }
        return price;
    }


    public void closeDb(){
        db.close();
    }

}

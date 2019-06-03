package com.ice.cj_ice.model.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

/**
 * Created by 17710890509 on 2018/8/13.
 */
/*
 * 在这个类的构造函数里面我们调用了父类的构造方法用来创建数据库文
 * 件，第二个构造方法只是为了方便构造（不用些那么多的参数）
 * 这个类继承了 SQLiteOpenHelper 类，并且重写了父类里面的onCreate方法和 onUpgrade方法，
 * onCreate方法当数据库文件不存在的时候会被调用来创建一个新的数
 * 据库文件
 */
public class CargoHelper extends SQLiteOpenHelper {

    private static CargoHelper helper;

    public static String CREATE_TABLE_CARGO = "create table "+ DatabaseStatic.TABLE_NAME_CARGO +"(" +
            //设置主键自增 Integer primary key autoincrement
            DatabaseStatic.ID + " real, " +
            DatabaseStatic.CARGO_NUM + " varchar(20), " +
            DatabaseStatic.CARGO_NAME + " varchar(20), " +
            DatabaseStatic.COST_PRICE + " varchar(20), " +
            DatabaseStatic.SHOP_PRICE + " varchar(20), " +
            DatabaseStatic.GAME_PRICE + " varchar(20), " +
            DatabaseStatic.WINNING_PROBABILITY + " varchar(20), " +
            DatabaseStatic.CARGO_CAPACITY + " varchar(20), " +
            DatabaseStatic.CHANNEL_SURPLUS + " varchar(20), " +
            DatabaseStatic.CARGO_LOCATION + " varchar(20), " +
            DatabaseStatic.SHOP_ID + " varchar(20), " +
            DatabaseStatic.SHOP_ID_SERVER + " varchar(20), " +
            DatabaseStatic.SHOP_PICTURE + " varchar(20), " +
            DatabaseStatic.SHOP_NAME + " varchar(20), " +
            DatabaseStatic.CARGO_STATE + " varchar(20))"; // 用于创建表的SQL语句

    private Context myContext = null;

    public CargoHelper(Context context) {
        super(context, DatabaseStatic.DATABASE_NAME, null, DatabaseStatic.DATABASE_VERSION);
        myContext = context;
    }


/*//将自定义的数据库创建类单例。 synchronize单例 防止多线程同时那啥  双重锁定
    public static CargoHelper getInstance(Context context) {
        if (helper == null) {
            synchronized (CargoHelper.class) {
                if (helper == null)
                    helper = new CargoHelper(context);
            }
        }
        return helper;
    }*/

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        Log.d("xuezhiyuan","创建货道表成功");
        sqLiteDatabase.execSQL(CREATE_TABLE_CARGO);
    }

    //升级数据库
    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int oldVersion, int newVersion) {
        for (int i = oldVersion; i < newVersion; i++) {
            switch (i) {
                case 1:
                    DataBaseHelper.upToDbVersion2(sqLiteDatabase);
                    break;
                case 2:
                    DataBaseHelper.upToDbVersion3(sqLiteDatabase);
                    break;
                default:
                    break;
            }
        }
    }
}

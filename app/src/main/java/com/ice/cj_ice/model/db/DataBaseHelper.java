package com.ice.cj_ice.model.db;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

/**
 * Created by Administrator on 2019/4/10.
 */

public class DataBaseHelper {
    public static void upToDbVersion2(SQLiteDatabase database) {
        String updateSql = "alter table " + DatabaseStatic.TABLE_NAME_CARGO + " add column store varchar(5)";
        database.execSQL(updateSql);
    }

    public static void upToDbVersion3(SQLiteDatabase database) {
        ContentValues values = new ContentValues();
        values.put("store", 100);
        database.update(DatabaseStatic.TABLE_NAME_CARGO, values, null, null);
    }
}

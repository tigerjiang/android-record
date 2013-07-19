package com.example.collection.data;

import java.util.ArrayList;
import java.util.Arrays;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

public class DBService {
    private static final DBService INSTANCE = new DBService();
    private static SQLiteDatabase mDatabase;
    private static DBHelper sDbHelper;

    private DBService() {

    }

    // 得到一个数据库实例
    public static synchronized DBService getInstance(Context context) {
        if (sDbHelper == null || mDatabase == null) {
            sDbHelper = new DBHelper(context);
            mDatabase = sDbHelper.getWritableDatabase();
        }
        return INSTANCE;
    }

    // 查询数据
    public Cursor queryVaule(String[] columns, String selection,
            String[] selectionArgs, String orderBy) {
        ArrayList<String> result = new ArrayList<String>();
        result.add("_id");
        result.addAll(Arrays.asList(columns));
        final int size = result.size();
        String[] values = (String[]) result.toArray(new String[size]);
        return mDatabase.query(DBHelper.DAYABASE_TABLE_NAME, values, selection,
                selectionArgs, null, null, "system_time desc");
    }

    // 插入数据
    public long insertValue(ContentValues values) {
        values.put("system_time", System.currentTimeMillis() / 1000);
        return mDatabase.insert(DBHelper.DAYABASE_TABLE_NAME, null, values);
    }

    // 更新数据
    public int updateValue(ContentValues values, String whereClause,
            String[] whereArgs) {
        return mDatabase.update(DBHelper.DAYABASE_TABLE_NAME, values,
                whereClause, whereArgs);
    }

    // 删除数据
    public int deleteValue(String whereClause, String[] whereArgs) {
        return mDatabase.delete(DBHelper.DAYABASE_TABLE_NAME, whereClause,
                whereArgs);
    }

    // 关闭数据库
    public void close() {
        if (mDatabase != null) {
            mDatabase.close();
            mDatabase = null;
        }
    }
}

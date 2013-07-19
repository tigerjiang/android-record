package com.example.collection.data;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteDatabase.CursorFactory;
import android.database.sqlite.SQLiteOpenHelper;

public class DBHelper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "collection.db";
    public static final String DAYABASE_TABLE_NAME = "collection";
    private static final int COLUMNS_COUNT = 50;
    private static final int DATABASE_VERSIOIN = 1;
    public static String[] COLUMNS = new String[COLUMNS_COUNT];
    private static StringBuffer sColumnsName;
    static {
        sColumnsName = new StringBuffer();
        COLUMNS[0] = "table_name";
        sColumnsName.append("_id")
                .append(" integer primary key autoincrement ").append(",");
        sColumnsName.append(COLUMNS[0]).append(" varchar ").append(",");
        for (int i = 1; i < COLUMNS_COUNT; i++) {
            COLUMNS[i] = "column" + "_" + i;
            sColumnsName.append(COLUMNS[i]).append(" varchar ").append(",");
        }
        sColumnsName.append("system_time").append(" integer ");
    }

    // 创建数据库
    public DBHelper(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSIOIN);
    }

    // 创建表
    @Override
    public void onCreate(SQLiteDatabase db) {
        String createTable = "CREATE TABLE " + DAYABASE_TABLE_NAME + " ( "
                + sColumnsName.toString() + " ) ";
        db.execSQL(createTable);

    }

    @Override
    public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {

    }

}

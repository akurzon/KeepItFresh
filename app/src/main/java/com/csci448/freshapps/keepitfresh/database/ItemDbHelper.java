package com.csci448.freshapps.keepitfresh.database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ItemDbHelper extends SQLiteOpenHelper {

    private static final int VERSION = 1;
    private static final String DATABASE_NAME = "Items.db";

    public ItemDbHelper(Context context) {
        super(context, DATABASE_NAME, null, VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("create table " + ItemDbSchema.ItemTable.NAME + "(" +
            "_id integer primary key autoincrement, " +
                ItemDbSchema.ItemTable.Cols.UUID + ", " +
                ItemDbSchema.ItemTable.Cols.NAME + ", " +
                ItemDbSchema.ItemTable.Cols.EXPIRE + ", " +
                ItemDbSchema.ItemTable.Cols.PURCHASE + ", " +
                ItemDbSchema.ItemTable.Cols.QUANTITY + ", " +
                ItemDbSchema.ItemTable.Cols.LOCATION + ", " +
                ItemDbSchema.ItemTable.Cols.ON_SHOPPING + ", " +
                ItemDbSchema.ItemTable.Cols.IS_CHECKED + ")"
        );

        db.execSQL("create table " + ItemDbSchema.LocationTable.NAME + "(" +
                "_id integer primary key autoincrement, " +
                ItemDbSchema.LocationTable.Cols.NAME + ")"
        );
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {

    }
}

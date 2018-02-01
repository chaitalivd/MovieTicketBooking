package com.example.indian.movieticketbooking.helper;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.indian.movieticketbooking.adapters.DataBaseHandler;

/**
 * Created by Chaitali on 26/01/2018.
 */

public class DataBaseHelper extends SQLiteOpenHelper {

    public DataBaseHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(DataBaseHandler.USER_TABLE_CREATE);
        db.execSQL(DataBaseHandler.REVIEW_TABLE_CREATE);
        db.execSQL(DataBaseHandler.TICKETS_TABLE_CREATE);
        db.execSQL(DataBaseHandler.AVAILABILITY_TABLE_CREATE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        //log the version upgrade..
        //upgrading...
        db.execSQL("DROP TABLE IF EXISTS "+"TEMPLATE");
        onCreate(db);
    }
}
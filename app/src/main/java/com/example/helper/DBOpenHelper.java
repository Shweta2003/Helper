package com.example.helper;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

public class DBOpenHelper extends SQLiteOpenHelper {
    private static final String CREATE_EVENTS_TABLE = "create table "+DBStructur.EVENT_TABLE_NAME+"(ID INTEGER PRIMARY KEY AUTOINCREMENT, " + DBStructur.EVENT+" TEXT," +
            DBStructur.TIME+" TEXT, "+ DBStructur.DATE+" TEXT, "+DBStructur.MONTH + " TEXT, "+ DBStructur.YEAR + " TEXT, "+DBStructur.Notify+" TEXT)";

    private static final String DROP_EVENTS_TABLE = "DROP TABLE IF EXISTS "+DBStructur.EVENT_TABLE_NAME;

    public DBOpenHelper(@Nullable Context context) {
        super(context, DBStructur.DB_NAME, null, DBStructur.DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase sqLiteDatabase) {
        sqLiteDatabase.execSQL(CREATE_EVENTS_TABLE);
    }

    @Override
    public void onUpgrade(SQLiteDatabase sqLiteDatabase, int i, int i1) {
        sqLiteDatabase.execSQL(DROP_EVENTS_TABLE);
    }

    public void SaveEvent(String event, String time, String date, String month, String year,String notify, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructur.EVENT,event);
        contentValues.put(DBStructur.TIME,time);
        contentValues.put(DBStructur.DATE,date);
        contentValues.put(DBStructur.MONTH,month);
        contentValues.put(DBStructur.YEAR,year);
        contentValues.put(DBStructur.Notify,notify);
        database.insert(DBStructur.EVENT_TABLE_NAME,null,contentValues);


    }

    public Cursor ReadEvents(String dtae, SQLiteDatabase database){
        String [] ProjectIcons = {DBStructur.EVENT,DBStructur.TIME,DBStructur.DATE,DBStructur.MONTH,DBStructur.YEAR};
        String selection = DBStructur.DATE+"=?";
        String [] selectionArgs = {dtae};

        return database.query(DBStructur.EVENT_TABLE_NAME,ProjectIcons,selection,selectionArgs,null,null,null);

    }

    public Cursor ReadIdEvents(String dtae, String event, String time, SQLiteDatabase database){
        String [] ProjectIcons = {DBStructur.ID,DBStructur.Notify};
        String selection = DBStructur.DATE+"=? and "+DBStructur.EVENT + "=? and "+DBStructur.TIME + "=?";
        String [] selectionArgs = {dtae,event,time};

        return database.query(DBStructur.EVENT_TABLE_NAME,ProjectIcons,selection,selectionArgs,null,null,null);

    }

    public Cursor ReadEventsMonth(String month,String year, SQLiteDatabase database){
        String [] ProjectIcons = {DBStructur.EVENT,DBStructur.TIME,DBStructur.DATE,DBStructur.MONTH,DBStructur.YEAR};
        String selection = DBStructur.MONTH+"=? and "+DBStructur.YEAR+"=?";
        String [] selectionArgs = {month,year};

        return database.query(DBStructur.EVENT_TABLE_NAME,ProjectIcons,selection,selectionArgs,null,null,null);

    }

    public void deleteEvent(String event, String date, String time, SQLiteDatabase database){
        String collection = DBStructur.EVENT+"=? and "+DBStructur.DATE+"=? and "+DBStructur.TIME+"=?";
        String[] selectArgs = {event,date,time};
        database.delete(DBStructur.EVENT_TABLE_NAME,collection,selectArgs);
    }

    public void updateEvent(String dtae, String event, String time,String NotifyVal, SQLiteDatabase database){
        ContentValues contentValues = new ContentValues();
        contentValues.put(DBStructur.Notify,NotifyVal);
        String selection = DBStructur.DATE+"=? and "+DBStructur.EVENT + "=? and "+DBStructur.TIME + "=?";
        String [] selectionArgs = {dtae,event,time};
        database.update(DBStructur.EVENT_TABLE_NAME,contentValues,selection,selectionArgs);
    }
}

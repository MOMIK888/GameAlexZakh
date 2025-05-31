package com.bestproject.main.android.Databases;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.bestproject.main.Game.DatabaseInterface;

public class PlayerInfo extends SQLiteOpenHelper implements DatabaseInterface {
    private static final String DB_NAME = "playerInfo.db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "playerInformation";
    private static final String COLUMN_ID = "id";
    private static final String COLUMN_INFO = "info";
    public PlayerInfo(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String query = "CREATE TABLE " + TABLE_NAME + " (" +
            COLUMN_ID + " INTEGER PRIMARY KEY, " +
            COLUMN_INFO + " TEXT);";
        db.execSQL(query);
    }
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }
    @Override
    public void setInfo(int index, String info) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();
        cv.put(COLUMN_ID, index);
        cv.put(COLUMN_INFO, info);

        long result = db.replace(TABLE_NAME, null, cv);
        db.close();
    }
    @Override
    public String getInfo(int index) {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{COLUMN_INFO},
            COLUMN_ID + "=?", new String[]{String.valueOf(index)},
            null, null, null);
        String info = null;
        if (cursor.moveToFirst()) {
            info = cursor.getString(cursor.getColumnIndexOrThrow(COLUMN_INFO));
        }
        cursor.close();
        db.close();
        return info;
    }
}

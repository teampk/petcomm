package com.example.petcomm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.petcomm.model.Dog;
import com.example.petcomm.model.FeedSchedule;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE IF NOT EXISTS FEEDSCHEDULE (_id INTEGER PRIMARY KEY AUTOINCREMENT, feederId TEXT, feedTime TEXT, feedAmount TEXT)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // FEED SCHEDULE LIST

    public void addFeederSchedule(String feederId, String feedTime, String feedSchedule){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO FEEDSCHEDULE VALUES(null, '" +feederId+"','"+feedTime+"','"+feedSchedule+"')");
        db.close();
    }
    public ArrayList<FeedSchedule> getScheduleData(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<FeedSchedule> scheduleData = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM FEEDSCHEDULE", null);
        while (cursor.moveToNext()){
            FeedSchedule scheduleElement = new FeedSchedule(
                    Integer.valueOf(cursor.getString(0)),
                    cursor.getString(1),
                    Integer.valueOf(cursor.getString(2)),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            scheduleData.add(scheduleElement);
        }

        return scheduleData;
    }

    public ArrayList<FeedSchedule> getScheduleDataByFeederId(String mFeederId){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<FeedSchedule> scheduleData = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM FEEDSCHEDULE WHERE feederId = '" + mFeederId + "';", null);
        while (cursor.moveToNext()){
            FeedSchedule scheduleElement = new FeedSchedule(
                    Integer.valueOf(cursor.getString(0)),
                    cursor.getString(1),
                    Integer.valueOf(cursor.getString(2)),
                    cursor.getString(3),
                    cursor.getString(4)
            );
            scheduleData.add(scheduleElement);
        }
        return scheduleData;
    }

    public void deleteScheduleByFeederId(String mFeederId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM FEEDSCHEDULE WHERE feederId = '" + mFeederId + "';");
        db.close();
    }


    // Delete All Data
    public void deleteDataAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM DOGLIST;");
        db.execSQL("DELETE FROM FEEDSCHEDULE;");
        db.close();
    }
}

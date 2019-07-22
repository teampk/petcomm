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
        db.execSQL("CREATE TABLE IF NOT EXISTS DOGLIST (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, gender TEXT," +
                "breeds TEXT, birth TEXT, weight TEXT, email TEXT, feederId TEXT, toiletId TEXT)");

        db.execSQL("CREATE TABLE IF NOT EXISTS FEEDSCHEDULE (_id INTEGER PRIMARY KEY AUTOINCREMENT, feederId TEXT, feedTime TEXT, feedAmount TEXT)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    // DOG DATA

    public void addDog(String name, String gender, String breeds, String birth, String weight, String email, String feederId, String toiletId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO DOGLIST VALUES(null, '" +name+"','"+gender+"','"+breeds+"','"+birth+"','"+weight+"','"+email+"','"+feederId+"','"+toiletId+"')");
        db.close();
    }

    public ArrayList<Dog> getDogData(){
        SQLiteDatabase db = getReadableDatabase();
        ArrayList<Dog> dogData = new ArrayList<>();
        Cursor cursor = db.rawQuery("SELECT * FROM DOGLIST", null);
        while (cursor.moveToNext()){
            Dog dogElement = new Dog(
                    Integer.valueOf(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8)
            );
            dogData.add(dogElement);
        }

        return dogData;
    }

    public Dog getDogById(int id){
        SQLiteDatabase db = getReadableDatabase();
        Dog dataElement = null;
        Cursor cursor = db.rawQuery("SELECT * FROM DOGLIST WHERE _id = '" + id + "';", null);
        while (cursor.moveToNext()){
            dataElement = new Dog(
                    Integer.valueOf(cursor.getString(0)),
                    cursor.getString(1),
                    cursor.getString(2),
                    cursor.getString(3),
                    cursor.getString(4),
                    cursor.getString(5),
                    cursor.getString(6),
                    cursor.getString(7),
                    cursor.getString(8)
            );
        }
        return dataElement;
    }

    public int getHightestDogId(){
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery("SELECT * FROM DOGLIST", null);
        int i=0;
        while (cursor.moveToNext()){
            i=Integer.valueOf(cursor.getString(0));
        }
        return i;
    }


    public void registerFeeder(int id, String feederId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE DOGLIST SET feederId='" + feederId + "' WHERE _id='" + id + "';");
    }
    public void registerToilet(int id, String toiletId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("UPDATE DOGLIST SET toiletId='" + toiletId + "' WHERE _id='" + id + "';");
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
                    cursor.getString(2),
                    cursor.getString(3)
            );
            scheduleData.add(scheduleElement);
        }

        return scheduleData;
    }


    // Delete All Data
    public void deleteDataAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM DOGLIST;");
        db.execSQL("DELETE FROM FEEDSCHEDULE;");
        db.close();
    }
}

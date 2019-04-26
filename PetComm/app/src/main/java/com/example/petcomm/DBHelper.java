package com.example.petcomm;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.petcomm.model.Dog;

import java.util.ArrayList;

public class DBHelper extends SQLiteOpenHelper {

    public DBHelper(Context context, String name, SQLiteDatabase.CursorFactory factory, int version){
        super(context, name, factory, version);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE DOGLIST (_id INTEGER PRIMARY KEY AUTOINCREMENT, name TEXT, gender TEXT," +
                "species TEXT, birth TEXT, weight TEXT, feederId, toiletId)");

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public void addDog(String name, String gender, String species, String birth, String weight, String feederId, String toiletId){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("INSERT INTO DOGLIST VALUES(null, '" +name+"','"+gender+"','"+species+"','"+birth+"','"+weight+"','"+feederId+"','"+toiletId+"')");
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
                    cursor.getString(7)
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
                    cursor.getString(7)
            );
        }
        return dataElement;
    }

    public void deleteDogDataAll(){
        SQLiteDatabase db = getWritableDatabase();
        db.execSQL("DELETE FROM DOGLIST;");
        db.close();
    }

}

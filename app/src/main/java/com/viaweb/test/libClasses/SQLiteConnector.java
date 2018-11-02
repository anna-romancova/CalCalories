package com.viaweb.test.libClasses;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.util.ArrayList;

import edu.itstap.calculator.Food;
import edu.itstap.calculator.FoodInHistory;
import edu.itstap.calculator.User;


public class SQLiteConnector extends SQLiteOpenHelper {
    private Context context;

    public SQLiteConnector(Context context, String name,
                           int version) {
        super(context, name, null, version);
        this.context = context;
       Log.e("Connector","DB has been connected");
        // TODO Auto-generated constructor stub
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("Create table historyMenu(_id integer primary key autoincrement,  Date TEXT,  menu TEXT);");
        db.execSQL("Create table foods(_id integer primary key autoincrement, name TEXT, proteins REAL, fats REAL, carbohydrates REAL,calories REAL);");

        Toast.makeText(context, "DB has been created", Toast.LENGTH_SHORT).show();


    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        if (oldVersion == 1 && newVersion == 2) {

        } else if (oldVersion == 2 && newVersion == 3) {

        } else if (oldVersion == 1 && newVersion == 3) {

        }

    }

    /*---methods of History----*/
    /*insert*/
    public long saveHistoryMenu(User user) {

        Gson gs = new Gson();
        String history = gs.toJson(user.getHistoryFoods().get(0));

        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("Date", String.valueOf(user.getHistoryFoods().get(0).get(0).getTime()));
        values.put("menu", history);


        long id = db.insert("historyMenu", null, values);


        return id;
    }

    /*Update*/



    /*select*/

    /*all*/
    public Cursor selectAllHistory() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from historyMenu ";
        Cursor c = db.rawQuery(selectQuery, null);
       /* ArrayList<ArrayList<FoodInHistory>> allHistory = new ArrayList<>();
        Gson gs = new Gson();
        if (c.moveToFirst()) {
            do {
                Type type = new TypeToken<ArrayList<FoodInHistory>>() {
                }.getType();
                ArrayList<FoodInHistory> fHistory = gs.fromJson((c.getString(c.getColumnIndex("menu"))), type);
                allHistory.add(fHistory);
                user.setHistoryFoods(allHistory);
            } while (c.moveToNext());
        }*/

        return c;
    }
    public ArrayList<ArrayList<FoodInHistory>> selectOneHistory(int id ){
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from historyMenu Where _id="+id;
        Cursor c = db.rawQuery(selectQuery, null);
          ArrayList<ArrayList<FoodInHistory>> allHistory = new ArrayList<>();
        Gson gs = new Gson();
        if (c.moveToFirst()) {
            do {
                Type type = new TypeToken<ArrayList<FoodInHistory>>() {
                }.getType();
                ArrayList<FoodInHistory> fHistory = gs.fromJson((c.getString(c.getColumnIndex("menu"))), type);
                allHistory.add(fHistory);

            } while (c.moveToNext());
        }
       return allHistory;

    }

    public User selectHistoryWithTime(String fromtime, String totime, User user) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from historyMenu where  DATE_FORMAT(Date,'%Y-%m-%d') BETWEEN '%" + fromtime + "%' AND  '%" + totime + "%' ";
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<ArrayList<FoodInHistory>> allHistory = new ArrayList<>();
        Gson gs = new Gson();
        if (c.moveToFirst()) {
            do {
                Type type = new TypeToken<ArrayList<FoodInHistory>>() {
                }.getType();
                ArrayList<FoodInHistory> fHistory = gs.fromJson((c.getString(c.getColumnIndex("menu"))), type);
                allHistory.add(fHistory);
                user.setHistoryFoods(allHistory);
            } while (c.moveToNext());
        }

        return user;


    }


    /*delete*/
    public void cleanHistory() {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("historyMenu", null,
                null);
    }
    public void cleanOneHistory(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("historyMenu", "_id=?",
                new String[]{String.valueOf(id)});
    }


    /*---methods of foods----*/
    /*insert*/
    public long insertFood(Food food) {

        /*Gson gs =new Gson();
        String history= gs.toJson(user.getHistoryFoods().get(0));*/


        SQLiteDatabase db = this.getWritableDatabase();
//        name , proteins , fats , carbohydrates ,calories
        ContentValues values = new ContentValues();
        values.put("name", food.getName());
        values.put("proteins", food.getProtein());
        values.put("fats", food.getFats());
        values.put("carbohydrates", food.getCarbohydrate());
        values.put("calories", food.getCalories());


        long id = db.insert("foods", null, values);


        return id;
    }


    /*Update*/
    public int updateFood(Food food) {
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put("name", food.getName());
        values.put("proteins", food.getProtein());
        values.put("fats", food.getFats());
        values.put("carbohydrates", food.getCarbohydrate());
        values.put("calories", food.getCalories());

        // updating row
        return db.update("foods", values, "name   = ?",
                new String[]{food.getName()});
    }


    /*select*/
    /*one*/
    public ArrayList<Food> selectFoods(String name) {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from foods where name Like '%" + name+"%'";
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<Food> resultist = new ArrayList<>();

        if (c.moveToFirst()) {
            do {
                resultist.add(new Food(c.getString(c.getColumnIndex("name")),
                        c.getDouble(c.getColumnIndex("calories")),
                        c.getDouble(c.getColumnIndex("proteins")),
                        c.getDouble(c.getColumnIndex("fats")),
                        c.getDouble(c.getColumnIndex("carbohydrates"))));

            } while (c.moveToNext());
        }

        return resultist;
    }

    /*all*/
    public Cursor selectAllFoods() {
        SQLiteDatabase db = this.getReadableDatabase();
        String selectQuery = "select * from foods ";
        Cursor c = db.rawQuery(selectQuery, null);
        ArrayList<Food> resultist = new ArrayList<>();

      /*  if (c.moveToFirst()) {
            do {
                resultist.add(new Food(c.getString(c.getColumnIndex("name")),
                        c.getDouble(c.getColumnIndex("calories")),
                        c.getDouble(c.getColumnIndex("proteins")),
                        c.getDouble(c.getColumnIndex("fats")),
                        c.getDouble(c.getColumnIndex("carbohydrates"))));

            } while (c.moveToNext());
        }*/

        return c;
    }


    /*delete*/
    public void deleteFood(int id) {
        SQLiteDatabase db = this.getWritableDatabase();
        db.delete("foods", "_id" + " = ?",
                new String[]{String.valueOf(id)});
    }


}

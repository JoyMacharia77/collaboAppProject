package com.joymacharia.collaboapp;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

import java.util.Date;

import static android.os.Build.ID;

public class DatabaseHelper extends SQLiteOpenHelper {

    private static final String TAG = "DatabaseHelper";
    private static final String TABLE_NAME = "tasks_table";
    private static final String COL1 = "ID";
    private static final String COL2 = "task";
   // private static final String COL3 = "deadline";

    public DatabaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
      /* String createTable = "CREATE TABLE " + TABLE_NAME + "(ID INTEGER PRIMARY KEY AUTOINCREMENT, " +
               COL2 + "TEXT," + COL3 + "TEXT)";*/
        /*String createTable = "CREATE TABLE " + TABLE_NAME + " (" + ID +" INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COL2 +" TEXT)"; */
        String createTable = "CREATE TABLE " + TABLE_NAME + " ("
                + COL1 + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + COL2 + " TEXT);";
       db.execSQL(createTable);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
          db.execSQL( "DROP TABLE IF EXISTS " + TABLE_NAME);
          onCreate(db);
    }

    public boolean addTasks(String item)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL2, item);

        Log.d(TAG, "addTasks: Adding" + item + "to" +TABLE_NAME);

        long result = db.insert(TABLE_NAME, null, contentValues);

        //if data is inserted incorrectly it will return -1
        if(result == -1)
        {
            return false;
        } else
        {
            return true;
        }
    }

    //Returns all data from db
    public Cursor getTasks()
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        Cursor tasks = db.rawQuery(query, null);
        return tasks;
    }

    //Returns only the ID that matches the task passed in
    public Cursor getItemID(String task)
    {
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "SELECT " + COL1 + " FROM " + TABLE_NAME +
                " WHERE " + COL2 + " = '" + task + "'";
        Cursor tasks = db.rawQuery(query, null);
        Log.d(TAG, "getItemID: query: " + query);
        return tasks;
    }

    //updates tasks
    public void updateTask(String newTask, int id, String oldTask){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "UPDATE " + TABLE_NAME + " SET " + COL2 +
                " = '" + newTask + "' WHERE " + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + oldTask + "'";
        Log.d(TAG, "updateTask: query: " + query);
        Log.d(TAG, "updateTask: Setting task to " + newTask);
        db.execSQL(query);
    }

    //delete tasks
    public void deleteTask(int id, String task){
        SQLiteDatabase db = this.getWritableDatabase();
        String query = "DELETE FROM " + TABLE_NAME + " WHERE "
                + COL1 + " = '" + id + "'" +
                " AND " + COL2 + " = '" + task + "'";
        Log.d(TAG, "deleteTask: query: " + query);
        Log.d(TAG, "deleteTask: Deleting " + task + " from database.");
        db.execSQL(query);
    }
}














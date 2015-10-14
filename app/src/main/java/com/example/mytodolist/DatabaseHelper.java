package com.example.mytodolist;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.example.mytodolist.DatabaseContract.*;

public class DatabaseHelper extends SQLiteOpenHelper {
    public static final int DATABASE_VERSION = 1;
    public static final String DATABASE_NAME = "mytodolist.db";

    private static final String TEXT_TYPE = " TEXT";
    private static final String COMMA = ",";
    private static final String SQL_CREATE_ENTRIES =
            "CREATE TABLE " + TodoListTable.TABLE_NAME + " ( " +
                    TodoListTable._ID + " INTEGER PRIMARY KEY, " +
                    TodoListTable.COLUMN_ENTRY + TEXT_TYPE + COMMA +
                    TodoListTable.COLUMN_MARKED + " INTEGER)";
    private static final String SQL_DELETE_ENTRIES =
            "DROP TABLE IF EXISTS " + TodoListTable.TABLE_NAME;

    public DatabaseHelper (Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(SQL_CREATE_ENTRIES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // there is no need for this now
    }
}

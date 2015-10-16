package com.example.mytodolist;

import android.app.Application;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;

public class MyApplicationClass extends Application {
    private static MyApplicationClass singleton;
    private ArrayList<Entry> dataset;

    public static MyApplicationClass getInstance () {
        return singleton;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        dataset = new ArrayList<Entry>();
        singleton = this;

        // performance test
//        for (int i = 0; i < 1000; i++) {
//            dataset.add(new Entry("test "+i));
//        }

        // load todolist from database
        // putting this here ensures that database is loaded only once in the app lifecycle
        DatabaseHelper dh = new DatabaseHelper(getApplicationContext());

        SQLiteDatabase db = dh.getReadableDatabase();

        String[] projection = {
                DatabaseContract.TodoListTable.COLUMN_ENTRY,
                DatabaseContract.TodoListTable.COLUMN_MARKED
        };

        Cursor c = db.query(DatabaseContract.TodoListTable.TABLE_NAME, projection, null, null, null, null, DatabaseContract.TodoListTable._ID);

        if (c != null && c.moveToFirst()) {
            do {
                String text = c.getString(c.getColumnIndex(DatabaseContract.TodoListTable.COLUMN_ENTRY));
                int mark = c.getInt(c.getColumnIndex(DatabaseContract.TodoListTable.COLUMN_MARKED));

                dataset.add(new Entry(text, mark));
            } while (c.moveToNext());

            c.close();
        }

        db.close();
    }

    public ArrayList<Entry> getDataset() {
        return dataset;
    }
}

package com.example.mytodolist;

import android.provider.BaseColumns;

public final class DatabaseContract {
    private DatabaseContract () {}

    public static abstract class TodoListTable implements BaseColumns {
        public static final String TABLE_NAME = "todolist";
        public static final String COLUMN_ENTRY = "entry";
        public static final String COLUMN_MARKED = "marked";
    }
}

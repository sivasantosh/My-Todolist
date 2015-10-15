package com.example.mytodolist;

import android.app.Application;

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
    }

    public Entry getEntry (int i) {
        return dataset.get(i);
    }

    public void appendEntry (Entry e) {
        dataset.add(e);
    }

    public int getEntryPos (Entry e) {
        return dataset.indexOf(e);
    }

    public int entryCount () {
        return dataset.size();
    }

    public Entry removeEntry (int i) {
        return dataset.remove(i);
    }
}

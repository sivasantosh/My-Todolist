package com.example.mytodolist;

import android.app.Application;
import android.util.Log;

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
        Log.d("testapp", "application oncreate");
        dataset = new ArrayList<Entry>();
        for (int i = 0; i < 5; i++) {
            appendEntry(new Entry("test entry " + i));
        }
        singleton = this;
    }

    public Entry getEntry (int i) {
        return dataset.get(i);
    }

    public void appendEntry (Entry e) {
        dataset.add(e);
    }

    public int getPos (Entry e) {
        return dataset.indexOf(e);
    }

    public int entryCount () {
        return dataset.size();
    }
}

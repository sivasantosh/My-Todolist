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

    public ArrayList<Entry> getDataset() {
        return dataset;
    }
}

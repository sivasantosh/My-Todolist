package com.example.mytodolist;

public class Entry {
    String text;
    int marked;
    boolean checked;

    public static final int MARK_DO = 0;
    public static final int MARK_DONE = 1;
    public static final int MARK_IMP = 2;

    Entry (String t) {
        text = t;
        marked = MARK_DO;
        checked = false;
    }
}

package com.example.mytodolist;

public class Entry {
    String text;
    int marked;
    boolean checked;

    public static final int MARK_DO = 0;
    public static final int MARK_DONE = 1;
    public static final int MARK_IMP = 2;

    Entry (String t) {
        createEntry (t, MARK_DO);
    }

    Entry (String t, int m) {
        createEntry(t, m);
    }

    private void createEntry (String t, int m) {
        text = t;
        marked = m;
        checked = false;
    }
}

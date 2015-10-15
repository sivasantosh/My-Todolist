package com.example.mytodolist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.ActionMode;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    MyAdapter adapter;
    MyApplicationClass appdata;
    ActionMode actionMode;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appdata = MyApplicationClass.getInstance();

        RecyclerView r = (RecyclerView) findViewById(R.id.todolistView);

        r.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(this);
        r.setAdapter(adapter);


        if (appdata.getDataset().size() == 0) {
            r.setVisibility(View.GONE);
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.addEntry:
                Intent intent = new Intent(this, TodoEntryActivity.class);
                startActivityForResult(intent, 1, null);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }

        return true;
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);

        MenuInflater menuInflater = getMenuInflater();
        menuInflater.inflate(R.menu.context_menu_main, menu);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String entry_text = data.getStringExtra("entry_text");

            if (entry_text != null && entry_text.length() > 0) {
                adapter.add(new Entry(entry_text));

                RecyclerView r = (RecyclerView) findViewById(R.id.todolistView);
                r.setVisibility(View.VISIBLE);
                r.smoothScrollToPosition(appdata.getDataset().size());
            }
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            String entry_text = data.getStringExtra("entry_text");
            int pos = data.getIntExtra("entry_pos", -1);

            if (entry_text.length() > 0 && pos >= 0) {
                adapter.update(entry_text, pos);
            }
        }
    }

    public void editEntry (int pos) {
        Entry e = appdata.getDataset().get(pos);
        Intent intent = new Intent(this, EditEntryActivity.class);
        intent.putExtra("entry_text", e.text);
        intent.putExtra("entry_pos", pos);

        startActivityForResult(intent, 2);
    }

    public void showContextMenu () {
        actionMode = startActionMode(actionModeCallback);
    }

    private ActionMode.Callback actionModeCallback = new ActionMode.Callback() {
        @Override
        public boolean onCreateActionMode(ActionMode mode, Menu menu) {
            MenuInflater inflater = mode.getMenuInflater();
            inflater.inflate(R.menu.context_menu_main, menu);
            return true;
        }

        @Override
        public boolean onPrepareActionMode(ActionMode mode, Menu menu) {
            return false;
        }

        @Override
        public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
            MyApplicationClass appdata = MyApplicationClass.getInstance();
            boolean flag = false;

            switch (item.getItemId()) {
                case R.id.context_menu_mark_done:
                    adapter.markCheckEntriesAs(Entry.MARK_DONE);
                    break;
                case R.id.context_menu_mark_imp:
                    adapter.markCheckEntriesAs(Entry.MARK_IMP);
                    break;
                case R.id.context_menu_no_mark:
                    adapter.markCheckEntriesAs(Entry.MARK_DO);
                    break;
                case R.id.context_menu_del:
                    adapter.deleteCheckEntries();
                    if (appdata.getDataset().size() == 0) {
                        RecyclerView r = (RecyclerView) findViewById(R.id.todolistView);
                        r.setVisibility(View.GONE);
                    }
                    break;
                case R.id.context_menu_select_all:
                    adapter.checkAllEntries();
                    flag = true;
                    break;
                case R.id.context_menu_select_none:
                    adapter.uncheckAllEntries();
                    flag = true;
                    break;
            }

            if (!flag) {
                actionMode.finish();
            }

            return true;
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.stopSelectMode();
        }
    };

    class SaveToDB extends AsyncTask<String, Integer, Boolean> {
        @Override
        protected Boolean doInBackground(String... params) {
            DatabaseHelper dh = new DatabaseHelper(getApplicationContext());
            SQLiteDatabase db = dh.getWritableDatabase();

            db.beginTransaction();

            try {
                db.delete(DatabaseContract.TodoListTable.TABLE_NAME, null, null);

                ContentValues values = new ContentValues();

                ArrayList<Entry> dataset = MyApplicationClass.getInstance().getDataset();

                for (int i = 0, s = dataset.size(); i < s; i++) {
                    values.clear();
                    Entry e = dataset.get(i);
                    values.put(DatabaseContract.TodoListTable.COLUMN_ENTRY, e.text);
                    values.put(DatabaseContract.TodoListTable.COLUMN_MARKED, e.marked);

                    db.insert(DatabaseContract.TodoListTable.TABLE_NAME, null, values);
                }

                db.setTransactionSuccessful();
            } finally {
                db.endTransaction();
            }

            db.close();

            return true;
        }
    }

    @Override
    protected void onStop() {
        new SaveToDB().execute("save");

        super.onStop();
    }
}

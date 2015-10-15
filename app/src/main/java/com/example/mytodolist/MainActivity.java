package com.example.mytodolist;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
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

public class MainActivity extends AppCompatActivity {
    MyAdapter adapter;
    MyApplicationClass appdata;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        appdata = MyApplicationClass.getInstance();

        RecyclerView r = (RecyclerView) findViewById(R.id.todolistView);

        r.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(this);
        r.setAdapter(adapter);

        new LoadFromDatabase().execute("test");
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
        startActionMode(actionModeCallback);
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

            switch (item.getItemId()) {
                case R.id.context_menu_mark_done:
                    adapter.markCheckEntriesAs(Entry.MARK_DONE);
                    return true;
                case R.id.context_menu_mark_imp:
                    adapter.markCheckEntriesAs(Entry.MARK_IMP);
                    return true;
                case R.id.context_menu_no_mark:
                    adapter.markCheckEntriesAs(Entry.MARK_DO);
                    return true;
                case R.id.context_menu_del:
                    adapter.deleteCheckEntries();
                    return true;
                case R.id.context_menu_select_all:
                    adapter.checkAllEntries();
                    return true;
                case R.id.context_menu_select_none:
                    adapter.uncheckAllEntries();
                    return true;
                default:
                    return false;
            }
        }

        @Override
        public void onDestroyActionMode(ActionMode mode) {
            adapter.stopSelectMode();
        }
    };

    // looks like this is not needed. It loads pretty fast.
    class LoadFromDatabase extends AsyncTask<String, Integer, Boolean> {
        MyApplicationClass appdata;

        @Override
        protected Boolean doInBackground(String... params) {
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

                    MyApplicationClass.getInstance().getDataset().add(new Entry(text, mark));
                } while (c.moveToNext());

                c.close();
            }

            return true;
        }

        @Override
        protected void onPostExecute(Boolean aBoolean) {
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    protected void onStop() {
        DatabaseHelper dh = new DatabaseHelper(this);
        SQLiteDatabase db = dh.getWritableDatabase();

        db.delete(DatabaseContract.TodoListTable.TABLE_NAME, null, null);

        ContentValues values = new ContentValues();

        MyApplicationClass appdata = MyApplicationClass.getInstance();

        while (appdata.getDataset().size() > 0) {
            values.clear();
            Entry e = appdata.getDataset().remove(0);
            values.put(DatabaseContract.TodoListTable.COLUMN_ENTRY, e.text);
            values.put(DatabaseContract.TodoListTable.COLUMN_MARKED, e.marked);

            db.insert(DatabaseContract.TodoListTable.TABLE_NAME, null, values);
        }

        super.onStop();
    }
}

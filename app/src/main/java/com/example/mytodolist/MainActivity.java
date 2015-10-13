package com.example.mytodolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    MyAdapter adapter;
    ArrayList<Entry> dataset;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dataset = new ArrayList<Entry>();
        for (int i = 0; i < 5; i++) {
            dataset.add(new Entry("test entry "+i));
        }

        RecyclerView r = (RecyclerView) findViewById(R.id.todolistView);

        r.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(this, dataset);
        r.setAdapter(adapter);
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
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            String entry_text = data.getStringExtra("entry_text");

            if (entry_text != null && entry_text.length() > 0) {
                adapter.add(new Entry(entry_text));

                RecyclerView r = (RecyclerView) findViewById(R.id.todolistView);
                r.smoothScrollToPosition(dataset.size());
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
        Entry e = dataset.get(pos);
        Intent intent = new Intent(this, EditEntryActivity.class);
        intent.putExtra("entry_text", e.text);
        intent.putExtra("entry_pos", pos);

        startActivityForResult(intent, 2);
    }
}

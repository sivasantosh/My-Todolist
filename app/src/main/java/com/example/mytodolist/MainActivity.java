package com.example.mytodolist;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    MyAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Entry> dataset = new ArrayList<Entry>();
        for (int i = 0; i < 2; i++) {
            dataset.add(new Entry("test entry "+i, (int) Math.floor(Math.random() * 3)));
        }

        RecyclerView r = (RecyclerView) findViewById(R.id.todolistView);

        r.setLayoutManager(new LinearLayoutManager(this));

        adapter = new MyAdapter(dataset);
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
            boolean mark_imp = data.getBooleanExtra("mark_imp", false);

            if (entry_text != null) {
                adapter.add(new Entry(entry_text, mark_imp?Entry.MARK_IMP:Entry.MARK_DO));
            }
        }
    }
}

class Entry {
    String text;
    int marked;

    public static final int MARK_DO = 0;
    public static final int MARK_DONE = 1;
    public static final int MARK_IMP = 2;

    Entry (String t, int m) {
        text = t;
        marked = m;
    }
}

class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    ArrayList<Entry> dataset;

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;

        ViewHolder (View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.imageView);
            textView = (TextView) v.findViewById(R.id.textView);
        }
    }

    MyAdapter (ArrayList<Entry> data) {
        dataset = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todolist_item_layout, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, int i) {
        Entry e = dataset.get(i);
        viewHolder.textView.setText(e.text);

        int m;
        switch (e.marked) {
            case 1:
                m = R.drawable.mark_done;
                break;
            case 2:
                m = R.drawable.mark_imp;
                break;
            default:
                m = R.drawable.mark_do;
                break;
        }

        viewHolder.imageView.setImageResource(m);
    }

    @Override
    public int getItemCount() {
        return dataset.size();
    }

    void add (Entry e) {
        dataset.add(e);
        notifyItemInserted(dataset.indexOf(e));
    }
}
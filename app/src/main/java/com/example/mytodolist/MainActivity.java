package com.example.mytodolist;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ArrayList<Entry> dataset = new ArrayList<Entry>();
        for (int i = 0; i < 10; i++) {
            dataset.add(new Entry("test entry "+i, (int) Math.floor(Math.random() * 3)));
        }

        RecyclerView r = (RecyclerView) findViewById(R.id.todolistView);

        r.setLayoutManager(new LinearLayoutManager(this));

        r.setAdapter(new MyAdapter(dataset));
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}

class Entry {
    String text;
    int marked;

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
}
package com.example.mytodolist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    ArrayList<Entry> dataset;
    MainActivity activity;
    boolean selectMode = false;

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        CheckBox checkBox;

        ViewHolder (View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.imageView);
            textView = (TextView) v.findViewById(R.id.textView);
            checkBox = (CheckBox) v.findViewById(R.id.checkBox);
        }
    }

    MyAdapter (MainActivity activity, ArrayList<Entry> data) {
        this.activity = activity;
        dataset = data;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todolist_item_layout, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int i) {
        Entry e = dataset.get(i);
        viewHolder.textView.setText(e.text);

        if (selectMode) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    viewHolder.checkBox.setChecked(!viewHolder.checkBox.isChecked());
                }
            });
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.editEntry(i);
                }
            });
        }

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

    void update (String text, int pos) {
        Entry e = dataset.get(pos);
        e.text = text;

        notifyItemChanged(pos);
    }

    boolean isSelectMode () {
        return selectMode;
    }

    void startSelectMode () {
        selectMode = true;
        notifyItemRangeChanged(0, dataset.size());
    }

    void stopSelectMode () {
        selectMode = false;
        notifyItemRangeChanged(0, dataset.size());
    }
}
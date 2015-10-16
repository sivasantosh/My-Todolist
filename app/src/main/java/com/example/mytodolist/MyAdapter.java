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
    MainActivity activity;
    ArrayList<Entry> dataset;
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

    MyAdapter (MainActivity activity) {
        this.activity = activity;
        dataset = MyApplicationClass.getInstance().getDataset();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todolist_item_layout, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Entry thisEntry = dataset.get(i);
        viewHolder.textView.setText(thisEntry.text);

        if (selectMode) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.checkBox.setChecked(thisEntry.checked);
            viewHolder.checkBox.setClickable(false);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thisEntry.checked = !thisEntry.checked;
                    notifyItemChanged(dataset.indexOf(thisEntry));
                }
            });
            viewHolder.itemView.setOnLongClickListener(null);
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.editEntry(dataset.indexOf(thisEntry));
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    gotoSelectModeFromItem(thisEntry);
                    return true;
                }
            });
        }

        int m;
        switch (thisEntry.marked) {
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
        if (!selectMode) {
            // goto select mode when clicked on the icon
            viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    gotoSelectModeFromItem(thisEntry);
                }
            });
        } else {
            viewHolder.imageView.setClickable(false);
        }
    }

    private void gotoSelectModeFromItem (Entry e) {
        e.checked = true;
        startSelectMode();
        activity.showContextMenu();
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

    void startSelectMode () {
        selectMode = true;
        notifyItemRangeChanged(0, dataset.size());
    }

    void stopSelectMode () {
        selectMode = false;

        uncheckAllEntries();
    }

    void uncheckAllEntries () {
        int size = dataset.size();
        for (int i = 0; i < size; i++) {
            dataset.get(i).checked = false;
        }

        notifyDataSetChanged();
    }

    void checkAllEntries () {
        int size = dataset.size();
        for (int i = 0; i < size; i++) {
            dataset.get(i).checked = true;
        }

        notifyDataSetChanged();
    }

    void markCheckEntriesAs (int mark) {
        int size = dataset.size();
        for (int i = 0; i < size; i++) {
            Entry e = dataset.get(i);
            if (e.checked) {
                e.marked = mark;
                notifyItemChanged(i);
            }
        }
    }

    void deleteCheckEntries () {
        int size = dataset.size();
        for (int i = size-1; i >= 0; i--) {
            if (dataset.get(i).checked) {
                dataset.remove(i);
                notifyItemRemoved(i);
            }
        }
    }
}
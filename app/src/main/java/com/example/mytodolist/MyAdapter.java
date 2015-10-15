package com.example.mytodolist;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.TextView;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.ViewHolder> {
    MyApplicationClass appdata;
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

    MyAdapter (MainActivity activity) {
        this.activity = activity;
        appdata = MyApplicationClass.getInstance();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todolist_item_layout, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        Entry e = appdata.getEntry(i);
        viewHolder.textView.setText(e.text);

        final Entry thisEntry = appdata.getEntry(i);

        if (selectMode) {
            viewHolder.checkBox.setVisibility(View.VISIBLE);
            viewHolder.checkBox.setChecked(e.checked);
            viewHolder.checkBox.setClickable(false);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    thisEntry.checked = !thisEntry.checked;
                    notifyItemChanged(appdata.getEntryPos(thisEntry));
                }
            });
        } else {
            viewHolder.checkBox.setVisibility(View.GONE);
            viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    activity.editEntry(appdata.getEntryPos(thisEntry));
                }
            });
            viewHolder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    thisEntry.checked = true;
                    activity.showContextMenu();
                    return true;
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
        return appdata.entryCount();
    }

    void add (Entry e) {
        appdata.appendEntry(e);
        notifyItemInserted(appdata.getEntryPos(e));
    }

    void update (String text, int pos) {
        Entry e = appdata.getEntry(pos);
        e.text = text;

        notifyItemChanged(pos);
    }

    boolean isSelectMode () {
        return selectMode;
    }

    void startSelectMode () {
        selectMode = true;
        notifyItemRangeChanged(0, appdata.entryCount());
    }

    void stopSelectMode () {
        uncheckAllEntries();

        selectMode = false;
    }

    void uncheckAllEntries () {
        int size = appdata.entryCount();
        for (int i = 0; i < size; i++) {
            appdata.getEntry(i).checked = false;
        }

        notifyItemRangeChanged(0, appdata.entryCount());
    }

    void checkAllEntries () {
        int size = appdata.entryCount();
        for (int i = 0; i < size; i++) {
            appdata.getEntry(i).checked = true;
        }

        notifyItemRangeChanged(0, appdata.entryCount());
    }

    void markCheckEntriesAs (int mark) {
        int size = appdata.entryCount();
        for (int i = 0; i < size; i++) {
            if (appdata.getEntry(i).checked) {
                appdata.getEntry(i).marked = mark;
                notifyItemChanged(i);
            }
        }
    }

    void deleteCheckEntries () {
        int size = appdata.entryCount();
        for (int i = size-1; i >= 0; i--) {
            if (appdata.getEntry(i).checked) {
                appdata.removeEntry(i);
                notifyItemRemoved(i);
            }
        }
    }
}
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
    ArrayList<Integer> visibleDataset;

    enum Mode { normal, select, filter };

    Mode currMode = Mode.normal;

    class ViewHolder extends RecyclerView.ViewHolder {
        ImageView imageView;
        TextView textView;
        CheckBox checkBox;

        ViewHolder (View v) {
            super(v);
            imageView = (ImageView) v.findViewById(R.id.imageView);
            textView = (TextView) v.findViewById(R.id.textView);
            checkBox = (CheckBox) v.findViewById(R.id.checkBox);
            v.setClickable(true);
        }
    }

    MyAdapter (MainActivity activity) {
        this.activity = activity;
        MyApplicationClass appdata = MyApplicationClass.getInstance();
        dataset = appdata.getDataset();
        visibleDataset = appdata.getVisibleDataset();
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.todolist_item_layout, viewGroup, false);
        ViewHolder vh = new ViewHolder(v);

        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, int i) {
        final Entry thisEntry;

        if (currMode == Mode.filter) {
            thisEntry = dataset.get(visibleDataset.get(i));
        } else {
            thisEntry = dataset.get(i);
        }

        viewHolder.textView.setText(thisEntry.text);

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

        viewHolder.checkBox.setClickable(false);
        viewHolder.imageView.setImageResource(m);

        switch (currMode) {
            case select:
                viewHolder.checkBox.setVisibility(View.VISIBLE);
                viewHolder.checkBox.setChecked(thisEntry.checked);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        thisEntry.checked = !thisEntry.checked;
                        notifyItemChanged(dataset.indexOf(thisEntry));
                    }
                });
                viewHolder.itemView.setOnLongClickListener(null);
                viewHolder.imageView.setClickable(false);
                break;
            case normal:
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

                // goto select mode when clicked on the icon
                viewHolder.imageView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        gotoSelectModeFromItem(thisEntry);
                    }
                });
                break;
            case filter:
                viewHolder.checkBox.setVisibility(View.GONE);
                viewHolder.imageView.setOnClickListener(null);
                viewHolder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        activity.stopLiveSearch();
                        activity.editEntry(dataset.indexOf(thisEntry));
                    }
                });
                viewHolder.itemView.setOnLongClickListener(null);

                break;
        }
    }

    private void gotoSelectModeFromItem (Entry e) {
        e.checked = true;
        startSelectMode();
        activity.showContextMenu();
    }

    @Override
    public int getItemCount() {
        int size;

        if (currMode == Mode.filter) {
            size = visibleDataset.size();
        } else {
            size = dataset.size();
        }

        return size;
    }

    void add (Entry e) {
        dataset.add(e);
        notifyItemInserted(dataset.indexOf(e));
    }

    void delete (int pos) {
        dataset.remove(pos);
        notifyItemRemoved(pos);
    }

    void onItemMove (int fromPos, int toPos) {
        if (fromPos < toPos) {
            for (int i = fromPos; i < toPos; i++) {
                Entry s = dataset.get(i);
                Entry s2 = dataset.get(i+1);
                dataset.set(i, s2);
                dataset.set(i+1, s);
            }
        } else {
            for (int i = fromPos; i > toPos; i--) {
                Entry s = dataset.get(i);
                Entry s2 = dataset.get(i-1);
                dataset.set(i, s2);
                dataset.set(i-1, s);
            }
        }

        notifyItemMoved(fromPos, toPos);
    }

    void update (String text, int pos) {
        Entry e = dataset.get(pos);
        e.text = text;

        notifyItemChanged(pos);
    }

    void startSelectMode () {
        currMode = Mode.select;
        notifyItemRangeChanged(0, dataset.size());
    }

    void stopSelectMode () {
        currMode = Mode.normal;

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

    public void filterTodolist (String str) {
        visibleDataset.clear();

        for (int i = 0, size = dataset.size(); i < size; i++) {
            if (dataset.get(i).text.contains(str)) {
                visibleDataset.add(i);
            }
        }

        notifyDataSetChanged();
    }

    public void setFilterMode (boolean flag) {
        if (flag) {
            currMode = Mode.filter;
        } else {
            currMode = Mode.normal;
        }
    }
}
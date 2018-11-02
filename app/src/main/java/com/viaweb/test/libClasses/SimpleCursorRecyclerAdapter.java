package com.viaweb.test.libClasses;

import android.content.Context;
import android.database.Cursor;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.viaweb.test.R;

import java.util.ArrayList;
import java.util.List;

public class SimpleCursorRecyclerAdapter extends CursorRecyclerAdapter<SimpleViewHolder> implements View.OnCreateContextMenuListener {

    private int mLayout;
    private int[] mFrom;
    private int[] mTo;
    private Cursor cursor;
    private String[] mOriginalFrom;
    private Context context;
    private int position;


    public SimpleCursorRecyclerAdapter(int layout, Cursor c, String[] from, int[] to) {
        super(c);
        mLayout = layout;
        mTo = to;
        cursor = c;
        mOriginalFrom = from;
        findColumns(c, from);

    }


    @Override
    public SimpleViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(mLayout, parent, false);
        context = parent.getContext();
        if(parent.getId()==R.id.recListOwnFoods){
            v.setOnCreateContextMenuListener(this);
        }




        return new SimpleViewHolder(v, mTo);
    }


    @Override
    public void onBindViewHolder(SimpleViewHolder holder, Cursor cursor) {
        final int count = mTo.length;
        final int[] from = mFrom;
        final SimpleViewHolder hol = holder;
            for (int i = 0; i < count; i++) {
                holder.views[i].setText(cursor.getString(from[i]));

        }


        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(hol.getAdapterPosition());
                return false;
            }
        });
    /*   holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(context ,hol.buttonViewOption);
                //inflating menu from xml resource
                popup.inflate(R.menu.options_menu);
                //adding click listener
                popup.setOnMenuItemClickListener(new PopupMenu.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(MenuItem item) {
                        switch (item.getItemId()) {
                            case R.id.item_delete:
                                //handle menu1 click
                                return true;
                            case R.id.item_update:
                                //handle menu2 click
                                return true;
                            default:
                                return false;
                        }
                    }
                });
                //displaying the popup
                popup.show();

            }
        });*/
    }

    /**
     * Create a map from an array of strings to an array of column-id integers in cursor c.
     * If c is null, the array will be discarded.
     *
     * @param c    the cursor to find the columns from
     * @param from the Strings naming the columns of interest
     */
    private void findColumns(Cursor c, String[] from) {
        if (c != null) {
            int i;
            int count = from.length;
            if (mFrom == null || mFrom.length != count) {
                mFrom = new int[count];
            }
            for (i = 0; i < count; i++) {
                mFrom[i] = c.getColumnIndexOrThrow(from[i]);
            }
        } else {
            mFrom = null;
        }
    }

    @Override
    public Cursor getCursor() {
        return cursor;
    }


    @Override
    public Cursor swapCursor(Cursor c) {
        findColumns(c, mOriginalFrom);
        return super.swapCursor(c);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add(0, v.getId(), 0, "Update");
        menu.add(0, v.getId(), 0, "Delete");

    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }
}

class SimpleViewHolder extends RecyclerView.ViewHolder {

    public TextView[] views;
    public TextView buttonViewOption;


    public SimpleViewHolder(View itemView, int[] to) {
        super(itemView);
        views = new TextView[to.length];
        buttonViewOption = itemView.findViewById(R.id.tvOptionsFoods);


        for (int i = 0; i < to.length; i++) {
            views[i] = (TextView) itemView.findViewById(to[i]);
        }

    }


}
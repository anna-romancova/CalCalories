package com.viaweb.test.Fragments;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.PopupMenu;
import android.widget.TextView;

import com.viaweb.test.Calculate;
import com.viaweb.test.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.itstap.calculator.FoodInHistory;

public class RecyclerAdapterSaveHistory extends RecyclerView.Adapter<RecyclerAdapterSaveHistory.MyViewHolder>
        implements View.OnCreateContextMenuListener{

    private ArrayList<FoodInHistory> foodList;
    String dateStr, proteineStr, fatsStr, carbohydratestr, caloriesStr;

    SimpleDateFormat simpleDateFormat;
    private Context mCtx;
    private int position;



    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, protein, fats, carbohydrate, calories, date, buttonViewOption ,weight;


        public MyViewHolder(View view) {
            super(view);
            date = view.findViewById(R.id.tvDateListHisroty);
            name = view.findViewById(R.id.tvNameListHisroty);
            protein = view.findViewById(R.id.tvHistoryProtein);
            fats = view.findViewById(R.id.tvHistoryFat);
            carbohydrate = view.findViewById(R.id.tvHistoryCargoh);
            calories = view.findViewById(R.id.tvHistoryCalories);
            buttonViewOption = view.findViewById(R.id.textViewOptions);
            weight= view.findViewById(R.id.tvHistoryWeight);

        }
    }


    public RecyclerAdapterSaveHistory(ArrayList<FoodInHistory> foodList, Context mCtx) {
        this.foodList = foodList;
        Log.e("foodList size", String.valueOf(foodList.size()));
        this.mCtx = mCtx;
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


    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // create a new view
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_of_list_history, parent, false);
        v.setOnCreateContextMenuListener(this);

        MyViewHolder vh = new MyViewHolder(v);
        return vh;
    }


    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        ArrayList<FoodInHistory> food = foodList;


        Date d = food.get(position).getTime();
        SimpleDateFormat formateDate = new SimpleDateFormat("MM-dd");
        dateStr = formateDate.format(d);

        holder.date.setText(dateStr);
        holder.name.setText(food.get(position).getFood().getName());
        holder.protein.setText(String.valueOf(food.get(position).getFood().getProtein()));
        holder.fats.setText(String.valueOf(food.get(position).getFood().getFats()));
        holder.carbohydrate.setText(String.valueOf(food.get(position).getFood().getCarbohydrate()));
        holder.calories.setText(String.valueOf(food.get(position).getFood().getCalories()));
        holder.weight.setText(String.valueOf(food.get(position).getWeightFood()));
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View v) {
                setPosition(holder.getAdapterPosition());
                return false;
            }
        });

     /*   holder.buttonViewOption.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //creating a popup menu
                PopupMenu popup = new PopupMenu(mCtx, holder.buttonViewOption);
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
        });
*/
    }



    @Override
    public int getItemCount() {
        return foodList.size();
    }
}
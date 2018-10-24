package com.viaweb.test;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

import edu.itstap.calculator.Food;

public class RecyclerAdapterSearchProduct extends RecyclerView.Adapter<RecyclerAdapterSearchProduct.MyViewHolder> {

    private List<Food> foodList;

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public TextView name, protein, fats, carbohydrate, calories;

        public MyViewHolder(View view) {
            super(view);
            name =  view.findViewById(R.id.tvNameFood);
            protein =  view.findViewById(R.id.tvProtein);
            fats =  view.findViewById(R.id.tvFat);
            carbohydrate =  view.findViewById(R.id.tvCargoh);
            calories = view.findViewById(R.id.tvCalories);

        }
    }


    public RecyclerAdapterSearchProduct(List<Food> foodList) {
        this.foodList = foodList;
    }

    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.result_search_product, parent, false);

        return new MyViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, int position) {
        Food food = foodList.get(position);
        holder.name.setText(food.getName());
        holder.protein.setText(String.valueOf(food.getProtein()));
        holder.fats.setText(String.valueOf(food.getFats()));
        holder.carbohydrate.setText(String.valueOf(food.getCarbohydrate()));
        holder.calories.setText(String.valueOf(food.getCalories()));
    }

    @Override
    public int getItemCount() {
        return foodList.size();
    }
}

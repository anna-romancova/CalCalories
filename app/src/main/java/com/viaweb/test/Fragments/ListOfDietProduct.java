package com.viaweb.test.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.viaweb.test.Calculate;
import com.viaweb.test.R;

import java.util.ArrayList;
import edu.itstap.calculator.FoodInHistory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;



/**
 * A simple {@link Fragment} subclass.
 */
public class ListOfDietProduct extends Fragment implements View.OnClickListener {
    private Calculate cal;
    private FloatingActionButton saveHistory;
    private RecyclerView recListDiet;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<FoodInHistory> foodArrayList;
    private LinearLayoutManager mLayoutManager;
    private TextView allResult;
    private Double proteineDob, fatsDob, carbohydrateDob, caloriesDob;


    public ListOfDietProduct() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cal.getFab().setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_of_diet_product, container, false);


        cal = (Calculate) getActivity();

        recListDiet = v.findViewById(R.id.recListDiet);
        foodArrayList = new ArrayList<>();
        foodArrayList = cal.getUser().getHistoryFoods().get(0);
        Log.e("foodArrayList", foodArrayList.toString());
        mAdapter = new RecyclerAdapterSaveHistory(foodArrayList, getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        recListDiet.setLayoutManager(mLayoutManager);
        recListDiet.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        recListDiet.setAdapter(mAdapter);
        ArrayList<FoodInHistory> arrayListFoodInHistory = cal.getUser().getHistoryFoods().get(0);
        allResult = v.findViewById(R.id.tvAllResult);
        proteineDob = 0.0;
        fatsDob = 0.0;
        carbohydrateDob = 0.0;
        caloriesDob = 0.0;
        for (int i = 0; i < arrayListFoodInHistory.size(); i++) {
            proteineDob += arrayListFoodInHistory.get(i).getFood().getProtein();
            fatsDob += arrayListFoodInHistory.get(i).getFood().getFats();
            carbohydrateDob += arrayListFoodInHistory.get(i).getFood().getCarbohydrate();
            caloriesDob += arrayListFoodInHistory.get(i).getFood().getCalories();

        }
        allResult.setText("Result:\n Proteines :" + String.valueOf((Math.floor(proteineDob))) + ";\n Fats:" +
                String.valueOf((Math.floor(fatsDob))) + ";\n Carbohydrates:"
                + String.valueOf((Math.floor(carbohydrateDob))) + "; \nCalories:" + String.valueOf((Math.floor(caloriesDob))));

        saveHistory = v.findViewById(R.id.saveHistory);
        saveHistory.setOnClickListener(this);
        if(cal.getUser().isUseSqLite()){
            saveHistory.setVisibility(View.VISIBLE);
        }

        return v;
    }

    @Override
    public void onStart() {
        super.onStart();
        cal = (Calculate) getActivity();

    }

    @Override
    public void onDetach() {
        super.onDetach();

    }
    /*    @Override
    public void onResume() {
        super.onResume();
        cal= (Calculate) getActivity();
        if(!cal.getUser().getHistoryFoods().isEmpty()){
            addRowTable();
        }
    }*/

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.saveHistory:


                break;
        }
    }

    private class SaveHistoryAsynckTask extends AsyncTask<Void, Void, Void> {


        @Override
        protected Void doInBackground(Void... voids) {


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            cal.invalidateOptionsMenu();
        }
    }
}

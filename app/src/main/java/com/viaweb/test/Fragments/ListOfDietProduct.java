package com.viaweb.test.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import  android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;

import com.viaweb.test.Calculate;
import com.viaweb.test.R;

import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import edu.itstap.calculator.Food;
import edu.itstap.calculator.FoodInHistory;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListOfDietProduct extends Fragment implements View.OnClickListener {
    private Calculate cal;
    private FloatingActionButton save;
    TableLayout tl;


    public ListOfDietProduct() {
        // Required empty public constructor
    }
    public void addRowTable(){
        Double proteine=0.0;
        Double fats=0.0;
        Double carb=0.0;
        Double calors=0.0;
        String t="";

        for( int i=0; i<cal.getUser().getHistoryFoods().size();i++)
        {


          ArrayList<FoodInHistory> fH=  cal.getUser().getHistoryFoods().get(i);
                for (FoodInHistory food : fH) {
                    TableRow row = (TableRow) LayoutInflater.from(getContext()).inflate(R.layout.attrib_row, null);
                    Date d= food.getTime();
                    SimpleDateFormat formateDate = new SimpleDateFormat("MM-dd");
                    t= formateDate.format(d);
                    ((TextView) row.findViewById(R.id.attrib_date)).setText("");
                    ((TextView) row.findViewById(R.id.attrib_name)).setText(food.getFood().getName());
                    ((TextView) row.findViewById(R.id.attrib_prot)).setText(String.valueOf(food.getFood().getProtein()));
                    proteine+=food.getFood().getProtein();
                    ((TextView) row.findViewById(R.id.attrib_fats)).setText(String.valueOf(food.getFood().getFats()));
                    fats+=food.getFood().getFats();
                    ((TextView) row.findViewById(R.id.attrib_carb)).setText(String.valueOf(food.getFood().getCarbohydrate()));
                    carb+=food.getFood().getCarbohydrate();
                    ((TextView) row.findViewById(R.id.attrib_calor)).setText(String.valueOf(food.getFood().getCalories()));
                    calors+=food.getFood().getCalories();
                    ((TextView) row.findViewById(R.id.attrib_weight)).setText(String.valueOf(food.getWeightFood()));
                    tl.addView(row);
                }

        }
        TableRow row = (TableRow) LayoutInflater.from(getContext()).inflate(R.layout.attrib_row, null);
        ((TextView) row.findViewById(R.id.attrib_date)).setText(t);
        ((TextView) row.findViewById(R.id.attrib_name)).setText("All");
        ((TextView) row.findViewById(R.id.attrib_prot)).setText(String.valueOf(proteine));
        ((TextView) row.findViewById(R.id.attrib_fats)).setText(String.valueOf(fats));
        ((TextView) row.findViewById(R.id.attrib_carb)).setText(String.valueOf(carb));
        ((TextView) row.findViewById(R.id.attrib_calor)).setText(String.valueOf(calors));
        ((TextView) row.findViewById(R.id.attrib_weight)).setText("");
        tl.addView(row);
        tl.requestLayout();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cal.getFab().setVisibility(View.INVISIBLE);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_list_of_diet_product, container, false);

        save=v.findViewById(R.id.saveHistory);
        save.setOnClickListener(this);

        tl=v.findViewById(R.id.tableListOfProduct);
        cal= (Calculate) getActivity();


        return v ;
    }

    @Override
    public void onStart() {
        super.onStart();
        cal= (Calculate) getActivity();
        if(!cal.getUser().getHistoryFoods().isEmpty()){
            addRowTable();
        }
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
        switch (view.getId()){
            case R.id.saveHistory:

                break;
        }
    }
    private  class SaveHistoryAsynckTask extends AsyncTask<Void,Void,Void> {


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

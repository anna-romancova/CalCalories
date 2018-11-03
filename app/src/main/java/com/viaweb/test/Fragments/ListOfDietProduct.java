package com.viaweb.test.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.AsyncTask;
import android.os.Bundle;

import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.TextView;
import com.viaweb.test.Calculate;
import com.viaweb.test.R;
import com.viaweb.test.libClasses.SQLiteConnector;
import com.viaweb.test.libClasses.SimpleCursorRecyclerAdapter;

import java.util.ArrayList;

import edu.itstap.calculator.Food;
import edu.itstap.calculator.FoodInHistory;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.widget.Toast;


/**
 * A simple {@link Fragment} subclass.
 */
public class ListOfDietProduct extends Fragment implements View.OnClickListener {
    private Calculate cal;
    private FloatingActionButton saveHistory;
    private SQLiteConnector connector;
    private RecyclerView recListDiet;
    private RecyclerView.Adapter mAdapter;
    private ArrayList<FoodInHistory> foodArrayList;
    private ArrayList<FoodInHistory> foodArrayListNew;
    private LinearLayoutManager mLayoutManager;
    private TextView allResult;
    private Double proteineDob, fatsDob, carbohydrateDob, caloriesDob;
    private TextView tvData;
    private EditText edWeight;
    private Double weightFoOneProduct;
    private FoodInHistory f;
    private ArrayList<FoodInHistory> arrayListFoodInHistory;


    public ListOfDietProduct() {
        // Required empty public constructor
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_list_of_diet_product, container, false);


        cal = (Calculate) getActivity();
        recListDiet = v.findViewById(R.id.recListDiet);
        foodArrayList = new ArrayList<>();
        foodArrayList =cal.getUser().getHistoryFoods().get(0);
        foodArrayListNew=new ArrayList<>();
        GetNewArrayListWithCalculateData  newList=new GetNewArrayListWithCalculateData();
        newList.execute();
        mAdapter = new RecyclerAdapterSaveHistory(foodArrayListNew, getContext());
        mLayoutManager = new LinearLayoutManager(getContext());
        recListDiet.setLayoutManager(mLayoutManager);
        recListDiet.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        recListDiet.setAdapter(mAdapter);
        arrayListFoodInHistory = cal.getUser().getHistoryFoods().get(0);
        allResult = v.findViewById(R.id.tvAllResult);
        updateTextResult();

        saveHistory = v.findViewById(R.id.saveHistory);
        saveHistory.setOnClickListener(this);
        if(cal.getUser().isUseSqLite()){
            saveHistory.setVisibility(View.VISIBLE);
            connector=new SQLiteConnector(getContext(),"OwnData",1);
        }

        return v;
    }
    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int position = -1;
        try {
            position = ((RecyclerAdapterSaveHistory)recListDiet.getAdapter()).getPosition();
        } catch (Exception e) {
            Log.d("onContextItemSelected", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        View view = mLayoutManager.findViewByPosition(position);
       final int index =position;
       Log.e("position",String.valueOf(index));
        if(item.getTitle()=="Update"){
            AlertDialog.Builder addProductToMyList = new AlertDialog.Builder(view.getContext());
            addProductToMyList.setTitle("Update");
            LayoutInflater inflater = getLayoutInflater();
            View view2 = inflater.inflate(R.layout.add_to_my_list, null, false);
            tvData = view2.findViewById(R.id.dataOfProduct);
            f=cal.getUser().getHistoryFoods().get(0).get(index);
            tvData.setText("Name:" + f.getFood().getName() + ", proteine:" + f.getFood().getProtein() +
                    ", fats:" + f.getFood().getFats() + ", carbohydrates:" +
                    f.getFood().getCarbohydrate() + ", calores: " + f.getFood().getCalories());
            edWeight = view2.findViewById(R.id.edWeight);
            edWeight.setText(String.valueOf(f.getWeightFood()));
            addProductToMyList.setView(view2);
            addProductToMyList.setCancelable(false);
            addProductToMyList.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    weightFoOneProduct = Double.valueOf(edWeight.getText().toString());
                    if (!weightFoOneProduct.isNaN()) {
                      Food food=  cal.calculateOneProduct(f.getFood(),weightFoOneProduct);
                      cal.getUser().getHistoryFoods().get(0).get(index).setWeightFood(weightFoOneProduct);
                        UpdateView();
                        updateTextResult();

                    }
                }
            });

            addProductToMyList.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog ad = addProductToMyList.create();
            ad.show();
        }
        else if(item.getTitle()=="Delete"){
            cal.getUser().getHistoryFoods().get(0).remove(index);
            UpdateView();
            updateTextResult();


        }
        else {return false;}
        return true;

    }
    private void UpdateView(){
        foodArrayListNew.clear();
        for (int i = 0; i < cal.getUser().getHistoryFoods().get(0).size(); i++) {
            Food newFood= cal.calculateOneProduct(cal.getUser().getHistoryFoods().get(0).get(i).getFood(),cal.getUser().getHistoryFoods().get(0).get(i).getWeightFood());
            FoodInHistory fd=  new FoodInHistory(newFood,cal.getUser().getHistoryFoods().get(0).get(i).getWeightFood());
            fd.setTime(cal.getUser().getHistoryFoods().get(0).get(i).getTime());
            foodArrayListNew.add(fd);

        }

        mAdapter = new RecyclerAdapterSaveHistory(foodArrayListNew, getContext());
        recListDiet.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();
    }
    private void updateTextResult(){

        proteineDob = 0.0;
        fatsDob = 0.0;
        carbohydrateDob = 0.0;
        caloriesDob = 0.0;
        for (int i = 0; i < arrayListFoodInHistory.size(); i++) {
            Food foodWithCalculate = cal.calculateOneProduct(arrayListFoodInHistory.get(i).getFood(), arrayListFoodInHistory.get(i).getWeightFood());

            proteineDob += foodWithCalculate.getProtein();
            fatsDob += foodWithCalculate.getFats();
            carbohydrateDob += foodWithCalculate.getCarbohydrate();
            caloriesDob += foodWithCalculate.getCalories();

        }
        allResult.setText("Result:\n Proteines :" + String.valueOf((Math.floor(proteineDob))) + ";\n Fats:" +
                String.valueOf((Math.floor(fatsDob))) + ";\n Carbohydrates:"
                + String.valueOf((Math.floor(carbohydrateDob))) + "; \nCalories:" + String.valueOf((Math.floor(caloriesDob))));
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
               connector.saveHistoryMenu(cal.getUser());
                Toast.makeText(getContext(),"Save your history",Toast.LENGTH_SHORT).show();
                cal.getUser().getHistoryFoods().clear();

                cal.invalidateOptionsMenu();
                FragmentTransaction ft = cal.getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameContainer, new SearchProduct());
                ft.commit();

                break;
        }
    }



    private class GetNewArrayListWithCalculateData extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
            for (int i = 0; i < foodArrayList.size(); i++) {
                Food newFood= cal.calculateOneProduct(foodArrayList.get(i).getFood(),foodArrayList.get(i).getWeightFood());
                FoodInHistory fd=  new FoodInHistory(newFood,foodArrayList.get(i).getWeightFood());
                fd.setTime(foodArrayList.get(i).getTime());
                foodArrayListNew.add(fd);

            }



            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            mAdapter = new RecyclerAdapterSaveHistory(foodArrayListNew, getContext());
            recListDiet.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            updateTextResult();

        }
    }
}

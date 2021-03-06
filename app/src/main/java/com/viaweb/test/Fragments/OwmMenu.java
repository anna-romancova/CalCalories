package com.viaweb.test.Fragments;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.ClipData;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import com.viaweb.test.Calculate;
import com.viaweb.test.R;
import com.viaweb.test.libClasses.SQLiteConnector;
import com.viaweb.test.libClasses.SimpleCursorRecyclerAdapter;
import android.support.design.widget.FloatingActionButton;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.itstap.calculator.Food;
import edu.itstap.calculator.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwmMenu extends Fragment implements View.OnClickListener {
    private RecyclerView recFoods;
    private SimpleCursorRecyclerAdapter adapter;
    private SQLiteConnector connector;
    private LinearLayoutManager mLayoutManager;
    private Calculate cal;
    private Cursor result;
    private FloatingActionButton addOwnFood;
    private EditText nameFoodAdd;
    private EditText protFoodAdd;
    private EditText fatsFoodAdd;
    private EditText carbFoodAdd;
    private EditText calorsFoodAdd;
    private String name;
    private String prot;
    private String fats;
    private String carb;
    private String calories;
    int  id=0;



    public OwmMenu() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
       View v=inflater.inflate(R.layout.fragment_owm_menu, container, false);
        recFoods=v.findViewById(R.id.recListOwnFoods);
        int[] view= new int[] {R.id.tvIdFood,R.id.tvNameOfList,R.id.tvProteinOfList,R.id.tvFatOfList,R.id.tvCargohOfList,R.id.tvCaloriesOfList};
        String[] cols=new String[]{"_id","name","proteins","fats","carbohydrates","calories"};
        adapter=new SimpleCursorRecyclerAdapter(R.layout.one_of_list,null,cols,view);
        mLayoutManager = new LinearLayoutManager(getContext());
        recFoods.setLayoutManager(mLayoutManager);
        recFoods.setItemAnimator(new DefaultItemAnimator());
        recFoods.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        recFoods.setAdapter(adapter);
        registerForContextMenu(recFoods);
        connector=new SQLiteConnector(getContext(),"OwnData",1);
        result =connector.selectAllFoods();
        if(result !=null){
            adapter.changeCursor(result);
        }
        addOwnFood=v.findViewById(R.id.addOwnFood);
        addOwnFood.setOnClickListener(this);

        return v;
    }


    @Override
    public boolean onContextItemSelected(MenuItem item) {

        int position = -1;
        try {
            position = ((SimpleCursorRecyclerAdapter)recFoods.getAdapter()).getPosition();
        } catch (Exception e) {
            Log.d("onContextItemSelected", e.getLocalizedMessage(), e);
            return super.onContextItemSelected(item);
        }
        View view = mLayoutManager.findViewByPosition(position);
        if(item.getTitle()=="Update"){
            name=((TextView) view.findViewById(R.id.tvNameOfList)).getText().toString();
            prot =((TextView) view.findViewById(R.id.tvProteinOfList)).getText().toString();
            fats =((TextView) view.findViewById(R.id.tvFatOfList)).getText().toString();
            carb =((TextView) view.findViewById(R.id.tvCargohOfList)).getText().toString();
            calories =((TextView) view.findViewById(R.id.tvCaloriesOfList)).getText().toString();
            AlertDialog.Builder upFood = new AlertDialog.Builder(getContext());
            upFood.setTitle("Update food!");
            LayoutInflater inflater = getLayoutInflater();
            View vUpFood = inflater.inflate(R.layout.add_food, null, false);

            nameFoodAdd =vUpFood.findViewById(R.id.edNameFood);
            protFoodAdd = vUpFood.findViewById(R.id.edProtein);
            fatsFoodAdd = vUpFood.findViewById(R.id.edFat);
            carbFoodAdd = vUpFood.findViewById(R.id.edCarbohydrate);
            calorsFoodAdd =vUpFood.findViewById(R.id.edCalories);
            nameFoodAdd.setText(name);
            protFoodAdd.setText(prot);
            fatsFoodAdd.setText(fats);
            carbFoodAdd.setText(carb);
            calorsFoodAdd.setText(calories);
            upFood.setView(vUpFood);
            upFood.setCancelable(false);
            upFood.setPositiveButton("Update", new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    Dialog f = (Dialog) dialog;
                    EditText nameFoodAdd = f.findViewById(R.id.edNameFood);
                    EditText protFoodAdd = f.findViewById(R.id.edProtein);
                    EditText fatsFoodAdd = f.findViewById(R.id.edFat);
                    EditText carbFoodAdd = f.findViewById(R.id.edCarbohydrate);
                    EditText calorsFoodAdd = f.findViewById(R.id.edCalories);

                    if (!nameFoodAdd.getText().toString().isEmpty() && !protFoodAdd.getText().toString().isEmpty()
                            && !fatsFoodAdd.getText().toString().isEmpty()
                            && !carbFoodAdd.getText().toString().isEmpty()
                            && !calorsFoodAdd.getText().toString().isEmpty()) {

                        connector.updateFood(new Food(nameFoodAdd.getText().toString(),
                                Double.parseDouble(calorsFoodAdd.getText().toString()),
                                Double.parseDouble(protFoodAdd.getText().toString()),
                                Double.parseDouble(fatsFoodAdd.getText().toString()),
                                Double.parseDouble(carbFoodAdd.getText().toString())));

                    }

                    result= connector.selectAllFoods();
                    if(result !=null){
                        adapter.changeCursor(result);
                    }
                }
            });

            upFood.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
            AlertDialog upF = upFood.create();
            upF.show();

        }
        else if(item.getTitle()=="Delete"){
            id =Integer.valueOf(((TextView)view.findViewById(R.id.tvIdFood)).getText().toString());
            connector.deleteFood(id);
            result= connector.selectAllFoods();
            if(result !=null){
                adapter.changeCursor(result);
            }

        }
        else {return false;}
        return true;

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.addOwnFood:
                AlertDialog.Builder addFood = new AlertDialog.Builder(getContext());
                addFood.setTitle("Add food!");
                LayoutInflater inflater = getLayoutInflater();
                View vAddFood = inflater.inflate(R.layout.add_food, null, false);
                addFood.setView(vAddFood);
                addFood.setCancelable(false);
                addFood.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dialog f = (Dialog) dialog;

                        nameFoodAdd = f.findViewById(R.id.edNameFood);
                        protFoodAdd = f.findViewById(R.id.edProtein);
                        fatsFoodAdd = f.findViewById(R.id.edFat);
                        carbFoodAdd = f.findViewById(R.id.edCarbohydrate);
                        calorsFoodAdd = f.findViewById(R.id.edCalories);
                        if (!nameFoodAdd.getText().toString().isEmpty() && !protFoodAdd.getText().toString().isEmpty()
                                && !fatsFoodAdd.getText().toString().isEmpty()
                                && !carbFoodAdd.getText().toString().isEmpty()
                                && !calorsFoodAdd.getText().toString().isEmpty()) {

                           connector.insertFood(new Food(nameFoodAdd.getText().toString(),
                                   Double.parseDouble(calorsFoodAdd.getText().toString()),
                                   Double.parseDouble(protFoodAdd.getText().toString()),
                                   Double.parseDouble(fatsFoodAdd.getText().toString()),
                                   Double.parseDouble(carbFoodAdd.getText().toString())));

                        }

                        result= connector.selectAllFoods();
                        if(result !=null){
                            adapter.changeCursor(result);
                        }
                    }
                });

                addFood.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog adF = addFood.create();
                adF.show();
                break;
        }

    }
}

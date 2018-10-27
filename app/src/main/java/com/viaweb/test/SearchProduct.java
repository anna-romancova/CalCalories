package com.viaweb.test;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;

import edu.itstap.calculator.Food;
import edu.itstap.calculator.User;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchProduct extends Fragment implements View.OnClickListener {
    private ImageButton search;
    private EditText searchNameFood;
    private Calculate cal;
    private RecyclerView recResultProduct;
    private RecyclerView.Adapter mAdapter;
    private Food foodOfList;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<Food> foodArrayList;
    private LinearLayout parentrSearch;
    private TextView tvData;
    private   EditText edWeight;
    private Double weightFoOneProduct;


    public void updateViews()
    {

    }

    public SearchProduct() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_search_product, null);
        cal=((Calculate) getActivity());

        search= v.findViewById(R.id.btnsearchProduct);
        search.setOnClickListener(this);
        mLayoutManager = new LinearLayoutManager(getContext());
        recResultProduct= v.findViewById(R.id.list_products);
        recResultProduct.setLayoutManager(mLayoutManager);
        foodArrayList = new ArrayList<>();
        foodArrayList =cal.testFood();
        mAdapter = new RecyclerAdapterSearchProduct(foodArrayList);
        recResultProduct.setItemAnimator(new DefaultItemAnimator());
        recResultProduct.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        recResultProduct.setAdapter(mAdapter);
        parentrSearch = v.findViewById(R.id.parentrSearch);
//        search.setOnClickListener(this);
        searchNameFood = v.findViewById(R.id.edSearchProduct);
        recResultProduct.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recResultProduct, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
                foodOfList = foodArrayList.get(position);
                if (cal.getUser() == null) {
                    cal.setUser(new User(""));
                }
//                Toast.makeText(getApplicationContext(), food.getName() + " is selected!", Toast.LENGTH_SHORT).show();
                AlertDialog.Builder addProductToMyList = new AlertDialog.Builder(view.getContext());
                addProductToMyList.setTitle("Add it's to my list");
                LayoutInflater inflater = getLayoutInflater();
                View view2 = inflater.inflate(R.layout.add_to_my_list, null, false);
                tvData = view2.findViewById(R.id.dataOfProduct);
                tvData.setText("Name:" + foodOfList.getName() + ", proteine:" + foodOfList.getProtein() + ", fats:" + foodOfList.getFats() + ", carbohydrates:" +
                        foodOfList.getCarbohydrate() + ", calores: " + foodOfList.getCalories());
                edWeight = view2.findViewById(R.id.edWeight);
                addProductToMyList.setView(view2);
                addProductToMyList.setCancelable(false);
                addProductToMyList.setPositiveButton("Add", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        weightFoOneProduct = Double.valueOf(edWeight.getText().toString());
                        if(!weightFoOneProduct.isNaN()) {
                            cal.getUser().getFoods().add(cal.calculateOneProduct(foodOfList, weightFoOneProduct));
                            Log.d(cal.TAG,cal.getUser().toString());
                            cal.invalidateOptionsMenu();

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

            @Override
            public void onLongClick(View view, int position) {


            }
        }));



        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onStart() {
        super.onStart();

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnsearchProduct:
                if(searchNameFood.getText()!=null){
                String st=searchNameFood.getText().toString();
                cal.searchProduct(st);
                    parentrSearch.requestFocus();
                    foodArrayList.clear();
                    foodArrayList = cal.getUser().getSearchFood();
                    mAdapter = new RecyclerAdapterSearchProduct(foodArrayList);
                    mAdapter.notifyDataSetChanged();
                    recResultProduct.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();
                }else {
                    Log.e(cal.TAG,"empty searchNameFood ");
                }
                break;
        }
    }
}

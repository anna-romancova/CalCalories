package com.viaweb.test.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.viaweb.test.Calculate;
import com.viaweb.test.R;
import com.viaweb.test.libClasses.UserClient;

import java.io.IOException;
import java.net.Socket;
import java.sql.Timestamp;
import java.util.ArrayList;

import edu.itstap.calculator.Food;
import edu.itstap.calculator.FoodInHistory;
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


    public boolean updateViews()
    {
        boolean b=false;
        try {
            parentrSearch.requestFocus();
            foodArrayList.clear();
            foodArrayList = cal.getUser().getSearchFood();
            mAdapter = new RecyclerAdapterSearchProduct(foodArrayList);
            mAdapter.notifyDataSetChanged();
            recResultProduct.setAdapter(mAdapter);
            mAdapter.notifyDataSetChanged();
            return b=true;
        }catch (Exception e){
            Log.d("SearchProduct","Something went wrong");
        }

    return b;

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
                            CalculateAsynckTask calculateAsynckTask=new CalculateAsynckTask();
                            calculateAsynckTask.execute();




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
        if( cal.getUser()!=null) {
            if (cal.getUser().isAutorization())
                cal.getFab().setVisibility(View.VISIBLE);
        }
    }
    public void searchProduct (String searchNameFoodString) {

        Log.d("nameFood", searchNameFoodString);

        if (!searchNameFoodString.isEmpty()) {

            if (cal.getUser() == null) {
                cal.setUser(new User(""));
            }
            if (cal.getUser().getFoods().isEmpty()){
                cal.getUser().getFoods().clear();

            }
            int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET);
            if (result == PackageManager.PERMISSION_GRANTED) {
            try {
                Socket soc=new Socket("10.0.2.2", 6447);
                cal.setSocket(soc);
                UserClient usClient = new UserClient(cal.getSocket());
                cal.setUser(usClient.searchFood(searchNameFoodString,cal.getUser()));
            } catch (IOException e) {
                e.printStackTrace();
            }

            }
        }

    }



    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnsearchProduct:
                if(searchNameFood.getText()!=null){
                String st=searchNameFood.getText().toString();
                    SerchAsynckTask asynkTask=new SerchAsynckTask();
                    asynkTask.execute(st);

                }else {
                    Log.e(cal.TAG,"empty searchNameFood ");
                }
                break;
        }
    }


    private  class SerchAsynckTask extends AsyncTask<String,Void,Void>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            search.setFocusableInTouchMode(false);
            search.setEnabled(false);
            search.setFocusable(false);
            Log.e("SerchAsynckTask","start");

        }

        @Override
        protected Void doInBackground(String... params) {
            searchProduct(params[0]);
            return null;
        }


        @Override
        protected void onPostExecute(Void aVoid) {
            if (updateViews()) {
                searchNameFood.setText("");
                search.setEnabled(true);
                search.setFocusableInTouchMode(true);
                search.setFocusable(true);
                Log.e("SerchAsynckTask", "stop");
            }

        }
    }

    private  class CalculateAsynckTask extends AsyncTask<Void,Void,Void>{
        @Override
        protected Void doInBackground(Void... voids) {
            Food foodWithCalculate =cal.calculateOneProduct(foodOfList, weightFoOneProduct);

            Timestamp time= new Timestamp(System.currentTimeMillis());

            FoodInHistory fH=new FoodInHistory(foodWithCalculate,weightFoOneProduct);
            fH.setTime(time);
            ArrayList<FoodInHistory> ar=new ArrayList<>();
            ar.add(fH);
            cal.getUser().getHistoryFoods().add(ar);
            Log.d(cal.TAG,cal.getUser().toString());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            cal.invalidateOptionsMenu();
        }
    }
}

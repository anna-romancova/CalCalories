package com.viaweb.test.Fragments;


import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
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
import android.widget.Toast;

import com.viaweb.test.Calculate;
import com.viaweb.test.R;
import com.viaweb.test.Services.ConnectionWithServer;
import com.viaweb.test.libClasses.ActionsUser;
import com.viaweb.test.libClasses.SQLiteConnector;
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
    private EditText edWeight;
    private Double weightFoOneProduct;
    private SQLiteConnector connector;
    private EditText nameFoodAdd;

    private EditText protFoodAdd;
    private EditText fatsFoodAdd;
    private EditText carbFoodAdd;
    private EditText calorsFoodAdd;
    private FloatingActionButton fab;
    ArrayList<ArrayList<FoodInHistory>> ar = new ArrayList<>();
    ArrayList<FoodInHistory> arItem = new ArrayList<>();


    public void updateViews() {

        parentrSearch.requestFocus();
        foodArrayList.clear();
        foodArrayList = cal.getUser().getSearchFood();
        mAdapter = new RecyclerAdapterSearchProduct(foodArrayList);
        mAdapter.notifyDataSetChanged();
        recResultProduct.setAdapter(mAdapter);
        mAdapter.notifyDataSetChanged();


    }

    public SearchProduct() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_search_product, null);
        cal = ((Calculate) getActivity());

        search = v.findViewById(R.id.btnsearchProduct);
        search.setOnClickListener(this);
        mLayoutManager = new LinearLayoutManager(getContext());
        recResultProduct = v.findViewById(R.id.list_products);
        recResultProduct.setLayoutManager(mLayoutManager);
        foodArrayList = new ArrayList<>();
        foodArrayList = cal.testFood();
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
                        if (!weightFoOneProduct.isNaN()) {
                            CalculateAsynckTask calculateAsynckTask = new CalculateAsynckTask();
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

        fab = v.findViewById(R.id.fab);
        if (cal.getUser() != null) {
            if (cal.getUser().isAutorization() && cal.getUser().isUseSqLite())
                fab.setVisibility(View.VISIBLE);
            connector = new SQLiteConnector(getContext(), "OwnData", 1);
        } else {
            fab.setVisibility(View.INVISIBLE);
        }


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(view, "Add new food", Snackbar.LENGTH_LONG)
                        .setAction("Add", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
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

                                    /*        AddFoodsynkTask addFood = new AddFoodsynkTask();
                                            addFood.execute();*/
                                            Food food = new Food(nameFoodAdd.getText().toString(), Double.valueOf(calorsFoodAdd.getText().toString()),
                                                    Double.valueOf(protFoodAdd.getText().toString()),
                                                    Double.valueOf(fatsFoodAdd.getText().toString()),
                                                    Double.valueOf(carbFoodAdd.getText().toString()));
                                            connector = new SQLiteConnector(getContext(), "OwnData", 1);
//                                            Log.e("connector", connector.toString());
                                            connector.insertFood(food);

                                            Toast.makeText(getContext(), "Your product have been add!", Toast.LENGTH_SHORT).show();

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

                            }
                        });

                snackbar.setActionTextColor(getResources().getColor(R.color.white));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                snackbar.show();
            }
        });


        return v;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

    }

    @Override
    public void onResume() {
        super.onResume();
        if (cal.getUser() != null) {
            if (cal.getUser().isUseSqLite()) {
                fab.setVisibility(View.VISIBLE);
            } else {
                fab.setVisibility(View.INVISIBLE);

            }
        }
    }
    /*   @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

    }*/

    @Override
    public void onStart() {
        super.onStart();

    }

    public void searchProduct(String searchNameFoodString) {

        Log.d("nameFood", searchNameFoodString);

        if (!searchNameFoodString.isEmpty()) {

            if (cal.getUser() == null) {
                cal.setUser(new User(""));
            }
            if (cal.getUser().getFoods().isEmpty()) {
                cal.getUser().getFoods().clear();

            }
            int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET);
            if (result == PackageManager.PERMISSION_GRANTED) {
                try {
//                    Socket soc = new Socket("10.0.2.2", 6447);
//                    Socket soc = new Socket("192.168.1.235", 6489);
                    Socket soc = new Socket("192.168.1.235", 6489);
                    cal.setSocket(soc);
                    UserClient usClient = new UserClient(cal.getSocket());
                    User us=usClient.searchFood(searchNameFoodString, cal.getUser());
                    if(cal.getUser().isUseSqLite()){
                        connector= new SQLiteConnector(getContext(), "OwnData", 1);
                      ArrayList<Food>foodsFromSqLite=  connector.selectFoods(searchNameFoodString);
                        us.getSearchFood().addAll(foodsFromSqLite);
                    }
                    cal.setUser(us);
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }
        }

    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsearchProduct:
                if (searchNameFood.getText() != null) {
                    String st = searchNameFood.getText().toString();
                    SerchAsynckTask asynkTask = new SerchAsynckTask();
                    asynkTask.execute(st);

                } else {
                    Log.e(cal.TAG, "empty searchNameFood ");
                }
                break;
        }
    }


    private class SerchAsynckTask extends AsyncTask<String, Void, Void> {


        @SuppressLint("ResourceAsColor")
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            search.setEnabled(false);
            search.setClickable(false);
            search.setFocusable(false);
//            Log.e("SerchAsynckTask", "start");

        }

        @Override
        protected Void doInBackground(String... params) {
                searchProduct(params[0]);

            return null;
        }


        @SuppressLint("ResourceAsColor")
        @Override
        protected void onPostExecute(Void aVoid) {
            updateViews();
            searchNameFood.setText("");
            search.setEnabled(true);
            search.setClickable(true);
            search.setFocusable(true);
//            Log.e("SerchAsynckTask", "stop");


        }
    }

    class AddFoodsynkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {
          /*  ArrayList<Food> newAdd = new ArrayList<>();
            Food food = new Food(nameFoodAdd.getText().toString(), Double.valueOf(calorsFoodAdd.getText().toString()),
                    Double.valueOf(protFoodAdd.getText().toString()),
                    Double.valueOf(fatsFoodAdd.getText().toString()),
                    Double.valueOf(carbFoodAdd.getText().toString()));
            food.setAdd(true);

            cal.getUser().getAddFood().add(food);
            PendingIntent pi = cal.createPendingResult(3, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            Intent intent = new Intent(getContext(), ConnectionWithServer.class)
                    .putExtra("user", cal.getUser())
                    .putExtra("pi", pi)
                    .setAction(ActionsUser.ADD_FOOD)
                    .setPackage(cal.getPackageName());
            int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET);
            if (result == PackageManager.PERMISSION_GRANTED) {
                getContext().startService(intent);
                Log.d("startService", "startService Add food");
            }*/
            Food food = new Food(nameFoodAdd.getText().toString(), Double.valueOf(calorsFoodAdd.getText().toString()),
                    Double.valueOf(protFoodAdd.getText().toString()),
                    Double.valueOf(fatsFoodAdd.getText().toString()),
                    Double.valueOf(carbFoodAdd.getText().toString()));
            Log.e("connector", connector.toString());
            connector.insertFood(food);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getContext(), "Your product have been add!", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();


    }

    private class CalculateAsynckTask extends AsyncTask<Void, Void, ArrayList<FoodInHistory>> {
        @Override
        protected ArrayList<FoodInHistory> doInBackground(Void... voids) {


            Timestamp time = new Timestamp(System.currentTimeMillis());

            FoodInHistory fH = new FoodInHistory(foodOfList, weightFoOneProduct);
            fH.setTime(time);
            arItem.add(fH);
            ar.add(arItem);
            cal.getUser().setHistoryFoods(ar);
            Log.d(cal.TAG, cal.getUser().toString());
            return arItem;
        }

        @Override
        protected void onPostExecute(ArrayList<FoodInHistory> arItem) {
            cal.invalidateOptionsMenu();

        }

    }

}

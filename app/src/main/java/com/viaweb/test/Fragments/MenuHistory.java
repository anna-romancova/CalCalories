package com.viaweb.test.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viaweb.test.Calculate;
import com.viaweb.test.R;

import java.util.ArrayList;

import edu.itstap.calculator.Food;
import edu.itstap.calculator.FoodInHistory;


/**
 * A simple {@link Fragment} subclass.
 */
public class MenuHistory extends Fragment {
    private Calculate cal;
    private RecyclerView listHistoryMenu;
    private RecyclerView.Adapter mAdapter;
    private LinearLayoutManager mLayoutManager;
    private ArrayList<FoodInHistory> arrFoodInHist;
    private ArrayList<FoodInHistory> foodArrayListNew;


    public MenuHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_menu_history, container, false);
        listHistoryMenu=v.findViewById(R.id.recHistorySaved);
        mLayoutManager = new LinearLayoutManager(getContext());
        listHistoryMenu.setLayoutManager(mLayoutManager);
        cal=((Calculate) getActivity());
        arrFoodInHist=new ArrayList<>();
        foodArrayListNew=new ArrayList<>();
        arrFoodInHist=OwnHistory.currentListOfNextFragment;
//        Log.e("arrFoodInHist",arrFoodInHist.toString());
        for (int i = 0; i < arrFoodInHist.size(); i++) {
            Food newFood= cal.calculateOneProduct(arrFoodInHist.get(i).getFood(),arrFoodInHist.get(i).getWeightFood());
            FoodInHistory fd=  new FoodInHistory(newFood,arrFoodInHist.get(i).getWeightFood());
            fd.setTime(arrFoodInHist.get(i).getTime());
            foodArrayListNew.add(fd);

        }
        mAdapter = new RecyclerAdapterSaveHistory(arrFoodInHist,getContext());
        listHistoryMenu.setItemAnimator(new DefaultItemAnimator());
        listHistoryMenu.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        listHistoryMenu.setAdapter(mAdapter);
        return  v;
    }

}

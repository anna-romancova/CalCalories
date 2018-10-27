package com.viaweb.test;


import android.annotation.SuppressLint;
import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;


/**
 * A simple {@link Fragment} subclass.
 */
public class SearchProduct extends Fragment implements View.OnClickListener {
    private ImageButton search;
    private EditText searchNameFood;
    private Calculate cal;
    private RecyclerView recResultProduct;
    private RecyclerView.Adapter mAdapter;
    LinearLayoutManager mLayoutManager;


    public SearchProduct() {
        // Required empty public constructor
    }
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_search_product, null);

        search= v.findViewById(R.id.btnsearchProduct);
        search.setOnClickListener(this);
        mLayoutManager = new LinearLayoutManager(getContext());
        recResultProduct= v.findViewById(R.id.list_products);



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
                cal=(Calculate) getActivity();
                cal.searchProduct(view);
                break;
        }
    }
}

package com.viaweb.test.Fragments;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.viaweb.test.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalculateOfReqired extends Fragment {


    public CalculateOfReqired() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_calculate_of_reqired, container, false);
        return v;
    }

}

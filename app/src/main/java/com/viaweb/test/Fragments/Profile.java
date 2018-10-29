package com.viaweb.test.Fragments;


import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.viaweb.test.Calculate;
import com.viaweb.test.R;


/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment {
    private Calculate cal;
    private EditText login;
    private EditText goal;
    private EditText password;
    private EditText email;



    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile, container, false);
        login=v.findViewById(R.id.edNaneUser);
        goal=v.findViewById(R.id.edGoalUser);
        password=v.findViewById(R.id.edPasswordUser);
        email=v.findViewById(R.id.edEmailUser);

        return  v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        cal=(Calculate) getActivity();
        if(cal.getUser().isAutorization()){
            login.setText(cal.getUser().getUserName());
            password.setText(cal.getUser().getPassword());
            email.setText(cal.getUser().getEmail());
            if(!String.valueOf(cal.getUser().getGoalOfCalories()).isEmpty()) {
                goal.setText(String.valueOf(cal.getUser().getGoalOfCalories()));
            }
            cal.getFab().setVisibility(View.VISIBLE);
        }
    }

}

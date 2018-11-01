package com.viaweb.test.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Switch;
import android.widget.Toast;

import com.viaweb.test.Calculate;
import com.viaweb.test.R;



/**
 * A simple {@link Fragment} subclass.
 */
public class Profile extends Fragment implements View.OnClickListener {
    private Calculate cal;
    private EditText login;
    private EditText goal;
    private EditText password;
    private EditText email;
    private Button updateDate;
    private Switch useSqLite;



    public Profile() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile, container, false);
        login=v.findViewById(R.id.edNaneUser);
        goal=v.findViewById(R.id.edGoalUser);
        cal=(Calculate)getActivity();
        password=v.findViewById(R.id.edPasswordUser);
        email=v.findViewById(R.id.edEmailUser);
        updateDate=v.findViewById(R.id.btnUpdateDateProfile);
        updateDate.setOnClickListener(this);
        useSqLite=v.findViewById(R.id.swAddFoods);
        useSqLite.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                   cal.getUser().setUseSqLite(isChecked);

            }
        });

        return  v;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        if(cal.getUser().isAutorization()){
            login.setText(cal.getUser().getUserName());
            password.setText(cal.getUser().getPassword());
            email.setText(cal.getUser().getEmail());
            useSqLite.setChecked(cal.getUser().isUseSqLite());
            if(!String.valueOf(cal.getUser().getGoalOfCalories()).isEmpty()) {
                goal.setText(String.valueOf(cal.getUser().getGoalOfCalories()));
            }
            cal.getFab().setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.btnUpdateDateProfile:
                UpdateDateAsynckTask up=new UpdateDateAsynckTask();
                up.execute();

                break;
        }

    }
    class UpdateDateAsynckTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            cal.getUser().setEmail(email.getText().toString());
            cal.getUser().setUserName(login.getText().toString());
            cal.getUser().setPassword(password.getText().toString());
            cal.getUser().setGoalOfCalories(Double.valueOf(goal.getText().toString()));
            cal.getUser().setProfileUpdate(true);
            cal.updateProfile(cal.getUser());
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getContext(),"Date Update",Toast.LENGTH_SHORT).show();


        }
    }
}

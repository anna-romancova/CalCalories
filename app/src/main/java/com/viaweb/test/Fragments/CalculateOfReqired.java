package com.viaweb.test.Fragments;


import android.Manifest;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.viaweb.test.Calculate;
import com.viaweb.test.R;
import com.viaweb.test.libClasses.UserClient;

import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalculateOfReqired extends Fragment implements View.OnClickListener,RadioGroup.OnCheckedChangeListener {
    private EditText weight;
    private Calculate cal;
    private EditText age;
    private EditText height;
    private Spinner styleOflife;
    private RadioGroup rGroupGender;
    private RadioButton man;
    private RadioButton wooman;
    private Button result;
    private FloatingActionButton save;
    int BMPformule;
    private TextView tvResult;
    private double resuliOfCalculationRequarium;



    public CalculateOfReqired() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_calculate_of_reqired, container, false);
        tvResult=v.findViewById(R.id.tvResultGoal);
        weight=v.findViewById(R.id.edGoalWeight);
        age=v.findViewById(R.id.edGoalAge);
        height=v.findViewById(R.id.edGoalHeight);
        styleOflife=v.findViewById(R.id.spTypeOfLife);
        man=v.findViewById(R.id.rbMan);
        wooman=v.findViewById(R.id.rbWooman);
        ArrayList<String> typOfLife = new ArrayList<>();
        //"The minimum level", "Low", "Average", "High", "Very high"
        typOfLife.add("The minimum level");
        typOfLife.add("Low");
        typOfLife.add("Average");
        typOfLife.add("High");
        typOfLife.add("Very high");
        ArrayAdapter<String> adapter =
                new ArrayAdapter<>(getContext(),  android.R.layout.simple_spinner_dropdown_item, typOfLife);
        adapter.setDropDownViewResource( android.R.layout.simple_spinner_dropdown_item);
        styleOflife.setAdapter(adapter);
        result=v.findViewById(R.id.btnGetResultGoal);
        result.setOnClickListener(this);
        tvResult=v.findViewById(R.id.tvResultGoal);
        rGroupGender=v.findViewById(R.id.rGroupGender);
        rGroupGender.setOnCheckedChangeListener(this);
        save=v.findViewById(R.id.saveInMyProfile);
        save.setOnClickListener(this);

        cal=(Calculate) getActivity();

        return v;
    }

    @Override
    public void onClick(View view) {
        switch ( view.getId()){
            case R.id.btnGetResultGoal:
                CalcolateReqwireAsynkTask cal=new CalcolateReqwireAsynkTask();
                cal.execute();
                break;
            case R.id.saveInMyProfile:
                SaveInDB saveInDB=new SaveInDB();
                saveInDB.execute();


                break;
        }

    }

    @Override
    public void onCheckedChanged(RadioGroup radioGroup, int checkedId) {
        switch (checkedId) {

            case R.id.rbMan:
                BMPformule=0;
                break;
            case R.id.rbWooman:
                BMPformule=1;
                break;
        }



    }

    class CalcolateReqwireAsynkTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            double BMR=0.0;
            if( BMPformule==0) {
                BMR = 88.36 + (13.4 * Double.parseDouble(weight.getText().toString())) + (4.8 * Double.parseDouble(height.getText().toString())) - (5.7 * Double.parseDouble(age.getText().toString()));
                Log.d("BMR man",String.valueOf(BMR));
            }else if(BMPformule==1){
                BMR = 447.6 + (9.2 * Double.parseDouble(weight.getText().toString())) + (3.1 * Double.parseDouble(height.getText().toString())) - (4.3 * Double.parseDouble(age.getText().toString()));
                Log.d("BMR wooman",String.valueOf(BMR));
            }
            Math.floor(BMR);

            if((styleOflife.getSelectedItem()).toString().equals("The minimum level")) {
                resuliOfCalculationRequarium =	BMR*1.2;
            }else if((styleOflife.getSelectedItem()).toString().equals("Low")) {
                resuliOfCalculationRequarium =	BMR*1.375;
            }else if((styleOflife.getSelectedItem()).toString().equals("Average")) {
                resuliOfCalculationRequarium =	BMR*1.55;
            }else if((styleOflife.getSelectedItem()).toString().equals("High")) {
                resuliOfCalculationRequarium =	BMR*1.725;
            }else if((styleOflife.getSelectedItem()).toString().equals("Very high")) {
                resuliOfCalculationRequarium =	BMR*1.9;
            }
            Math.floor(resuliOfCalculationRequarium);


            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if(tvResult.getText()!=null){
                tvResult.setText(" ");
            }
            tvResult.setText("You need "+String.valueOf(Math.floor(resuliOfCalculationRequarium))+" kcal per day.");
            save.setVisibility(View.VISIBLE);
        }
    }
    class  SaveInDB extends  AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            int result = ContextCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET);
            if (result == PackageManager.PERMISSION_GRANTED) {
                try {
                    Socket soc=new Socket("10.0.2.2", 6447);
                    UserClient usClient = new UserClient(soc);
                    cal.getUser().setInsertGoal(true);
                    cal.getUser().setGoalOfCalories((Math.floor(resuliOfCalculationRequarium)));
                    cal.setUser(usClient.saveGoalInProfile(cal.getUser()));
                } catch (IOException e) {
                    e.printStackTrace();
                }

            }

            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            Toast.makeText(getContext(),"Information has been saved",Toast.LENGTH_SHORT).show();
        }
    }
}

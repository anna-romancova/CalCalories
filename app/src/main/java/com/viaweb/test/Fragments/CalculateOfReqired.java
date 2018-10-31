package com.viaweb.test.Fragments;


import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
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

import com.viaweb.test.R;

import java.util.ArrayList;


/**
 * A simple {@link Fragment} subclass.
 */
public class CalculateOfReqired extends Fragment implements View.OnClickListener {
    private EditText weight;
    private EditText age;
    private EditText height;
    private Spinner styleOflife;
    private RadioButton man;
    private RadioButton wooman;
    private Button result;
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



        return v;
    }

    @Override
    public void onClick(View view) {
        switch ( view.getId()){
            case R.id.btnGetResultGoal:
                CalcolateReqwireAsynkTask cal=new CalcolateReqwireAsynkTask();
                cal.execute();



                break;
        }

    }
    class CalcolateReqwireAsynkTask extends AsyncTask<Void,Void,Void>{

        @Override
        protected Void doInBackground(Void... voids) {
            double BMR=0.0;
            if(man.isSelected()) {
                BMR = 88.36 + (13.4 * Double.parseDouble(weight.getText().toString())) + (4.8 * Double.valueOf(height.getText().toString())) - (5.7 * Double.valueOf(age.getText().toString()));
            }else if(wooman.isSelected()){
                BMR = 447.6 + (9.2 * Double.parseDouble(weight.getText().toString())) + (3.1 * Double.valueOf(height.getText().toString())) - (4.3 * Double.valueOf(age.getText().toString()));
            }
            Math.floor(BMR);

            if((styleOflife.getSelectedItem()).equals("The minimum level")) {
                resuliOfCalculationRequarium =	BMR*1.2;
            }else if((styleOflife.getSelectedItem()).equals("Low")) {
                resuliOfCalculationRequarium =	BMR*1.375;
            }else if((styleOflife.getSelectedItem()).equals("Average")) {
                resuliOfCalculationRequarium =	BMR*1.55;
            }else if((styleOflife.getSelectedItem()).equals("High")) {
                resuliOfCalculationRequarium =	BMR*1.725;
            }else if((styleOflife.getSelectedItem()).equals("Very high")) {
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
        }
    }
}

package com.viaweb.test.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.viaweb.test.Calculate;
import com.viaweb.test.R;
import com.viaweb.test.libClasses.SQLiteConnector;
import com.viaweb.test.libClasses.SimpleCursorRecyclerAdapter;

import edu.itstap.calculator.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnHistory extends Fragment  implements View.OnClickListener {
    private RecyclerView recListOfHistory;
    private SimpleCursorRecyclerAdapter adapter;
    private SQLiteConnector connector;
    private LinearLayoutManager mLayoutManager;
    private Calculate cal;
    private Cursor result;
    private FloatingActionButton clearOfHistory;


    public OwnHistory() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v= inflater.inflate(R.layout.fragment_own_history, container, false);
        recListOfHistory=v.findViewById(R.id.recListOwnHistory);
        int[] view= new int[] {R.id.tvIdHistoreUser,R.id.tvDateHistoryUser};
        String[] cols=new String[]{"_id","Date"};
        adapter=new SimpleCursorRecyclerAdapter(R.layout.one_item_only_date,null,cols,view);
        mLayoutManager = new LinearLayoutManager(getContext());
        recListOfHistory.setLayoutManager(mLayoutManager);
        recListOfHistory.setItemAnimator(new DefaultItemAnimator());
        recListOfHistory.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        recListOfHistory.setAdapter(adapter);
        connector=new SQLiteConnector(getContext(),"OwnData",1);
        result =connector.selectAllHistory();
        if(result !=null){
            adapter.changeCursor(result);
        }
        unregisterForContextMenu(recListOfHistory);
        clearOfHistory=v.findViewById(R.id.clearHistory);
        clearOfHistory.setOnClickListener(this);
        recListOfHistory.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recListOfHistory, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
              Toast.makeText(getContext(),"short click",Toast.LENGTH_LONG).show();
            }
            @Override
            public void onLongClick(View view, int position) {
                Toast.makeText(getContext(),"Long click",Toast.LENGTH_LONG).show();
            }
        }));
        return v ;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.clearHistory:
                connector.cleanHistory();
                result =connector.selectAllHistory();
                adapter.changeCursor(result);
                break;
        }

    }
}

package com.viaweb.test.Fragments;


import android.app.AlertDialog;
import android.content.DialogInterface;
import android.database.Cursor;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.viaweb.test.Calculate;
import com.viaweb.test.R;
import com.viaweb.test.libClasses.SQLiteConnector;
import com.viaweb.test.libClasses.SimpleCursorRecyclerAdapter;

import java.util.ArrayList;

import edu.itstap.calculator.FoodInHistory;
import edu.itstap.calculator.User;

/**
 * A simple {@link Fragment} subclass.
 */
public class OwnHistory extends Fragment implements View.OnClickListener {
    public static ArrayList<FoodInHistory> currentListOfNextFragment = new ArrayList<>();
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
    public View onCreateView(final LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View v = inflater.inflate(R.layout.fragment_own_history, container, false);
        recListOfHistory = v.findViewById(R.id.recListOwnHistory);
        int[] view = new int[]{R.id.tvIdHistoreUser, R.id.tvDateHistoryUser};
        final String[] cols = new String[]{"_id", "Date"};
        adapter = new SimpleCursorRecyclerAdapter(R.layout.one_item_only_date, null, cols, view);
        mLayoutManager = new LinearLayoutManager(getContext());
        recListOfHistory.setLayoutManager(mLayoutManager);
        recListOfHistory.setItemAnimator(new DefaultItemAnimator());
        recListOfHistory.addItemDecoration(new MyDividerItemDecoration(getContext(), LinearLayoutManager.VERTICAL, 16));
        recListOfHistory.setAdapter(adapter);
        connector = new SQLiteConnector(getContext(), "OwnData", 1);
        result = connector.selectAllHistory();
        if (result != null) {
            adapter.changeCursor(result);
        }
        unregisterForContextMenu(recListOfHistory);
        cal = (Calculate) getActivity();
        clearOfHistory = v.findViewById(R.id.clearHistory);
        clearOfHistory.setOnClickListener(this);
        recListOfHistory.addOnItemTouchListener(new RecyclerTouchListener(getContext(), recListOfHistory, new RecyclerTouchListener.ClickListener() {
            @Override
            public void onClick(View view, int position) {
//                Log.e("short click", "" + (position + 1));
                TextView idTv = ((TextView) view.findViewById(R.id.tvIdHistoreUser));
                int id = Integer.parseInt(idTv.getText().toString());
                currentListOfNextFragment = connector.selectOneHistory(id).get(0);
                if (!currentListOfNextFragment.isEmpty()) {
                    FragmentTransaction ft = cal.getSupportFragmentManager().beginTransaction();
                    ft.replace(R.id.frameContainer, new MenuHistory());
                    ft.commit();
                }
            }

            @Override
            public void onLongClick(View view, int position) {
//                Toast.makeText(getContext(),"Long click"+position,Toast.LENGTH_LONG).show();
                connector.cleanOneHistory(position);
                result = connector.selectAllHistory();
                if (result != null) {
                    adapter.changeCursor(result);
                }
            }
        }));
        return v;
    }


    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.clearHistory:
                connector.cleanHistory();
                result = connector.selectAllHistory();
                adapter.changeCursor(result);
                break;
        }

    }
}

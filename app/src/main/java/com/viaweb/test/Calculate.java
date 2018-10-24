package com.viaweb.test;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.FragmentTransaction;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.constraint.Group;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.Snackbar;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.app.Fragment;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;

import android.widget.Toast;

import java.util.ArrayList;

import edu.itstap.calculator.Food;
import edu.itstap.calculator.User;

public class Calculate extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {
    private static final String TAG_1 ="Add food" ;
    private AddFood addFood;
    private FragmentTransaction ft;
    private Fragment searchProduct;
    public  User user;
    private RecyclerView recResultProduct;
    private RecyclerView.Adapter mAdapter;
    private FloatingActionButton fab;
    private  DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private RecyclerView.LayoutManager mLayoutManager;
    private Group aut;
    ArrayList<Food> foodArrayList;


    private class SQLiteConnector extends SQLiteOpenHelper {
        private Context context;

        public SQLiteConnector(Context context, String name,
                               int version) {
            super(context, name, null, version);
            this.context=context;
            Toast.makeText(context, "DB has been connected", Toast.LENGTH_SHORT).show();
            // TODO Auto-generated constructor stub
        }

        @Override
        public void onCreate(SQLiteDatabase db) {
            db.execSQL("Create table hostoryFoods(_id integer primary key autoincrement,name varchar(50), email varchar(50));");
            Toast.makeText(context, "DB has been created", Toast.LENGTH_SHORT).show();



        }

        @Override
        public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

            if(oldVersion==1&&newVersion==2){

            }else if(oldVersion==2&&newVersion==3){

            }else if(oldVersion==1&&newVersion==3){

            }

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        ft=getFragmentManager().beginTransaction();
        searchProduct=new SearchProduct();
        ft.replace(R.id.frameContainer,searchProduct);
        ft.commit();

        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        fab = findViewById(R.id.fab);
        fab.setVisibility(View.INVISIBLE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar snackbar = Snackbar.make(view, "Add new food", Snackbar.LENGTH_LONG)
                        .setAction("Add", new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                addFood=new AddFood();
                                addFood.show(getSupportFragmentManager(),TAG_1);
                            }
                        });
                snackbar.setActionTextColor(getResources().getColor(R.color.white));
                View snackBarView = snackbar.getView();
                snackBarView.setBackgroundColor(getResources().getColor(R.color.colorPrimary));
                snackbar.show();
            }
        });
        drawer = findViewById(R.id.drawer_layout);
         toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView =findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
         aut=findViewById(R.id.grAutorisationGroup);
    }

    @Override
    protected void onStart() {
        super.onStart();
        recResultProduct= searchProduct.getView().findViewById(R.id.list_products);
//        recResultProduct.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        recResultProduct.setLayoutManager(mLayoutManager);
        foodArrayList = new ArrayList<>();
        mAdapter = new RecyclerAdapterSearchProduct(foodArrayList);
        recResultProduct.setAdapter(mAdapter);
        navigationView.getMenu().setGroupVisible(R.id.grAutorisationGroup,false);
//        aut.setVisibility(View.INVISIBLE);

    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10) {
            switch (requestCode) {
                case 1:
                    Log.e("autorization","autorization");
                    fab.setVisibility(View.VISIBLE);
                    navigationView.getMenu().setGroupVisible(R.id.grAutorisationGroup,true);


                break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();


            switch (id){
                case R.id.sing_in:
//                    Toast.makeText(this,"sing in",Toast.LENGTH_SHORT).show();
                    AlertDialog.Builder singIn = new AlertDialog.Builder(this);
                    singIn.setTitle("Sing In");
                    LayoutInflater inflater = getLayoutInflater();
                    View view=inflater.inflate(R.layout.sing_in, null,false);
                    singIn.setView(view);
                    singIn.setCancelable(false);
                    singIn.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            Dialog f = (Dialog) dialog;
                            EditText login = f.findViewById(R.id.login);
                            EditText password = f.findViewById(R.id.password);
                            String loginString = login.getText().toString();
                            String passwordString = password.getText().toString();
                            if (!loginString.isEmpty() && !passwordString.isEmpty()) {
                                PendingIntent pi = createPendingResult(1, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
                                Intent intent = new Intent(getBaseContext(), ConnectionWithServer.class)
                                        .putExtra("login", loginString)
                                        .putExtra("password", passwordString)
                                        .putExtra("pi", pi)
                                        .setAction(ActionsUser.AUTORIZATION)
                                        .setPackage(getPackageName());
                                int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);
                                if (result == PackageManager.PERMISSION_GRANTED) {
                                    getApplicationContext().startService(intent);
                                    Log.d("startService", "startService");
                                }
                            }
                        }










//

                    });

                    singIn.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

                        @Override
                        public void onClick(DialogInterface dialog, int which)
                        {
                            dialog.dismiss();
                        }
                    });
                    AlertDialog ad = singIn.create();
                    ad.show();


                    break;
                case R.id.sing_out:
                    Toast.makeText(this,"sing out",Toast.LENGTH_SHORT).show();
                    break;
            }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_camera) {
            // Handle the camera action
        } else if (id == R.id.nav_gallery) {

        } else if (id == R.id.nav_slideshow) {

        } else if (id == R.id.nav_manage) {

        } else if (id == R.id.nav_share) {

        } else if (id == R.id.nav_send) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


}

package com.viaweb.test;

import android.Manifest;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.PendingIntent;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.constraint.Group;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
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
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import edu.itstap.calculator.Food;
import edu.itstap.calculator.User;

public class Calculate extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    public static final String TAG = "Calculate";
    private AddFood addFood;
    private FragmentTransaction ft;
    private SearchProduct searchProduct;
    private ListOfDietProduct tableListProduct;
    public  static User user;
    private EditText edWeight;


    private RecyclerView recResultProduct;
    private RecyclerView.Adapter mAdapter;
    private FloatingActionButton fab;
    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private ImageButton search;
    private RecyclerView.LayoutManager mLayoutManager;
    private EditText searchNameFood;
    private ArrayList<Food> foodArrayList;
    private LinearLayout parentrSearch;


    private class SQLiteConnector extends SQLiteOpenHelper {
        private Context context;

        public SQLiteConnector(Context context, String name,
                               int version) {
            super(context, name, null, version);
            this.context = context;
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

            if (oldVersion == 1 && newVersion == 2) {

            } else if (oldVersion == 2 && newVersion == 3) {

            } else if (oldVersion == 1 && newVersion == 3) {

            }

        }

    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentTransaction ft= getSupportFragmentManager().beginTransaction();
//        ft =  getFragmentManager().beginTransaction();
        tableListProduct =new ListOfDietProduct() ;
        searchProduct = new SearchProduct();
        ft.replace(R.id.frameContainer, searchProduct);
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
                                addFood = new AddFood();
                                addFood.show(getSupportFragmentManager(), TAG);
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
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);


    }

    public ArrayList<Food> testFood() {
        ArrayList<Food> f = new ArrayList<>();
        f.add(new Food("t", 2.5, 2.5, 2.5, 2.5));
        f.add(new Food("t1", 2.5, 2.5, 2.5, 2.5));
        f.add(new Food("t2", 2.5, 2.5, 2.5, 2.5));
        f.add(new Food("t3", 2.5, 2.5, 2.5, 2.5));
        f.add(new Food("t4", 2.5, 2.5, 2.5, 2.5));
        return f;
    }

    @Override
    protected void onStart() {
        super.onStart();
        navigationView.getMenu().setGroupVisible(R.id.grAutorisationGroup, false);


//        recResultProduct.setHasFixedSize(true);
//      aut.setVisibility(View.INVISIBLE);
//        search = searchProduct.getView().findViewById(R.id.btnsearchProduct);
    }

    public Food calculateOneProduct(Food food, Double d) {
        Double resultProt = food.getProtein() * d;
        Double resultFats = food.getFats() * d;
        Double resultCarb = food.getCarbohydrate() * d;
        Double resultCalor = food.getCalories() * d;
        return new Food(food.getName(), resultCalor, resultProt, resultFats, resultCarb);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10) {
            switch (requestCode) {
                case 1:
                    Log.e("autorization", "autorization");
                    setUser(((User) data.getSerializableExtra("user")));
                    fab.setVisibility(View.VISIBLE);
                    navigationView.getMenu().setGroupVisible(R.id.grAutorisationGroup, true);
                    invalidateOptionsMenu();

                    break;
                case 2:
                    setUser(((User) data.getSerializableExtra("user")));

                    Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameContainer);
                    SearchProduct fragment = (SearchProduct)f;
                    fragment.updateViews();
                    //Log.e("searchFood", user.toString());
              /*      foodArrayList.clear();
                    foodArrayList = user.getSearchFood();
                    mAdapter = new RecyclerAdapterSearchProduct(foodArrayList);
                    mAdapter.notifyDataSetChanged();
                    recResultProduct.setAdapter(mAdapter);
                    mAdapter.notifyDataSetChanged();*/
                    break;
            }
        }
    }

    @Override
    public void onBackPressed() {
        drawer = findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        MenuItem singIn = menu.findItem(R.id.sing_in);
        MenuItem singOut = menu.findItem(R.id.sing_out);
        MenuItem registr = menu.findItem(R.id.registarion);
        MenuItem listProduct = menu.findItem(R.id.list_products_item);

        if (getUser() != null && getUser().isAutorization()) {
            singOut.setVisible(true);
            singIn.setVisible(false);
            registr.setVisible(false);
        } else {
            singIn.setVisible(true);
            registr.setVisible(true);
            singOut.setVisible(false);
        }

        if(getUser()==null){
            listProduct.setVisible(false);
        }else if(getUser()!=null&&!getUser().getFoods().isEmpty()){
            listProduct.setVisible(true);
        }
        return true;

    }

/*    @Override
    public boolean onPrepareOptionsMenu(Menu menu)
    {
        MenuItem singIn = menu.findItem(R.id.sing_in);
        MenuItem singOut = menu.findItem(R.id.sing_out);
        MenuItem registr = menu.findItem(R.id.registarion);
        if(user!=null && user.isAutorization())
        {
            singOut.setVisible(true);
            singIn.setVisible(false);
            registr.setVisible(false);
        }
        else
        {
            singIn.setVisible(true);
            registr.setVisible(true);
            singOut.setVisible(false);
        }
        return true;
    }*/

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();
        switch (id) {
            case R.id.sing_in:
//                    Toast.makeText(this,"sing in",Toast.LENGTH_SHORT).show();
                AlertDialog.Builder singIn = new AlertDialog.Builder(this);
                singIn.setTitle("Sing In");
                LayoutInflater inflater = getLayoutInflater();
                View view = inflater.inflate(R.layout.sing_in, null, false);
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
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog ad = singIn.create();
                ad.show();


                break;
            case R.id.sing_out:
                Toast.makeText(this, "sing out", Toast.LENGTH_SHORT).show();
                break;
            case R.id.registarion:
                Toast.makeText(this, "registarion", Toast.LENGTH_SHORT).show();
                break;
            case R.id.list_products_item:
                getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer,new ListOfDietProduct()).commit();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            // Handle the camera action
        } else if (id == R.id.nav_generate) {
//            onRestart();
             FragmentTransaction  ft = getSupportFragmentManager().beginTransaction();
            ft.replace(R.id.frameContainer, searchProduct);
            ft.commit();

        } else if (id == R.id.nav_history) {

        } else if (id == R.id.nav_calculate) {

        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public User getUser() {
        return user;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.btnsearchProduct:
                String searchNameFoodString = searchNameFood.getText().toString();
                Log.d("nameFood", searchNameFoodString);

                if (!searchNameFoodString.isEmpty()) {

                    if (getUser() == null) {
                        user = new User("");
                    }
                    PendingIntent pi = createPendingResult(2, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
                    Intent intent = new Intent(getBaseContext(), ConnectionWithServer.class)
                            .putExtra("nameProduct", searchNameFoodString)
                            .putExtra("user", getUser())
                            .putExtra("pi", pi)
                            .setAction(ActionsUser.SEARCH)
                            .setPackage(getPackageName());
                    int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);
                    if (result == PackageManager.PERMISSION_GRANTED) {
                        getApplicationContext().startService(intent);
                        Log.d("startService", "startService");
                    }
                }
                parentrSearch.requestFocus();

                break;
        }

    }

    public void searchProduct (String searchNameFoodString) {
        Log.d("nameFood", searchNameFoodString);

        if (!searchNameFoodString.isEmpty()) {

            if (getUser() == null) {
                user = new User("");
            }
            PendingIntent pi = createPendingResult(2, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            Intent intent = new Intent(getBaseContext(), ConnectionWithServer.class)
                    .putExtra("nameProduct", searchNameFoodString)
                    .putExtra("user", getUser())
                    .putExtra("pi", pi)
                    .setAction(ActionsUser.SEARCH)
                    .setPackage(getPackageName());
            int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);
            if (result == PackageManager.PERMISSION_GRANTED) {
                getApplicationContext().startService(intent);
                Log.d("startService", "startService");
            }
        }

    }
}

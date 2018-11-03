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
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.StrictMode;
import android.preference.PreferenceActivity;
import android.support.design.widget.FloatingActionButton;

import android.support.design.widget.Snackbar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.ContextCompat;
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
import android.widget.EditText;

import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.viaweb.test.Fragments.CalculateOfReqired;
import com.viaweb.test.Fragments.ListOfDietProduct;
import com.viaweb.test.Fragments.MenuHistory;
import com.viaweb.test.Fragments.OwmMenu;
import com.viaweb.test.Fragments.OwnHistory;
import com.viaweb.test.Fragments.Profile;
import com.viaweb.test.Fragments.SearchProduct;
import com.viaweb.test.Services.ConnectionWithServer;
import com.viaweb.test.libClasses.ActionsUser;
import com.viaweb.test.libClasses.SQLiteConnector;
import com.viaweb.test.libClasses.UserClient;

import java.net.Socket;
import java.util.ArrayList;

import edu.itstap.calculator.Food;
import edu.itstap.calculator.FoodInHistory;
import edu.itstap.calculator.User;

public class Calculate extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener, View.OnClickListener {
    public static final String TAG = "Calculate";
    private FragmentTransaction ft;
    private SearchProduct searchProduct;
    private ListOfDietProduct tableListProduct;
    public static User user;
    private String loginString;
    private String passwordString;
    private UserClient userClient;
    private Socket socket;


    private SQLiteConnector connector;
    private SQLiteDatabase db;


    private String emailStr;


    private DrawerLayout drawer;
    private ActionBarDrawerToggle toggle;
    private NavigationView navigationView;
    private TextView tvLoginUsersHeader;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
//        ft =  getFragmentManager().beginTransaction();
        tableListProduct = new ListOfDietProduct();
        searchProduct = new SearchProduct();
        ft.replace(R.id.frameContainer, searchProduct);
        ft.commit();


        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }

        drawer = findViewById(R.id.drawer_layout);
        toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
        navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        tvLoginUsersHeader = navigationView.getHeaderView(0).findViewById(R.id.tvLoginUsersHeader);


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

    public UserClient getUserClient() {
        return userClient;
    }

    public void setUserClient(UserClient userClient) {
        this.userClient = userClient;
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
        d = Math.floor(d * 100) / 100.0;
        Double resultProt = (food.getProtein() * 0.01) * d;
        resultProt = Math.floor(resultProt * 100) / 100.0;
        Double resultFats = (food.getFats() * 0.01) * d;
        resultFats = Math.floor(resultFats * 100) / 100.0;
        Double resultCarb = (food.getCarbohydrate() * 0.01) * d;
        resultCarb = Math.floor(resultCarb * 100) / 100.0;
        Double resultCalor = (food.getCalories() * 0.01) * d;
        resultCalor = Math.floor(resultCalor * 100) / 100.0;
        return new Food(food.getName(), resultCalor, resultProt, resultFats, resultCarb);

    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == 10) {
            switch (requestCode) {
                case 1:
                    //autor
                    setUser(((User) data.getSerializableExtra("user")));
//                    Log.e("autorization aftre user", getUser().toString());
                    stopService(new Intent(getBaseContext(), ConnectionWithServer.class));
                    break;
                case 4:
                    //reg
                    setUser(((User) data.getSerializableExtra("user")));
//                    Log.e("registration aftre user", getUser().toString());
                    stopService(new Intent(getBaseContext(), ConnectionWithServer.class));
                    break;
                case 5:
                    setUser(((User) data.getSerializableExtra("user")));
                    getUser().setProfileUpdate(false);
                    stopService(new Intent(getBaseContext(), ConnectionWithServer.class));
                    break;
            }

            if (getUser().isAutorization()) {
                navigationView.getMenu().setGroupVisible(R.id.grAutorisationGroup, true);
                tvLoginUsersHeader.setText(this.getUser().getUserName() + " Goal:" + this.getUser().getGoalOfCalories());
                if(getUser().isUseSqLite()) {
                    navigationView.getMenu().findItem(R.id.nav_my_history).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_my_products).setVisible(true);
                }else {
                    navigationView.getMenu().findItem(R.id.nav_my_history).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_my_products).setVisible(false);
                }
            } else {
                navigationView.getMenu().setGroupVisible(R.id.grAutorisationGroup, false);
                navigationView.getMenu().findItem(R.id.nav_my_history).setVisible(false);
                navigationView.getMenu().findItem(R.id.nav_my_products).setVisible(false);
                tvLoginUsersHeader.setText("");
            }
            invalidateOptionsMenu();
        } else if (resultCode == 11) {
            switch (requestCode) {
                case 2:
                    //search food
                  /*  setUser(((User) data.getSerializableExtra("user")));
                    Fragment f = getSupportFragmentManager().findFragmentById(R.id.frameContainer);
                    SearchProduct fragment = (SearchProduct)f;
                    fragment.updateViews();
                    stopService(new Intent(getBaseContext(),ConnectionWithServer.class));*/
                    break;
                case 3:
                    //add prod
                    stopService(new Intent(getBaseContext(), ConnectionWithServer.class));
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

        if (getUser() == null || getUser().getHistoryFoods().isEmpty()) {
            listProduct.setVisible(false);
        } else if (getUser() != null && !getUser().getHistoryFoods().isEmpty()) {
            listProduct.setVisible(true);
        }
        return true;

    }


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
                        loginString = login.getText().toString();
                        passwordString = password.getText().toString();
                        if (!loginString.isEmpty() && !passwordString.isEmpty()) {
                            AutorizationAsynkTask aut = new AutorizationAsynkTask();
                            aut.execute();


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
                this.setUser(new User(""));
                getUser().setAutorization(false);
                navigationView.getMenu().setGroupVisible(R.id.grAutorisationGroup, false);
                tvLoginUsersHeader.setText("");
                invalidateOptionsMenu();
                searchProduct.getFab().setVisibility(View.INVISIBLE);

                FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
                ft.replace(R.id.frameContainer, searchProduct);
                ft.commit();

                break;
            case R.id.registarion:
                Toast.makeText(this, "registarion", Toast.LENGTH_SHORT).show();

                AlertDialog.Builder registarion = new AlertDialog.Builder(this);
                registarion.setTitle("Registarion");
                LayoutInflater inf = getLayoutInflater();
                View v = inf.inflate(R.layout.registration_user, null, false);
                registarion.setView(v);
                registarion.setCancelable(false);
                registarion.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Dialog f = (Dialog) dialog;
                        EditText login = f.findViewById(R.id.edLoginReg);
                        EditText password = f.findViewById(R.id.edPasswordReg);
                        EditText email = f.findViewById(R.id.edEmailReg);

                        loginString = login.getText().toString();
                        passwordString = password.getText().toString();
                        emailStr = email.getText().toString();
                        if (!loginString.isEmpty() && !passwordString.isEmpty() && !emailStr.isEmpty()) {
                            RegistrationAsynkTask reg = new RegistrationAsynkTask();
                            reg.execute();
                        }
                    }


//

                });

                registarion.setNeutralButton("Cancel", new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                AlertDialog registr = registarion.create();
                registr.show();

                break;
            case R.id.list_products_item:

                getSupportFragmentManager().beginTransaction().replace(R.id.frameContainer, new ListOfDietProduct()).commit();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
        switch (id) {
            case R.id.nav_profile:

                ft.replace(R.id.frameContainer, new Profile());
                ft.commit();
                break;
            case R.id.nav_generate:

                ft.replace(R.id.frameContainer, searchProduct);
                ft.commit();
                break;
            case R.id.nav_my_products:
                ft.replace(R.id.frameContainer, new OwmMenu());
                ft.commit();
                break;
            case R.id.nav_calculate:
                ft.replace(R.id.frameContainer, new CalculateOfReqired());
                ft.commit();
                break;
            case R.id.nav_my_history:
                ft.replace(R.id.frameContainer, new OwnHistory());
                ft.commit();
                break;


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

        }

    }


    public void updateProfile(User user) {
        PendingIntent pi = createPendingResult(5, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
        Intent intent = new Intent(getApplicationContext(), ConnectionWithServer.class)
                .putExtra("user", user)
                .putExtra("pi", pi)
                .setAction(ActionsUser.UPDATE_DATA_PROFILE)
                .setPackage(getPackageName());
        int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);
        if (result == PackageManager.PERMISSION_GRANTED) {
            startService(intent);
            Log.d("startService", "startService");
        }
    }

    public void searchProduct(String searchNameFoodString) {
        Log.d("nameFood", searchNameFoodString);

        if (!searchNameFoodString.isEmpty()) {

            if (getUser() == null) {
                user = new User("");
            }
            if (getUser().getFoods().isEmpty()) {
                getUser().getFoods().clear();
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

    class AutorizationAsynkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

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

            return null;
        }


    }

    class RegistrationAsynkTask extends AsyncTask<Void, Void, Void> {

        @Override
        protected Void doInBackground(Void... voids) {

            PendingIntent pi = createPendingResult(4, new Intent(), PendingIntent.FLAG_UPDATE_CURRENT);
            Intent intent = new Intent(getBaseContext(), ConnectionWithServer.class)
                    .putExtra("login", loginString)
                    .putExtra("password", passwordString)
                    .putExtra("pi", pi)
                    .putExtra("email", emailStr)
                    .setAction(ActionsUser.REGISTRATION)
                    .setPackage(getPackageName());
            int result = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.INTERNET);
            if (result == PackageManager.PERMISSION_GRANTED) {
                getApplicationContext().startService(intent);
                Log.d("startService", "startService");
            }

            return null;
        }

    }


    @Override
    protected void onResumeFragments() {
        super.onResumeFragments();
        invalidateOptionsMenu();
        if (getUser() != null) {
            if (getUser().isAutorization()) {
                navigationView.getMenu().setGroupVisible(R.id.grAutorisationGroup, true);
                if(getUser().isUseSqLite()) {
                    navigationView.getMenu().findItem(R.id.nav_my_history).setVisible(true);
                    navigationView.getMenu().findItem(R.id.nav_my_products).setVisible(true);
                }else {
                    navigationView.getMenu().findItem(R.id.nav_my_history).setVisible(false);
                    navigationView.getMenu().findItem(R.id.nav_my_products).setVisible(false);
                }
                tvLoginUsersHeader.setText(this.getUser().getUserName() + " Goal:" + this.getUser().getGoalOfCalories());
            } else {
                navigationView.getMenu().setGroupVisible(R.id.grAutorisationGroup, false);
                tvLoginUsersHeader.setText("");
            }
        }

    }
}

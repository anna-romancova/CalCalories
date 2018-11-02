package com.viaweb.test.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.IBinder;
import android.util.Log;

import com.viaweb.test.libClasses.UserClient;
import com.viaweb.test.libClasses.ActionsUser;

import java.io.IOException;
import java.net.Socket;

import edu.itstap.calculator.User;

public class ConnectionWithServer extends Service {
    private UserClient userClient;
    private String action;
    private User user;
    private Intent curentIntent;
    final String TAG="ConnectionWithServer";



    public ConnectionWithServer() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        userClient = new UserClient("10.0.2.2", 6447);
        try {
//            userClient = new UserClient( new Socket("10.0.2.2", 6447));
            Socket soc=new Socket("192.168.31.116", 6489);
//            Socket soc=new Socket("192.168.31.116", 6489);
            userClient = new UserClient( soc);
        } catch (IOException e) {
            e.printStackTrace();
        }
//        userClient = new UserClient("192.168.31.116", 6447);//c phone sudo ifconfig

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        action = intent.getAction();
        curentIntent=intent;
        ConnectionAsynkTask connect=new ConnectionAsynkTask();
        connect.execute();
        return START_NOT_STICKY;
    }


    @Override
    public void onDestroy() {
        stopSelf();
        super.onDestroy();

    }

    class  ConnectionAsynkTask extends AsyncTask<Void,Void,Void>{



        @Override
        protected Void doInBackground(Void... voids) {
            switch (action) {
                case ActionsUser.AUTORIZATION:
                    user = userClient.autorization(curentIntent.getStringExtra("login"),
                            curentIntent.getStringExtra("password"));
                    if (user.isAutorization()) {
                        PendingIntent pi = curentIntent.getParcelableExtra("pi");

                        Intent intentAut = new Intent().putExtra("user", user);
                        try {

                            pi.send(ConnectionWithServer.this, 10, intentAut);
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }

                    Log.i("TAG", user.toString());


                    break;
                case ActionsUser.SEARCH:
                    /*User us = (User) curentIntent.getSerializableExtra("user");
                    Log.d("before server", us.toString());
//                       userClient = new UserClient("192.168.31.116", 6447);//c phone sudo ifconfig
                    user = userClient.searchFood(curentIntent.getStringExtra("nameProduct"), us);
                    Log.i("from server", user.toString());
                    if (!user.getSearchFood().isEmpty()) {

                        PendingIntent pi1 = curentIntent.getParcelableExtra("pi");

                        Intent intentSearch = new Intent().putExtra("user", user);
                        try {
                            pi1.send(ConnectionWithServer.this, 11, intentSearch);
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }
                    }*/
                    break;
                case ActionsUser.REGISTRATION:
                    user= userClient.registration(curentIntent.getStringExtra("login"),
                            curentIntent.getStringExtra("password"),
                            curentIntent.getStringExtra("email"));
                    if (user.isReg()) {

                        PendingIntent pi = curentIntent.getParcelableExtra("pi");

                        Intent intentReg = new Intent().putExtra("user",user);
                        try {
                            pi.send(ConnectionWithServer.this,10 , intentReg);
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }

                    }
                    break;
                case ActionsUser.ADD_FOOD:
                    boolean addF= userClient.addNewFood(((User)curentIntent.getSerializableExtra("user")));
                    if (addF) {
                        PendingIntent pi = curentIntent.getParcelableExtra("pi");
                        Intent intentResAddFood = new Intent().putExtra("res",true);
                        try {
                            pi.send(ConnectionWithServer.this,11 , intentResAddFood);
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }

                    }

                    break;
                case ActionsUser.UPDATE_DATA_PROFILE:
                    user= userClient.updateDataUser(((User) curentIntent.getSerializableExtra("user")));
                    if (user.isProfileUpdate()) {
                        PendingIntent pi = curentIntent.getParcelableExtra("pi");
                        Intent intentReg = new Intent().putExtra("user",user);
                        try {
                            pi.send(ConnectionWithServer.this,10 , intentReg);
                        } catch (PendingIntent.CanceledException e) {
                            e.printStackTrace();
                        }


                    }
                    break;


            }




            return null;
        }
    }
}

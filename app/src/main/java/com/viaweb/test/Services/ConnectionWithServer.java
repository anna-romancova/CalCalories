package com.viaweb.test.Services;

import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.viaweb.test.libClasses.UserClient;
import com.viaweb.test.libClasses.ActionsUser;

import edu.itstap.calculator.User;

public class ConnectionWithServer extends Service {
    private UserClient userClient;
    private String action;
    private User user;
    final String TAG="ConnectionWithServer";



    public ConnectionWithServer() {
    }

    @Override
    public void onCreate() {
        super.onCreate();
//        userClient = new UserClient("10.0.2.2", 6447);
//        userClient = new UserClient("10.0.2.2", 6447);
                      userClient = new UserClient("192.168.31.116", 6447);//c phone sudo ifconfig

    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        action = intent.getAction();

        switch (action) {
            case ActionsUser.AUTORIZATION:
                final Intent  autIntent=intent;
                Thread tr=new Thread(new Runnable() {
                    @Override
                    public void run() {

                        user = userClient.autorization(autIntent.getStringExtra("login"),
                                autIntent.getStringExtra("password"));
                        if (user.isAutorization()) {
                            PendingIntent pi = autIntent.getParcelableExtra("pi");

                            Intent intentAut = new Intent().putExtra("user",user);
                            try {

                                pi.send(ConnectionWithServer.this,10 , intentAut);
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }

                            Log.i("TAG", user.toString());
                        }



                    }
                });
                tr.start();
                break;
            case ActionsUser.SEARCH:
                final Intent  searchInt=intent;
                Thread trSearch=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        User us=(User) searchInt.getSerializableExtra("user");
                        Log.d("before server",us.toString());
//                       userClient = new UserClient("192.168.31.116", 6447);//c phone sudo ifconfig
                        user= userClient.searchFood(searchInt.getStringExtra("nameProduct"),us);
                        Log.i("from server", user.toString());
                        if (!user.getSearchFood().isEmpty()) {

                            PendingIntent pi = searchInt.getParcelableExtra("pi");

                            Intent intentSearch = new Intent().putExtra("user",user);
                            try {
                                pi.send(ConnectionWithServer.this,10 , intentSearch);
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }


                        }



                    }
                });
                trSearch.start();



                break;
            case ActionsUser.REGISTRATION:
                final Intent  registrationInt=intent;
                Thread trReg=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        user= userClient.registration(registrationInt.getStringExtra("login"),registrationInt.getStringExtra("password"),registrationInt.getStringExtra("email"));
                        if (user.isReg()) {

                            PendingIntent pi = registrationInt.getParcelableExtra("pi");

                            Intent intentReg = new Intent().putExtra("user",user);
                            try {
                                pi.send(ConnectionWithServer.this,10 , intentReg);
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }


                        }

                    }
                });
                trReg.start();

                break;
            case ActionsUser.ADD_FOOD:

                break;
            case ActionsUser.UPDATE_DATA_PROFILE:
                final Intent intentUpProf= intent;
                Thread trUp=new Thread(new Runnable() {
                    @Override
                    public void run() {
                        user= userClient.updateDataUser(((User) intentUpProf.getSerializableExtra("user")));
                        if (user.isProfileUpdate()) {
                            PendingIntent pi = intentUpProf.getParcelableExtra("pi");
                            Intent intentReg = new Intent().putExtra("user",user);
                            try {
                                pi.send(ConnectionWithServer.this,10 , intentReg);
                            } catch (PendingIntent.CanceledException e) {
                                e.printStackTrace();
                            }


                        }

                    }
                });
                trUp.start();

                break;


        }




        return START_STICKY;
    }


    @Override
    public void onDestroy() {
        stopSelf();
        super.onDestroy();

    }
}

package com.viaweb.test.libClasses;

import android.util.Log;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.ArrayList;

import edu.itstap.calculator.Food;
import edu.itstap.calculator.User;



public class UserClient {
    private Socket s;
    private int port;
    private String serverHost;
    private ObjectInputStream ois;
    private ObjectOutputStream oos;
    private User user;
    private Food food;

    public UserClient(Socket s) {
        this.s = s;
    }

    /*
        public UserClient(String serverHost, int port) {
            this.serverHost = serverHost;
            this.port = port;
            try {
                this.connect();

            } catch (UnknownHostException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }*/
    private void writers() throws IOException{
            oos=new ObjectOutputStream( new BufferedOutputStream(s.getOutputStream()));
            oos.flush();
            ois=new ObjectInputStream(new BufferedInputStream(s.getInputStream()));
    }
    public void closeWriters(){
        try {
            if(s!=null) {
                oos.close();
                ois.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }



    public User getUser() {
        return user;
    }


    public void setUser(User user) {
        this.user = user;
    }


    public User autorization(String usName, String psw) {
        User user=new User(usName);
        user.setPassword(psw);
        user.setReg(true);
        user.setAutorization(false);

        try {
            writers();
            oos.writeObject(user);
            oos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {

            user=(User)ois.readObject();
            this.setUser(user);


        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }

        return user;
    }

    public User registration(String usName, String psw, String email) {
        boolean reg=false;
        User user=new User(usName);
        user.setPassword(psw);
        user.setReg(reg);
        user.setEmail(email);

        try {
            writers();
            oos.writeObject(user);
            oos.flush();

        } catch (IOException e) {
            e.printStackTrace();
        }
        try {


            user=(User)ois.readObject();
            this.setUser(user);

            reg=user.isReg();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }




        return user;

    }

    public boolean addNewFood(User user) {
        boolean add=false;
        try {
            writers();
            oos.writeObject(user);
            oos.flush();
            add=true;

        } catch (IOException e) {

            e.printStackTrace();
        }

        return add;
    }
    public User searchFood(String nameFood,User us) {

        Food searchFood =new Food(nameFood, 0.0, 0.0, 0.0, 0.0);
        this.setUser(us);
        searchFood.setSearch(true);
        if(this.getUser().getSearchFood().isEmpty()) {
            this.getUser().getSearchFood().clear();
        }
        this.getUser().getSearchFood().add(searchFood);
        Log.e("before search  food", this.getUser().toString());
        try {
            writers();
            oos.writeObject(this.getUser());
            oos.flush();
            us=(User)ois.readObject();
            this.setUser(us);

        } catch (IOException e) {

            e.printStackTrace();
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        }finally {
            closeWriters();
        }



        return us;
    }


    @Override
    public String toString() {
        return "UserClient [s=" + s + ", port=" + port + ", serverHost=" + serverHost + ", ois=" + ois + ", oos=" + oos
                + ", user=" + user + ", food=" + food + "]";
    }




    public User saveGoalInProfile(User user) {

        try {
            writers();
            oos.writeObject(user);
            oos.flush();
        } catch (IOException e) {

            e.printStackTrace();
        }
        try {
            user=(User)ois.readObject();
            this.setUser(user);

        } catch (IOException e) {

            e.printStackTrace();
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
        return user;

    }


    public User updateDataUser(User user) {
        try {
            writers();
            oos.writeObject(user);
            oos.flush();
        } catch (IOException e) {

            e.printStackTrace();
        }
        try {
            user=(User)ois.readObject();
            this.setUser(user);

        } catch (IOException e) {

            e.printStackTrace();
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
        return user;

    }


    public User saveHistoryMenu(User user) {

        try {
            writers();
            oos.writeObject(user);
            oos.flush();
        } catch (IOException e) {

            e.printStackTrace();
        }

        try {
            user=(User)ois.readObject();
            this.setUser(user);

        } catch (IOException e) {

            e.printStackTrace();
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
        return user;

    }


    public User cleanAllHistory(User user2) {

        try {
            writers();
            oos.writeObject(user2);
            oos.flush();
        } catch (IOException e) {

            e.printStackTrace();
        }

        try {
            user=(User)ois.readObject();
            this.setUser(user);

        } catch (IOException e) {

            e.printStackTrace();
        } catch (ClassNotFoundException e) {

            e.printStackTrace();
        }
        return user;

    }


}

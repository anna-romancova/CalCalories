package com.viaweb.test;

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


    public UserClient(String serverHost, int port) {
        this.serverHost = serverHost;

        this.port = port;
        try {
            this.connect();
            oos=new ObjectOutputStream(s.getOutputStream());
            oos.flush();
            ois=new ObjectInputStream(s.getInputStream());
        } catch (UnknownHostException e) {
            e.printStackTrace();
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


    private void connect() throws  IOException
    {
        s = new Socket(serverHost, port);
        System.out.println("connect");

    }
    public User autorization(String usName, String psw) {
        User user=new User(usName);
        user.setPassword(psw);
        user.setReg(true);
        user.setAutorization(false);

        try {
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

    public boolean addNewFood(String name,Double calories,Double protein, Double fats, Double carbohydrate) {
        boolean add=false;
        ArrayList<Food> newAdd=new ArrayList<>();
        food=new Food(name, calories, protein, fats, carbohydrate);
        food.setAdd(true);

        getUser().getAddFood().add(food);
        System.out.println(getUser().toString());

        try {
            oos.writeObject(getUser());
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
        this.getUser().getSearchFood().add(searchFood);

        try {
            oos.writeObject(this.getUser());
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


    @Override
    public String toString() {
        return "UserClient [s=" + s + ", port=" + port + ", serverHost=" + serverHost + ", ois=" + ois + ", oos=" + oos
                + ", user=" + user + ", food=" + food + "]";
    }


    public User saveGoalInProfile(User user) {

        try {
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

package com.example.Chasse.Model;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Game {

    private final ArrayList<User> users = new ArrayList<>();
    public Course course = new Course();
    private int code = -1;
    private long userId = -1;
    private static Game instance;

    public Game(){}

    public static Game getInstance(){
        if(instance == null){
            instance = new Game();
        }
        return instance;
    }

    public boolean addUser(User user) {
        if(this.users.contains(user)) {
            return false;
        } else {
            this.users.add(user);
            return true;
        }
    }

    public List<User> getUsers() {
        return users;
    }

    public int getCode() {
        return code;
    }

    public void setCode() {
        this.code = new Random().nextInt(1000000);
    }

    public void setCode(int code){
        this.code = code;
    }

    public long getUserId() {
        return userId;
    }

    public void setUserId(long userId) {
        this.userId = userId;
    }
}

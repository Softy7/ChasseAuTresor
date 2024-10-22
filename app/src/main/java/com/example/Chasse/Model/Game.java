package com.example.Chasse.Model;

import java.util.ArrayList;
import java.util.Random;

public class Game {

    public ArrayList<User> users;
    public Course course;
    private int code;

    public Game(){
        this.users = new ArrayList<>();
        this.course = new Course();
    }

    public boolean addUser(User user) {
        if(this.users.contains(user)) {
            return false;
        } else {
            this.users.add(user);
            return true;
        }
    }

    public int getCode() {
        return code;
    }

    public void setCode() {
        this.code = new Random().nextInt(1000000);
    }
}

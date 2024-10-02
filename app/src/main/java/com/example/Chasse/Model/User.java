package com.example.Chasse.Model;

import java.util.ArrayList;

public class User {


    private String email;
    private String firstName;
    private String lastName;
    private String userName;
    private String sexe;
    private String password;
    private ArrayList<User> friends;

    public User() {}
    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getFirstName() {
        return this.firstName;
    }

    public void setFirstName(String firstName) {
        this.firstName = firstName;
    }

    public String getLastName() {
        return this.lastName;
    }

    public void setLastName(String lastName) {
        this.lastName = lastName;
    }

    public String getUserName() {
        return this.userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getSexe() {
        return this.sexe;
    }

    public void setSexe(String sexe) {
        this.sexe = sexe;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public ArrayList<User> getFriends() {
        return this.friends;
    }
    public boolean newFriend(User user) {
        if(this.friends.contains(user)) return false;
        else this.friends.add(user); return true;
    }
}

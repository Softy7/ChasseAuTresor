package com.example.Chasse.Model;

import java.util.ArrayList;

public class User {


    private String email;
    private String firstName;
    private String lastName;
    private String pseudo;
    private long id;
    private ArrayList<User> friends;

    public User() {}


    public User(String email, String firstName, String lastName, String pseudo, long id){
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.pseudo = pseudo;
        this.id = id;
        this.friends = new ArrayList<User>();
    }


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

    public String getPseudo() {
        return this.pseudo;
    }

    public void setPseudo(String pseudo) {
        this.pseudo = pseudo;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public ArrayList<User> getFriends() {
        return this.friends;
    }

    @Override
    public String toString() {
        return "User{" +
                "email='" + email + '\'' +
                ", firstName='" + firstName + '\'' +
                ", lastName='" + lastName + '\'' +
                ", pseudo='" + pseudo + '\'' +
                ", id=" + id +
                ", friends=" + friends +
                '}';
    }
}

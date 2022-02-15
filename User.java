package com.example.ticketbooking.model;

public class User {
    public String username;
    public String password;
    public String type;
    public User(String username,String password,String type){
        this.username=username;
        this.password=password;
        this.type=type;
    }
    public User(){

    }
}

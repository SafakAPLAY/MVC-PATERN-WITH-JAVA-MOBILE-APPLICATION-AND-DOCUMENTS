package com.example.ticketbooking.controler;

import com.example.ticketbooking.model.User;

import java.util.ArrayList;

class UserDOA {
    static ArrayList<User> users=new ArrayList<User>();
    public void addBookingmaneger(){
        users.add( new User("ad1","password","admin"));
        users.add( new User("normal1","password","normal"));
    }
}

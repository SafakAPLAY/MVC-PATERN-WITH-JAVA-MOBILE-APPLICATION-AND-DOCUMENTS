package com.example.ticketbooking.controler;
import com.example.ticketbooking.model.Show;

import java.util.ArrayList;

public class ShowDAO {
    static ArrayList<Show> shows=new ArrayList<Show>();
    public void addShow(){
        shows.add( new Show());
    }
}

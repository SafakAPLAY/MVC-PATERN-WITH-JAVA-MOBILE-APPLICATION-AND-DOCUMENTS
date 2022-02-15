package com.example.ticketbooking.model;

public class Reservation {
    public String showName;
    public String seatNumber;
    public String ownerName;


    public Reservation(String showName,String ownerName,String seatNumber){
        this.showName=showName;
        this.seatNumber=seatNumber;
        this.ownerName=ownerName;
    }


}

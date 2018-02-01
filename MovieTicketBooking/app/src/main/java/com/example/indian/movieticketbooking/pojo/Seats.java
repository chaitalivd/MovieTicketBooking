package com.example.indian.movieticketbooking.pojo;

import java.io.Serializable;
import java.util.ArrayList;

public class Seats implements Serializable
{
    private ArrayList<String> seat_no;

    public ArrayList<String> getSeat_no() {
        return seat_no;
    }

    public void setSeat_no(ArrayList<String> seat_no) {
        this.seat_no = seat_no;
    }

    public ArrayList<String> getAvalability() {
        return avalability;
    }

    public void setAvalability(ArrayList<String> avalability) {
        this.avalability = avalability;
    }

    private ArrayList<String> avalability;

    public Seats(ArrayList<String> seat_no, ArrayList<String> avalability) {
        this.seat_no = seat_no;
        this.avalability = avalability;
    }

    @Override
    public String toString() {
        return seat_no+", "+avalability;
    }
}
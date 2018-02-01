package com.example.indian.movieticketbooking.pojo;
import java.util.ArrayList;
import java.util.List;
public class Hall_locations
{
    private String theatre_name;

    private String ticket_price;

    private String show_timings;

    private List<Seats> seats;

    public void setTheatre_name(String theatre_name){
        this.theatre_name = theatre_name;
    }
    public String getTheatre_name(){
        return this.theatre_name;
    }
    public void setTicket_price(String ticket_price){
        this.ticket_price = ticket_price;
    }
    public String getTicket_price(){
        return this.ticket_price;
    }
    public void setShow_timings(String show_timings){
        this.show_timings = show_timings;
    }
    public String getShow_timings(){
        return this.show_timings;
    }
    public void setSeats(List<Seats> seats){
        this.seats = seats;
    }
    public List<Seats> getSeats(){
        return this.seats;
    }
}
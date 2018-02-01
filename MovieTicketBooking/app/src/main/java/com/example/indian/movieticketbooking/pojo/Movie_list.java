package com.example.indian.movieticketbooking.pojo;
import org.json.JSONArray;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;
public class Movie_list implements Serializable
{
    private String movie_name;

    private String movie_desription;

    private String picture;

    public String getMovie_name() {
        return movie_name;
    }

    public void setMovie_name(String movie_name) {
        this.movie_name = movie_name;
    }

    public String getMovie_desription() {
        return movie_desription;
    }

    public void setMovie_desription(String movie_desription) {
        this.movie_desription = movie_desription;
    }

    public String getPicture() {
        return picture;
    }

    public void setPicture(String picture) {
        this.picture = picture;
    }

    public ArrayList<String> getTheatreNames() {
        return theatreNames;
    }

    public void setTheatreNames(ArrayList<String> theatreNames) {
        this.theatreNames = theatreNames;
    }

    public ArrayList<String> getTheatrePrices() {
        return theatrePrices;
    }

    public void setTheatrePrices(ArrayList<String> theatrePrices) {
        this.theatrePrices = theatrePrices;
    }

    public ArrayList<String> getTheatreTimings() {
        return theatreTimings;
    }

    public void setTheatreTimings(ArrayList<String> theatreTimings) {
        this.theatreTimings = theatreTimings;
    }

    private ArrayList<String> theatreNames = new ArrayList<>();

    private ArrayList<String> theatrePrices = new ArrayList<>();

    private ArrayList<String> theatreTimings = new ArrayList<>();

    public ArrayList<Seats> getSeatData() {
        return seatData;
    }

    public void setSeatData(ArrayList<Seats> seatData) {
        this.seatData = seatData;
    }

    private ArrayList<Seats> seatData = new ArrayList<>();

    //private List<Hall_locations> hall_locations;



    public Movie_list(String movie_name, String movie_desription, String picture,ArrayList<String> theatreNames, ArrayList<String> theatrePrices, ArrayList<String> theatreTimings, ArrayList<Seats> seatData){
        this.movie_name = movie_name;
        this.movie_desription = movie_desription;
        this.picture = picture;
        this.theatreNames = theatreNames;
        this.theatrePrices = theatrePrices;
        this.theatreTimings = theatreTimings;
        this.seatData = seatData;
    }
}
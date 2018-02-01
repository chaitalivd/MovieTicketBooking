package com.example.indian.movieticketbooking.pojo;
import java.util.ArrayList;
import java.util.List;
public class Root
{
    private String message;

    private List<Movie_list> movie_list;

    public void setMessage(String message){
        this.message = message;
    }
    public String getMessage(){
        return this.message;
    }
    public void setMovie_list(List<Movie_list> movie_list){
        this.movie_list = movie_list;
    }
    public List<Movie_list> getMovie_list(){
        return this.movie_list;
    }
}
package com.example.indian.movieticketbooking.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

import com.example.indian.movieticketbooking.R;
import com.example.indian.movieticketbooking.adapters.MovieListAdapter;
import com.example.indian.movieticketbooking.adapters.TheatreListAdapter;
import com.example.indian.movieticketbooking.pojo.Movie_list;
import com.example.indian.movieticketbooking.pojo.Seats;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Iterator;

public class TheatreSelectionActivity extends AppCompatActivity {

    String movieName;
    ArrayList<String> theatreNames = new ArrayList<>();
    ArrayList<String> theatrePrice = new ArrayList<>();
    ArrayList<String> theatreTime = new ArrayList<>();
    ArrayList<Seats> seatList = new ArrayList<>();
    ListView threatreList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_theatre_selection);

        Button calendarManual = (Button)findViewById(R.id.tomoDate_btn);
        threatreList = (ListView)findViewById(R.id.TheatreListView);
        TextView movieNameTextView = (TextView)findViewById(R.id.movieNameTheatre);

        Calendar calendar = Calendar.getInstance();
        Date today = calendar.getTime();
        calendar.add(Calendar.DAY_OF_YEAR, 1);
        Date tomorrow = calendar.getTime();
        DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
        String tomorrowAsString = dateFormat.format(tomorrow);
        calendarManual.setText(tomorrowAsString);

        Intent intent = getIntent();
        if(null != intent){
            movieName = getIntent().getExtras().getString("movie_name");
            theatreNames = (ArrayList<String>) getIntent().getExtras().get("theatre_names");
            theatrePrice = (ArrayList<String>) getIntent().getExtras().get("theatre_price");
            theatreTime = (ArrayList<String>) getIntent().getExtras().get("theatre_timings");
            seatList = (ArrayList<Seats>) getIntent().getExtras().get("theatre_seat_list");

        }
        movieNameTextView.setText(movieName);

        TheatreListAdapter theatreListAdapter = new TheatreListAdapter(this, theatreNames,theatrePrice,theatreTime,movieName,tomorrowAsString,seatList);
        threatreList.setAdapter(theatreListAdapter);
    }
}

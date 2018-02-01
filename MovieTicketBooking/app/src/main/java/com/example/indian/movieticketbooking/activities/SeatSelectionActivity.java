package com.example.indian.movieticketbooking.activities;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.GridView;
import android.widget.Toast;

import com.example.indian.movieticketbooking.R;
import com.example.indian.movieticketbooking.adapters.GridViewAdapter;
import com.example.indian.movieticketbooking.pojo.Movie_list;
import com.example.indian.movieticketbooking.pojo.Seats;

import java.nio.BufferUnderflowException;
import java.util.ArrayList;

public class SeatSelectionActivity extends AppCompatActivity {

    private GridView gridView;
    private GridViewAdapter gridAdapter;

    String movieName, theatreName, theatrePrice, movieDate, movietime;
    ArrayList<Seats> seatData = new ArrayList<Seats>();
    ArrayList<String> selectedSeats = new ArrayList<String>();
    int pay = 0;
    boolean check = false;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seat_selection);

        Intent intent = getIntent();
        if(null != intent){
            movieName = getIntent().getExtras().getString("movie_name");
            theatreName = getIntent().getExtras().getString("theatre_name");
            theatrePrice = getIntent().getExtras().getString("theatre_price");
            movieDate = getIntent().getExtras().getString("movie_date");
            movietime = getIntent().getExtras().getString("movie_time");
            seatData = (ArrayList<Seats>) getIntent().getExtras().get("seat_data");
        }

        gridView = (GridView) findViewById(R.id.gridView);
        gridAdapter = new GridViewAdapter(this,movieName,theatreName,theatrePrice,movieDate,movietime,seatData);
        gridView.setAdapter(gridAdapter);
        selectedSeats = GridViewAdapter.getList();

        Button payment = (Button)findViewById(R.id.payment);
        payment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(selectedSeats.size()>0){
                    pay = Integer.parseInt(theatrePrice)*(selectedSeats.size());
                    Intent i = new Intent(SeatSelectionActivity.this,ConfirmBookingActivity.class);
                    i.putExtra("movie_name",movieName);
                    i.putExtra("theatre_name",theatreName);
                    i.putExtra("movie_date",movieDate);
                    i.putExtra("movie_time",movietime);
                    i.putExtra("selected_list",selectedSeats);
                    i.putExtra("payment",pay);
                    startActivity(i);
                }
                else{
                    Toast.makeText(SeatSelectionActivity.this,"Please selected atlest one seat",Toast.LENGTH_SHORT).show();
                }

            }
        });
    }
}

package com.example.indian.movieticketbooking.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.example.indian.movieticketbooking.R;
import com.example.indian.movieticketbooking.adapters.DataBaseHandler;
import com.example.indian.movieticketbooking.adapters.TicketListAdapter;
import com.example.indian.movieticketbooking.pojo.BookedTickets;

/**
 * Created By Chaitali
 */
public class BookingsActivity extends AppCompatActivity {

    DataBaseHandler dataBaseHandler;
    LinearLayout list_lyt, no_history_lyt;
    ListView ticketsList;
    Button backToHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bookings);

        SharedPreferences sp = getSharedPreferences("myPref", MODE_PRIVATE);
        String getUserName = sp.getString("userName", null);

        no_history_lyt = (LinearLayout)findViewById(R.id.noPurchase);
        list_lyt = (LinearLayout)findViewById(R.id.bookedTicktes);
        ticketsList = (ListView)findViewById(R.id.cartList);
        backToHome = (Button)findViewById(R.id.btnContinueShopping);

        dataBaseHandler = new DataBaseHandler(this);
        dataBaseHandler = dataBaseHandler.open();

        BookedTickets bookedTickets = dataBaseHandler.getBookedTickets(getUserName);
        if(bookedTickets.movieNames.size() > 0){
            list_lyt.setVisibility(View.VISIBLE);
            no_history_lyt.setVisibility(View.GONE);
            TicketListAdapter ticketListAdapter = new TicketListAdapter(this,bookedTickets.movieNames,bookedTickets.theatreNames,bookedTickets.schedules,bookedTickets.seatNos);
            ticketsList.setAdapter(ticketListAdapter);
        }
        else{
            list_lyt.setVisibility(View.GONE);
            no_history_lyt.setVisibility(View.VISIBLE);
            backToHome.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(BookingsActivity.this, HomeActivity.class);
                    intent.putExtra("json_category","movies.json");
                    intent.putExtra("category_title","movie list fetched Successfully");
                    startActivity(intent);
                }
            });
        }

    }
}

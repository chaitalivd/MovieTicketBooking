package com.example.indian.movieticketbooking.activities;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indian.movieticketbooking.R;
import com.example.indian.movieticketbooking.adapters.DataBaseHandler;
import com.example.indian.movieticketbooking.pojo.Seats;

import java.util.ArrayList;

public class ConfirmBookingActivity extends AppCompatActivity {

    String movieName, theatreName, movieDate, movietime, schedule, seatNumber;
    String cardNumberStr,cardExpiryStr,cardCvvStr,cardNameStr;
    ArrayList<String> selectedSeats = new ArrayList<String>();
    int pay;
    DataBaseHandler dataBaseHandler;
    String errMsg = "Field is Empty";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_confirm_booking);

        dataBaseHandler = new DataBaseHandler(this);

        TextView moviename = (TextView)findViewById(R.id.nameOfTheMovie_field);
        TextView theatrename = (TextView)findViewById(R.id.locationOfTheMovie_field);
        TextView dateAndtime = (TextView)findViewById(R.id.dateOfTheMovie_field);
        TextView tickets = (TextView)findViewById(R.id.ticketOfTheMovie_field);
        TextView total = (TextView)findViewById(R.id.totalPay_field);
        Button paymentBill = (Button)findViewById(R.id.confirm_payment_btn);

        Intent intent = getIntent();
        if(null != intent){
            movieName = getIntent().getExtras().getString("movie_name");
            theatreName = getIntent().getExtras().getString("theatre_name");
            movieDate = getIntent().getExtras().getString("movie_date");
            movietime = getIntent().getExtras().getString("movie_time");
            selectedSeats = (ArrayList<String>) getIntent().getExtras().get("selected_list");
            pay = getIntent().getExtras().getInt("payment");
        }

        moviename.setText(movieName);
        theatrename.setText(theatreName);
        schedule = movieDate+", "+movietime;
        dateAndtime.setText(schedule);
        seatNumber = selectedSeats.size()+", SEAT-"+selectedSeats;
        tickets.setText(seatNumber);
        total.setText("Rs."+pay);

        paymentBill.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                billPayment();
            }
        });

    }

    private void billPayment() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog_to_pay);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        final EditText cardNumber, cardExpiry, cardCvv, cardName;
        Button finalPay;

        cardNumber = (EditText)dialog.findViewById(R.id.cardNumber_et);
        cardExpiry = (EditText)dialog.findViewById(R.id.expiryCardNumber_et);
        cardCvv = (EditText)dialog.findViewById(R.id.cvvCardNumber_et);
        cardName = (EditText)dialog.findViewById(R.id.CardName_et);
        finalPay = (Button)dialog.findViewById(R.id.finalPay_btn);

        finalPay.setText("PAY Rs."+pay);

        finalPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cardNumberStr = cardNumber.getText().toString();
                cardExpiryStr = cardExpiry.getText().toString();
                cardCvvStr = cardCvv.getText().toString();
                cardNameStr = cardName.getText().toString();

                if(cardNumberStr.length() == 0){
                    cardNumber.setError(errMsg);
                }
                else if(cardExpiryStr.length() == 0){
                    cardExpiry.setError(errMsg);
                }
                else if(cardCvvStr.length() == 0){
                    cardCvv.setError(errMsg);
                }
                else if(cardNameStr.length() == 0){
                    cardName.setError(errMsg);
                }
                else{
                    SharedPreferences sp = getSharedPreferences("myPref", MODE_PRIVATE);
                    String getUserName = sp.getString("userName", null);
                    String status = "No";
                    for(int i = 0; i < selectedSeats.size(); i++){
                        dataBaseHandler = dataBaseHandler.open();
                        dataBaseHandler.insertAvailability(movieName,theatreName,selectedSeats.get(i),status);
                    }
                    dataBaseHandler = dataBaseHandler.open();
                    dataBaseHandler.insertTickets(getUserName, movieName, theatreName, schedule, seatNumber);
                    Toast.makeText(ConfirmBookingActivity.this, "HAPPY WATCHING", Toast.LENGTH_SHORT).show();
                    Intent i = new Intent(ConfirmBookingActivity.this,HomeActivity.class);
                    i.putExtra("json_category","movies.json");
                    i.putExtra("category_title","movie list fetched Successfully");
                    startActivity(i);
                    dialog.dismiss();
                }
            }
        });
        dialog.show();
    }
}

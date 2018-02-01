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
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indian.movieticketbooking.R;
import com.example.indian.movieticketbooking.adapters.DataBaseHandler;
import com.example.indian.movieticketbooking.adapters.ReviewListAdapter;
import com.example.indian.movieticketbooking.pojo.Movie_list;
import com.example.indian.movieticketbooking.pojo.Review;

import java.util.ArrayList;


public class SelectedMovieDetailsActivity extends AppCompatActivity {

    private Movie_list movie_list;

    String reviewStr,rateStar,movieNameReview;
    DataBaseHandler dataBaseHandler;
    ListView rateList;
    TextView noReviews;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_selected_movie_details);
        dataBaseHandler = new DataBaseHandler(this);

        Intent intent = getIntent();
        if(null != intent){
            movie_list = (Movie_list)intent.getExtras().getSerializable("movie_data");
        }


        TextView movieName = (TextView)findViewById(R.id.movieNameView);
        ImageView moviePic = (ImageView)findViewById(R.id.movieImageView);
        TextView movieDescription = (TextView)findViewById(R.id.movieDescriptionView);
        rateList = (ListView)findViewById(R.id.reviewListView);
        noReviews = (TextView)findViewById(R.id.noReviews);
        Button bookTickets = (Button)findViewById(R.id.movieBook_btn);

        fetchReviews();

        bookTickets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent(SelectedMovieDetailsActivity.this,TheatreSelectionActivity.class);
                intent1.putExtra("movie_name",movie_list.getMovie_name());
                intent1.putExtra("theatre_names",movie_list.getTheatreNames());
                intent1.putExtra("theatre_price",movie_list.getTheatrePrices());
                intent1.putExtra("theatre_timings",movie_list.getTheatreTimings());
                intent1.putExtra("theatre_seat_list",movie_list.getSeatData());
                startActivity(intent1);
            }
        });

        movieName.setText(movie_list.getMovie_name());
        movieDescription.setText(movie_list.getMovie_desription());

        String pImg = movie_list.getPicture();
        int resID = getResources().getIdentifier("@drawable" + pImg, "drawable", this.getPackageName());
        moviePic.setImageResource(resID);

        Button rate = (Button)findViewById(R.id.rateMeBtn);
        rate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                customRateReview();
                fetchReviews();
            }
        });

    }

    private void fetchReviews() {
        dataBaseHandler = dataBaseHandler.open();
        Review revDb = dataBaseHandler.getUserReviews(movie_list.getMovie_name());
        if(revDb.userName.size() > 0){
            rateList.setVisibility(View.VISIBLE);
            noReviews.setVisibility(View.GONE);
            ReviewListAdapter reviewListAdapter = new ReviewListAdapter(this, revDb.userName,revDb.userRate,revDb.userReview);
            rateList.setAdapter(reviewListAdapter);
        }
        else{
            rateList.setVisibility(View.GONE);
            noReviews.setVisibility(View.VISIBLE);
        }
    }

    private void customRateReview() {
        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.alert_dialog_to_rate_review);
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        final EditText review;
        Button btn;
        final RatingBar rb;

        rb = (RatingBar)dialog.findViewById(R.id.ratingBar);
        review = (EditText)dialog.findViewById(R.id.reviewEt);
        btn = (Button)dialog.findViewById(R.id.btn);

        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                reviewStr = review.getText().toString();
                rateStar = String.valueOf(rb.getRating());
                SharedPreferences sp = getSharedPreferences("myPref", MODE_PRIVATE);
                String getUserName = sp.getString("userName",null);
                movieNameReview = movie_list.getMovie_name();

                dataBaseHandler = dataBaseHandler.open();
                dataBaseHandler.insertReview(getUserName,movieNameReview,rateStar,reviewStr);
                Toast.makeText(SelectedMovieDetailsActivity.this,"Thank You For Reviewing!",Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
}

package com.example.indian.movieticketbooking.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.view.View;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indian.movieticketbooking.R;
import com.example.indian.movieticketbooking.adapters.DataBaseHandler;
import com.example.indian.movieticketbooking.adapters.MovieListAdapter;
import com.example.indian.movieticketbooking.pojo.Hall_locations;
import com.example.indian.movieticketbooking.pojo.Movie_list;
import com.example.indian.movieticketbooking.pojo.Seats;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;

import static android.support.design.widget.NavigationView.*;

public class HomeActivity extends AppCompatActivity implements OnNavigationItemSelectedListener {

    DataBaseHandler dataBaseHandler;
    String jsonCategory,categoryTitle;
    ListView categoryList;
    LinkedHashMap<String,Movie_list> selectedMovie = new LinkedHashMap<>();
    TextView userName;
    NavigationView navigationView;
    View mHeaderView;
    ArrayList<String> movieNames = new ArrayList<>();
    ArrayList<String> movieImages = new ArrayList<>();
    ArrayList<String> movieDescription = new ArrayList<>();
    ArrayList<String> theatreNames = new ArrayList<>();
    ArrayList<String> theatrePrices = new ArrayList<>();
    ArrayList<String> theatreTimings = new ArrayList<>();
    ArrayList<String> theatreSeats = new ArrayList<>();
    ArrayList<String> theatreSeatAvailability = new ArrayList<>();
    ArrayList<Seats> seatList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        fetchIntentValues();

        categoryList = (ListView)findViewById(R.id.listView);

        getJsonData();

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mHeaderView = navigationView.getHeaderView(0);
        userName = (TextView)mHeaderView.findViewById(R.id.textViewUn);
        ImageView userProfile = (ImageView)mHeaderView.findViewById(R.id.imageViewPic);
        SharedPreferences sp = this.getSharedPreferences("myPref", MODE_PRIVATE);
        String displayName = sp.getString("userName",null);
        try{
            if(displayName != null){
                userName.setText(displayName);
                dataBaseHandler = dataBaseHandler.open();
                Bitmap displayImage = dataBaseHandler.getImage(displayName);
                if(displayImage != null){
                    userProfile.setImageBitmap(displayImage);
                }
            }
        }
        catch (NullPointerException e){
            System.out.println("Error:"+e);
        }
        navigationView.setNavigationItemSelectedListener(this);
    }


    private void fetchIntentValues(){
        jsonCategory=getIntent().getExtras().getString("json_category");
        categoryTitle=getIntent().getExtras().getString("category_title");
    }

    public String loadJSONFromAsset() {
        String json = null;
        try {
            InputStream is = this.getAssets().open(jsonCategory);
            int size = is.available();
            byte[] buffer = new byte[size];
            is.read(buffer);
            is.close();
            json = new String(buffer, "UTF-8");
        } catch (IOException ex) {
            ex.printStackTrace();
            return null;
        }
        return json;
    }

    private void getJsonData(){
        try {
            // get JSONObject from JSON file
            JSONObject obj = new JSONObject(loadJSONFromAsset());
            // fetch JSONArray named users
            JSONArray userArray = obj.getJSONArray("movie_list");
            // implement for loop for getting users list data
            for (int i = 0; i < userArray.length(); i++) {
                // create a JSONObject for fetching single user data
                JSONObject userDetail = userArray.getJSONObject(i);
                theatreNames.clear();
                theatrePrices.clear();
                theatreTimings.clear();
                seatList.clear();
                // fetch email and name and store it in arraylist
                movieNames.add(userDetail.getString("movie_name"));
                movieImages.add(userDetail.getString("picture"));
                movieDescription.add(userDetail.getString("movie_desription"));
                JSONArray hallLocations = (JSONArray) userDetail.get("hall_locations");
                for (int j = 0; j < hallLocations.length(); j++) {
                    theatreSeats.clear();
                    theatreSeatAvailability.clear();
                    JSONObject theatreDetail = hallLocations.getJSONObject(j);
                    theatreNames.add(theatreDetail.getString("theatre_name"));
                    theatrePrices.add(theatreDetail.getString("ticket_price"));
                    theatreTimings.add(theatreDetail.getString("show_timings"));
                    JSONArray seats = (JSONArray) theatreDetail.get("seats");
                    System.out.println("Seats Array:" + seats);
                    for (int z = 0; z < seats.length(); z++) {
                        JSONObject seatDetails = seats.getJSONObject(z);
                        theatreSeats.add(seatDetails.getString("seat_no"));
                        theatreSeatAvailability.add(seatDetails.getString("avalability"));
                    }
                    Seats seatObj = new Seats(theatreSeats,theatreSeatAvailability);
                    seatList.add(seatObj);
                }
                selectedMovie.put(userDetail.getString("movie_name"), new Movie_list(userDetail.getString("movie_name"), userDetail.getString("movie_desription"), userDetail.getString("picture"), theatreNames, theatrePrices, theatreTimings,seatList));
            }
            MovieListAdapter movieListAdapter =new MovieListAdapter(this, movieNames, movieImages, selectedMovie);
            categoryList.setAdapter(movieListAdapter);
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        dataBaseHandler = new DataBaseHandler(this);
        dataBaseHandler = dataBaseHandler.open();
        navigationView = (NavigationView) findViewById(R.id.nav_view);
        mHeaderView = navigationView.getHeaderView(0);
        userName = (TextView)mHeaderView.findViewById(R.id.textViewUn);
        ImageView userProfile = (ImageView)mHeaderView.findViewById(R.id.imageViewPic);
        SharedPreferences sp = this.getSharedPreferences("myPref", MODE_PRIVATE);
        String displayName = sp.getString("userName",null);
        try{
            if(displayName != null){
                userName.setText(displayName);
                dataBaseHandler = dataBaseHandler.open();
                Bitmap displayImage = dataBaseHandler.getImage(displayName);
                if(displayImage != null){
                    userProfile.setImageBitmap(displayImage);
                }
            }
        }
        catch (NullPointerException e){
            System.out.println("Error:"+e);
        }
        navigationView.setNavigationItemSelectedListener(this);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_profile) {
            Intent intent = new Intent(HomeActivity.this, ProfileActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_bookings) {
            Intent intent = new Intent(HomeActivity.this, BookingsActivity.class);
            startActivity(intent);
        } else if (id == R.id.nav_signOut) {
            Toast.makeText(HomeActivity.this,"SignOut Successful",Toast.LENGTH_SHORT).show();
            SharedPreferences sp = this.getSharedPreferences("myPref", MODE_PRIVATE);
            SharedPreferences.Editor editor = sp.edit();
            editor.putString("userName",null);
            editor.commit();
            Intent intent = new Intent(HomeActivity.this, LoginActivity.class);
            startActivity(intent);
        }
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }
}

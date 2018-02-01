package com.example.indian.movieticketbooking.adapters;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.indian.movieticketbooking.R;
import com.example.indian.movieticketbooking.activities.HomeActivity;
import com.example.indian.movieticketbooking.activities.ProfileActivity;
import com.example.indian.movieticketbooking.activities.SelectedMovieDetailsActivity;
import com.example.indian.movieticketbooking.activities.TheatreSelectionActivity;
import com.example.indian.movieticketbooking.pojo.Movie_list;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by Chaitali on 27/01/2018.
 */

public class MovieListAdapter extends BaseAdapter{

    private Activity context;
    ArrayList<String> movieNames = new ArrayList<>();
    ArrayList<String> movieImages = new ArrayList<>();
    ArrayList<String> keys = new ArrayList<>();
    ArrayList<Movie_list> values = new ArrayList<>();
    private LinkedHashMap<String,Movie_list> movieTypeMap;

    public MovieListAdapter(Activity context, ArrayList<String> movieNames, ArrayList<String> movieImages, LinkedHashMap<String,Movie_list> movieTypeMap) {
        this.context = context;
        this.movieNames = movieNames;
        this.movieImages = movieImages;
        this.movieTypeMap = movieTypeMap;


        for(Map.Entry<String,Movie_list> entry : movieTypeMap.entrySet()){
            keys.add(entry.getKey());
            values.add(entry.getValue());
        }

    }
    @Override
    public int getCount() {
        return movieNames.size();
    }

    @Override
    public Object getItem(int position) {
        return null;
    }

    @Override
    public long getItemId(int position) {
        return 0;
    }

    @Override
    public View getView(final int position, View view, ViewGroup viewGroup) {
        if (view == null) {
            final Holder holder = new Holder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_movie_list_view, viewGroup, false);

            holder.movieImg = (ImageView) view.findViewById(R.id.moviePic_iv);
            String pImg = movieImages.get(position);

            Resources resID = view.getResources();
            int resIDRes = resID.getIdentifier("@drawable" + pImg, "drawable", viewGroup.getContext().getPackageName());
            holder.movieImg.setImageResource(resIDRes);

            holder.movieName = (TextView) view.findViewById(R.id.movieName_tv);
            holder.movieName.setText(movieNames.get(position));

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(context, SelectedMovieDetailsActivity.class);
                    Bundle b = new Bundle();
                    b.putSerializable("movie_data",values.get(position));
                    intent.putExtras(b);
                    context.startActivity(intent);

                }
            });

            holder.bookMovie = (Button) view.findViewById(R.id.book_btn);
            holder.bookMovie.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Movie_list movie_list = values.get(position);
                    Intent intent1 = new Intent(context,TheatreSelectionActivity.class);
                    intent1.putExtra("movie_name",movie_list.getMovie_name());
                    intent1.putExtra("theatre_names",movie_list.getTheatreNames());
                    intent1.putExtra("theatre_price",movie_list.getTheatrePrices());
                    intent1.putExtra("theatre_timings",movie_list.getTheatreTimings());
                    intent1.putExtra("theatre_seat_list",movie_list.getSeatData());
                    context.startActivity(intent1);
                    Toast.makeText(context, movieNames.get(position), Toast.LENGTH_SHORT).show();
                }
            });
        }
        return view;
    }

    private class Holder {
        TextView movieName;
        ImageView movieImg;
        Button bookMovie;
    }
}



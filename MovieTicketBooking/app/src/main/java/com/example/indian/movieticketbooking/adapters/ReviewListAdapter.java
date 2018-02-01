package com.example.indian.movieticketbooking.adapters;

import android.app.Activity;
import android.content.Context;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import android.widget.TextView;


import com.example.indian.movieticketbooking.R;
import com.example.indian.movieticketbooking.activities.SelectedMovieDetailsActivity;
import com.example.indian.movieticketbooking.pojo.Movie_list;

import java.util.ArrayList;
import java.util.Iterator;

/**
 * Created by Chaitali on 30/01/2018.
 */

public class ReviewListAdapter extends BaseAdapter {

    private Activity context;
    //Context context;
    ArrayList<String> userNames = new ArrayList<>();
    ArrayList<String> movieRate = new ArrayList<>();
    ArrayList<String> movieReview = new ArrayList<>();

    public ReviewListAdapter(Activity context, ArrayList<String> userNamesPre, ArrayList<String> movieRatePre, ArrayList<String> movieReviewPre) {
        this.context = context;
        this.userNames = userNamesPre;
        this.movieRate = movieRatePre;
        this.movieReview = movieReviewPre;

    }

    @Override
    public int getCount() {
        return movieReview.size();
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
    public View getView(int position, View view, ViewGroup viewGroup) {
            final Holder holder = new Holder();
            LayoutInflater inflater=context.getLayoutInflater();
            view = inflater.inflate(R.layout.custom_review_list_view, null);

            holder.userNameTv = (TextView) view.findViewById(R.id.usernameRes);
            holder.userNameTv.setText(userNames.get(position));

            holder.userStarTv = (TextView) view.findViewById(R.id.ratingRes);
            holder.userStarTv.setText("Ratings : "+movieRate.get(position));


            holder.userReviewTv = (TextView) view.findViewById(R.id.descpRes);
            holder.userReviewTv.setText(movieReview.get(position));
            return view;
    }

    private class Holder {
        TextView userNameTv, userStarTv, userReviewTv;
    }
}

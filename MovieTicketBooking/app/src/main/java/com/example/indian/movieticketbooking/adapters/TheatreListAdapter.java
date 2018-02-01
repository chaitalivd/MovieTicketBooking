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
import com.example.indian.movieticketbooking.activities.SeatSelectionActivity;
import com.example.indian.movieticketbooking.activities.SelectedMovieDetailsActivity;
import com.example.indian.movieticketbooking.pojo.Movie_list;
import com.example.indian.movieticketbooking.pojo.Seats;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedHashMap;

/**
 * Created by Chaitali on 30/01/2018.
 */

public class TheatreListAdapter extends BaseAdapter{

    private Activity context;
    ArrayList<String> theatreNames = new ArrayList<>();
    ArrayList<String> theatrePrice = new ArrayList<>();
    ArrayList<String> theatreTimings = new ArrayList<>();
    ArrayList<Seats> seatList = new ArrayList<>();
    String movieName, date;

    public TheatreListAdapter(Activity context, ArrayList<String> theatreNames, ArrayList<String> theatrePrice, ArrayList<String> theatreTimings, String moviename, String date, ArrayList<Seats> seatList) {
        this.context = context;
        this.theatreNames = theatreNames;
        this.theatrePrice = theatrePrice;
        this.theatreTimings = theatreTimings;
        this.movieName = moviename;
        this.date = date;
        this.seatList = seatList;

        }

    @Override

    public int getCount() {
        return theatreNames.size();
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
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_theatre_list_view, viewGroup, false);

            holder.theatreNameList = (TextView) view.findViewById(R.id.theatreName_tv);
            holder.theatrePriceList = (TextView)view.findViewById(R.id.theatrePrice_tv);
            holder.movieTimeOne = (Button)view.findViewById(R.id.timeOne_btn);

            holder.theatreNameList.setText(theatreNames.get(position));
            holder.theatrePriceList.setText("Price : "+theatrePrice.get(position));
            holder.movieTimeOne.setText(theatreTimings.get(position));

            holder.movieTimeOne.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Seats> checkList = new ArrayList<Seats>();
                    checkList.add(seatList.get(position));

                    Intent intent = new Intent(context, SeatSelectionActivity.class);
                    intent.putExtra("movie_name",movieName);
                    intent.putExtra("theatre_name",theatreNames.get(position));
                    intent.putExtra("theatre_price",theatrePrice.get(position));
                    intent.putExtra("movie_date",date);
                    intent.putExtra("movie_time",theatreTimings.get(position));
                    Bundle b = new Bundle();
                    b.putSerializable("seat_data",checkList);
                    intent.putExtras(b);
                    context.startActivity(intent);
                }
            });
        }
        return view;
    }

    private class Holder {
        TextView theatreNameList, theatrePriceList;
        Button movieTimeOne;
    }
}

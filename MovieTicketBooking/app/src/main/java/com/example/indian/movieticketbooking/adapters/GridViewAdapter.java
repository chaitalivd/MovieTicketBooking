package com.example.indian.movieticketbooking.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

import com.example.indian.movieticketbooking.R;
import com.example.indian.movieticketbooking.activities.SeatSelectionActivity;
import com.example.indian.movieticketbooking.pojo.SeatStatus;
import com.example.indian.movieticketbooking.pojo.Seats;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * Created by Chaitali on 31/01/2018.
 */
public class GridViewAdapter extends BaseAdapter {

    private Activity context;
    String movieName, theatreName, theatrePrice, movieDate, movietime;
    String op;
    private ArrayList<Seats> data = new ArrayList<Seats>();
    private static ArrayList<String> seatNumsList = new ArrayList<>();
    ArrayList<String> mySeatNo;
    ArrayList<String> mySeatAvail;
    DataBaseHandler dataBaseHandler;


    public GridViewAdapter(Activity context, String movieName, String theatreName, String theatrePrice, String movieDate, String movietime, ArrayList<Seats> data) {
        this.context = context;
        this.movieName = movieName;
        this.theatreName = theatreName;
        this.theatrePrice = theatrePrice;
        this.movieDate = movieDate;
        this.movietime = movietime;
        this.data = data;
        dataBaseHandler = new DataBaseHandler(context);
        seatNumsList.clear();

            for (Seats str : data) {
                op = str.toString();
            }
            String[] output = op.split("],");
            String str1 = output[0];
            str1 = str1.replaceAll("\\[|\\]", "");
            String str2 = output[1];
            str2 = str2.replaceAll("\\[|\\]", "");

            mySeatNo = new ArrayList<String>(Arrays.asList(str1.split(", ")));
            System.out.println(mySeatNo);
            mySeatAvail = new ArrayList<String>(Arrays.asList(str2.split(", ")));
            System.out.println(mySeatAvail);

    }

    @Override
    public int getCount() {
        return mySeatNo.size();
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
        seatNumsList.clear();
        if (view == null) {
            final Holder holder = new Holder();
            view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.custom_grid_seats_view, viewGroup, false);
            holder.imageTitle = (Button) view.findViewById(R.id.textBtn);

            holder.imageTitle.setText(mySeatNo.get(position));
            String seatCheck = mySeatNo.get(position);

            dataBaseHandler = dataBaseHandler.open();
            String dateDb = dataBaseHandler.getLastBookingDate();
            String[] op = dateDb.split(",");
            String dateSplited = op[0];

            Calendar calendar = Calendar.getInstance();
            Date today = calendar.getTime();
            calendar.add(Calendar.DAY_OF_YEAR, 1);
            Date tomorrow = calendar.getTime();
            DateFormat dateFormat = new SimpleDateFormat("dd-MMM-yyyy");
            String tomorrowAsString = dateFormat.format(tomorrow);

            if(dateSplited.equalsIgnoreCase(tomorrowAsString)){
                dataBaseHandler = dataBaseHandler.open();
                SeatStatus seatStatus = dataBaseHandler.getSeatAvailability(movieName,theatreName);
                if (seatStatus.seatBlocked.size() > 0) {
                    for (int i = 0; i < seatStatus.seatBlocked.size(); i++) {
                        if (seatCheck.equalsIgnoreCase(seatStatus.seatBlocked.get(i))) {
                            holder.imageTitle.setBackgroundColor(Color.parseColor("#DCDCDC"));
                            holder.imageTitle.setTextColor(Color.parseColor("#FFFFFF"));
                            holder.imageTitle.setEnabled(false);
                            break;
                        } else {
                            holder.imageTitle.setBackgroundColor(Color.parseColor("#FFFFFF"));
                            holder.imageTitle.setTextColor(Color.parseColor("#000000"));
                            holder.imageTitle.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    seatNumsList.add(holder.imageTitle.getText().toString());
                                    holder.imageTitle.setBackgroundColor(Color.CYAN);
                                }
                            });
                        }

                    }
                } else {
                    if (mySeatAvail.get(position).equalsIgnoreCase("yes") || (mySeatAvail.get(position).equalsIgnoreCase(" yes"))) {
                        holder.imageTitle.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        holder.imageTitle.setTextColor(Color.parseColor("#000000"));
                        holder.imageTitle.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View view) {
                                seatNumsList.add(holder.imageTitle.getText().toString());
                                holder.imageTitle.setBackgroundColor(Color.CYAN);
                            }
                        });
                    } else {
                        holder.imageTitle.setBackgroundColor(Color.parseColor("#DCDCDC"));
                        holder.imageTitle.setTextColor(Color.parseColor("#FFFFFF"));
                        holder.imageTitle.setEnabled(false);
                    }
                }
            }
            else{
                if (mySeatAvail.get(position).equalsIgnoreCase("yes") || (mySeatAvail.get(position).equalsIgnoreCase(" yes"))) {
                    holder.imageTitle.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    holder.imageTitle.setTextColor(Color.parseColor("#000000"));
                    holder.imageTitle.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            seatNumsList.add(holder.imageTitle.getText().toString());
                            holder.imageTitle.setBackgroundColor(Color.CYAN);
                        }
                    });
                } else {
                    holder.imageTitle.setBackgroundColor(Color.parseColor("#DCDCDC"));
                    holder.imageTitle.setTextColor(Color.parseColor("#FFFFFF"));
                    holder.imageTitle.setEnabled(false);
                }
            }
        }
        return view;

    }

    public static ArrayList<String> getList() {
        return seatNumsList;
    }

    private class Holder {
        Button imageTitle;
    }
}

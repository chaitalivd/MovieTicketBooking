package com.example.indian.movieticketbooking.adapters;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.example.indian.movieticketbooking.R;

import java.util.ArrayList;

/**
 * Created by Chaitali on 01/02/2018.
 */

public class TicketListAdapter extends BaseAdapter{
    private Activity context;
    ArrayList<String> movieNames = new ArrayList<>();
    ArrayList<String> theatreNames = new ArrayList<>();
    ArrayList<String> schedule = new ArrayList<>();
    ArrayList<String> seatNos = new ArrayList<>();

    public TicketListAdapter(Activity context, ArrayList<String> movieNames, ArrayList<String> theatreNames, ArrayList<String> schedule, ArrayList<String> seatNos) {
        this.context = context;
        this.movieNames = movieNames;
        this.theatreNames = theatreNames;
        this.schedule = schedule;
        this.seatNos = seatNos;
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
    public View getView(int position, View view, ViewGroup viewGroup) {
        final Holder holder = new Holder();
        LayoutInflater inflater=context.getLayoutInflater();
        view = inflater.inflate(R.layout.custom_bookings_list_view, null);

        holder.movieNameTv = (TextView) view.findViewById(R.id.movieNameBook);
        holder.movieNameTv.setText(movieNames.get(position));

        holder.theatreNameTv = (TextView) view.findViewById(R.id.theatreNameBook);
        holder.theatreNameTv.setText(theatreNames.get(position));

        holder.scheduleTv = (TextView) view.findViewById(R.id.scheduleBook);
        holder.scheduleTv.setText(schedule.get(position));

        holder.seatTv = (TextView) view.findViewById(R.id.seatNoBook);
        holder.seatTv.setText(seatNos.get(position));

        return view;
    }

    private class Holder {
        TextView movieNameTv, theatreNameTv, scheduleTv, seatTv;
    }
}

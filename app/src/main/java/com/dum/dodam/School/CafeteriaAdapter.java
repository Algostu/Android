package com.dum.dodam.School;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.R;
import com.dum.dodam.School.dataframe.CafeteriaFrame;

import java.util.ArrayList;

public class CafeteriaAdapter extends RecyclerView.Adapter<CafeteriaAdapter.Holder> {

    private ArrayList<CafeteriaFrame> list = new ArrayList<CafeteriaFrame>();

    public CafeteriaAdapter(ArrayList<CafeteriaFrame> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.school_cafeteria_card, parent, false);
        CafeteriaAdapter.Holder holder = new CafeteriaAdapter.Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, int position) {
        holder.lunch_monday.setText(list.get(position).lunch_monday);
        holder.lunch_tuesday.setText(list.get(position).lunch_tuesday);
        holder.lunch_wednesday.setText(list.get(position).lunch_wednesday);
        holder.lunch_thursday.setText(list.get(position).lunch_thursday);
        holder.lunch_friday.setText(list.get(position).lunch_friday);

        holder.monday.setText("월(" + list.get(position).date_monday + ")");
        holder.tuesday.setText("화(" + list.get(position).date_tuesday + ")");
        holder.wednesday.setText("수(" + list.get(position).date_wednesday + ")");
        holder.thursday.setText("목(" + list.get(position).date_thursday + ")");
        holder.friday.setText("금(" + list.get(position).date_friday + ")");

        holder.week.setText(String.valueOf(position + 1) + " 주차");
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {

        private TextView monday;
        private TextView tuesday;
        private TextView wednesday;
        private TextView thursday;
        private TextView friday;

        private TextView lunch_monday;
        private TextView lunch_tuesday;
        private TextView lunch_wednesday;
        private TextView lunch_thursday;
        private TextView lunch_friday;

        private TextView week;

        Holder(View itemView) {
            super(itemView);
            monday = itemView.findViewById(R.id.monday);
            tuesday = itemView.findViewById(R.id.tuesday);
            wednesday = itemView.findViewById(R.id.wednesday);
            thursday = itemView.findViewById(R.id.thursday);
            friday = itemView.findViewById(R.id.friday);

            lunch_monday = itemView.findViewById(R.id.lunch_monday);
            lunch_tuesday = itemView.findViewById(R.id.lunch_tuesday);
            lunch_wednesday = itemView.findViewById(R.id.lunch_wednesday);
            lunch_thursday = itemView.findViewById(R.id.lunch_thursday);
            lunch_friday = itemView.findViewById(R.id.lunch_friday);
            lunch_monday.setSelected(true);
            lunch_tuesday.setSelected(true);
            lunch_wednesday.setSelected(true);
            lunch_thursday.setSelected(true);
            lunch_friday.setSelected(true);

            week = itemView.findViewById(R.id.week);
        }
    }
}
package com.dum.dodam.Scheduler;

import android.annotation.SuppressLint;
import android.os.Build;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;

public class TimeTableAdapter extends RecyclerView.Adapter<TimeTableAdapter.Holder> {
    private final ArrayList<String> list;

    public TimeTableAdapter(ArrayList<String> list) {
        this.list = list;
    }

//    public interface OnListItemSelectedInterface {
//        void onItemSelected(View v, int position);
//    }

    @NonNull
    @Override
    public TimeTableAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scheduler_time_table_card, parent, false);
        TimeTableAdapter.Holder holder = new TimeTableAdapter.Holder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView period;
        protected TextView subject;

        public Holder(View view) {
            super(view);
            this.period = (TextView) view.findViewById(R.id.period);
            this.subject = (TextView) view.findViewById(R.id.subject);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull TimeTableAdapter.Holder holder, final int position) {
        int hour = Integer.parseInt(getCurrentTime());
        if (position == hour - 9) {
            holder.period.setTextAppearance(R.style.textAppearanceInTime);
            holder.subject.setTextAppearance(R.style.textAppearanceInTime);
        }
        holder.period.setText(String.format("%d교시", position + 1));
        holder.subject.setText(list.get(position));

        holder.itemView.setTag(position);
    }

    public String getCurrentTime() {
        SimpleDateFormat format = new SimpleDateFormat("HH");

        String format_time = format.format(System.currentTimeMillis());

        return format_time;
    }
}
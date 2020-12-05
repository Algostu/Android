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

import com.dum.dodam.LocalDB.TimeTableDay;
import com.dum.dodam.R;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class TimeTableFullAdapter extends RecyclerView.Adapter<TimeTableFullAdapter.Holder> {
    private final ArrayList<TimeTableDay> list;

    public TimeTableFullAdapter(ArrayList<TimeTableDay> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public TimeTableFullAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scheduler_time_table_item, parent, false);
        TimeTableFullAdapter.Holder holder = new TimeTableFullAdapter.Holder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView DOW;
        protected TextView subject1;
        protected TextView subject2;
        protected TextView subject3;
        protected TextView subject4;
        protected TextView subject5;
        protected TextView subject6;
        protected TextView subject7;


        public Holder(View view) {
            super(view);
            this.DOW = view.findViewById(R.id.DOW);
            this.subject1 = view.findViewById(R.id.subject1);
            this.subject2 = view.findViewById(R.id.subject2);
            this.subject3 = view.findViewById(R.id.subject3);
            this.subject4 = view.findViewById(R.id.subject4);
            this.subject5 = view.findViewById(R.id.subject5);
            this.subject6 = view.findViewById(R.id.subject6);
            this.subject7 = view.findViewById(R.id.subject7);
        }
    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @SuppressLint("ResourceAsColor")
    @Override
    public void onBindViewHolder(@NonNull TimeTableFullAdapter.Holder holder, final int position) {

        try {
            holder.DOW.setText(getDOW(list.get(position).date));
        } catch (ParseException e) {
            e.printStackTrace();
        }
        try {
            holder.subject1.setText(list.get(position).subject.get(1));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            holder.subject2.setText(list.get(position).subject.get(2));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            holder.subject3.setText(list.get(position).subject.get(3));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            holder.subject4.setText(list.get(position).subject.get(4));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            holder.subject5.setText(list.get(position).subject.get(5));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            holder.subject6.setText(list.get(position).subject.get(6));
        } catch (Exception e) {
            e.printStackTrace();
        }
        try {
            holder.subject7.setText(list.get(position).subject.get(7));
        } catch (Exception e) {
            e.printStackTrace();
        }

        holder.itemView.setTag(position);
}

    public String getDOW(int date) throws ParseException {
        Calendar cal = Calendar.getInstance();
        String res;
        int year = cal.get(Calendar.YEAR);
        int month = cal.get(Calendar.MONTH)+1;

        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Date change_date = dateFormat.parse(String.format("%d%02d%02d", year, month, date));

        cal.setTime(change_date);
        int DOW = cal.get(Calendar.DAY_OF_WEEK);
        if (DOW == 2) {
            res = "월요일";
        } else if (DOW == 3) {
            res = "화요일";
        } else if (DOW == 4) {
            res = "수요일";
        } else if (DOW == 5) {
            res = "목요일";
        } else if (DOW == 6) {
            res = "금요일";
        } else {
            res = "???";
        }
        return res;
    }
}

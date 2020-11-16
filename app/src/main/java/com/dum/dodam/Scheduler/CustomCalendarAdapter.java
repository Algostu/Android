package com.dum.dodam.Scheduler;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.LocalDB.TodoList;
import com.dum.dodam.R;

import java.util.ArrayList;
import java.util.Calendar;

public class CustomCalendarAdapter extends RecyclerView.Adapter<CustomCalendarAdapter.Holder> {
    private ArrayList<TodoList> list = new ArrayList<TodoList>();
    private OnListItemSelectedInterface mListener;
    private Context context;

    public CustomCalendarAdapter(Context context, ArrayList<TodoList> list, OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.scheduler_calendar_event_item, parent, false);
        CustomCalendarAdapter.Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public static class Holder extends RecyclerView.ViewHolder {
        protected TextView startTime;
        protected TextView endTime;
        protected TextView todo_content;

        public Holder(View view) {
            super(view);
            this.startTime = (TextView) view.findViewById(R.id.startTime);
            this.endTime = (TextView) view.findViewById(R.id.endTime);
            this.todo_content = (TextView) view.findViewById(R.id.todo_content);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull CustomCalendarAdapter.Holder holder, final int position) {
        Calendar calendar = Calendar.getInstance();

        calendar.setTimeInMillis(list.get(position).start);
        holder.startTime.setText(calendar.toString());

        calendar.setTimeInMillis(list.get(position).end);
        holder.endTime.setText(calendar.toString());

        holder.todo_content.setText(list.get(position).title);

        holder.itemView.setTag(position);
    }
}

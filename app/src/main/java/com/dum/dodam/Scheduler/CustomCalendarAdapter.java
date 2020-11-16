package com.dum.dodam.Scheduler;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.R;

import java.util.ArrayList;

class TodoData {
    String time;
    String todo;
    public boolean visible;

    public TodoData(String time, String todo) {
        this.time = time;
        this.todo = todo;
    }
}

class TodoDataList {
    ArrayList<TodoData> todoList;

    public TodoDataList(ArrayList<TodoData> list) {
        this.todoList = list;
    }

    public TodoDataList() {

    }
}

public class CustomCalendarAdapter extends RecyclerView.Adapter<CustomCalendarAdapter.Holder> {
    private ArrayList<TodoData> list = new ArrayList<TodoData>();
    private OnListItemSelectedInterface mListener;
    private Context context;

    public CustomCalendarAdapter(Context context, ArrayList<TodoData> list, OnListItemSelectedInterface listener) {
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
        protected TextView time;
        protected TextView todo_content;
        protected ImageView ic_remove;
        protected CheckBox todo_done;

        public Holder(View view) {
            super(view);
            this.time = (TextView) view.findViewById(R.id.time);
            this.todo_content = (TextView) view.findViewById(R.id.todo_content);
            this.ic_remove = (ImageView) view.findViewById(R.id.ic_remove);
            this.todo_done = (CheckBox) view.findViewById(R.id.todo_done);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

        }
    }

    @Override
    public void onBindViewHolder(@NonNull CustomCalendarAdapter.Holder holder, final int position) {
        holder.time.setText(list.get(position).time);
        holder.todo_content.setText(list.get(position).todo);

        holder.itemView.setTag(position);

        final CustomCalendarAdapter.Holder orgHolder = holder;
        holder.todo_done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if(b==true){
                    orgHolder.todo_content.setTextColor(Color.GRAY);
                    orgHolder.todo_content.setTypeface(null, Typeface.ITALIC);
                    orgHolder.todo_content.setPaintFlags(orgHolder.todo_content.getPaintFlags()| Paint.STRIKE_THRU_TEXT_FLAG);

                } else {
                    orgHolder.todo_content.setTextColor(Color.BLACK);
                    orgHolder.todo_content.setTypeface(null, Typeface.NORMAL);
                    orgHolder.todo_content.setPaintFlags(0);
                }
            }
        });

        if(list.get(position).visible){
            holder.ic_remove.setVisibility(View.VISIBLE);
        } else {
            holder.ic_remove.setVisibility(View.GONE);
        }
        holder.ic_remove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                list.remove(position);
                notifyItemRemoved(position);
            }
        });
    }
}

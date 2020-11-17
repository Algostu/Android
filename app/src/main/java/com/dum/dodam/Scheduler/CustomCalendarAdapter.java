package com.dum.dodam.Scheduler;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Typeface;
import android.graphics.drawable.GradientDrawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.LocalDB.Todo;
import com.dum.dodam.R;

import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;

public class CustomCalendarAdapter extends RecyclerView.Adapter<CustomCalendarAdapter.Holder> {
    private ArrayList<Todo> list = new ArrayList<Todo>();
    private OnListItemSelectedInterface mListener;
    private Context context;
    private Realm realm;

    public CustomCalendarAdapter(Context context, ArrayList<Todo> list, OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
        this.realm = Realm.getDefaultInstance();
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

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView time;
        protected TextView todo_content;
        protected ImageView ic_remove;
        protected CheckBox todo_done;
        protected ImageView iv_color_ball;

        public Holder(View view) {
            super(view);
            this.time = (TextView) view.findViewById(R.id.time);
            this.todo_content = (TextView) view.findViewById(R.id.todo_content);
            this.ic_remove = (ImageView) view.findViewById(R.id.ic_remove);
            this.todo_done = (CheckBox) view.findViewById(R.id.todo_done);
            this.iv_color_ball = (ImageView) view.findViewById(R.id.iv_color_ball);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                }
            });

            ic_remove.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        realm.executeTransactionAsync(new Realm.Transaction() {
                            @Override
                            public void execute(Realm realm) {
                                int poisition = getAbsoluteAdapterPosition();
                                Todo deleteTodo = realm.where(Todo.class).equalTo("ID", list.get(poisition).ID).equalTo("title", list.get(poisition).title).findFirst();
                                if (deleteTodo != null) {
                                    list.remove(poisition);
                                    notifyItemRemoved(poisition);
                                    deleteTodo.deleteFromRealm();
                                }
                            }
                        });
                    } catch (ArrayIndexOutOfBoundsException e) {
                        e.printStackTrace();
                    }

                }
            });

        }
    }

    @Override
    public void onBindViewHolder(@NonNull CustomCalendarAdapter.Holder holder, final int position) {
        Calendar calendar = Calendar.getInstance();
        String startEndTime = "";

        calendar.setTimeInMillis(list.get(position).start);
        startEndTime = startEndTime + String.valueOf(calendar.get(Calendar.MONTH) + 1) + String.valueOf(calendar.get(Calendar.DATE));

        calendar.setTimeInMillis(list.get(position).end);
        startEndTime = startEndTime + String.valueOf(calendar.get(Calendar.MONTH) + 1) + String.valueOf(calendar.get(Calendar.DATE));

        holder.time.setText(startEndTime);

        GradientDrawable bgShape = (GradientDrawable) holder.iv_color_ball.getBackground();
        bgShape.setColor(list.get(position).color);

        holder.todo_content.setText(list.get(position).title);

        holder.itemView.setTag(position);

        holder.todo_done.setChecked(list.get(position).done);
        if (list.get(position).done == true) {
            holder.todo_content.setTextColor(Color.GRAY);
            holder.todo_content.setTypeface(null, Typeface.ITALIC);
            holder.todo_content.setPaintFlags(holder.todo_content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.todo_content.setTextColor(Color.BLACK);
            holder.todo_content.setTypeface(null, Typeface.NORMAL);
            holder.todo_content.setPaintFlags(0);
        }

        final CustomCalendarAdapter.Holder orgHolder = holder;
        holder.todo_done.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, final boolean b) {
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        Todo todo = realm.where(Todo.class).equalTo("ID", list.get(position).ID).equalTo("title", list.get(position).title).findFirst();
                        if (b == true) {
                            todo.done = true;
                        } else {
                            todo.done = false;
                        }
                    }
                });

                if (b == true) {
                    orgHolder.todo_content.setTextColor(Color.GRAY);
                    orgHolder.todo_content.setTypeface(null, Typeface.ITALIC);
                    orgHolder.todo_content.setPaintFlags(orgHolder.todo_content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    orgHolder.todo_content.setTextColor(Color.BLACK);
                    orgHolder.todo_content.setTypeface(null, Typeface.NORMAL);
                    orgHolder.todo_content.setPaintFlags(0);
                }
            }
        });
    }
}

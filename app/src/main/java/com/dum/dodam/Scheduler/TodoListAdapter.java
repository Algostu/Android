package com.dum.dodam.Scheduler;

import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.GradientDrawable;
import android.util.Log;
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

import io.realm.Realm;

public class TodoListAdapter extends RecyclerView.Adapter<TodoListAdapter.Holder> {
    private ArrayList<Todo> list = new ArrayList<>();
    private Realm realm;

    public TodoListAdapter(ArrayList<Todo> list, Realm realm) {
        this.list = list;
        this.realm = realm;
    }

    @NonNull
    @Override
    public TodoListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.work_todo, parent, false);
        TodoListAdapter.Holder holder = new TodoListAdapter.Holder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        final protected TextView todo_content;
        final protected ImageView ic_remove;
        final protected CheckBox todo_done;
        final protected ImageView iv_color_ball;

        public Holder(View view) {
            super(view);
            this.todo_content = (TextView) view.findViewById(R.id.todo_content);
            this.ic_remove = (ImageView) view.findViewById(R.id.ic_remove);
            this.todo_done = (CheckBox) view.findViewById(R.id.todo_done);
            this.iv_color_ball = view.findViewById(R.id.iv_color_ball);

            try {
                ic_remove.setOnClickListener(new OnSingleClickListener() {
                    @Override
                    public void onSingleClick(View v) {
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
            } catch (ArrayIndexOutOfBoundsException e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void onBindViewHolder(@NonNull TodoListAdapter.Holder holder, final int position) {
        holder.todo_content.setText(list.get(position).title);
        holder.todo_done.setChecked(list.get(position).done);
        GradientDrawable bgShape = (GradientDrawable) holder.iv_color_ball.getBackground();
        bgShape.setColor(list.get(position).color);

        if (list.get(position).done == true) {
            holder.todo_content.setTextColor(Color.GRAY);
//            holder.todo_content.setTypeface(null, Typeface.ITALIC);
            holder.todo_content.setPaintFlags(holder.todo_content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        } else {
            holder.todo_content.setTextColor(Color.BLACK);
//            holder.todo_content.setTypeface(null, Typeface.NORMAL);
            if ((holder.todo_content.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0){
                holder.todo_content.setPaintFlags( holder.todo_content.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
            }
        }

        if (list.get(position).visible) {
            Log.d("TodoListAdapter", "here we go 1");
            Log.d("TodoListAdapter", "color list: " + list.get(position).color);
            holder.ic_remove.setVisibility(View.VISIBLE);
            holder.iv_color_ball.setVisibility(View.GONE);
            Log.d("TodoListAdapter", "done");
        } else {
            Log.d("TodoListAdapter", "here we go 2");
            Log.d("TodoListAdapter", "color list: " + list.get(position).color);
            holder.ic_remove.setVisibility(View.GONE);
            holder.iv_color_ball.setVisibility(View.VISIBLE);
            Log.d("TodoListAdapter", "done");
        }
        final TodoListAdapter.Holder orgHolder = holder;
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
//                    orgHolder.todo_content.setTypeface(null, Typeface.ITALIC);
                    orgHolder.todo_content.setPaintFlags(orgHolder.todo_content.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                } else {
                    orgHolder.todo_content.setTextColor(Color.BLACK);
//                    orgHolder.todo_content.setTypeface(null, Typeface.NORMAL);
                    if ((orgHolder.todo_content.getPaintFlags() & Paint.STRIKE_THRU_TEXT_FLAG) > 0){
                        orgHolder.todo_content.setPaintFlags( orgHolder.todo_content.getPaintFlags() & (~ Paint.STRIKE_THRU_TEXT_FLAG));
                    }
                }
            }
        });
    }
}
package com.example.myapplication;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class MyAdapter extends RecyclerView.Adapter<MyAdapter.Holder> {
    private ArrayList<Article> list = new ArrayList<>();

    public MyAdapter(ArrayList<Article> list) {
        this.list = list;
    }

    @NonNull
    @Override
    public MyAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.community_list, parent, false);
        Holder holder = new Holder(view);

        return holder;
    }

    https://duckssi.tistory.com/12
    https://hyesunzzang.tistory.com/28
    https://developer.android.com/guide/topics/ui/layout/recyclerview?hl=ko#java

    @Override
    public void onBindViewHolder(@NonNull MyAdapter.Holder holder, int position) {
        holder.title.setText(list.get(position).title);
        holder.title.setText(list.get(position).title);
        holder.title.setText(list.get(position).title);
        holder.title.setText(list.get(position).title);
        holder.title.setText(list.get(position).title);
        holder.title.setText(list.get(position).title);
    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class Holder extends RecyclerView.ViewHolder {
        protected TextView title;
        protected TextView content;
        protected TextView writer;
        protected TextView time;
        protected TextView reply;
        protected TextView heart;

        public Holder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title);
            this.content = (TextView) view.findViewById(R.id.content);
            this.writer = (TextView) view.findViewById(R.id.writer);
            this.time = (TextView) view.findViewById(R.id.time);
            this.reply = (TextView) view.findViewById(R.id.reply);
            this.heart = (TextView) view.findViewById(R.id.heart);
        }
    }


}
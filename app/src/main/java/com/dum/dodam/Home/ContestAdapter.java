package com.dum.dodam.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dum.dodam.Home.dataframe.ContestFrame;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.R;

import java.util.ArrayList;

public class ContestAdapter extends RecyclerView.Adapter<ContestAdapter.Holder> {
    private ArrayList<ContestFrame> list = new ArrayList<ContestFrame>();
    private Context context;
    private OnListItemSelectedInterface mListener;
    private UserJson user;

    public ContestAdapter(Context context, ArrayList<ContestFrame> list, OnListItemSelectedInterface listener, UserJson user) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    public interface OnListItemSelectedInterface {
        void onContestItemSelected(View v, int position);
    }

    @NonNull
    @Override
    public ContestAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_contest_card, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull ContestAdapter.Holder holder, final int position) {
        holder.contest_name.setText(list.get(position).title);
        Glide.with(context).load(list.get(position).imageUrl).into(holder.image_view);

        holder.content = list.get(position).content;
        holder.contestID = String.valueOf(list.get(position).contestID);
        holder.date = list.get(position).start + " - " + list.get(position).end;
        holder.prize = String.valueOf(list.get(position).firstPrize);
        holder.sponsor = String.valueOf(list.get(position).sponsor);
        holder.imageUrl = list.get(position).imageUrl;
        holder.firstPrize = list.get(position).firstPrize;
        holder.homePage = list.get(position).homePage;
        holder.start = list.get(position).start;
        holder.end = list.get(position).end;
        holder.area = list.get(position).area;

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView contest_name;
        protected ImageView image_view;

        protected String contestID;
        protected String content;
        protected String date;
        protected String prize;
        protected String sponsor;

        protected String firstPrize;
        protected String homePage;
        protected String imageUrl;
        protected String start;
        protected String end;
        protected String area;

        public Holder(View view) {
            super(view);
            this.contest_name = (TextView) view.findViewById(R.id.contest_name);
            this.image_view = (ImageView) view.findViewById(R.id.contest_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onContestItemSelected(v, getAdapterPosition());
                }
            });
        }
    }

}
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
import com.dum.dodam.Home.dataframe.HotArticleFrame;
import com.dum.dodam.Home.dataframe.MyCommunityFrame2;
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
        this.user = user;
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
        holder.period.setText(list.get(position).period);
        holder.contest_name.setText(list.get(position).contest_name);
        Glide.with(context).load(list.get(position).imageUrl).into(holder.image_view);

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView period;
        protected TextView contest_name;
        protected ImageView image_view;

        public Holder(View view) {
            super(view);
            this.period = (TextView) view.findViewById(R.id.period);
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
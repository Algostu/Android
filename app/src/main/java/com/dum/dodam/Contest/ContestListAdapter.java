package com.dum.dodam.Contest;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.dum.dodam.R;
import com.dum.dodam.Contest.dataframe.ContestFrame;

import java.util.ArrayList;

public class ContestListAdapter extends RecyclerView.Adapter<ContestListAdapter.Holder> {
    private ArrayList<ContestFrame> list = new ArrayList<ContestFrame>();
    private OnListItemSelectedInterface mListener;
    private Context context;

    public ContestListAdapter(Context context, ArrayList<ContestFrame> list, OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    @NonNull
    @Override
    public ContestListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contest_card, parent, false);
        ContestListAdapter.Holder holder = new ContestListAdapter.Holder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView contestID;
        protected TextView title;
        protected TextView content;
        protected ImageView imageView;
        protected TextView date;
        protected TextView prize;
        protected TextView sponsor;

        protected String firstPrize;
        protected String homePage;
        protected String imageUrl;
        protected String start;
        protected String end;
        protected String area;


        public Holder(View view) {
            super(view);
            this.contestID = (TextView) view.findViewById(R.id.contestID);
            this.title = (TextView) view.findViewById(R.id.title);
            this.content = (TextView) view.findViewById(R.id.content);
            this.date = (TextView) view.findViewById(R.id.date);
            this.prize = (TextView) view.findViewById(R.id.prize);
            this.sponsor = (TextView) view.findViewById(R.id.host);
            this.imageView = (ImageView) view.findViewById(R.id.image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, getAdapterPosition());
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull ContestListAdapter.Holder holder, final int position) {
        holder.title.setText(list.get(position).title);
        holder.content.setText(list.get(position).content);
        holder.contestID.setText(String.valueOf(list.get(position).contestID));
        holder.date.setText(list.get(position).start + " - " + list.get(position).end);
        holder.prize.setText(String.valueOf(list.get(position).firstPrize));
        holder.sponsor.setText(String.valueOf(list.get(position).sponsor));
        holder.imageUrl = list.get(position).imageUrl;
        Glide.with(context).load(holder.imageUrl).into(holder.imageView);
        holder.firstPrize = list.get(position).firstPrize;
        holder.homePage = list.get(position).homePage;
        holder.start = list.get(position).start;
        holder.end = list.get(position).end;
        holder.area = list.get(position).area;

        holder.itemView.setTag(position);
    }
}
package com.dum.dodam.Univ.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.R;
import com.dum.dodam.Univ.dataframe.MajorFrame;
import com.dum.dodam.Univ.dataframe.UnivFrame;

import java.util.ArrayList;

public class RankingRecyclerAdapter extends RecyclerView.Adapter<RankingRecyclerAdapter.Holder> {
    private ArrayList<UnivFrame> univList = new ArrayList<>();
    private ArrayList<MajorFrame> majorList = new ArrayList<>();
    private Context context;
    private OnListItemSelectedInterface mListener;
    private int type;

    public RankingRecyclerAdapter(Context context, ArrayList<UnivFrame> univList, ArrayList<MajorFrame> majorList, int type, OnListItemSelectedInterface listener) {
        this.context = context;
        this.univList = univList;
        this.majorList = majorList;
        this.mListener = listener;
        this.type = type;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.univ_ranking_page_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        if (type == 1) {
            return (null != univList ? univList.size() : 0);
        } else {
            return (null != majorList ? majorList.size() : 0);
        }
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView tv_rank;
        protected TextView tv_title;
        protected ImageView iv_logo;

        public Holder(View view) {
            super(view);
            this.tv_rank = view.findViewById(R.id.tv_rank);
            this.tv_title = view.findViewById(R.id.tv_title);
            this.iv_logo = view.findViewById(R.id.iv_logo);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, getAbsoluteAdapterPosition());
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RankingRecyclerAdapter.Holder holder, final int position) {

        String rank;
        String title;
        String logoName;

        if(type==1){
            rank = univList.get(position).
        }

        holder.tv_rank.setText(rank);
        holder.tv_title.setText(title);

        //        int resId = context.getResources().getIdentifier(logoName, "drawable", context.getPackageName());
//        logo.setBackgroundResource();


        holder.itemView.setTag(position);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }
}
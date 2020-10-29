package com.dum.dodam.Univ;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.R;
import com.dum.dodam.Univ.dataframe.LiveShowFrame;

import java.util.ArrayList;

public class LiveShowAdapter extends RecyclerView.Adapter<LiveShowAdapter.Holder> {
    private ArrayList<LiveShowFrame> list = new ArrayList<LiveShowFrame>();
    private Context context;
    private LiveShowAdapter.OnListItemSelectedInterface mListener;

    public LiveShowAdapter(Context context, ArrayList<LiveShowFrame> list, LiveShowAdapter.OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    @NonNull
    @Override
    public LiveShowAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collage_liveshow_card, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView school;
        protected TextView major;
        protected TextView content1;
        protected TextView content2;
        protected TextView content3;
        protected TextView time;
        protected TextView heart;
        protected ImageView heart_ic;

        public Holder(View view) {
            super(view);
            this.school = (TextView) view.findViewById(R.id.school);
            this.major = (TextView) view.findViewById(R.id.major);
            this.content1 = (TextView) view.findViewById(R.id.content1);
            this.content2 = (TextView) view.findViewById(R.id.content2);
            this.content3 = (TextView) view.findViewById(R.id.content3);
            this.time = (TextView) view.findViewById(R.id.time);
            this.heart = (TextView) view.findViewById(R.id.heart);
            this.heart_ic = (ImageView) view.findViewById(R.id.icon_heart);

            heart_ic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    mListener.onItemSelected(view, getAdapterPosition());
                }
            });
        }
    }

    @Override
    public void onBindViewHolder(@NonNull LiveShowAdapter.Holder holder, final int position) {
        holder.school.setText(list.get(position).school);
        holder.major.setText(list.get(position).major);
        holder.content1.setText(list.get(position).content1);
        holder.content2.setText(list.get(position).content2);
        holder.content3.setText(list.get(position).content3);
        holder.time.setText(list.get(position).time);
        holder.heart.setText(list.get(position).heart);

        holder.itemView.setTag(position);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }
}
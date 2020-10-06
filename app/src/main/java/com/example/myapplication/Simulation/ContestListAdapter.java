package com.example.myapplication.Simulation;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.R;
import com.example.myapplication.Simulation.dataframe.ContestListFrame;

import java.io.BufferedInputStream;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;

public class ContestListAdapter extends RecyclerView.Adapter<ContestListAdapter.Holder> {
    private ArrayList<ContestListFrame> list = new ArrayList<ContestListFrame>();
    private OnListItemSelectedInterface mListener;

    public ContestListAdapter(ArrayList<ContestListFrame> list, OnListItemSelectedInterface listener) {
        this.list = list;
        this.mListener = listener;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    @NonNull
    @Override
    public ContestListAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.simulation_contest_card, parent, false);
        ContestListAdapter.Holder holder = new ContestListAdapter.Holder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView contestID;
        protected ImageView image;
        protected TextView title;
        protected TextView date;
        protected TextView prize;
        protected TextView content;
        protected TextView host;


        public Holder(View view) {
            super(view);
            this.contestID = (TextView) view.findViewById(R.id.contestID);
            this.title = (TextView) view.findViewById(R.id.title);
            this.content = (TextView) view.findViewById(R.id.content);
            this.date = (TextView) view.findViewById(R.id.date);
            this.prize = (TextView) view.findViewById(R.id.prize);
            this.host = (TextView) view.findViewById(R.id.host);
            this.image = (ImageView) view.findViewById(R.id.image);

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
        holder.content.setText(list.get(position).contents);
        holder.contestID.setText(list.get(position).contestID);
        holder.date.setText(list.get(position).receptionDate);
        holder.prize.setText(String.valueOf(list.get(position).totalPrice));
        holder.host.setText(String.valueOf(list.get(position).supporter));
        try {
            URL url = new URL(list.get(position).pictureUrl);
            URLConnection conn = url.openConnection();
            conn.connect();
            BufferedInputStream bis = new BufferedInputStream(conn.getInputStream());
            Bitmap bm = BitmapFactory.decodeStream(bis);
            bis.close();
            holder.image.setImageBitmap(bm);
        } catch (Exception e) {
        }
        holder.itemView.setTag(position);
    }
}
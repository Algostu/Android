package com.dum.dodam.Univ;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Univ.dataframe.UnivNewsFrame;
import com.dum.dodam.Community.TIME_MAXIMUM;
import com.dum.dodam.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UnivNewsAdapter extends RecyclerView.Adapter<UnivNewsAdapter.Holder> {
    private ArrayList<UnivNewsFrame> list = new ArrayList<UnivNewsFrame>();
    private Context context;

    public UnivNewsAdapter(Context context, ArrayList<UnivNewsFrame> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public UnivNewsAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.collage_news_card, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView content;
        protected TextView time;

        public Holder(View view) {
            super(view);
            this.content = (TextView) view.findViewById(R.id.news_content);
            this.time = (TextView) view.findViewById(R.id.news_time);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull UnivNewsAdapter.Holder holder, final int position) {
        holder.content.setText(list.get(position).content);

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date wriDate;
            wriDate = format.parse(list.get(position).writtenTime);
            TIME_MAXIMUM timeDiff = new TIME_MAXIMUM();
            String diffStr = timeDiff.calculateTime(wriDate);
            holder.time.setText(diffStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.itemView.setTag(position);
    }
}
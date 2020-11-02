package com.dum.dodam.Univ;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Community.ArticleListAdapter;
import com.dum.dodam.Univ.dataframe.UnivArticleFrame;
import com.dum.dodam.Community.TIME_MAXIMUM;
import com.dum.dodam.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class UnivNewsAdapter extends RecyclerView.Adapter<UnivNewsAdapter.Holder> {
    private ArrayList<UnivArticleFrame> list = new ArrayList<UnivArticleFrame>();
    private Context context;
    private UnivNewsAdapter.OnListItemSelectedInterface mListener;

    public UnivNewsAdapter(Context context, ArrayList<UnivArticleFrame> list, UnivNewsAdapter.OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
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
        protected int articleID;

        public Holder(View view) {
            super(view);
            this.content = (TextView) view.findViewById(R.id.news_content);
            this.time = (TextView) view.findViewById(R.id.news_time);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onItemSelected(v, getAdapterPosition());
                }
            });
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

        holder.articleID = list.get(position).articleID;

        holder.itemView.setTag(position);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }
}
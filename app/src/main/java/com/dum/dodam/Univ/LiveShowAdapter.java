package com.dum.dodam.Univ;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.R;
import com.dum.dodam.Univ.dataframe.LiveShowFrame;

import java.util.ArrayList;

public class LiveShowAdapter extends RecyclerView.Adapter<LiveShowAdapter.Holder> {
    private ArrayList<LiveShowFrame> list = new ArrayList<LiveShowFrame>();
    private Context context;
    private LiveShowAdapter.OnListItemSelectedInterface mListener2;

    public LiveShowAdapter(Context context, ArrayList<LiveShowFrame> list, LiveShowAdapter.OnListItemSelectedInterface listener2) {
        this.context = context;
        this.list = list;
        this.mListener2 = listener2;
    }

    @NonNull
    @Override
    public LiveShowAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.univ_liveshow_card, parent, false);
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
//        protected TextView heart;
//        protected ImageView heart_ic;

        protected int univID;
        protected int liveShowID;
        protected int userID;

        public Holder(final View view) {
            super(view);
            this.school = (TextView) view.findViewById(R.id.school);
            this.major = (TextView) view.findViewById(R.id.major);
            this.content1 = (TextView) view.findViewById(R.id.content1);
            this.content2 = (TextView) view.findViewById(R.id.content2);
            this.content3 = (TextView) view.findViewById(R.id.content3);
            this.time = (TextView) view.findViewById(R.id.time);
//            this.heart = (TextView) view.findViewById(R.id.heart);
//            this.heart_ic = (ImageView) view.findViewById(R.id.icon_heart);

//            heart_ic.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    mListener.onItemSelected(view, getAdapterPosition());
//                }
//            });

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener2.onItemSelected2(view, getAdapterPosition());
                }
            });


        }
    }

    @Override
    public void onBindViewHolder(@NonNull LiveShowAdapter.Holder holder, final int position) {
        String[] contents = list.get(position).content.split("\n");

        holder.school.setText(list.get(position).univTitle);
        holder.major.setText(list.get(position).major);
        holder.content1.setText(contents[0].trim());
        String time = list.get(position).writtenTime;
        holder.time.setText(String.format("%s월%s일\n%s~", time.substring(5, 7), time.substring(8, 10), time.substring(11, 16)));
//        holder.heart.setText(list.get(position).heart);

        holder.univID = list.get(position).univID;
        holder.liveShowID = list.get(position).liveShowID;
        holder.userID = list.get(position).userID;

        holder.itemView.setTag(position);
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected2(View v, int position);
    }
}
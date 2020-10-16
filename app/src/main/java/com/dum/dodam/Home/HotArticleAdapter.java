package com.dum.dodam.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Home.dataframe.HotArticleFrame;
import com.dum.dodam.Home.dataframe.MyCommunityFrame2;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.R;

import java.util.ArrayList;

public class HotArticleAdapter extends RecyclerView.Adapter<HotArticleAdapter.Holder> {
    private ArrayList<HotArticleFrame> list = new ArrayList<HotArticleFrame>();
    private Context context;
    private OnListItemSelectedInterface mListener;
    private UserJson user;

    public HotArticleAdapter(Context context, ArrayList<HotArticleFrame> list, OnListItemSelectedInterface listener, UserJson user) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
        this.user = user;
    }

    public interface OnListItemSelectedInterface {
        void onArticleItemSelected(View v, int position);
    }

    @NonNull
    @Override
    public HotArticleAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_hot_article_card, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull HotArticleAdapter.Holder holder, final int position) {
        String community_name = new String();
        String type = "";
        ArrayList<MyCommunityFrame2> com = new ArrayList<MyCommunityFrame2>();
        if (0 == list.get(position).communityType) {
            type = "전국 ";
            com = user.comAll;
        } else if (1 == list.get(position).communityType) {
            type = "지역 ";
            com = user.comRegion;
        } else if (2 == list.get(position).communityType) {
            type = "교내 ";
            com = user.comSchool;
        }
        for (MyCommunityFrame2 frame : com) {
            if (frame.communityID == list.get(position).communityID)
                community_name = type + frame.title;
        }

        holder.communityID = list.get(position).communityID;
        holder.communityType = list.get(position).communityType;
        holder.community.setText(community_name);
        holder.articleID.setText(String.valueOf(list.get(position).articleID));
        holder.title.setText(list.get(position).title);
        holder.content.setText(list.get(position).content);
        holder.reply.setText(String.valueOf(list.get(position).reply));
        holder.heart.setText(String.valueOf(list.get(position).heart));
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView community;
        protected TextView articleID;
        protected TextView title;
        protected TextView content;
        protected TextView reply;
        protected TextView heart;
        protected int communityType;
        protected int communityID;

        public Holder(View view) {
            super(view);
            this.community = (TextView) view.findViewById(R.id.community);
            this.articleID = (TextView) view.findViewById(R.id.articleID);
            this.title = (TextView) view.findViewById(R.id.title);
            this.content = (TextView) view.findViewById(R.id.content);
            this.reply = (TextView) view.findViewById(R.id.reply);
            this.heart = (TextView) view.findViewById(R.id.heart);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onArticleItemSelected(v, getAdapterPosition());
                }
            });
        }
    }

}
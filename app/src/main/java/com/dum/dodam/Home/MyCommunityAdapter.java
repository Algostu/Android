package com.dum.dodam.Home;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Home.dataframe.MyCommunityFrame;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.R;

import java.util.ArrayList;

public class MyCommunityAdapter extends RecyclerView.Adapter<MyCommunityAdapter.Holder> {
    private ArrayList<MyCommunityFrame> list = new ArrayList<MyCommunityFrame>();
    private Context context;
    private OnListItemSelectedInterface mListener;
    public UserJson user;

    public MyCommunityAdapter(Context context, ArrayList<MyCommunityFrame> list, OnListItemSelectedInterface listener, UserJson user) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
        this.user = user;
    }

    public interface OnListItemSelectedInterface {
        void onCommunityItemSelected(View v, int position);
    }

    @NonNull
    @Override
    public MyCommunityAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.home_my_community_card, parent, false);
        Holder holder = new Holder(view);

        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyCommunityAdapter.Holder holder, final int position) {
        String community_name = new String();
        if (2 == list.get(position).communityType) community_name = "교내 " + user.comSchool.get(0).title;
        else if (2 == list.get(position).communityType) community_name = "교내 " +  user.comSchool.get(1).title;
        else if (1 == list.get(position).communityType) community_name = "동네 " +  user.comRegion.get(0).title;
        else if (1 == list.get(position).communityType) community_name = "동네 " +  user.comRegion.get(1).title;
        else if (1 == list.get(position).communityType) community_name = "동네 " +  user.comRegion.get(2).title;
        else if (0 == list.get(position).communityType) community_name =  user.comAll.get(0).title;
        else if (0 == list.get(position).communityType) community_name =  user.comAll.get(1).title;
        else if (0 == list.get(position).communityType) community_name =  user.comAll.get(2).title;

        holder.communityType = list.get(position).communityType;
        holder.communityID = list.get(position).communityID;
        holder.community_name.setText(community_name);
        holder.title.setText(list.get(position).title);
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected TextView community_name;
        protected TextView title;
        protected int communityID;
        protected int communityType;

        public Holder(View view) {
            super(view);
            this.community_name = (TextView) view.findViewById(R.id.communityID);
            this.title = (TextView) view.findViewById(R.id.title);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onCommunityItemSelected(v, getAdapterPosition());
                }
            });
        }
    }

}
package com.dum.dodam.Univ.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.MultiTransformation;
import com.bumptech.glide.load.resource.bitmap.CenterCrop;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.bumptech.glide.request.RequestOptions;
import com.dum.dodam.Community.dataframe.ImageData;
import com.dum.dodam.R;

import java.util.ArrayList;

public class InstagramAdapter extends RecyclerView.Adapter<InstagramAdapter.Holder> {
    private ArrayList<ImageData> list = new ArrayList<ImageData>();
    private Context context;
    private OnListItemSelectedInterface mListener;

    public InstagramAdapter(Context context, ArrayList<ImageData> list, OnListItemSelectedInterface listener) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
    }

    public interface OnListItemSelectedInterface {
        void onFeedSelected(View v, int position);
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.univ_instagram_card, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {
//        holder.contest_name.setText("");
        MultiTransformation multiOption = new MultiTransformation(new CenterCrop(), new RoundedCorners(20));
        Glide.with(context).load(list.get(position).media_url).apply(RequestOptions.bitmapTransform(multiOption)).into(holder.image_view);
        holder.feedID = list.get(position).id;
        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder {
//        protected TextView contest_name;
        protected ImageView image_view;

        protected String feedID;

        public Holder(View view) {
            super(view);
//            this.contest_name = (TextView) view.findViewById(R.id.contest_name);
            this.image_view = (ImageView) view.findViewById(R.id.contest_image);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mListener.onFeedSelected(v, getAdapterPosition());
                }
            });
        }
    }

}
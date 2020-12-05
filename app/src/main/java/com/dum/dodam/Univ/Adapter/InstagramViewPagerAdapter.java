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

public class InstagramViewPagerAdapter extends RecyclerView.Adapter<InstagramViewPagerAdapter.Holder> {
    private static final String TAG = InstagramViewPagerAdapter.class.getName();
    private ArrayList<ImageData> list = new ArrayList<ImageData>();
    private Context context;



    public InstagramViewPagerAdapter(Context context, ArrayList<ImageData> list) {
        this.context = context;
        this.list = list;
    }

    @NonNull
    @Override
    public Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.univ_instagram_viewpager_item, parent, false);
        Holder holder = new Holder(view);
        return holder;
    }

    public class Holder extends RecyclerView.ViewHolder {
        protected ImageView photo;

        public Holder(View view) {
            super(view);
            this.photo = (ImageView) view.findViewById(R.id.photo);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull Holder holder, final int position) {

        MultiTransformation multiOption = new MultiTransformation(new CenterCrop(), new RoundedCorners(20));
        Glide.with(context).load(list.get(position).media_url).apply(RequestOptions.bitmapTransform(multiOption)).into(holder.photo);

        holder.itemView.setTag(position);
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }



}
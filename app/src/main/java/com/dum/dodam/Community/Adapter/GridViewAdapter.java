package com.dum.dodam.Community.Adapter;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.dum.dodam.Community.dataframe.ImageData;
import com.dum.dodam.R;

import java.util.ArrayList;

public class GridViewAdapter extends BaseAdapter {
    Context context;
    int layout;
    public ArrayList<ImageData> img;
    LayoutInflater inf;

    public GridViewAdapter(Context context, int layout, ArrayList<ImageData> img) {
        this.context = context;
        this.layout = layout;
        this.img = img;
        inf = (LayoutInflater) context.getSystemService
                (Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return img.size();
    }

    @Override
    public Object getItem(int position) {
        return img.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    public void refresh(ArrayList<ImageData> items)
    {
        this.img = items;
        notifyDataSetChanged();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (convertView==null)
            convertView = inf.inflate(layout, null);
        ImageView iv = (ImageView)convertView.findViewById(R.id.img_view);
        Log.d("GridViewAdapter", "media_url " + img.get(position).media_url);
        Glide.with(context).load(img.get(position).media_url).into(iv);

//        convertView.setLayoutParams(new GridView.LayoutParams(GridView.AUTO_FIT, 500));

        return convertView;
    }

}

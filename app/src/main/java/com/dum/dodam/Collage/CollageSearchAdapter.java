package com.dum.dodam.Collage;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.dum.dodam.Collage.dataframe.CollageFrame;
import com.dum.dodam.R;

import java.util.ArrayList;

public class CollageSearchAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<CollageFrame> list;
    private LayoutInflater inflate;
    private CollageSearchAdapter.ViewHolder viewHolder;

    public CollageSearchAdapter(Context context, ArrayList<CollageFrame> list) {
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }


    @Override
    public View getView(int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.collage_search_card, null);
            viewHolder = new CollageSearchAdapter.ViewHolder();
            viewHolder.collage_name = (TextView) convertView.findViewById(R.id.collage_name);
            viewHolder.collage_logo = (ImageView) convertView.findViewById(R.id.collage_logo);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (CollageSearchAdapter.ViewHolder) convertView.getTag();
        }

        // 리스트에 있는 데이터를 리스트뷰 셀에 뿌린다.
        viewHolder.collage_name.setText(list.get(position).univName);
//        viewHolder.collage_logo.setImageDrawable();
        return convertView;
    }

    class ViewHolder {
        public TextView collage_name;
        public ImageView collage_logo;
    }

}

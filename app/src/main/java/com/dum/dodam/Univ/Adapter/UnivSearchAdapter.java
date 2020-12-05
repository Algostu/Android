package com.dum.dodam.Univ.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dum.dodam.R;
import com.dum.dodam.Univ.dataframe.UnivFrame;
import com.dum.dodam.Utils.TextUtils;
import com.google.gson.Gson;

import java.util.ArrayList;

public class UnivSearchAdapter extends BaseAdapter {

    private Context context;
    private ArrayList<UnivFrame> list;
    private LayoutInflater inflate;
    private UnivSearchAdapter.ViewHolder viewHolder;
    private EditText tvSearchText;

    public UnivSearchAdapter(Context context, ArrayList<UnivFrame> list, EditText tvSearchText) {
        this.list = list;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
        this.tvSearchText = tvSearchText;
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
    public View getView(final int position, View convertView, ViewGroup viewGroup) {
        if (convertView == null) {
            convertView = inflate.inflate(R.layout.univ_search_card, null);
            viewHolder = new UnivSearchAdapter.ViewHolder();
            viewHolder.collage_name = (TextView) convertView.findViewById(R.id.collage_name);
            viewHolder.collage_logo = (ImageView) convertView.findViewById(R.id.collage_logo);
            viewHolder.remove_icon = (ImageView) convertView.findViewById(R.id.ic_remove);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UnivSearchAdapter.ViewHolder) convertView.getTag();
        }

        // 검색한 쿼리와 동일한 부분을 강조한다.
        String query = tvSearchText.getText().toString();
        viewHolder.collage_name.setText("");
        int start = list.get(position).univName.indexOf(query);
        if (start != -1){
            TextUtils.setColorInPartitial(list.get(position).univName, start, start+query.length(), "#fbdd56", viewHolder.collage_name);
        } else {
            viewHolder.collage_name.setText(list.get(position).univName);
        }

        // logo
        viewHolder.collage_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_school));

        // remove Icon
        if (query.length() == 0){
            viewHolder.remove_icon.setVisibility(View.VISIBLE);
            viewHolder.remove_icon.setClickable(true);
            viewHolder.remove_icon.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    list.remove(position);
                    notifyDataSetInvalidated();
                    removeHistory();
                }
            });
        } else {
            viewHolder.remove_icon.setVisibility(View.GONE);
        }

//        viewHolder.collage_logo.setImageDrawable();
        return convertView;
    }

    class ViewHolder {
        public TextView collage_name;
        public ImageView collage_logo;
        public ImageView remove_icon;
    }

    public void removeHistory(){
        // 저장되어있는 알람 중 오래된것들 삭제
        SharedPreferences sharedPref = context.getSharedPreferences(
                "auto", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("univSearchHistory", gson.toJson(list));
        editor.commit();
    }
}

package com.dum.dodam.Univ.Adapter;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.AssetManager;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.dum.dodam.R;
import com.dum.dodam.Univ.dataframe.MajorFrame;
import com.dum.dodam.Univ.dataframe.UnivFrame;
import com.dum.dodam.Utils.TextUtils;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Arrays;

public class UnivSearchAdapter extends BaseAdapter {

    // debug
    public final String TAG = "UnivSearchAdapter";

    private Context context;
    private ArrayList<UnivFrame> list;
    private ArrayList<MajorFrame> list2;
    private ArrayList<String> logoNameList;
    private LayoutInflater inflate;
    private UnivSearchAdapter.ViewHolder viewHolder;
    private EditText tvSearchText;

    public UnivSearchAdapter(Context context, ArrayList<UnivFrame> list, ArrayList<MajorFrame> list2, EditText tvSearchText) {
        this.list = list;
        this.list2 = list2;
        this.context = context;
        this.inflate = LayoutInflater.from(context);
        this.tvSearchText = tvSearchText;
        try {
            AssetManager assetMgr = context.getAssets();
            logoNameList = new ArrayList<>(Arrays.asList(assetMgr.list("logo/")));
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

    @Override
    public int getCount() {
        if (list.size() > 0)
            return list.size();
        else
            return list2.size();
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
            viewHolder.region_name = (TextView) convertView.findViewById(R.id.region_name);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (UnivSearchAdapter.ViewHolder) convertView.getTag();
        }
        // 검색한 문자열
        String query = tvSearchText.getText().toString();
        Log.d("searchAdapter", "Here we go");
        // 학교 검색일 경우
        if (list.size() > 0) {
            Log.d("searchAdapter", "To list 1");
            // logo
            viewHolder.collage_logo.setVisibility(View.VISIBLE);
            viewHolder.collage_logo.setImageDrawable(context.getResources().getDrawable(R.drawable.ic_university));
            String univEngName = list.get(position).engname;
            if (univEngName != null) {
                for (String filename : logoNameList) {
                    if (filename.toString().split("\\.")[0].equals(univEngName)) {
                        AssetManager assetMgr = context.getAssets();
                        try {
                            InputStream is = assetMgr.open("logo/" + filename);
                            viewHolder.collage_logo.setImageDrawable(Drawable.createFromStream(is, null));
                            is.close();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }

                    }
                }
            }
            // 검색한 쿼리와 동일한 부분을 강조한다.
            viewHolder.collage_name.setText("");
            int start = list.get(position).univName.indexOf(query);
            if (start != -1) {
                TextUtils.setColorInPartitial(list.get(position).univName, start, start + query.length(), "#03DAC5", viewHolder.collage_name);
            } else {
                viewHolder.collage_name.setText(list.get(position).univName);
            }
        }
        // 학과 검색일 경우
        else {
            Log.d("searchAdapter", "To list 2");
            //logo
            viewHolder.collage_logo.setVisibility(View.GONE);
            // 검색한 쿼리와 동일한 부분을 강조한다.
            viewHolder.collage_name.setText("");
            int start = list2.get(position).mClass.indexOf(query) + 7;
            if (start != -1) {
                TextUtils.setColorInPartitial("       " + list2.get(position).mClass, start, start + query.length(), "#03DAC5", viewHolder.collage_name);
            } else {
                viewHolder.collage_name.setText(list2.get(position).mClass);
            }
        }
        // remove Icon
        if (query.length() == 0) {
            viewHolder.region_name.setVisibility(View.GONE);
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
            viewHolder.region_name.setVisibility(View.VISIBLE);
            if (list.size() > 0) {
//                viewHolder.region_name.setText(list.get(position).subRegion);
                viewHolder.region_name.setText("관련 대학");
            } else {
                String subText = list2.get(position).avg_salary;
                if (subText != null) {
                    viewHolder.region_name.setText(subText.split("\\.")[0] + "만원");
                } else {
                    viewHolder.region_name.setText("정보미제공");
                }

            }
        }

//        viewHolder.collage_logo.setImageDrawable();
        return convertView;
    }

    class ViewHolder {
        public TextView collage_name;
        public ImageView collage_logo;
        public ImageView remove_icon;
        public TextView region_name;
    }

    public void removeHistory() {
        // 저장되어있는 알람 중 오래된것들 삭제
        SharedPreferences sharedPref = context.getSharedPreferences(
                "auto", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("univSearchHistory", gson.toJson(list));
        editor.commit();
    }
}

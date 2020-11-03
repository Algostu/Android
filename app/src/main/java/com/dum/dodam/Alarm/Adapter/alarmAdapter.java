package com.dum.dodam.Alarm.Adapter;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Alarm.Data.AlarmData;
import com.dum.dodam.Community.TIME_MAXIMUM;
import com.dum.dodam.Home.dataframe.MyCommunityFrame2;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class alarmAdapter extends RecyclerView.Adapter<alarmAdapter.Holder> {
    private ArrayList<AlarmData> list = new ArrayList<>();
    private Context context;
    private alarmAdapter.OnListItemSelectedInterface mListener;
    private ArrayList<MyCommunityFrame2> comAll;
    private ArrayList<MyCommunityFrame2> comRegion;
    private ArrayList<MyCommunityFrame2> comSchool;

    public alarmAdapter(Context context, ArrayList<AlarmData> list, alarmAdapter.OnListItemSelectedInterface listener, UserJson user) {
        this.context = context;
        this.list = list;
        this.mListener = listener;
        this.comAll = user.comAll;
        this.comRegion = user.comRegion;
        this.comSchool = user.comSchool;
    }

    public interface OnListItemSelectedInterface {
        void onItemSelected(View v, int position);
    }

    @NonNull
    @Override
    public alarmAdapter.Holder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.alarm_tab_sub_item, parent, false);
        alarmAdapter.Holder holder = new alarmAdapter.Holder(view);

        return holder;
    }

    @Override
    public int getItemCount() {
        return (null != list ? list.size() : 0);
    }

    public class Holder extends RecyclerView.ViewHolder{
        protected TextView title;
        protected TextView content;
        protected TextView time;
        protected ConstraintLayout bg;

        public Holder(View view) {
            super(view);
            this.title = (TextView) view.findViewById(R.id.title);
            this.content = (TextView) view.findViewById(R.id.content);
            this.time = (TextView) view.findViewById(R.id.time);
            this.bg = (ConstraintLayout) view.findViewById(R.id.comment);

            view.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    bg.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    mListener.onItemSelected(v, getAdapterPosition());
                }
            });

            view.setOnLongClickListener(new View.OnLongClickListener(){
                @Override
                public boolean onLongClick(View v){
                    Log.d("hello", "long clicked");
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("삭제알림");
                    builder.setMessage("알림을 삭제 하시겠습니까?");
                    builder.setPositiveButton("확인",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    int position = getAdapterPosition();
                                    ((MainActivity)context).deleteAlarm(list, position);
                                    notifyItemRemoved(position);
                                }
                            });
                    builder.show();
                    return true;
                }
            });

        }

    }

    @Override
    public void onBindViewHolder(@NonNull alarmAdapter.Holder holder, final int position) {
        int communityType = list.get(position).type;
        int communityID = list.get(position).communityID;
        String content = list.get(position).content;

        String title = new String();
        String type = "";
        ArrayList<MyCommunityFrame2> com = new ArrayList<MyCommunityFrame2>();
        if (0 == communityType) {
            type = "전국-";
            com = comAll;
        } else if (1 == communityType) {
            type = "지역-";
            com = comRegion;
        } else if (2 == communityType) {
            type = "교내-";
            com = comSchool;
        }
        for (MyCommunityFrame2 frame : com) {
            if (frame.communityID == communityID)
                title = type + frame.title + "게시판";
        }

        holder.title.setText(title);
        int lastIndex = content.length() > 15 ? 15 : content.length();
        holder.content.setText(list.get(position).title + " : " + content.substring(0, lastIndex));
        holder.itemView.setTag(position);

//        배경색
        if(list.get(position).read == 0){
            holder.bg.setBackgroundColor(Color.parseColor("#FFFFFF"));
        } else {
            holder.bg.setBackgroundColor(Color.parseColor("#FCEDD4"));
        }

        try {
            SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
            Date wriDate;
            wriDate = format.parse(list.get(position).time);
            TIME_MAXIMUM timeDiff = new TIME_MAXIMUM();
            String diffStr = timeDiff.calculateTime(wriDate);
            holder.time.setText(diffStr);
        } catch (ParseException e) {
            e.printStackTrace();
        }
    }
}

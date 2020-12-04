package com.dum.dodam.Scheduler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.LocalDB.TimeTableDB;
import com.dum.dodam.LocalDB.TimeTableDay;
import com.dum.dodam.LocalDB.TimeTableModule;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.google.android.material.button.MaterialButton;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class TimeTableFullPopUp extends DialogFragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TimeTableFullAdapter adapter;
    private ArrayList<TimeTableDay> list = new ArrayList<>();
    private int date;

    private Realm realm;

    public TimeTableFullPopUp(int date) {
        this.date = date;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scheduler_time_table_popup, container, false);
        view.setClickable(true);

        RealmTimeTableInit();
        loadTimeTable();

        adapter = new TimeTableFullAdapter(list);
//
//        for (TimeTableDay timeTableDay : list) {
//            for (String string : timeTableDay.subject) {
//                Log.d("RHC", "onCreateView: " + string);
//            }
//        }
        TextView timetable = view.findViewById(R.id.timetable);
        UserJson user = ((MainActivity) getActivity()).user;
        timetable.setText(String.format("%s's 시간표", user.userName));

        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        MaterialButton btn_modify = view.findViewById(R.id.btn_modify);
        btn_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragmentPopup(new TimeTableModify());
            }
        });

        return view;
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View view = getActivity().getLayoutInflater().inflate(R.layout.scheduler_time_table_popup, null);
        builder.setView(view);

        return builder.create();
    }

    public void loadTimeTable() {
        list.clear();
        TimeTableDB timeTableDB = realm.where(TimeTableDB.class).findFirst();

        Calendar cal = Calendar.getInstance();
        SimpleDateFormat dateFmt = new SimpleDateFormat("dd");

        cal.set(Calendar.DATE, date);
        Log.d("RHC", "loadTimeTable: " + cal.getTime());
        cal.set(Calendar.DAY_OF_WEEK, Calendar.MONDAY);
        Log.d("RHC", "loadTimeTable: " + cal.getTime());
        int mondayDate = Integer.parseInt(dateFmt.format(cal.getTime()));
        cal.set(Calendar.DAY_OF_WEEK, Calendar.FRIDAY);
        Log.d("RHC", "loadTimeTable: " + cal.getTime());
        int fridayDate = Integer.parseInt(dateFmt.format(cal.getTime()));

        if (mondayDate > fridayDate) {
            int week = cal.get(Calendar.WEEK_OF_MONTH);
            if (week == 1) {
                mondayDate = 1;
            } else {
                fridayDate = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
            }
        }

        Log.d("RHC", String.format("%d %d", mondayDate, fridayDate));

        for (int i = mondayDate; i < fridayDate + 1; i++) {
            list.add(timeTableDB.days.get(i));
        }
    }

    private void RealmTimeTableInit() {
        RealmConfiguration config = new RealmConfiguration.Builder().name("TimeTableDB.realm").schemaVersion(1).modules(new TimeTableModule()).build();
        realm = Realm.getInstance(config);
    }
}

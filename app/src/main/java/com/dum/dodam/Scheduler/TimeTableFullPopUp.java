package com.dum.dodam.Scheduler;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.LocalDB.TimeTableDB;
import com.dum.dodam.LocalDB.TimeTableDay;
import com.dum.dodam.LocalDB.TimeTableModule;
import com.dum.dodam.R;

import java.util.ArrayList;

import io.realm.Realm;
import io.realm.RealmConfiguration;

public class TimeTableFullPopUp extends DialogFragment {

    private RecyclerView recyclerView;
    private RecyclerView.LayoutManager layoutManager;
    private TimeTableFullAdapter adapter;
    private ArrayList<TimeTableDay> list;

    private Realm realm;

    public static TimeTableFullPopUp getInstance() {
        TimeTableFullPopUp e = new TimeTableFullPopUp();
        return e;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scheduler_time_table_popup, container, false);
        view.setClickable(true);

        RealmTimeTableInit();
        loadTimeTable();

        adapter = new TimeTableFullAdapter(list);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_alarm);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

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
        list.remove(0);
    }

    private void RealmTimeTableInit() {
        RealmConfiguration config = new RealmConfiguration.Builder().name("TimeTableDB.realm").schemaVersion(1).modules(new TimeTableModule()).build();
        realm = Realm.getInstance(config);
    }
}

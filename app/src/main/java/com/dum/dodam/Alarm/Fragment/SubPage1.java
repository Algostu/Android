package com.dum.dodam.Alarm.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Alarm.Adapter.alarmAdapter;
import com.dum.dodam.Alarm.Data.AlarmData;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;

import java.util.ArrayList;

public class SubPage1 extends Fragment implements alarmAdapter.OnListItemSelectedInterface {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private UserJson user;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private ArrayList<AlarmData> list;
    private RecyclerView.LayoutManager layoutManager;

    public SubPage1() {
    }

    public static SubPage1 newInstance() {
        SubPage1 fragment = new SubPage1();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_tab_sub, container, false);

        view.setClickable(true);

        user = ((MainActivity) getActivity()).getUser();

        list = ((MainActivity) getActivity()).getAlarmList();
        Log.d("subPage debug", "list size : " + list.size());
        adapter = new alarmAdapter(getContext(), list, this, user);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_alarm);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        Log.d("subPage debug", "list size : " + list.size());
        list = ((MainActivity) getActivity()).getAlarmList();
        adapter.notifyDataSetChanged();
        Log.d("subPage debug", "list size changed : " + list.size());
        super.onResume();
    }

    @Override
    public void onItemSelected(View v, int position) {
        AlarmData alarmData = list.get(position);
        ArrayList<AlarmData> new_list = ((MainActivity)getActivity()).checkAlarm(position);
        Log.d("subPage debug", "list size : " + list.size());
        if(new_list!=null){
            list = new_list;
            adapter.notifyDataSetChanged();
            Log.d("subPage debug", "list size changed : " + list.size());
        }
    }
}
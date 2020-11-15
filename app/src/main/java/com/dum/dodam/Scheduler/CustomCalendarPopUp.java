package com.dum.dodam.Scheduler;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.R;
import com.dum.dodam.databinding.SchedulerCalendarPopupBinding;
import com.kizitonwose.calendarview.model.CalendarDay;

import java.util.ArrayList;

public class CustomCalendarPopUp extends Fragment implements CustomCalendarAdapter.OnListItemSelectedInterface {

    private SchedulerCalendarPopupBinding binding;

    private CustomCalendarAdapter adapter;

    private String date;
    private String dayOfWeek;
    private ArrayList<TodoData> list;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public CustomCalendarPopUp(CalendarDay day, ArrayList<TodoData> list) {
        this.list = list;
        this.date = day.getDate().toString();
        this.dayOfWeek = day.getDate().getDayOfWeek().toString();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.scheduler_calendar_popup, container, false);

        adapter = new CustomCalendarAdapter(getContext(), list, this);

        binding.dateText.setText(date);
        binding.weekOfDateText.setText(dayOfWeek);

        RecyclerView recyclerView = binding.rvTodo;
        recyclerView.setHasFixedSize(true);
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        return binding.getRoot();
    }

    @Override
    public void onItemSelected(View v, int position) {
    }
}

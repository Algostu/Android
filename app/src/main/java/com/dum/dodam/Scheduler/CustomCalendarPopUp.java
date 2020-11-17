package com.dum.dodam.Scheduler;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.LocalDB.Todo;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.databinding.SchedulerCalendarPopupBinding;
import com.kizitonwose.calendarview.model.CalendarDay;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

public class CustomCalendarPopUp extends Fragment implements CustomCalendarAdapter.OnListItemSelectedInterface {

    private SchedulerCalendarPopupBinding binding;

    private CustomCalendarAdapter adapter;

    private String date;
    private String dayOfWeek;
    private ArrayList<Todo> list;

    public ImageView ic_trashcan;
    public ImageView ic_go_to;

    @RequiresApi(api = Build.VERSION_CODES.O)
    public CustomCalendarPopUp(CalendarDay day, ArrayList<Todo> list) {
        this.list = list;
        this.date = day.getDate().toString();
        this.dayOfWeek = day.getDate().getDayOfWeek().toString();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.scheduler_calendar_popup, container, false);
        binding.getRoot().setClickable(true);

        adapter = new CustomCalendarAdapter(getContext(), list, this);

        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        final Calendar calendar = Calendar.getInstance();
        Date dateData;
        try {
            dateData = format.parse(date);
            calendar.setTime(dateData);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        binding.dateText.setText(date);
        binding.weekOfDateText.setText(dayOfWeek);

        ic_trashcan = binding.icRemove;

        ic_trashcan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Todo todo : list) {
                    todo.visible ^= true;
                }
                adapter.notifyDataSetChanged();
            }
        });

        ic_go_to = binding.icGoTo;
        final Activity host = (Activity) binding.getRoot().getContext();
        ic_go_to.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) view.getContext()).onBackPressed();
//                ((MainActivity) view.getContext()).replaceFragmentFull(Scheduler.newInstance(calendar.get(Calendar.DATE)));
                ((MainActivity) view.getContext()).replaceFragmentFull(new Scheduler(calendar.get(Calendar.DATE)));

            }
        });


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

package com.dum.dodam.Scheduler;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.tabs.TabLayout;

import java.util.Calendar;

public class Scheduler extends Fragment {

    private ViewPager pager;
    private SchedulerPagerAdapter viewPageAdapter;
    private int date = -1;

    private int this_month;
    private int this_year;
    private int sDayNum;
    private int endDate;

    public Scheduler(int date) {
        this.date = date;
    }

    public Scheduler() {
    }

//    public static Scheduler newInstance(int date) {
//        Bundle args = new Bundle();
//        args.putInt("date", date);
//        Scheduler scheduler = new Scheduler();
//        scheduler.setArguments(args);
//        return scheduler;
//    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scheduler_, container, false);
        view.setClickable(true);

        Calendar cal = Calendar.getInstance(); //캘린더 인스턴스 얻기
        int today = cal.get(Calendar.DATE);
        this_month = cal.get(Calendar.MONTH) + 1;
        this_year = cal.get(Calendar.YEAR);
        cal.set(Calendar.DATE, 1); //현재 달을 1일로 설정.
        sDayNum = cal.get(Calendar.DAY_OF_WEEK); // 1일의 요일 얻어오기, SUNDAY (1), MONDAY(2) , TUESDAY(3),.....
        endDate = cal.getActualMaximum(Calendar.DATE); //달의 마지막일 얻기

        if (date != -1) {
            today = date;
        }

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("스케줄러");

        pager = (ViewPager) view.findViewById(R.id.pager);
        viewPageAdapter = new SchedulerPagerAdapter(getChildFragmentManager(), sDayNum, endDate, this_month, this_year);
        pager.setAdapter(viewPageAdapter);

        TabLayout tab_layout = (TabLayout) view.findViewById(R.id.tab_layout);
        tab_layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        tab_layout.setupWithViewPager(pager);

        tab_layout.getTabAt(today - 1).select();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentFull(new CustomCalendar());
            }
        });
        return view;
    }
}
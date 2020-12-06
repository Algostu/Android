package com.dum.dodam.Cafeteria;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dum.dodam.LocalDB.CafeteriaWeek;
import com.dum.dodam.R;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class CafeteriaTab extends Fragment {

    private List<Integer> mydate;

    private final ArrayList<CafeteriaWeek> cafeteriaWeeks;
    private boolean isFirstWeekNull;

    public CafeteriaTab(ArrayList<CafeteriaWeek> cafeteriaWeeks, boolean isFirstNull) {
        this.cafeteriaWeeks = cafeteriaWeeks;
        this.isFirstWeekNull = isFirstNull;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cafeteria_, container, false);
        view.setClickable(true);

        mydate = getWeekNDate();

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setTitle(String.format("%d 월", mydate.get(1)));

        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        CafeteriaViewPageAdapter viewPageAdapter = new CafeteriaViewPageAdapter(getChildFragmentManager(), cafeteriaWeeks, isFirstWeekNull);
        pager.setAdapter(viewPageAdapter);

        TabLayout tab_layout = (TabLayout) view.findViewById(R.id.tab_layout);
        tab_layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        tab_layout.setupWithViewPager(pager);

        if (tab_layout.getTabCount() > mydate.get(3))
            tab_layout.getTabAt(mydate.get(3)).select();

        return view;
    }

    public static ArrayList<Integer> getWeekNDate() {
        ArrayList<Integer> result = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        int this_week = c.get(Calendar.WEEK_OF_MONTH);
        int today = c.get(Calendar.DATE); //오늘 일자 저장
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        int last_date = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        c.set(Calendar.DAY_OF_MONTH, 1); //DAY_OF_MONTH를 1로 설정 (월의 첫날)
        int start_DOW = c.get(Calendar.DAY_OF_WEEK); //그 주의 요일 반환 (일:1 ~ 토:7)

        result.add(year);
        result.add(month + 1);
        result.add(today);
        result.add(this_week);
        result.add(last_date);
        result.add(start_DOW);

        return result;
    }
}
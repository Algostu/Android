package com.dum.dodam.Scheduler;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.DragEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dum.dodam.R;
import com.dum.dodam.Scheduler.decorator.EventDecorator;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;

import java.util.List;

public class CustomCalendar extends Fragment implements OnDateSelectedListener {

    MaterialCalendarView widget;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scheduler_calendar, container, false);
        view.setClickable(true);

        widget = view.findViewById(R.id.calendarView);
        widget.setTitleAnimationOrientation(MaterialCalendarView.HORIZONTAL);

        CalendarDay today = CalendarDay.today();
        widget.setCurrentDate(today);

        widget.setOnDateChangedListener(this);

        Button btn = view.findViewById(R.id.btn);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                List<CalendarDay> dates = widget.getSelectedDates();

                for (CalendarDay a : dates) {
                    Log.d("RHC", "DATES: " + a);
                }
            }
        });

        widget.setDynamicHeightEnabled(true);

        return view;
    }

    @Override
    public void onDateSelected(@NonNull MaterialCalendarView widget, @NonNull CalendarDay date, boolean selected) {

    }
}

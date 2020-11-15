package com.dum.dodam.Scheduler;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.dum.dodam.Alarm.Adapter.PageAdapter;
import com.dum.dodam.R;
import com.google.gson.Gson;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class CalenderPopUpFragment extends DialogFragment implements View.OnClickListener {

    public static String TAG = "CalenderPopUpFragment";

    public Calendar startDate;
    public Calendar endDate;

    public String startDateStr;
    public String endDateStr;

    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");

    public String timeDuration;

    PageAdapter pageAdapter;
    ViewPager viewPage;

    public static CalenderPopUpFragment newInstance(String startDate, String endDate) {
        Bundle args = new Bundle();
        Gson gson = new Gson();
        args.putString("startDate", startDate);
        args.putString("endDate", endDate);
        CalenderPopUpFragment calenderPopUpFragment = new CalenderPopUpFragment();
        calenderPopUpFragment.setArguments(args);
        return calenderPopUpFragment;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.todo_calender_view, container, false);
        Bundle bundle = getArguments();

//        // load date from parent fragment
//        if (bundle != null) {
//            TIME_MAXIMUM timeCal = new TIME_MAXIMUM();
//
//            startDateStr = bundle.getString("startDate");
//            endDateStr = bundle.getString("endDate");
//            try {
//                Date date = new Date();
//                Calendar lastYear = Calendar.getInstance();
//                lastYear.setTime(format.parse(startDateStr));
//                lastYear.add(Calendar.YEAR, -1);
//                Calendar nextYear = Calendar.getInstance();
//                nextYear.setTime(format.parse(startDateStr));
//                nextYear.add(Calendar.YEAR, 1);
//
//                startDate = Calendar.getInstance();
//                endDate = Calendar.getInstance();
//                startDate.setTime(format.parse(startDateStr));
//                endDate.setTime(format.parse(endDateStr));
//
//                TIME_MAXIMUM dateCal = new TIME_MAXIMUM();
//                int dateDuration = dateCal.calculateTime(format.parse(startDateStr), format.parse(endDateStr));
//
//                ArrayList<Date> dateArrayList = new ArrayList<>();
//
//                for(int i=0; i<=dateDuration; i++){
//                    dateArrayList.add(startDate.getTime());
//                    startDate.add(Calendar.DATE, 1);
//                }
//
////                calendar.init(startDate.getTime(), nextYear.getTime()) //
////                        .inMode(CalendarPickerView.SelectionMode.RANGE)
////                        .withSelectedDates(dateArrayList);
//
//
//            } catch (ParseException e) {
//                e.printStackTrace();
//            }
//        }


        // confirm button
//        Button btn = view.findViewById(R.id.complete);
//        btn.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Bundle bundle = new Bundle();
//                Log.d(TAG, "selected Date" + calendar.getSelectedDates());
//                bundle.putString("startDate", format.format(startDate.getTime()));
//                bundle.putString("endDate", format.format(endDate.getTime()));
//
//                Intent intent = new Intent().putExtras(bundle);
//                getTargetFragment().onActivityResult(getTargetRequestCode(), 0, intent);
//                dismiss();
//            }
//        });


        return view;
    }

    @Override
    public void onClick(View v){
        dismiss();
    }
}

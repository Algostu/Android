package com.dum.dodam.Scheduler;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.dum.dodam.R;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

public class TimePickerFragment extends DialogFragment implements TimePickerDialog.OnTimeSetListener, View.OnClickListener {
    public static final String TAG = "TimePickerFragment";
    public Calendar date;
    public int type;
    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public android.widget.TimePicker timePicker;

    public static TimePickerFragment newInstance(int type, String date) {
        Bundle args = new Bundle();
        Gson gson = new Gson();
        args.putString("date", date);
        args.putInt("type", type);
        TimePickerFragment timePicker = new TimePickerFragment();
        timePicker.setArguments(args);
        return timePicker;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.todo_calender_view_tab, container, false);
        return view;
    }

    @Override
    public void onTimeSet(TimePicker timePicker, int i, int i1) {
        Log.d(TAG, "hour: " + i + "minute: " + i1);
        Bundle bundle = new Bundle();
        if (type == 0){
            bundle.putString("startDate", format.format(date.getTime()));
        } else {
            bundle.putString("endDate", format.format(date.getTime()));
        }

        Intent intent = new Intent().putExtras(bundle);
        getTargetFragment().onActivityResult(getTargetRequestCode(), 0, intent);
        dismiss();
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        Bundle bundle = getArguments();
        if (bundle != null) {
            type = bundle.getInt("type");
            String dateStr = bundle.getString("date");
            Log.d(TAG, "dateStr" + dateStr);
            try {
                date = Calendar.getInstance();
                date.setTime(format.parse(dateStr));
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        TimePickerDialog timePickerDialog = new TimePickerDialog(getContext(), this,
                date.get(Calendar.HOUR), date.get(Calendar.MINUTE), DateFormat.is24HourFormat(getContext())
        );


        return timePickerDialog;
    }

    @Override
    public void onClick(View v){
        dismiss();
    }
}
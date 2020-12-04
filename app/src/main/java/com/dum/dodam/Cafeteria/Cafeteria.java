package com.dum.dodam.Cafeteria;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dum.dodam.LocalDB.CafeteriaWeek;
import com.dum.dodam.R;

public class Cafeteria extends Fragment {
    private final CafeteriaWeek frame;

    public Cafeteria(CafeteriaWeek frame) {
        this.frame = frame;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cafeteria_card, container, false);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getActivity().getWindow().setStatusBarColor(Color.parseColor("#fbdd56"));
            }
        } else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getActivity().getWindow().setStatusBarColor(Color.BLACK);
        }

        TextView monday = view.findViewById(R.id.monday);
        TextView tuesday = view.findViewById(R.id.tuesday);
        TextView wednesday = view.findViewById(R.id.wednesday);
        TextView thursday = view.findViewById(R.id.thursday);
        TextView friday = view.findViewById(R.id.friday);
        TextView lunch_monday = view.findViewById(R.id.lunch_monday);
        TextView lunch_tuesday = view.findViewById(R.id.lunch_tuesday);
        TextView lunch_wednesday = view.findViewById(R.id.lunch_wednesday);
        TextView lunch_thursday = view.findViewById(R.id.lunch_thursday);
        TextView lunch_friday = view.findViewById(R.id.lunch_friday);

        monday.setText(String.format("월요일 (%s)", frame.mondayDate));
        tuesday.setText(String.format("화요일 (%s)", frame.tuesdayDate));
        wednesday.setText(String.format("수요일 (%s)", frame.wednesdayDate));
        thursday.setText(String.format("목요일 (%s)", frame.thursdayDate));
        friday.setText(String.format("금요일 (%s)", frame.fridayDate));

        lunch_monday.setText(frame.monday.replace(" ", "\n"));
        lunch_tuesday.setText(frame.tuesday.replace(" ", "\n"));
        lunch_wednesday.setText(frame.wednesday.replace(" ", "\n"));
        lunch_thursday.setText(frame.thursday.replace(" ", "\n"));
        lunch_friday.setText(frame.friday.replace(" ", "\n"));

        return view;
    }
}
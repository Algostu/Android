package com.dum.dodam.Cafeteria;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dum.dodam.Cafeteria.dataframe.CafeteriaFrame;
import com.dum.dodam.R;

public class Cafeteria extends Fragment {
    static String TAG = "RHC";

    private CafeteriaFrame frame;

    public Cafeteria(CafeteriaFrame frame) {
        this.frame = frame;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cafeteria_card, container, false);

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

        monday.setText(String.format("월요일 (%s)", frame.date_monday));
        tuesday.setText(String.format("화요일 (%s)", frame.date_tuesday));
        wednesday.setText(String.format("수요일 (%s)", frame.date_wednesday));
        thursday.setText(String.format("목요일 (%s)", frame.date_thursday));
        friday.setText(String.format("금요일 (%s)", frame.date_friday));

        lunch_monday.setText(frame.lunch_monday.replace(",", "\n"));
        lunch_tuesday.setText(frame.lunch_tuesday.replace(",", "\n"));
        lunch_wednesday.setText(frame.lunch_wednesday.replace(",", "\n"));
        lunch_thursday.setText(frame.lunch_thursday.replace(",", "\n"));
        lunch_friday.setText(frame.lunch_friday.replace(",", "\n"));

        if (frame.lunch_monday.equals(" ") || frame.lunch_monday.equals("") || frame.lunch_monday == null) {
            lunch_monday.setText("제공되는 식단 정보가 없습니다.");
        }
        if (frame.lunch_tuesday.equals(" ") || frame.lunch_tuesday.equals("") || frame.lunch_tuesday == null) {
            lunch_tuesday.setText("제공되는 식단 정보가 없습니다.");
        }
        if (frame.lunch_wednesday.equals(" ") || frame.lunch_wednesday.equals("") || frame.lunch_wednesday == null) {
            lunch_wednesday.setText("제공되는 식단 정보가 없습니다.");
        }
        if (frame.lunch_thursday.equals(" ") || frame.lunch_thursday.equals("") || frame.lunch_thursday == null) {
            lunch_thursday.setText("제공되는 식단 정보가 없습니다.");
        }
        if (frame.lunch_friday.equals(" ") || frame.lunch_friday.equals("") || frame.lunch_friday == null) {
            lunch_friday.setText("제공되는 식단 정보가 없습니다.");
        }


        return view;
    }
}
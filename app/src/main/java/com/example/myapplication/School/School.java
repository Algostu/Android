package com.example.myapplication.School;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;
import com.example.myapplication.School.dataframe.CafeteriaFrame;

import java.text.SimpleDateFormat;
import java.util.Date;

public class School extends Fragment {
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.school, container, false);
        getCafeteriaMenu();
        return view;
    }

    public void getCafeteriaMenu() {
        CafeteriaFrame menu = new CafeteriaFrame();
        menu.lunch = "돈까스";
        menu.school_name = "아주대";

        long now = System.currentTimeMillis();
        Date data = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yymm");
        String date = dateFormat.format(data);
        String filename = date + "_cafeteria_menu";
        Log.d("RHC", "filename : " + filename);
        Log.d("RHC", "dir : " + getContext().getFilesDir().toString());
//        if()
//
//        File file = new File(getContext().getFilesDir(), filename);
    }
}
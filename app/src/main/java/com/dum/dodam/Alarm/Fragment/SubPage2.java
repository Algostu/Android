package com.dum.dodam.Alarm.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.fragment.app.Fragment;

import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.R;

public class SubPage2 extends Fragment {
    private static final String ARG_SECTION_NUMBER = "section_number";
    private UserJson user;

    public SubPage2() {
    }

    /**
     * Returns a new instance of this fragment for the given section
     * number.
     */
    public static SubPage2 newInstance() {
        SubPage2 fragment = new SubPage2();
//        Bundle args = new Bundle();
//        args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_tab_sub, container, false);
        view.setClickable(true);
        return view;
    }

}

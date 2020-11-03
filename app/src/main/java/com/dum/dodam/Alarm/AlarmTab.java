package com.dum.dodam.Alarm;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dum.dodam.Alarm.Adapter.PageAdapter;
import com.dum.dodam.R;
import com.google.android.material.tabs.TabLayout;

public class AlarmTab extends Fragment {
    TabLayout tabs;
    PageAdapter pageAdapter;
    ViewPager viewPage;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_tab, container, false);
        setHasOptionsMenu(false);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        pageAdapter = new PageAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPage = (ViewPager) view.findViewById(R.id.container);
        viewPage.setAdapter(pageAdapter);

        tabs = (TabLayout) view.findViewById(R.id.tabs);
        tabs.setupWithViewPager(viewPage);

        return view;
    }

    @Override
    public void onResume() {
        refresh();
        super.onResume();
    }

    private void refresh() {
        pageAdapter.notifyDataSetChanged();
    }


}

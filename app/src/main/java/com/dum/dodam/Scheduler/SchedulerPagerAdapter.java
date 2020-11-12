package com.dum.dodam.Scheduler;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class SchedulerPagerAdapter extends FragmentStatePagerAdapter {

    private static final String TAG = "RHC";
    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> pageTitles = new ArrayList<>();

    public SchedulerPagerAdapter(@NonNull FragmentManager fm, int sDay_num, int end_date) {
        super(fm);

        Log.d(TAG, "IN!");

        for (int i = 1; i < end_date + 1; i++) {
            pageTitles.add(String.format("%d일", i));
            fragments.add(new SchedulerPager(i + 1, sDay_num % 7));
            sDay_num++;
        }
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles.get(position);
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments.get(position);
    }

    @Override
    public int getCount() {
        return fragments.size();
    }
}

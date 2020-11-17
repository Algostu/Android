package com.dum.dodam.Scheduler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import java.util.ArrayList;

public class SchedulerPagerAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> pageTitles = new ArrayList<>();

    public SchedulerPagerAdapter(@NonNull FragmentManager fm, int sDay_num, int end_date, int this_month, int this_year) {
        super(fm);

        for (int i = 1; i < end_date + 1; i++) {
            pageTitles.add(String.format("%d일", i));
            fragments.add(new SchedulerPager(sDay_num % 7, i, this_month, this_year));
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

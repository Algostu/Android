package com.dum.dodam.Cafeteria;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dum.dodam.LocalDB.CafeteriaWeek;

import java.util.ArrayList;

public class CafeteriaViewPageAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> pageTitles = new ArrayList<>();

    public CafeteriaViewPageAdapter(@NonNull FragmentManager fm, ArrayList<CafeteriaWeek> menu, boolean isFirstWeekNull) {
        super(fm);

        int size = menu.size();
        int iter;
        if (isFirstWeekNull) iter = 2;
        else iter = 1;

        for (int i = 0; i < size; i++) {
            pageTitles.add(String.format("%d주차", i + iter));
            fragments.add(new Cafeteria(menu.get(i)));
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

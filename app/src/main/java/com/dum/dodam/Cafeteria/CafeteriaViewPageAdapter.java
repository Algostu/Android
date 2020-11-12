package com.dum.dodam.Cafeteria;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dum.dodam.Cafeteria.dataframe.CafeteriaFrame;
import com.dum.dodam.Univ.UnivCommunity;
import com.dum.dodam.Univ.UnivNews;
import com.dum.dodam.Univ.dataframe.UnivFrame;

import java.util.ArrayList;
import java.util.Arrays;

public class CafeteriaViewPageAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> pageTitles = new ArrayList<>();

    public CafeteriaViewPageAdapter(@NonNull FragmentManager fm, ArrayList<CafeteriaFrame> menu) {
        super(fm);

        int size = menu.size();

        for (int i = 0; i < size; i++) {
            pageTitles.add(String.format("%d주차", i + 1));
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

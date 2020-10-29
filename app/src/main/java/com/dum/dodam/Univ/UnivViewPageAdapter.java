package com.dum.dodam.Univ;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dum.dodam.Univ.dataframe.UnivFrame;

import java.util.ArrayList;
import java.util.Arrays;

public class UnivViewPageAdapter extends FragmentStatePagerAdapter {

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> pageTitles = new ArrayList<>(Arrays.asList("공지글", "대학게시판"));

    public UnivViewPageAdapter(@NonNull FragmentManager fm, UnivFrame univ) {
        super(fm);
        fragments.add(UnivNews.newInstance(0, 1));
        fragments.add(UnivCommunity.newInstance(0, 2));
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

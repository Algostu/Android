package com.dum.dodam.Univ.Adapter;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dum.dodam.Univ.Fragment.RankingPage;

import java.util.ArrayList;

public class RankingPageAdapter extends FragmentStatePagerAdapter {

    // debug
    public final String TAG = "RankingPageAdapter";

    ArrayList<Fragment> fragments = new ArrayList<>();
    ArrayList<String> pageTitles = new ArrayList<>();

    public RankingPageAdapter(@NonNull FragmentManager fm) {
        super(fm);

        fragments.add(new RankingPage(1));
        pageTitles.add("대학교랭킹");

        fragments.add(new RankingPage(2));
        pageTitles.add("학과랭킹");
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

    @Override

    public int getItemPosition(Object object) {
        Log.d(TAG, "getItemPosition: ");
        return POSITION_NONE;
    }
}

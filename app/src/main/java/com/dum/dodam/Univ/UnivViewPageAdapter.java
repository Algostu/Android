package com.dum.dodam.Univ;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.dum.dodam.Univ.dataframe.UnivFrame;

public class UnivViewPageAdapter extends FragmentPagerAdapter {

    Fragment[] fragments = new Fragment[2];
    String[] pageTitles = new String[]{"공지글", "대학게시판"};

    public UnivViewPageAdapter(@NonNull FragmentManager fm, UnivFrame univ) {
        super(fm);
        fragments[0] = UnivNews.newInstance(0, 1);
        fragments[1] = UnivCommunity.newInstance(0, 2);
    }

    @Nullable
    @Override
    public CharSequence getPageTitle(int position) {
        return pageTitles[position];
    }

    @NonNull
    @Override
    public Fragment getItem(int position) {
        return fragments[position];
    }

    @Override
    public int getCount() {
        return fragments.length;
    }

}

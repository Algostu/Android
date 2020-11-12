package com.dum.dodam.Alarm.Adapter;

import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;

import com.dum.dodam.Alarm.Fragment.SubPage1;
import com.dum.dodam.Alarm.Fragment.SubPage2;

public class PageAdapter extends FragmentStatePagerAdapter {

    String[] tabNameList;

    public PageAdapter(FragmentManager fm) {
        super(fm);
        // Tab 기본 설정
        tabNameList = new String[]{"알림"};
//        tabNameList = new String [] {"알림", "쪽지"};
    }

    @Override
    public Fragment getItem(int position) {
        // getItem is called to instantiate the fragment for the given page.
        // Return a PlaceholderFragment (defined as a static inner class below).
//        if (position == 0)
        return SubPage1.newInstance();
//        else
//            return SubPage2.newInstance();
    }

    @Override
    public int getCount() {
        // Show 3 total pages.
        return 1;
    }

    @Override
    public CharSequence getPageTitle(int position) {
        return tabNameList[position];
    }

}
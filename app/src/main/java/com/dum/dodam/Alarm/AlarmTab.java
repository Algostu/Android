package com.dum.dodam.Alarm;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.dum.dodam.Alarm.Adapter.PageAdapter;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;

public class AlarmTab extends Fragment {
    //    TabLayout tabs;
    PageAdapter pageAdapter;
    ViewPager viewPage;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ActionBar actionbar;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.alarm_tab, container, false);
        setHasOptionsMenu(true);
        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.

        pageAdapter = new PageAdapter(getChildFragmentManager());

        // Set up the ViewPager with the sections adapter.
        viewPage = (ViewPager) view.findViewById(R.id.container);
        viewPage.setAdapter(pageAdapter);

//        tabs = (TabLayout) view.findViewById(R.id.tabs);
//        tabs.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white_gray));
//        tabs.setupWithViewPager(viewPage);

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("메세지 도착!");
        actionBar.setSubtitle("내게 온 알림을 확인해보세요:)");

        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu_alarm, menu);
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        MenuItem menu2 = menu.findItem(R.id.delete_all);
        menu2.setVisible(true);
        MenuItem menu1 = menu.findItem(R.id.delete_read);
        menu1.setVisible(true);

        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        Log.d("hello", "ho");
        switch (item.getItemId()) {
            case R.id.delete_all:
                ((MainActivity) getActivity()).deleteAlarm(false);
                break;
            case R.id.delete_read:
                ((MainActivity) getActivity()).deleteAlarm(true);
                break;
        }
        refresh();
        return super.onOptionsItemSelected(item);
    }

    public void refresh() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }

    @Override
    public void onResume() {
//        refresh();
//        pageAdapter.notifyDataSetChanged();
        super.onResume();
    }

}

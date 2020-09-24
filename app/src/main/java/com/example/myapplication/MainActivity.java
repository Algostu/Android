package com.example.myapplication;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Community.CommunityList;
import com.example.myapplication.Home.Home;
import com.example.myapplication.Mypage.Mypage;
import com.example.myapplication.School.School;
import com.example.myapplication.Simulation.Simulation;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {

    private long pressedTime;

    private BottomNavigationView navigation;
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.item_home:
                    replaceFragment(new Home());
                    return true;
                case R.id.item_school:
                    replaceFragment(new School());
                    return true;
                case R.id.item_community:
                    replaceFragment(new CommunityList());
                    return true;
                case R.id.item_simulation:
                    replaceFragment(new Simulation());
                    return true;
                case R.id.item_mypage:
                    replaceFragment(new Mypage());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        navigation = findViewById(R.id.nav_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.item_home);

    }


//    @Override
//    public void onBackPressed() {
//        //super.onBackPressed();
//        if (pressedTime == 0) {
//            Toast.makeText(MainActivity.this, "한번 더 누르면 종료됩니다", Toast.LENGTH_LONG).show();
//            pressedTime = System.currentTimeMillis();
//        } else {
//            int seconds = (int) (System.currentTimeMillis() - pressedTime);
//
//            if (seconds > 2000) {
//                pressedTime = 0;
//            } else {
//                finish();
//            }
//        }
//    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_main, fragment);
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate();
        transaction.commit();
    }

    public void replaceFragmentFull(Fragment fragment) {
        navigation.setVisibility(fragment.getView().GONE);

        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
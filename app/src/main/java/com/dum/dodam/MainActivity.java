package com.dum.dodam;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dum.dodam.Community.CommunityList;
import com.dum.dodam.Home.Home;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.Mypage.Mypage;
import com.dum.dodam.School.School;
import com.dum.dodam.Simulation.Simulation;

import com.google.android.material.bottomnavigation.BottomNavigationView;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigation;
    Intent intent;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
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
        intent = getIntent();
        UserJson user = (UserJson)intent.getSerializableExtra("user");
        Log.d("Test", "nickName  :  "+user.nickName);
        Log.d("Test", "regionName  :  "+user.regionName);
        Log.d("Test", "townName  :  "+user.townName);
        Log.d("Test", "schoolGender  :  "+user.schoolGender);
        Log.d("Test", "email  :  "+user.email);


        navigation = findViewById(R.id.nav_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.item_home);
    }

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_main, fragment);
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate();
        transaction.commit();
    }

    public void replaceFragmentFull(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment, fragment);
        transaction.addToBackStack(null);
        transaction.commit();
    }
}
package com.dum.dodam;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dum.dodam.Univ.SearchUniv;
import com.dum.dodam.Community.CommunityList;
import com.dum.dodam.Home.Home;
import com.dum.dodam.Home.dataframe.MyCommunityFrame2;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.Mypage.Mypage;
import com.dum.dodam.School.School;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.InstanceIdResult;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigation;
    public UserJson user;
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
                    replaceFragment(new SearchUniv());
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

        user = (UserJson) intent.getSerializableExtra("user");
//        Log.d("Test", "nickName  :  " + user.nickName);
//        Log.d("Test", "regionName  :  " + user.regionName);
//        Log.d("Test", "townName  :  " + user.townName);
//        Log.d("Test", "schoolGender  :  " + user.schoolGender);
//        Log.d("Test", "email  :  " + user.email);
//        Log.d("Test", "userName  :  " + user.userName);

        for (MyCommunityFrame2 r : user.comAll) {
            r.communityType = 0;
        }

        for (MyCommunityFrame2 r : user.comRegion) {
            r.communityType = 1;
        }

        for (MyCommunityFrame2 r : user.comSchool) {
            r.communityType = 2;
        }

        // 처음 인증을 요구하는 메시지 출력
        SharedPreferences sharedPref = getSharedPreferences(
                "auto", Context.MODE_PRIVATE);
        final SharedPreferences.Editor editor = sharedPref.edit();
        String loginBefore = sharedPref.getString("loginBefore", null);

        if (user.authorized.equals("0") && loginBefore == null) {
            AlertDialog.Builder builder = new AlertDialog.Builder(this);
            builder.setTitle("환영합니다.");
            builder.setMessage("가입하신 후 일주일 안에 인증을 완료해주세요.\n(마이페이지->'?' 버튼)");
            builder.setPositiveButton("확인",
                    new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int which) {
                            editor.putString("loginBefore", "true");
                            editor.commit();
                        }
                    });
            builder.show();
        }

        navigation = findViewById(R.id.nav_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);
        navigation.setSelectedItemId(R.id.item_home);

        FirebaseInstanceId.getInstance().getInstanceId()
                .addOnCompleteListener(new OnCompleteListener<InstanceIdResult>() {
                    @Override
                    public void onComplete(@NonNull Task<InstanceIdResult> task) {
                        if (!task.isSuccessful()) {
                            Log.w("FCM Log", "getInstanceId failed", task.getException());
                            return;
                        }
                        String token = task.getResult().getToken();
                    }
                });
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

    public UserJson getUser() {
        return user;
    }

    public void setNavigationMenu() {
        navigation.setSelectedItemId(R.id.item_school);
    }

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }
}
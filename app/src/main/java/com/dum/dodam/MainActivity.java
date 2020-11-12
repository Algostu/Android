package com.dum.dodam;

import android.app.AlertDialog;
import android.app.NotificationManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.res.Configuration;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dum.dodam.Alarm.AlarmTab;
import com.dum.dodam.Alarm.Data.AlarmData;
import com.dum.dodam.Community.Article;
import com.dum.dodam.Community.CommunityList;
import com.dum.dodam.Home.Home;
import com.dum.dodam.Home.dataframe.MyCommunityFrame2;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.Cafeteria.Cafeteria;
import com.dum.dodam.Scheduler.Scheduler;
import com.dum.dodam.Univ.SearchUniv;
import com.dum.dodam.httpConnection.BaseResponse;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;

public class MainActivity extends AppCompatActivity {
    private BottomNavigationView navigation;
    public UserJson user;
    public Home home;
    public CommunityList comm;
    public SearchUniv sear;
    Intent intent;

    private final BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            switch (menuItem.getItemId()) {
                case R.id.item_home:
                    replaceFragment(home);
                    return true;
                case R.id.item_scheduler:
                    replaceFragment(new Scheduler());
                    return true;
                case R.id.item_community:
                    replaceFragment(comm);
                    return true;
//                case R.id.item_simulation:
//                    replaceFragment(sear);
//                    return true;
                case R.id.item_mypage:
                    replaceFragment(new AlarmTab());
                    return true;
            }
            return false;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        home = new Home();
        comm = new CommunityList();
        sear = new SearchUniv();
        View view = getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getWindow().setStatusBarColor(Color.parseColor("#FFFFFF"));
            }
        } else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getWindow().setStatusBarColor(Color.BLACK);
        }

        Log.d("debug", "hello my mainActivity");
        setContentView(R.layout.activity_main);
        SharedPreferences sharedPref = getSharedPreferences(
                "auto", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        intent = getIntent();
        String json = sharedPref.getString("userObject", "");
        user = gson.fromJson(json, UserJson.class);

        // 처음 인증을 요구하는 메시지 출력
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

        // fcm token 변경 유무 확인
        String token = sharedPref.getString("fcmToken", "");
        if (token.equals("") == false) {
            RetrofitService service = RetrofitAdapter.getInstance(this);
            Call<BaseResponse> call = service.registerFCM(token);
            call.enqueue(new retrofit2.Callback<BaseResponse>() {
                @Override
                public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                    if (response.isSuccessful()) {
                        if (response.body().checkError(getApplicationContext()) != 0) {
                            Toast.makeText(getApplicationContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        editor.remove("fcmToken");
                        editor.commit();
                    }
                }

                @Override
                public void onFailure(Call<BaseResponse> call, Throwable t) {
                    Toast.makeText(getApplicationContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                }
            });
        }

        navigation = findViewById(R.id.nav_bar);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        // notification으로 이곳으로 넘어 온 경우 alarm 페이지로 전환
        Log.d("debug", "notification" + intent.getStringExtra("notification"));
        if (intent.getStringExtra("notification") != null) {
            navigation.setSelectedItemId(R.id.item_mypage);
            // Notification 제거
            NotificationManager notificationManager = (NotificationManager) getSystemService(Context.NOTIFICATION_SERVICE);
            notificationManager.cancelAll();
        } else {
            navigation.setSelectedItemId(R.id.item_home);
        }

        for (MyCommunityFrame2 r : user.comAll) {
            r.communityType = 0;
        }

        for (MyCommunityFrame2 r : user.comRegion) {
            r.communityType = 1;
        }

        for (MyCommunityFrame2 r : user.comSchool) {
            r.communityType = 2;
        }
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

    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);
    }

    public ArrayList<AlarmData> getAlarmList() {
        // 저장되어있는 알람 중 오래된것들 삭제
        SharedPreferences sharedPref = getSharedPreferences(
                "auto", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        ArrayList<AlarmData> alarmDataArr;
        String json = sharedPref.getString("alarm", "");
        if (json.equals("") == true) {
            alarmDataArr = new ArrayList<AlarmData>();
            Log.d("alarm debug", "no data stored");
        } else {
            Log.d("alarm debug", "data are stored");
            Type listType = new TypeToken<ArrayList<AlarmData>>() {
            }.getType();
            alarmDataArr = gson.fromJson(json, listType);
        }
        Iterator<AlarmData> i = alarmDataArr.iterator();
        long curTime = System.currentTimeMillis();
        while (i.hasNext()) {
            AlarmData alarm = i.next(); // must be called before you can call i.remove()
            // Do something
            try {
                SimpleDateFormat format = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
                long diffTime = (curTime - format.parse(alarm.time).getTime()) / (60 * 60 * 24 * 1000);
                if (diffTime >= 15)
                    i.remove();
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("alarm", gson.toJson(alarmDataArr));
        editor.commit();

        return alarmDataArr;
    }

    public ArrayList<AlarmData> checkAlarm(ArrayList<AlarmData> alarmDataArr, int index) {
        // 저장되어있는 알람 중 오래된것들 삭제
        SharedPreferences sharedPref = getSharedPreferences(
                "auto", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        AlarmData alarmData;
        SharedPreferences.Editor editor = sharedPref.edit();

        if (alarmDataArr.size() > index) {
            alarmData = alarmDataArr.get(index);
            replaceFragmentFull(Article.newInstance(alarmData.articleID, alarmData.communityType, alarmData.communityID));
            alarmDataArr.get(index).read = 0;
            editor.putString("alarm", gson.toJson(alarmDataArr));
            editor.commit();
        }

        return alarmDataArr;
    }

    public ArrayList<AlarmData> deleteAlarm(ArrayList<AlarmData> alarmDataArr, int index) {
        // 저장되어있는 알람 중 오래된것들 삭제
        SharedPreferences sharedPref = getSharedPreferences(
                "auto", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        AlarmData alarmData;
        SharedPreferences.Editor editor = sharedPref.edit();

        if (alarmDataArr.size() > index) {
            alarmDataArr.remove(index);
            editor.putString("alarm", gson.toJson(alarmDataArr));
            editor.commit();
        }

        return alarmDataArr;
    }

    public ArrayList<AlarmData> deleteAlarm(boolean readOnly) {
        // 저장되어있는 알람 중 오래된것들 삭제
        SharedPreferences sharedPref = getSharedPreferences(
                "auto", Context.MODE_PRIVATE);
        Gson gson = new Gson();
        ArrayList<AlarmData> alarmDataArr;
        if (readOnly) {
            String json = sharedPref.getString("alarm", "");
            if (json.equals("") == true) {
                alarmDataArr = new ArrayList<AlarmData>();
                Log.d("alarm debug", "no data stored");
            } else {
                Log.d("alarm debug", "data are stored");
                Type listType = new TypeToken<ArrayList<AlarmData>>() {
                }.getType();
                alarmDataArr = gson.fromJson(json, listType);
            }
            Iterator<AlarmData> i = alarmDataArr.iterator();
            while (i.hasNext()) {
                AlarmData alarm = i.next(); // must be called before you can call i.remove()
                // Do something
                try {
                    if (alarm.read == 0)
                        i.remove();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } else {
            alarmDataArr = new ArrayList<>();
        }
        SharedPreferences.Editor editor = sharedPref.edit();
        editor.putString("alarm", gson.toJson(alarmDataArr));
        editor.commit();

        return alarmDataArr;
    }


}
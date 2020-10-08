package com.dum.dodam.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.kakao.auth.AuthType;
import com.kakao.usermgmt.response.model.AgeRange;
import com.kakao.usermgmt.response.model.Gender;
import com.kakao.usermgmt.response.model.UserAccount;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import com.dum.dodam.Login.Data.UserInfo;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;


import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class startUpActivity extends AppCompatActivity {
    private static final String TAG = "KHK";
    public UserInfo user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.start_main);

        SharedPreferences sharedPref = getSharedPreferences(
                "auto", Context.MODE_PRIVATE);
        String autoLogin = sharedPref.getString("autoLogin",null);

        if (autoLogin != null ){
            String expStr = sharedPref.getString("expDate",null);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date expDate;
            Date curDate = new Date();
            try {
                expDate = format.parse(expStr);
                if(curDate.before(expDate)){
                    // extend expired date
                    Calendar c = Calendar.getInstance();
                    c.setTime(curDate);
                    c.add(Calendar.DATE, 25);
                    String expDateStr = format.format(c.getTime());
                    SharedPreferences.Editor edit = sharedPref.edit();
                    edit.putString("expDate", expDateStr);
                    edit.clear();
                    edit.commit();
                    // load user object and pass it to main Activity
                    Gson gson = new Gson();
                    String json = sharedPref.getString("userObject", "");
                    UserJson user = gson.fromJson(json, UserJson.class);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user",user);
                    intent.putExtras(bundle);
                    startActivity(intent);
                    return;
                }
            } catch (ParseException e) {
                e.printStackTrace();
            }
        }
        this.replaceFragment(new Login());
        return;
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate();
        transaction.commit();
    }

    public void login(long id, String token) {
        RetrofitAdapter adapter = new RetrofitAdapter();
        RetrofitService service = adapter.getInstance("http://49.50.164.11:5000/", this);
        Call<UserJson> call = service.kakaoLogin(id, token);
        call.enqueue(new retrofit2.Callback<UserJson>() {
            @Override
            public void onResponse(Call<UserJson> call, retrofit2.Response<UserJson> response) {
                if (response.isSuccessful()) {
                    UserJson result = response.body();
                    if (result.status.equals("success")){
                        Log.d("KHK", result.toString());
                        // make curDate + 30 string as expired date
                        Calendar c = Calendar.getInstance();
                        c.setTime(new Date());
                        c.add(Calendar.DATE, 25);
                        String expDateStr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(c.getTime());
                        // make user object to string to store
                        Gson gson = new Gson();
                        String json = gson.toJson(result);
                        // store to sharePreference for future Use
                        SharedPreferences sharedPref = getSharedPreferences(
                                "auto", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("autoLogin", "true");
                        editor.putString("userObject", json);
                        editor.putString("expDate", expDateStr);
                        editor.commit();
                        // make intent and start main activity
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user",result);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }
                    else {
                        // if need to sign up
                        startUpActivity.this.replaceFragment(new SignUP());
                        Log.d(TAG, "onResponse: Fails " + response.body().status);
                    }

                } else {
                    Log.d(TAG, "onResponse: Fails " + response.body());
                }
            }
            @Override
            public void onFailure(Call<UserJson> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


}



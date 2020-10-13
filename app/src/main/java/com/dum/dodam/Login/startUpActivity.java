package com.dum.dodam.Login;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.dum.dodam.Login.Data.LoginResponse;
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
        String keyHash = com.kakao.util.helper.Utility.getKeyHash(this /* context */);
        Log.d(TAG, keyHash);
        StrictMode.setThreadPolicy(new StrictMode.ThreadPolicy.Builder()
                .permitDiskReads()
                .permitDiskWrites()
                .permitNetwork().build());

        SharedPreferences sharedPref = getSharedPreferences(
                "auto", Context.MODE_PRIVATE);
        String autoLogin = sharedPref.getString("autoLogin", null);
        String auth = sharedPref.getString("auth", null);
        int auth_int;
        if (auth == null) {
            this.replaceFragment(new Login());
            return;
        } else {
            auth_int = Integer.parseInt(auth);
        }

        if (autoLogin != null && auth_int == 1) {
            String expStr = sharedPref.getString("expDate", null);
            SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
            Date expDate;
            Date curDate = new Date();
            try {
                expDate = format.parse(expStr);
                if (curDate.before(expDate)) {
                    // extend expired date
                    Calendar c = Calendar.getInstance();
                    c.setTime(curDate);
                    c.add(Calendar.DATE, 25);
                    String expDateStr = format.format(c.getTime());
                    SharedPreferences.Editor edit = sharedPref.edit();
                    edit.putString("expDate", expDateStr);
//                    edit.clear();
                    edit.commit();
                    // load user object and pass it to main Activity
                    Gson gson = new Gson();
                    String json = sharedPref.getString("userObject", "");
                    UserJson user = gson.fromJson(json, UserJson.class);
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user", user);
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

    public void replaceFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate();
        transaction.commit();
    }

    public void login(long id, String token) {
        RetrofitAdapter adapter = new RetrofitAdapter();
        RetrofitService service = adapter.getInstance(this);
        Call<LoginResponse> call = service.kakaoLogin(id, token);
        call.enqueue(new retrofit2.Callback<LoginResponse>() {
            @Override
            public void onResponse(Call<LoginResponse> call, retrofit2.Response<LoginResponse> response) {
                if (response.isSuccessful()) {
                    LoginResponse result = response.body();
                    if (result.checkError(getApplicationContext()) != 0) {
                        if (result.errorCode == 3) {
                            // if need to sign up
                            startUpActivity.this.replaceFragment(new SignUP());
                            Log.d(TAG, "onResponse: Fails " + response.body().status);
                        }
                        return;
                    }
                    if (result.status.equals("<success>")) {
                        UserJson userInfo = result.body;
                        Log.d("KHK", user.toString());
                        // make curDate + 30 string as expired date
                        Calendar c = Calendar.getInstance();
                        c.setTime(new Date());
                        c.add(Calendar.DATE, 25);
                        String expDateStr = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'").format(c.getTime());
                        // make user object to string to store
                        Gson gson = new Gson();
                        String json = gson.toJson(userInfo);
                        // store to sharePreference for future Use
                        SharedPreferences sharedPref = getSharedPreferences(
                                "auto", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPref.edit();
                        editor.putString("autoLogin", "true");
                        editor.putString("userObject", json);
                        editor.putString("expDate", expDateStr);
                        editor.putString("auth", userInfo.authorized);
                        editor.commit();
                        // make intent and start main activity
                        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("user", userInfo);
                        intent.putExtras(bundle);
                        startActivity(intent);
                    }

                } else {
                    Log.d(TAG, "onResponse: Fails " + response.body());
                }
            }

            @Override
            public void onFailure(Call<LoginResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }


}



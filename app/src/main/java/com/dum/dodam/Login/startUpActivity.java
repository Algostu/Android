package com.dum.dodam.Login;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.StrictMode;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.dum.dodam.Login.Data.LoginResponse;
import com.dum.dodam.Login.Data.UserInfo;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.google.gson.Gson;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;

public class startUpActivity extends AppCompatActivity {
    private static final String TAG = "KHK";
    public UserInfo user;
    Intent intent;
    public int scenarioNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        Log.d(TAG, "hello my startupActivity");
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

        intent = getIntent();
        Gson gson = new Gson();

        // 중복 실행 방지
        if (!isTaskRoot()) {
            if (intent.hasCategory(Intent.CATEGORY_LAUNCHER) && Intent.ACTION_MAIN.equals(intent.getAction())) {
                Log.w("hello", "Main Activity is not the root.  Finishing Main Activity instead of launching.");
                finish();
                return;
            }
        }

        if (intent != null && intent.hasExtra("login")) {
            SharedPreferences.Editor edit = sharedPref.edit();
            edit.clear();
            edit.commit();
            scenarioNo = intent.getIntExtra("login", 0);
            this.replaceFragment(new Login());
            return;
        }

        String autoLogin = sharedPref.getString("autoLogin", null);
        String auth = sharedPref.getString("auth", null);
        int auth_int = 0;
        if (auth == null) {
            this.replaceFragment(new Login());
            return;
        } else {
            auth_int = Integer.parseInt(auth);
            if (auth_int == 0){
                scenarioNo = -1;
                this.replaceFragment(new Login());
                return;
            }
        }

        if (autoLogin != null) {
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
                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    // push 알람으로 실행할 경우
                    Log.d("hello", "flags " + intent.getFlags());
                    if ((int)intent.getFlags() / 100000000 == 3){
                        intent.putExtra("notification", "1");
                        intent.putExtra("notifyID", 2);
                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    }
                    startActivity(intent);
                    this.finish();
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
                        startActivity(intent);
                        finish();
                        return;
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



package com.example.myapplication.Login;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.myapplication.Login.Data.School;
import com.example.myapplication.Login.Data.UserInfo;
import com.example.myapplication.Login.Data.UserJson;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.kakao.usermgmt.response.model.AgeRange;
import com.kakao.usermgmt.response.model.Gender;
import com.kakao.usermgmt.response.model.UserAccount;

import java.util.ArrayList;

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

        this.replaceFragment(new Login());

    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.main, fragment);
        FragmentManager manager = getSupportFragmentManager();
        manager.popBackStackImmediate();
        transaction.commit();
    }

    public void login(long id, String token) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://49.50.164.11:5000/auth/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<UserJson> call = service.kakaoLogin(id, token);

        call.enqueue(new retrofit2.Callback<UserJson>() {
            @Override
            public void onResponse(Call<UserJson> call, retrofit2.Response<UserJson> response) {
                if (response.isSuccessful()) {
                    UserJson result = response.body();

                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putSerializable("user",result);
                    intent.putExtras(bundle);

                    startActivity(intent);
                    Log.d(TAG, "onResponse: Success " + response.body());
                } else {
                    // if need to sign up
                    startUpActivity.this.replaceFragment(new SignUP());
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<UserJson> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public interface RetrofitService {
        @GET("login")
        Call<UserJson> kakaoLogin(@Query("id") long id, @Query("token") String token);
    }
}



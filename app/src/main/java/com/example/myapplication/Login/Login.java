package com.example.myapplication.Login;

import android.content.Context;
import android.content.SharedPreferences;
import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.Login.Data.School;
import com.example.myapplication.Login.Data.UserInfo;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import com.google.gson.JsonObject;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.AgeRange;
import com.kakao.usermgmt.response.model.Gender;
import com.kakao.usermgmt.response.model.Profile;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.OptionalBoolean;
import com.kakao.util.exception.KakaoException;

import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;


public class Login extends Fragment {

    private Button btn_custom_login;
    private Button btn_custom_login_out;
    private SessionCallback sessionCallback = new SessionCallback();
    Session session;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.login, container, false);

        btn_custom_login = (Button) view.findViewById(R.id.btn_custom_login3);
//        btn_custom_login_out = (Button) findViewById(R.id.btn_custom_login2);

        session = Session.getCurrentSession();
        session.addCallback(sessionCallback);

        btn_custom_login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.open(AuthType.KAKAO_LOGIN_ALL, Login.this);
            }
        });

//        btn_custom_login_out.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                UserManagement.getInstance()
//                        .requestLogout(new LogoutResponseCallback() {
//                            @Override
//                            public void onCompleteLogout() {
//                                Toast.makeText(LoginActivity.this, "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
//                            }
//                        });
//            }
//        });

        Context context = getActivity();
        SharedPreferences sharedPref = context.getSharedPreferences(
                "auto", Context.MODE_PRIVATE);

        String autoLogin = sharedPref.getString("autoLogin",null);

        if (autoLogin != null)
            session.open(AuthType.KAKAO_LOGIN_ALL, Login.this);

        return view;
    }

    @Override
    public void onDestroy() {
        super.onDestroy();

        // 세션 콜백 삭제
        Session.getCurrentSession().removeCallback(sessionCallback);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        // 카카오톡|스토리 간편로그인 실행 결과를 받아서 SDK로 전달
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    public class SessionCallback implements ISessionCallback {

        // 로그인에 성공한 상태
        @Override
        public void onSessionOpened() {
            requestMe();
        }

        // 로그인에 실패한 상태
        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            Log.e("SessionCallback :: ", "onSessionOpenFailed : " + exception.getMessage());
        }

        // 사용자 정보 요청
        public void requestMe() {
            UserManagement.getInstance()
                    .me(new MeV2ResponseCallback() {
                        @Override
                        public void onSessionClosed(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "세션이 닫혀 있음: " + errorResult);
                        }

                        @Override
                        public void onFailure(ErrorResult errorResult) {
                            Log.e("KAKAO_API", "사용자 정보 요청 실패: " + errorResult);
                        }

                        @Override
                        public void onSuccess(MeV2Response result) {
                            long userID = result.getId();
                            UserAccount kakaoAccount = result.getKakaoAccount();
                            String email = kakaoAccount.getEmail();
                            AgeRange ageRange = kakaoAccount.getAgeRange();
                            Gender gender = kakaoAccount.getGender();
                            AccessToken token = Session.getCurrentSession().getTokenInfo();
                            String accessToken = token.getAccessToken();
                            long expTime = token.accessTokenExpiresAt().getTime();
                            Date date = new Date();

                            // debug
                            Log.i("KAKAO_API", "사용자 아이디: " + userID);
                            Log.i("KAKAO_API", "사용자 나이: " + ageRange.getValue());
                            Log.i("KAKAO_API", "사용자 이메일: " + email);
                            Log.i("KAKAO_API", "사용자 성별: " + gender.getValue());
                            Log.i("KAKAO_API", "사용자 토큰: " + accessToken);
                            Log.i("KAKAO_API", "사용자 토큰 만료 시간: " + expTime);
                            Log.i("KAKAO_API", "사용자 현재 시간: " + date.getTime());

                            // 이메일
                            if (kakaoAccount == null) {
                                Toast.makeText(getActivity(), "아이디 정보를 제공해주셔야 합니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                            // 나이
                            if (ageRange != null) {
                                if (!(ageRange == AgeRange.AGE_15_19 || ageRange == AgeRange.AGE_20_29) && ageRange != AgeRange.AGE_RANGE_UNKNOWN) {
                                    Toast.makeText(getActivity(), "나이 제한에 걸리셨습니다.", Toast.LENGTH_SHORT).show();
                                    return;
                                }
                            }
                            // 성별
                            if (gender == null){
                                Toast.makeText(getActivity(), "성별 정보를 제공해주셔야 합니다.", Toast.LENGTH_SHORT).show();
                            }
                            ((startUpActivity)getActivity()).user = new UserInfo(userID, accessToken, kakaoAccount.getEmail()
                                    ,kakaoAccount.getGender().getValue(),kakaoAccount.getAgeRange().getValue(), expTime);
                            ((startUpActivity)getActivity()).login(userID, accessToken);
                        }
                    });
        }
    }
}
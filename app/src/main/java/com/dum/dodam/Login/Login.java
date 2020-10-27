package com.dum.dodam.Login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dum.dodam.Login.Data.UserInfo;
import com.dum.dodam.R;
import com.dum.dodam.httpConnection.BaseResponse;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.auth.authorization.accesstoken.AccessToken;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeV2ResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.MeV2Response;
import com.kakao.usermgmt.response.model.AgeRange;
import com.kakao.usermgmt.response.model.Gender;
import com.kakao.usermgmt.response.model.UserAccount;
import com.kakao.util.exception.KakaoException;

import java.util.Date;

import retrofit2.Call;


public class Login extends Fragment {

    private Button btn_custom_login;
    private Button btn_custom_login_out;
    private SessionCallback sessionCallback = new SessionCallback();
    public int kakaoID;
    public int scene;
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

        scene =  ((startUpActivity)getActivity()).scenarioNo;
        if (scene == -1){
            session.open(AuthType.KAKAO_LOGIN_ALL, Login.this);
            return inflater.inflate(R.layout.start_main, container, false);
        }

//        btn_custom_login_out.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });

        // 로그아웃
        if(scene == 1){
            this.logout();
        }
        // 탈퇴
        else if (scene == 2){
            session.open(AuthType.KAKAO_LOGIN_ALL, Login.this);
        }
        return view;
    }

    public void withdraw(){
        UserManagement.getInstance().requestUnlink(new UnLinkResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Toast.makeText(getContext(),"회원탈퇴 실패! 다시 시도해 주세요.",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Toast.makeText(getContext(),"회원탈퇴 실패! 다시 시도해 주세요.",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onNotSignedUp() {
                Toast.makeText(getContext(),"가입되지 않은 계정입니다.",Toast.LENGTH_LONG).show();
            }

            @Override
            public void onSuccess(Long result) {
                RetrofitAdapter adapter = new RetrofitAdapter();
                RetrofitService service = adapter.getInstance(getContext());
                Call<BaseResponse> call = service.kakaoSignOut(kakaoID);
                call.enqueue(new retrofit2.Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            BaseResponse result = response.body();
                            if(result.checkError(getContext())!=0) return;
                            Toast.makeText(getContext(),"회원탈퇴 성공!",Toast.LENGTH_LONG).show();
                        } else {
                            Toast.makeText(getContext(),"회원탈퇴 실패! 다시 시도해 주세요.",Toast.LENGTH_LONG).show();
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Toast.makeText(getContext(),"회원탈퇴 실패! 다시 시도해 주세요.",Toast.LENGTH_LONG).show();
                    }
                });
            }
        });
    }

    public void logout(){
        UserManagement.getInstance()
                .requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                        RetrofitAdapter adapter = new RetrofitAdapter();
                        RetrofitService service = adapter.getInstance(getContext());
                        Call<BaseResponse> call = service.kakaoLogout();

                        call.enqueue(new retrofit2.Callback<BaseResponse>() {
                            @Override
                            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                                if (response.isSuccessful()) {
                                    BaseResponse result = response.body();
                                    if(result.checkError(getContext())!=0) return;
                                    Toast.makeText(getContext(), "로그아웃 되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(),"로그아웃 실패! 다시 시도해 주세요.",Toast.LENGTH_LONG).show();
                                }
                            }

                            @Override
                            public void onFailure(Call<BaseResponse> call, Throwable t) {
                                Toast.makeText(getContext(),"로그아웃 실패! 다시 시도해 주세요.",Toast.LENGTH_LONG).show();
                            }
                        });
                    }
                });
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
                            if (gender == null) {
                                Toast.makeText(getActivity(), "성별 정보를 제공해주셔야 합니다.", Toast.LENGTH_SHORT).show();
                            }
                            // 탈퇴
                            if(scene == 2){
                                kakaoID = (int)userID;
                                withdraw();
                                return;
                            }
                            // 가입 혹은 로그인
                            ((startUpActivity) getActivity()).user = new UserInfo(userID, accessToken, kakaoAccount.getEmail()
                                    , kakaoAccount.getGender().getValue(), kakaoAccount.getAgeRange().getValue(), expTime);
                            ((startUpActivity) getActivity()).login(userID, accessToken);
                        }
                    });
        }
    }
}
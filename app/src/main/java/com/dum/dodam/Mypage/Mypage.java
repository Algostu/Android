package com.dum.dodam.Mypage;

import android.app.AlertDialog;
import android.content.ClipData;
import android.content.ClipboardManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.Login.startUpActivity;
import com.dum.dodam.MainActivity;
import com.dum.dodam.Mypage.Dataframe.MyWorkResponse;
import com.dum.dodam.Mypage.Dataframe.SurveyResponse;
import com.dum.dodam.R;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;

import retrofit2.Call;

public class Mypage extends Fragment {
    // debug
    public final String TAG = "Mypage";

    private TextView nickName;
    private TextView emailAddress;
    private ImageView isChecked;
    private TextView schoolName;
    private TextView gradeNclass;
    private TextView code;

    private TextView survey_code;
    private TextView recommend_code;
    private TextView my_article_number;
    private TextView my_comment_number;
    private TextView my_heart_number;
    private TextView copy_code;

    UserJson user;
    public String surveyLink;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.mypage, container, false);
        view.setClickable(true);
        setHasOptionsMenu(true);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getActivity().getWindow().setStatusBarColor(Color.parseColor("#FBFBFB"));
            }
        } else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getActivity().getWindow().setStatusBarColor(Color.BLACK);
        }

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setDisplayShowTitleEnabled(false);

        user = ((MainActivity) getActivity()).getUser();


        survey_code = view.findViewById(R.id.survey_code);
        recommend_code = view.findViewById(R.id.recommend_code);
        my_article_number = view.findViewById(R.id.my_article_number);
        my_comment_number = view.findViewById(R.id.my_comment_number);
        my_heart_number = view.findViewById(R.id.my_heart_number);
        copy_code = view.findViewById(R.id.copy_code);

        nickName = view.findViewById(R.id.nick_name);
        emailAddress = view.findViewById(R.id.mail_address);
        schoolName = view.findViewById(R.id.school);
        gradeNclass = view.findViewById(R.id.grade_n_class_num);
        isChecked = view.findViewById(R.id.confirm);
        isChecked.setVisibility(View.GONE);

        recommend_code.setText(user.recommendCode);

        nickName.setText(user.nickName);
        emailAddress.setText(user.email);
        schoolName.setText(user.schoolName);

        if (user.grade == 13) {
            gradeNclass.setText("졸업생");
        } else if (user.grade == 12) {
            gradeNclass.setText("3학년 " + user.classNum + "반");
        } else if (user.grade == 11) {
            gradeNclass.setText("2학년 " + user.classNum + "반");
        } else if (user.grade == 10) {
            gradeNclass.setText("1학년 " + user.classNum + "반");
        } else if (user.grade == 9) {
            gradeNclass.setText("예비고등");
        } else {
            gradeNclass.setText("중학생");
        }

        if (user.authorized.equals("1")) {
            isChecked.setVisibility(View.GONE);
        }

        isChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("인증되지 않은 사용자입니다.");
                builder.setMessage("학생증을 촬영하여 앞, 뒷면을 닉네임과 함께 보내주세요.");
                builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int id) {
                        String content = "인증과정은 악의적으로 사용하는 유저를 판별해내기 위해 필요한 과정입니다.\n학생증 사진은 학교 인증 후 즉시 폐기될 예정이오니 아래 내용을 지우지 말고 학생증 사진을 첨부하여 도담도담으로 보내주세요.\n\n";
                        content += "이름: " + user.userName + "\n";
                        content += "학교: " + schoolName.getText().toString() + "\n";
                        content += "학년반,: " + gradeNclass.getText().toString() + "\n";
                        content += "\n-----------------------------------------\n";

                        Intent emailintent = new Intent(android.content.Intent.ACTION_SEND);
                        emailintent.setType("plain/text");
                        emailintent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{"dum.dodamdodam@gmail.com"});
                        emailintent.putExtra(android.content.Intent.EXTRA_SUBJECT, "도담도담 학생증 인증");
                        emailintent.putExtra(android.content.Intent.EXTRA_TEXT, content);
                        startActivity(Intent.createChooser(emailintent, "Send mail..."));
                    }
                });
                builder.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                    }
                });
                builder.show();
            }
        });

        recommend_code.setText(user.recommendCode);

        recommend_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent Sharing_intent = new Intent(Intent.ACTION_SEND);
                Sharing_intent.setType("text/plain");

                String Test_Message = user.recommendCode;

                Sharing_intent.putExtra(Intent.EXTRA_TEXT, Test_Message);

                Intent Sharing = Intent.createChooser(Sharing_intent, "공유하기");
                startActivity(Sharing);
            }
        });

        copy_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ClipboardManager clipboardManager = (ClipboardManager) getContext().getSystemService(Context.CLIPBOARD_SERVICE);
                ClipData clipData = ClipData.newPlainText("ID", user.recommendCode); //클립보드에 ID라는 이름표로 id 값을 복사하여 저장
                clipboardManager.setPrimaryClip(clipData);

                Toast.makeText(getContext(), "추천 코드가 복사되었습니다.", Toast.LENGTH_SHORT).show();
            }
        });

        ImageView edit_user_info = view.findViewById(R.id.edit_user_info);
        edit_user_info.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AlertDialog.Builder dig = new AlertDialog.Builder(getContext());
                dig.setTitle("학년, 반 변경하기");
                dig.setMessage("숫자만 입력하세요!");
                final LinearLayout linearLayout = (LinearLayout) v.inflate(getContext(), R.layout.dialog_modify_userinfo, null);
                dig.setView(linearLayout);
                dig.setPositiveButton("변경", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        EditText grade = linearLayout.findViewById(R.id.grade);
                        EditText classNum = linearLayout.findViewById(R.id.class_num);
                        if (TextUtils.isDigitsOnly(grade.getText().toString())) {
                            int g = Integer.parseInt(grade.getText().toString());
                            if (g > 0 && g < 4) {
                                if (TextUtils.isDigitsOnly(classNum.getText().toString())) {
                                    Toast.makeText(getContext(), "변경되었습니다.", Toast.LENGTH_SHORT).show();
                                } else {
                                    Toast.makeText(getContext(), "잘못된 정보입니다.", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getContext(), "잘못된 정보입니다.", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Toast.makeText(getContext(), "잘못된 정보입니다.", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
                dig.setNegativeButton("취소", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {

                    }
                });

                dig.show();
            }
        });
        getMyWork();
        getSurveyLink();
        return view;
    }

    private void getSurveyLink() {
        RetrofitAdapter rAdapter = new RetrofitAdapter();
        RetrofitService service = rAdapter.getInstance(getActivity());
        Call<SurveyResponse> call = service.getSurveyLink();

        call.enqueue(new retrofit2.Callback<SurveyResponse>() {
            @Override
            public void onResponse(Call<SurveyResponse> call, retrofit2.Response<SurveyResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().checkError(getContext()) != 0) {
                        return;
                    }
                    Log.d(TAG, "response" + response.raw());
                    SurveyResponse result = response.body();
                    surveyLink = result.body;
                    survey_code.setText("설문조사 링크입니다. 참여 부탁드립니다.(클릭)");
                    survey_code.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(surveyLink));
                            startActivity(intent);
                        }
                    });
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<SurveyResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    private void getMyWork() {
        RetrofitAdapter rAdapter = new RetrofitAdapter();
        RetrofitService service = rAdapter.getInstance(getActivity());
        Call<MyWorkResponse> call = service.getMyWork();

        call.enqueue(new retrofit2.Callback<MyWorkResponse>() {
            @Override
            public void onResponse(Call<MyWorkResponse> call, retrofit2.Response<MyWorkResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().checkError(getContext()) != 0) {
                        return;
                    }
                    Log.d(TAG, "response" + response.raw());
                    MyWorkResponse result = response.body();
                    my_article_number.setText(String.valueOf(result.body.numArticles));
                    my_comment_number.setText(String.valueOf(result.body.numReplies));
                    my_heart_number.setText(String.valueOf(result.body.numHearts));
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<MyWorkResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.option_menu_mypage, menu);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        View view = getActivity().getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getActivity().getWindow().setStatusBarColor(Color.parseColor("#fbdd56"));
            }
        } else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getActivity().getWindow().setStatusBarColor(Color.BLACK);
        }
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.ask_center:
                Log.d("RHC", "onOptionsItemSelected: 문의하기");
                String uriText =
                        "mailto: dum.dodamdodam@gmail.com" +
                                "?subject=" + Uri.encode("") +
                                "&body=" + Uri.encode("User info" + "\nName: " + user.userName + "\nSchool: " + user.schoolName + "\nGrade: " + user.grade + "\n위의 내용을 지우기 말고 문의해주세요.\n\n");

                Uri uri = Uri.parse(uriText);

                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                startActivity(Intent.createChooser(sendIntent, "Send email"));
                break;
            case R.id.termsNcondition:
                Log.d("RHC", "onOptionsItemSelected: 약관보기");
                ((MainActivity) getActivity()).replaceFragmentFull(new TermsNCondition());

                break;
            case R.id.signout:
                Log.d("RHC", "onOptionsItemSelected: 로그아웃");
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("로그아웃");
                builder.setMessage("로그아웃 하시겠습니까?");
                builder.setPositiveButton("예(로그아웃)",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "로그아웃 중!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), startUpActivity.class);
                                intent.putExtra("login", 1);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });
                builder.setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();
                break;
            case R.id.withdrawal:
                Log.d("RHC", "onOptionsItemSelected: 회원탈퇴");
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                builder2.setTitle("회원탈퇴");
                builder2.setMessage("회원탈퇴하시겠습니까?\n(재가입시 30일 소요)");
                builder2.setPositiveButton("예(회원탈퇴)",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(), "회원탈퇴 중!", Toast.LENGTH_LONG).show();
                                Intent intent = new Intent(getActivity(), startUpActivity.class);
                                intent.putExtra("login", 2);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                                getActivity().finish();
                            }
                        });
                builder2.setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder2.show();
                break;
        }
//        refresh();
        return super.onOptionsItemSelected(item);
    }

    public void refresh() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }

}
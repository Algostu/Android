package com.dum.dodam.Mypage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.Login.startUpActivity;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;

public class Mypage extends Fragment {

    private TextView userName;
    private TextView nickName;
    private TextView emailAddress;
    private ImageView isChecked;
    private TextView schoolName;
    private TextView grade;
    private TextView code;
    //    private TextView age;
//    private TextView gender;
    private TextView withDraw;
    private TextView logout;
    //    private TextView ask_center;
    UserJson user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.mypage, container, false);
        view.setClickable(true);

        user = ((MainActivity) getActivity()).getUser();

        userName = view.findViewById(R.id.user_name);
        nickName = view.findViewById(R.id.nick_name);
        emailAddress = view.findViewById(R.id.mail_address);
        schoolName = view.findViewById(R.id.school_name);
        grade = view.findViewById(R.id.grade);
//        age = view.findViewById(R.id.preage);
//        gender = view.findViewById(R.id.gender);
        isChecked = view.findViewById(R.id.confirm);
//        ask_center = view.findViewById(R.id.ask_center);
        code = view.findViewById(R.id.tv_recommend);
        code.setText(user.recommendCode);

        userName.setText(user.userName);
        nickName.setText(user.nickName);
        emailAddress.setText(user.email);
        schoolName.setText(user.schoolName);
//        age.setText("낭랑 " + String.valueOf(user.age) + "세");

        isChecked.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (user.authorized.equals("1")) {
                    Toast.makeText(getContext(), "인증된 사용자입니다.", Toast.LENGTH_SHORT).show();
                } else {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("인증되지 않은 사용자입니다.");
                    builder.setMessage("학생증을 촬영하여 앞, 뒷면을 닉네임과 함께 보내주세요.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                            String content = "인증과정은 악의적으로 사용하는 유저를 판별해내기 위해 필요한 과정입니다.\n학생증 사진은 학교 인증 후 즉시 폐기될 예정이오니 아래 내용을 지우지 말고 학생증 사진을 첨부하여 도담도담으로 보내주세요.\n\n";
                            content += "이름: " + userName.getText().toString() + "\n";
                            content += "학교: " + schoolName.getText().toString() + "\n";
                            content += "학년: " + grade.getText().toString() + "\n";
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
            }
        });


        if (user.grade == 13) {
            grade.setText("졸업생");
        } else if (user.grade == 12) {
            grade.setText("3학년");
        } else if (user.grade == 11) {
            grade.setText("2학년");
        } else if (user.grade == 10) {
            grade.setText("1학년");
        } else if (user.grade == 9) {
            grade.setText("예비고등");
        } else {
            grade.setText("중학생");
        }

//        if (user.gender == 1) {
//            gender.setText("남학생");
//        } else {
//            gender.setText("여학생");
//        }

        if (user.authorized.equals("1")) {
            isChecked.setImageResource(R.drawable.ic_confirm);
        }

        withDraw = (TextView) view.findViewById(R.id.withdrawal);
        logout = (TextView) view.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
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
            }
        });

        withDraw.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("회월탈퇴");
                builder.setMessage("회원탈퇴하시겠습니까?\n(재가입시 30일 소요)");
                builder.setPositiveButton("예(회원탈퇴)",
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
                builder.setNegativeButton("아니요",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                            }
                        });
                builder.show();
            }
        });

//        TextView mypage_title = (TextView) view.findViewById(R.id.mypage_title);
//        mypage_title.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Snackbar snackbar = Snackbar.make(view, "Snackbar 메시지입니다.\n확인을 누르면 사라집니다.", Snackbar.LENGTH_INDEFINITE);
//                snackbar.setAction("확인", new View.OnClickListener() {
//                    @Override
//                    public void onClick(View v) {
//                        snackbar.dismiss();
//                    }
//                });
//                snackbar.show();
//            }
//        });
//
//        userName.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                final Snackbar snackbar = Snackbar.make(view, "Snackbar 메시지입니다.\n잠시 후 사라집니다.", Snackbar.LENGTH_SHORT);
//                View view = snackbar.getView();
//                FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view.getLayoutParams();
//                params.gravity = Gravity.CENTER_HORIZONTAL | Gravity.TOP;
//
//                view.setLayoutParams(params);
//                snackbar.show();
//            }
//        });

        TextView ask_sentence = view.findViewById(R.id.ask_sentence);
        ask_sentence.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String uriText =
                        "mailto: dum.dodamdodam@gmail.com" +
                                "?subject=" + Uri.encode("") +
                                "&body=" + Uri.encode("User info" + "\nName: " + user.userName + "\nSchool: " + user.schoolName + "\nGrade: " + user.grade + "\n위의 내용을 지우기 말고 문의해주세요.\n\n");

                Uri uri = Uri.parse(uriText);

                Intent sendIntent = new Intent(Intent.ACTION_SENDTO);
                sendIntent.setData(uri);
                startActivity(Intent.createChooser(sendIntent, "Send email"));
            }
        });

        return view;
    }
}
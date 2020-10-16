package com.dum.dodam.Mypage;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
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

import org.w3c.dom.Text;

public class Mypage extends Fragment {

    private TextView userName;
    private TextView nickName;
    private TextView emailAddress;
    private ImageView isChecked;
    private TextView schoolName;
    private TextView grade;
    private TextView age;
    private TextView gender;
    private TextView withDraw;
    private TextView logout;
    UserJson user;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.mypage, container, false);

        user = ((MainActivity) getActivity()).getUser();

        Log.d("Test", "nickName  :  " + user.nickName);
        Log.d("Test", "regionName  :  " + user.regionName);
        Log.d("Test", "townName  :  " + user.townName);
        Log.d("Test", "schoolGender  :  " + user.schoolGender);
        Log.d("Test", "email  :  " + user.email);
        Log.d("Test", "userName  :  " + user.userName);

        userName = view.findViewById(R.id.user_name);
        nickName = view.findViewById(R.id.nick_name);
        emailAddress = view.findViewById(R.id.mail_address);
        schoolName = view.findViewById(R.id.school_name);
        grade = view.findViewById(R.id.grade);
        age = view.findViewById(R.id.preage);
        gender = view.findViewById(R.id.gender);
        isChecked = view.findViewById(R.id.confirm);

        userName.setText(user.userName);
        nickName.setText(user.nickName);
        emailAddress.setText(user.email);
        schoolName.setText(user.schoolName);
        age.setText("낭랑 " + String.valueOf(user.age) + "세");


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


        if (user.gender == 1) {
            gender.setText("남학생");
        } else {
            gender.setText("여학생");
        }

        if (user.authorized.equals("1")) {
            isChecked.setImageResource(R.drawable.ic_confirm);
        }

        withDraw = (TextView)view.findViewById(R.id.withdrawal);
        logout = (TextView)view.findViewById(R.id.logout);

        logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setTitle("로그아웃");
                builder.setMessage("로그아웃 하시겠습니까?");
                builder.setPositiveButton("예(로그아웃)",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Toast.makeText(getContext(),"로그아웃 중!",Toast.LENGTH_LONG).show();
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
                                Toast.makeText(getContext(),"회원탈퇴 중!",Toast.LENGTH_LONG).show();
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

        return view;
    }
}
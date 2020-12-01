package com.dum.dodam.Login;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dum.dodam.R;

public class SignUP extends Fragment {
    private CheckBox checkAll;
    private CheckBox checkBox1;
    private CheckBox checkBox2;
    private CheckBox checkBox3;
    private CheckBox checkBox4;
    private Button btnContinue;
    private TextView contract1;
    private TextView contract2;
    private TextView contract3;
    private TextView contract4;

    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.signup1, container, false);

        View window = getActivity().getWindow().getDecorView();
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
                window.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getActivity().getWindow().setStatusBarColor(Color.parseColor("#fbfbfb"));
            }
        }else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getActivity().getWindow().setStatusBarColor(Color.BLACK);
        }

        btnContinue = view.findViewById(R.id.continues);
        checkAll = view.findViewById(R.id.checkAll);
        checkBox1 = view.findViewById(R.id.check1);
        checkBox2 = view.findViewById(R.id.check2);
        checkBox3 = view.findViewById(R.id.check3);
        checkBox4 = view.findViewById(R.id.check4);
        contract1 = view.findViewById(R.id.contract1);
        contract2 = view.findViewById(R.id.contract2);
        contract3 = view.findViewById(R.id.contract3);
        contract4 = view.findViewById(R.id.contract4);

        contract1.setMovementMethod(new ScrollingMovementMethod());
        contract2.setMovementMethod(new ScrollingMovementMethod());
        contract3.setMovementMethod(new ScrollingMovementMethod());
        contract4.setMovementMethod(new ScrollingMovementMethod());

        contract1.setText(getString(R.string.terms_and_conditions1));
        contract2.setText(getString(R.string.terms_and_conditions2));
        contract3.setText(getString(R.string.terms_and_conditions3));
        contract4.setText(getString(R.string.terms_and_conditions4));

        checkAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkAll.isChecked()) {
                    if (!checkBox1.isChecked()) checkBox1.toggle();
                    if (!checkBox2.isChecked()) checkBox2.toggle();
                    if (!checkBox3.isChecked()) checkBox3.toggle();
                    if (!checkBox4.isChecked()) checkBox4.toggle();
                } else {
                    if (checkBox1.isChecked()) checkBox1.toggle();
                    if (checkBox2.isChecked()) checkBox2.toggle();
                    if (checkBox3.isChecked()) checkBox3.toggle();
                    if (checkBox4.isChecked()) checkBox4.toggle();
                }
            }
        });

        checkBox4.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBox1.isChecked() && checkBox2.isChecked() && checkBox3.isChecked() && checkBox4.isChecked()) {
                    if (!checkAll.isChecked()) checkAll.toggle();
                } else {
                    if (checkAll.isChecked()) checkAll.toggle();
                }
            }
        });

        btnContinue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox1.isChecked() && checkBox2.isChecked() && checkBox3.isChecked()) {
                    ((startUpActivity) getActivity()).replaceFragment(new SIgnUP2());
                } else {
                    Toast.makeText(getContext(), "필수 약관에 동의해야지만 가입할 수 있습니다.", Toast.LENGTH_LONG).show();
                }
            }
        });

        return view;
    }

}

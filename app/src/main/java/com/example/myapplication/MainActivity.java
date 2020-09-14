package com.example.myapplication;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

public class MainActivity extends AppCompatActivity {
    private Button btn_home;
    private Button btn_school;
    private Button btn_board;
    private Button btn_simulation;
    private Button btn_mypage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        btn_home = findViewById(R.id.btn_home);
        btn_school = findViewById(R.id.btn_school);
        btn_board = findViewById(R.id.btn_board);
        btn_simulation = findViewById(R.id.btn_simulation);
        btn_mypage = findViewById(R.id.btn_mypage);


        btn_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new Home());
            }
        });
        btn_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new School());
            }
        });
        btn_board.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new Board());
            }
        });
        btn_simulation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new Simulation());
            }
        });
        btn_mypage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                replaceFragment(new Mypage());
            }
        });
    }

    public void replaceFragment(Fragment fragment){
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.fragment_main,fragment);
        transaction.commit();
    }
}




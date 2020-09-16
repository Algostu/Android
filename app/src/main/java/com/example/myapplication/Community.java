package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class Community extends Fragment {

    private Button btn_school;
    private Button btn_town;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community, container, false);
        btn_school = (Button) view.findViewById(R.id.school_community);
        btn_town = (Button) view.findViewById(R.id.town_community);

        btn_school.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentFull(new CommunityList("School"));
            }
        });

        btn_town.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentFull(new CommunityList("Town"));
            }
        });


        return view;
    }
}
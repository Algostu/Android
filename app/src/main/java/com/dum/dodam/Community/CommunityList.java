package com.dum.dodam.Community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.dum.dodam.Home.dataframe.MyCommunityFrame2;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;

import java.util.ArrayList;

//        R.id.school_free        article_type = 2;
//        R.id.school_question   article_type = 1;
//        R.id.region_free       article_type = 3;
//        R.id.region_recruit    article_type = 4;
//        R.id.region_question   article_type = 5;
//        R.id.country_entry     article_type = 6;
//        R.id.country_academy   article_type = 7;
//        R.id.country_univ      article_type = 8;

public class CommunityList extends Fragment {
    private static final String TAG = "RHC";
    ArrayList<MyCommunityFrame2> comAll;
    ArrayList<MyCommunityFrame2> comRegion;
    ArrayList<MyCommunityFrame2> comSchool;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        comAll = ((MainActivity) getActivity()).user.comAll;
        comRegion = ((MainActivity) getActivity()).user.comRegion;
        comSchool = ((MainActivity) getActivity()).user.comSchool;

        View view = inflater.inflate(R.layout.community_list, container, false);

        textViewClickListener(view, R.id.school_question, comSchool.get(0).communityID, 2);
        textViewClickListener(view, R.id.school_free, comSchool.get(1).communityID, 2);
        textViewClickListener(view, R.id.region_question, comRegion.get(0).communityID, 1);
        textViewClickListener(view, R.id.region_recruit, comRegion.get(1).communityID, 1);
        textViewClickListener(view, R.id.region_free, comRegion.get(2).communityID, 1);
        textViewClickListener(view, R.id.country_academy, comAll.get(0).communityID, 0);
        textViewClickListener(view, R.id.country_question, comAll.get(1).communityID, 0);
        textViewClickListener(view, R.id.country_free, comAll.get(2).communityID, 0);

        return view;
    }

    public void textViewClickListener(View view, final int id, final int communityID, final int communityType) {
        TextView textView = view.findViewById(id);

        String title = new String();
        String type = "";
        ArrayList<MyCommunityFrame2> com = new ArrayList<MyCommunityFrame2>();
        if (0 == communityType) {
            type = "전국 ";
            com = comAll;
        } else if (1 == communityType) {
            type = "지역 ";
            com = comRegion;
        } else if (2 == communityType) {
            type = "교내 ";
            com = comSchool;
        }
        for (MyCommunityFrame2 frame : com) {
            if (frame.communityID == communityID)
                title = type + frame.title;
        }
        textView.setText(title);

        final String finalTitle = title;
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentFull(new Community(communityType, communityID, finalTitle));
            }
        });
    }
}
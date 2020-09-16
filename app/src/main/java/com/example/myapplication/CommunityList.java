package com.example.myapplication;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

public class CommunityList extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<ArticleList> list = new ArrayList<>();

    private String community_name;

    public CommunityList(String community_name) {
        this.community_name = community_name;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_list, container, false);

        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText(community_name + " Community");
        ImageButton btn_write_article = (ImageButton) view.findViewById(R.id.btn_write_article);

        btn_write_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentFull(new ArticleWrite());
            }
        });

        list = ArticleList.tempFunctionCreateArticle(10);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_articles);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        adapter = new ArticleAdapter(getContext(), list);
        recyclerView.setAdapter(adapter);

        return view;
    }

    @Override
    public void onDestroy() {
        BottomNavigationView navigation = getActivity().findViewById(R.id.nav_bar);
        navigation.setVisibility(View.VISIBLE);
        super.onDestroy();
    }
}

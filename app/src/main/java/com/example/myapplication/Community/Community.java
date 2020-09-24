package com.example.myapplication.Community;

import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Community.dataframe.ArticleList;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;


public class Community extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String community_name;
    private int article_type;

    private ArrayList<ArticleList> list = new ArrayList<>();

    public Community(int article_type, ArrayList<ArticleList> list) {
        this.list = list;
        this.article_type = article_type;
        if (article_type == 2 || article_type == 3 || article_type == 6)
            this.community_name = "자유 게시판";
        else if (article_type == 1 || article_type == 5)
            this.community_name = "질문 게시판";
        else if (article_type == 4)
            this.community_name = "모집 게시판";
        else if (article_type == 7)
            this.community_name = "학원&인강 게시판";
        else
            this.community_name = "대학 게시판";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.article_list, container, false);

        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText(community_name);
        ImageButton btn_write_article = (ImageButton) view.findViewById(R.id.btn_write_article);

        btn_write_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentFull(new ArticleWrite(article_type));
            }
        });

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

    @Override
    public void onResume() {
        adapter.notifyDataSetChanged();
        super.onResume();
    }
}
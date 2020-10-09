package com.dum.dodam.Community;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Community.dataframe.ArticleListFrame;
import com.dum.dodam.Community.dataframe.ArticleListResponse;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Community extends Fragment implements ArticleListAdapter.OnListItemSelectedInterface {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "RHC";
    private int cnt_readArticleList = 0;

    private String community_name;
    private int communityID;
    private int communityType;

    private ArrayList<ArticleListFrame> list = new ArrayList<>();

    public Community(int communityType, int communityID) {
        this.communityID = communityID;
        this.communityType = communityType;

        if (communityID == 2 || communityID == 3 || communityID == 6)
            this.community_name = "자유 게시판";
        else if (communityID == 1 || communityID == 5)
            this.community_name = "질문 게시판";
        else if (communityID == 4)
            this.community_name = "모집 게시판";
        else if (communityID == 7)
            this.community_name = "학원&인강 게시판";
        else
            this.community_name = "대학 게시판";
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.article_list, container, false);
        view.setClickable(true);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText(community_name);
        ImageButton btn_write_article = (ImageButton) view.findViewById(R.id.btn_write_article);

        btn_write_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentFull(new ArticleWrite(communityType, communityID));
            }
        });

        adapter = new ArticleListAdapter(getContext(), list, this);

        readArticleList();

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_articles);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
        return view;
    }

    @Override
    public void onDestroy() {
        BottomNavigationView navigation = getActivity().findViewById(R.id.nav_bar);
        View nav_view = getActivity().findViewById(R.id.nav_view);
        nav_view.setVisibility(View.VISIBLE);
        navigation.setVisibility(View.VISIBLE);
        super.onDestroy();
    }

    public void readArticleList() {
        com.dum.dodam.httpConnection.RetrofitService service = RetrofitAdapter.getInstance("http://49.50.164.11:5000/", getContext());

        long now = System.currentTimeMillis();
        Date data = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-mm-dd HH:MM:SS");
        String date;
        if (list.isEmpty()) {
            date = dateFormat.format(data);
        } else {
            date = list.get(list.size() - 1).writtenTime;
        }
//        Call<ArticleListResponse> call = service.goArticle(communityType, communityID, date);
        Call<ArticleListResponse> call = service.goArticle(0, 1, "latest");

        call.enqueue(new Callback<ArticleListResponse>() {
            @Override
            public void onResponse(Call<ArticleListResponse> call, Response<ArticleListResponse> response) {
                if (response.isSuccessful()) {
                    ArticleListResponse result = response.body();
                    if (result.checkError(getActivity()) != 0) return;
                    list.addAll(result.body);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Upload Fail", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: Fail");
                }
            }

            @Override
            public void onFailure(Call<ArticleListResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                if (cnt_readArticleList < 5) readArticleList();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_readArticleList++;
            }
        });
    }

    @Override
    public void onResume() {
        list.clear();
        readArticleList();
        super.onResume();
    }

    @Override
    public void onItemSelected(View v, int position) {
        ArticleListAdapter.Holder holder = (ArticleListAdapter.Holder) recyclerView.findViewHolderForAdapterPosition(position);
        String article_ID = holder.articleID.getText().toString();

        ((MainActivity) getActivity()).replaceFragmentFull(new Article(Integer.parseInt(article_ID), communityType, communityID));
    }
}
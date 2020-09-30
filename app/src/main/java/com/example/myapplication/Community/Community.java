package com.example.myapplication.Community;

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

import com.example.myapplication.Community.dataframe.ArticleListFrame;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;


public class Community extends Fragment implements ArticleAdapter.OnListItemSelectedInterface {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "RHC";
    private int cnt_readArticleList = 0;

    private String community_name;
    private int article_type;

    private ArrayList<ArticleListFrame> list = new ArrayList<>();

    public Community(int article_type) {
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
        view.setClickable(true);
        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText(community_name);
        ImageButton btn_write_article = (ImageButton) view.findViewById(R.id.btn_write_article);

        btn_write_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentFull(new ArticleWrite(article_type));
            }
        });

        adapter = new ArticleAdapter(getContext(), list, this);

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://49.50.164.11:5000/article/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<ArrayList<ArticleListFrame>> call = service.goArticle(article_type, "latest");

        call.enqueue(new Callback<ArrayList<ArticleListFrame>>() {
            @Override
            public void onResponse(Call<ArrayList<ArticleListFrame>> call, Response<ArrayList<ArticleListFrame>> response) {
                if (response.isSuccessful()) {
                    ArrayList<ArticleListFrame> result = response.body();
                    list.clear();
                    list.addAll(result);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Upload Fail", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: Fail");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ArticleListFrame>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                if (cnt_readArticleList < 10) readArticleList();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_readArticleList++;
            }
        });
    }

    @Override
    public void onResume() {
        readArticleList();
        super.onResume();
    }

    @Override
    public void onItemSelected(View v, int position) {
        ArticleAdapter.Holder holder = (ArticleAdapter.Holder) recyclerView.findViewHolderForAdapterPosition(position);
        String article_ID = holder.article_ID.getText().toString();

        ((MainActivity) getActivity()).replaceFragmentFull(new Article(Integer.parseInt(article_ID), article_type));
    }

    public interface RetrofitService {
        @GET("articleList")
        Call<ArrayList<ArticleListFrame>> goArticle(@Query("articleType") int article_type, @Query("articleTime") String articleTime);
    }
}
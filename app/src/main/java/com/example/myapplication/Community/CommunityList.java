package com.example.myapplication.Community;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Community.dataframe.ArticleListFrame;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

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

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community_list, container, false);
        textViewClickListener(view, R.id.school_free, 2);
        textViewClickListener(view, R.id.school_question, 1);
        textViewClickListener(view, R.id.region_free, 3);
        textViewClickListener(view, R.id.region_recruit, 4);
        textViewClickListener(view, R.id.region_question, 5);
        textViewClickListener(view, R.id.country_entry, 6);
        textViewClickListener(view, R.id.country_academy, 7);
        textViewClickListener(view, R.id.country_univ, 8);
        return view;
    }

    public void textViewClickListener(View view, final int id, final int article_type) {
        TextView textView = view.findViewById(id);
        textView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

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

                            ((MainActivity) getActivity()).replaceFragmentFull(new Community(article_type, result));
                        } else {
                            Toast.makeText(getContext(), "No data", Toast.LENGTH_SHORT).show();
                            Log.d(TAG, "onResponse: Fail");
                        }
                    }

                    @Override
                    public void onFailure(Call<ArrayList<ArticleListFrame>> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                    }
                });
            }
        });
    }

    public interface RetrofitService {
        //        @FormUrlEncoded
        @GET("articleList")
        Call<ArrayList<ArticleListFrame>> goArticle(@Query("articleType") int article_type, @Query("articleTime") String articleTime);
    }

}
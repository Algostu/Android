package com.example.myapplication.Community;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Community.dataframe.ArticleFrame;
import com.example.myapplication.R;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Article extends Fragment {
    private static final String TAG = "RHC";

    private TextView writer;
    private TextView title;
    private TextView content;
    private TextView time;
    private TextView reply;
    private TextView heart;
    private TextView article_ID;
    private int articleID;
    private int articleType;

    public Article(int articleID, int articleType) {
        this.articleID = articleID;
        this.articleType = articleType;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.article_, container, false);
        view.setClickable(true);

        writer = view.findViewById(R.id.writer);
        title = view.findViewById(R.id.title);
        content = view.findViewById(R.id.content);
        time = view.findViewById(R.id.time);
        reply = view.findViewById(R.id.reply);
        heart = view.findViewById(R.id.heart);
        article_ID = view.findViewById(R.id.article_ID);
        readArticle();

        return view;
    }

    public void readArticle() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://49.50.164.11:5000/article/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<ArticleFrame> call = service.readArticle(articleID, articleType);

        call.enqueue(new retrofit2.Callback<ArticleFrame>() {
            @Override
            public void onResponse(Call<ArticleFrame> call, retrofit2.Response<ArticleFrame> response) {
                if (response.isSuccessful()) {
                    ArticleFrame articleFrame = response.body();
                    writer.setText(articleFrame.nickName);
                    title.setText(articleFrame.title);
                    content.setText(articleFrame.content);
                    time.setText(articleFrame.writtenTime);
                    reply.setText(articleFrame.reply);
                    heart.setText(articleFrame.heart);
                    article_ID.setText(articleFrame.articleID);
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ArticleFrame> call, Throwable t) {
                readArticle();
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public interface RetrofitService {
        @GET("read")
        Call<ArticleFrame> readArticle(@Query("articleID") int articleID, @Query("articleType") int articleType);
    }
}

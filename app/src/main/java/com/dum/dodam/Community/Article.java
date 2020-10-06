package com.dum.dodam.Community;

import android.annotation.SuppressLint;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Community.dataframe.ArticleCommentFrame;
import com.dum.dodam.Community.dataframe.ArticleFrame;
import com.dum.dodam.R;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public class Article extends Fragment implements ArticleCommentAdapter.OnListItemSelectedInterface {
    private static final String TAG = "RHC";

    private int cnt_readArticle = 0;
    private int cnt_readArticleComment = 0;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<ArticleCommentFrame> list = new ArrayList<>();
    private TextView writer;
    private TextView title;
    private TextView content;
    private TextView time;
    private TextView reply;
    private TextView heart;
    private TextView article_ID;

    private ImageView upload_comment;
    private EditText comment;
    private CheckBox ck_isAnonymous;

    private int articleID;
    private int articleType;
    private int parentReplyID = 0;

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
        comment = view.findViewById(R.id.comment);
        ck_isAnonymous = view.findViewById(R.id.ck_isAnonymous);
        upload_comment = view.findViewById(R.id.upload_comment);

        upload_comment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (comment.getText().toString().matches("")) {
                    Toast.makeText(getContext(), "내용을 입력하세요", Toast.LENGTH_SHORT).show();
                } else {
                    UploadComment();
                }
            }
        });

        readArticle();

        adapter = new ArticleCommentAdapter(getContext(), list, this);

        readArticleComment();

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_comment);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

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
                Log.d(TAG, "onFailure: " + t.getMessage());
                if (cnt_readArticle < 10) readArticle();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_readArticle++;
            }
        });
    }

    public void readArticleComment() {
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://49.50.164.11:5000/article/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<ArrayList<ArticleCommentFrame>> call = service.readArticleComment(articleID, articleType);

        call.enqueue(new retrofit2.Callback<ArrayList<ArticleCommentFrame>>() {
            @Override
            public void onResponse(Call<ArrayList<ArticleCommentFrame>> call, retrofit2.Response<ArrayList<ArticleCommentFrame>> response) {
                if (response.isSuccessful()) {
                    ArrayList<ArticleCommentFrame> articleCommentList = response.body();
                    list.clear();
                    list.addAll(articleCommentList);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ArticleCommentFrame>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                if (cnt_readArticleComment < 10) readArticle();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_readArticleComment++;
            }
        });
    }

    public void UploadComment() {

        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("userId", 1);
        paramObject.addProperty("parentReplyID", parentReplyID);
        paramObject.addProperty("isAnonymous", ck_isAnonymous.isChecked());
        paramObject.addProperty("content", comment.getText().toString());

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://49.50.164.11:5000/article/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<String> call = service.uploadComment(paramObject);

        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    String result = response.body();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body().toString());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                if (cnt_readArticleComment < 10) readArticle();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_readArticleComment++;
            }
        });
    }

    public interface RetrofitService {
        @GET("read")
        Call<ArticleFrame> readArticle(@Query("articleID") int articleID, @Query("articleType") int articleType);

        @GET("readComment")
        Call<ArrayList<ArticleCommentFrame>> readArticleComment(@Query("articleID") int articleID, @Query("articleType") int articleType);

        @POST("uploadComment")
        Call<String> uploadComment(@Body JsonObject body);
    }

    @SuppressLint("ResourceType")
    @Override
    public void onItemSelected(View v, int position) {
        ArticleCommentAdapter.CommentViewHolder holder = (ArticleCommentAdapter.CommentViewHolder) recyclerView.findViewHolderForAdapterPosition(position);
        int replyID = holder.replyID;
        if (parentReplyID == 0) {
            parentReplyID = replyID;
            v.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.coral_pink));
        } else {
            parentReplyID = 0;
            v.setBackgroundColor(ContextCompat.getColor(getContext(), Color.TRANSPARENT));
        }
    }
}

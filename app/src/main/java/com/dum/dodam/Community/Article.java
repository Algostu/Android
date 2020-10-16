package com.dum.dodam.Community;

import android.animation.ObjectAnimator;
import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Community.dataframe.ArticleCommentFrame;
import com.dum.dodam.Community.dataframe.ArticleCommentResponse;
import com.dum.dodam.Community.dataframe.ArticleFrame;
import com.dum.dodam.Community.dataframe.ArticleResponse;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.httpConnection.BaseResponse;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.facebook.shimmer.Shimmer;
import com.facebook.shimmer.ShimmerFrameLayout;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import retrofit2.Call;

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
    private ImageView heart_ic;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ActionBar actionbar;

    private ImageView upload_comment;
    private EditText comment;
    private CheckBox ck_isAnonymous;

    private int articleID;
    private int communityType;
    private int communityID;
    private int parentReplyID = 0;
    private int added_heart = 0;
    private int org_heart;
    private int edit;

//    private ShimmerFrameLayout shimmerFrameLayout;

    public Article(int articleID, int communityType, int communityID) {
        this.articleID = articleID;
        this.communityType = communityType;
        this.communityID = communityID;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.article_, container, false);
        view.setClickable(true);
        setHasOptionsMenu(true);

//        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
//        shimmerFrameLayout.startShimmer();

        ((MainActivity) getActivity()).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_PAN);

        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionbar.setDisplayHomeAsUpEnabled(true);

        writer = view.findViewById(R.id.writer);
        title = view.findViewById(R.id.title);
        content = view.findViewById(R.id.content);
        time = view.findViewById(R.id.time);
        reply = view.findViewById(R.id.reply);
        heart = view.findViewById(R.id.heart);
        heart_ic = view.findViewById(R.id.icon_heart);

        comment = view.findViewById(R.id.comment);
        ck_isAnonymous = view.findViewById(R.id.ck_isAnonymous);
        upload_comment = view.findViewById(R.id.upload_comment);

        heart_ic.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (added_heart == 0) added_heart = 1;
                else added_heart = 0;
                Log.d("hi", "image view");
                RetrofitAdapter adapter = new RetrofitAdapter();
                com.dum.dodam.httpConnection.RetrofitService service = adapter.getInstance(getContext());
                Call<BaseResponse> call = service.modifyHeart(articleID, communityType, communityID, added_heart);

                call.enqueue(new retrofit2.Callback<BaseResponse>() {
                    @Override
                    public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
                        if (response.isSuccessful()) {
                            BaseResponse result = response.body();
                            if (result.checkError(getActivity()) != 0) return;
                            heart.setText(String.valueOf(org_heart + added_heart));
                            if (added_heart == 0) {
                                heart_ic.setImageResource(R.drawable.ic_heart);
                            } else {
                                heart_ic.setImageResource(R.drawable.explodin_heart);
                            }
                        } else {
                            Log.d(TAG, "onResponse: Fail " + response.body());
                        }
                    }

                    @Override
                    public void onFailure(Call<BaseResponse> call, Throwable t) {
                        Log.d(TAG, "onFailure: " + t.getMessage());
                        Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

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
        RetrofitAdapter adapter = new RetrofitAdapter();
        com.dum.dodam.httpConnection.RetrofitService service = adapter.getInstance(getContext());
        Call<ArticleResponse> call = service.readArticle(articleID, communityType, communityID);

        call.enqueue(new retrofit2.Callback<ArticleResponse>() {
            @Override
            public void onResponse(Call<ArticleResponse> call, retrofit2.Response<ArticleResponse> response) {
                if (response.isSuccessful()) {
                    ArticleResponse result = response.body();
                    if (result.checkError(getActivity()) != 0) return;
                    ArticleFrame articleFrame = result.body;
                    writer.setText(articleFrame.nickName);
                    title.setText(articleFrame.title);
                    content.setText(articleFrame.content);
                    time.setText(articleFrame.writtenTime);
                    reply.setText(articleFrame.reply);
                    heart.setText(articleFrame.heart);
                    org_heart = Integer.parseInt(articleFrame.heart);
                    added_heart = articleFrame.heartPushed;

//                    writer.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
//                    title.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
//                    content.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
//                    reply.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
//                    heart.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
//                    time.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
//
//                    shimmerFrameLayout.stopShimmer();
//                    shimmerFrameLayout.setVisibility(View.GONE);

                    edit = articleFrame.edit;

                    if (added_heart == 0) {
                        heart_ic.setImageResource(R.drawable.ic_heart);
                    } else {
                        heart_ic.setImageResource(R.drawable.explodin_heart);
                    }
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<ArticleResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                if (cnt_readArticle < 10) readArticle();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_readArticle++;
            }
        });
    }

    public void readArticleComment() {
        RetrofitAdapter rAdapter = new RetrofitAdapter();
        com.dum.dodam.httpConnection.RetrofitService service = rAdapter.getInstance(getContext());
        Call<ArticleCommentResponse> call = service.readArticleComment(articleID, communityType, communityID);

        call.enqueue(new retrofit2.Callback<ArticleCommentResponse>() {
            @Override
            public void onResponse(Call<ArticleCommentResponse> call, retrofit2.Response<ArticleCommentResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<ArticleCommentFrame> replys = response.body().body.replys;
                    ArrayList<ArticleCommentFrame> reReplys = response.body().body.reReplys;
                    list.clear();
                    for (ArticleCommentFrame reply : replys) {
                        list.add(reply);
                        for (ArticleCommentFrame reReply : reReplys) {
                            if (reReply.parentReplyID == reply.replyID) {
                                list.add(reReply);
                            }
                        }
                    }
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<ArticleCommentResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                if (cnt_readArticleComment < 10) readArticle();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_readArticleComment++;
            }
        });
    }

    public void UploadComment() {
        JsonObject paramObject = new JsonObject();
        paramObject.addProperty("communityType", communityType);
        paramObject.addProperty("communityID", communityID);
        paramObject.addProperty("articleID", articleID);
        paramObject.addProperty("parentID", parentReplyID);
        paramObject.addProperty("isAnonymous", ck_isAnonymous.isChecked());
        paramObject.addProperty("content", comment.getText().toString());

        Log.d(TAG, "올릴 때 " + String.valueOf(parentReplyID));

        com.dum.dodam.httpConnection.RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<String> call = service.uploadComment(paramObject);

        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    String result = response.body();
                    comment.setText("");
                    comment.clearFocus();
                    refresh();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
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

    public void DeleteArticle() {
        com.dum.dodam.httpConnection.RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<String> call = service.deleteArticle(communityType, communityID, articleID);
        call.enqueue(new retrofit2.Callback<String>() {
            @Override
            public void onResponse(Call<String> call, retrofit2.Response<String> response) {
                if (response.isSuccessful()) {
                    String result = response.body();
                    refresh();
                    Toast.makeText(getContext(), "삭제되었습니다.", Toast.LENGTH_SHORT).show();
                    getActivity().getSupportFragmentManager().popBackStack();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<String> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                if (cnt_readArticleComment < 5) readArticle();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_readArticleComment++;
            }
        });
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
            v.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.transparent));
        }
    }

    @Override
    public void onPrepareOptionsMenu(@NonNull Menu menu) {
        if (edit == 1) {
            MenuItem menu2 = menu.findItem(R.id.report);
            menu2.setVisible(false);
            MenuItem menu1 = menu.findItem(R.id.delete);
            menu1.setVisible(true);
        } else {
            MenuItem menu2 = menu.findItem(R.id.report);
            menu2.setVisible(true);
            MenuItem menu1 = menu.findItem(R.id.delete);
            menu1.setVisible(false);
        }
        super.onPrepareOptionsMenu(menu);
    }

    @Override
    public void onCreateOptionsMenu(@NonNull Menu menu, @NonNull MenuInflater inflater) {
        MenuInflater menuInflater = getActivity().getMenuInflater();
        menuInflater.inflate(R.menu.option_menu, menu);
        super.onCreateOptionsMenu(menu, inflater);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case R.id.delete:
                DeleteArticle();
                break;
            case R.id.report:
                //
                break;
            case android.R.id.home:
                //select back button
                getActivity().getSupportFragmentManager().popBackStack();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    public void refresh() {
        FragmentTransaction transaction = getActivity().getSupportFragmentManager().beginTransaction();
        transaction.detach(this).attach(this).commit();
    }

}

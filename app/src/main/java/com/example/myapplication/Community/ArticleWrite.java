package com.example.myapplication.Community;

import android.content.Context;
import android.os.Bundle;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ImageButton;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import com.example.myapplication.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;

import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class ArticleWrite extends Fragment {

    public static final MediaType JSON = MediaType.get("application/json; charset=utf-8");

    private int user_id = 1;
    private boolean is_anonymous = true;
    private int article_type;
    private String content;
    private String title;

    private CheckBox checkBox;
    private EditText et_title;
    private EditText et_content;
    private ImageButton btn_upload_article;

    public ArticleWrite(int article_type) {
        this.article_type = article_type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        getActivity().getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);

        View view = inflater.inflate(R.layout.wrtie_article, container, false);

        checkBox = view.findViewById(R.id.checkbox_isAnonymous);
        et_title = view.findViewById(R.id.et_title);
        et_content = view.findViewById(R.id.et_content);

        et_title.requestFocus();
        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        inputMethodManager.toggleSoftInput(InputMethodManager.SHOW_FORCED, 0);

        btn_upload_article = view.findViewById(R.id.btn_upload_article);
        btn_upload_article.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (checkBox.isChecked()) {
                    is_anonymous = true;
                } else {
                    is_anonymous = false;
                }
                title = et_title.getText().toString();
                content = et_content.getText().toString();

                connectServer(view);
                InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.toggleSoftInput(InputMethodManager.HIDE_IMPLICIT_ONLY, 0);

                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
        return view;
    }

    public void connectServer(View view) {
        String postUrl = "http://49.50.164.11:5000/article/write";

        JSONObject json = new JSONObject();
        try {
            json.put("articleType", article_type);
            json.put("userId", user_id);
            json.put("isAnonymous", is_anonymous);
            json.put("title", title);
            json.put("content", content);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        RequestBody body = RequestBody.create(json.toString(), JSON);
        postRequest(postUrl, body);
    }

    public void postRequest(String postUrl, RequestBody postBody) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .post(postBody)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
            }
        });
    }

}

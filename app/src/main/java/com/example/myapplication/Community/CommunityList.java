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

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Community.dataframe.ArticleList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

//        R.id.school_free        article_type = 2;
//        R.id.school_question   article_type = 1;
//        R.id.region_free       article_type = 3;
//        R.id.region_recruit    article_type = 4;
//        R.id.region_question   article_type = 5;
//        R.id.country_entry     article_type = 6;
//        R.id.country_academy   article_type = 7;
//        R.id.country_univ      article_type = 8;

public class CommunityList extends Fragment {
    public static String TAG = "RHC";
    private ArrayList<ArticleList> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.community, container, false);
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
                String url = String.format("http://49.50.164.11:5000/article/articleList?articleType=%d&articleTime=latest", article_type);
                list.clear();
                getRequest(url, article_type);
            }
        });
    }

    public void getRequest(String postUrl, final int article_type) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Log.d(TAG, "serverFailure: " + e.toString());
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String result = response.body().string();
//                result = result.substring(1, result.length() - 1);
                try {
                    JSONArray jsonArray = new JSONArray(result);
                    Log.d(TAG, "josnArray.length = " + jsonArray.length());
                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);
                        list.add(new ArticleList(Integer.parseInt(jsonObject.get("articleId").toString()),
                                jsonObject.get("title").toString(),
                                jsonObject.get("content").toString(),
                                jsonObject.get("nickName").toString(),
                                jsonObject.get("writtenTime").toString(),
                                Integer.parseInt(jsonObject.get("heart").toString()),
                                Integer.parseInt(jsonObject.get("reply").toString())));
                    }
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity) getActivity()).replaceFragmentFull(new Community(article_type, list));
                        }
                    });

                } catch (JSONException e) {
                    Log.d(TAG, "JSON: False " + e.toString());
                    e.printStackTrace();
                }
            }
        });
    }
}
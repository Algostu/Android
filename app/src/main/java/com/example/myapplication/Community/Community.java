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

import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.example.myapplication.Community.dataframe.ArticleFrame;
import com.google.android.material.bottomnavigation.BottomNavigationView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;


public class Community extends Fragment implements ArticleAdapter.OnListItemSelectedInterface {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "RHC";

    private String community_name;
    private int article_type;

    private ArrayList<ArticleFrame> list = new ArrayList<>();

    public Community(int article_type, ArrayList<ArticleFrame> list) {
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

        adapter = new ArticleAdapter(getContext(), list, this);
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


    @Override
    public void onItemSelected(View v, int position) {
        ArticleAdapter.Holder holder = (ArticleAdapter.Holder) recyclerView.findViewHolderForAdapterPosition(position);
        // https://thepassion.tistory.com/300 ID가 제대로 안들어옴
        
        String article_ID = holder.article_ID.toString();

        Log.d(TAG, "ARTICLEID:" + article_ID);
        String url = String.format("http://49.50.164.11:5000/article/read?articleID=%s&articleType=%d", article_ID, article_type);
        getRequest(url);
    }

    public void getRequest(String postUrl) {
        OkHttpClient client = new OkHttpClient();

        Request request = new Request.Builder()
                .url(postUrl)
                .build();

        client.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(okhttp3.Call call, IOException e) {
                Toast.makeText(getContext(), "Server error", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onResponse(okhttp3.Call call, Response response) throws IOException {
                String result = response.body().string();
                Bundle bundle = new Bundle();
                final Fragment fragment_article = new Article();

                try {
                    Log.d(TAG, "onResponse: " + result);
                    JSONObject jsonObject = new JSONObject(result);
                    Log.d(TAG, "josnobject.tostring = " + jsonObject.toString());

                    bundle.putString("writer", jsonObject.get("nickName").toString());
                    bundle.putString("title", jsonObject.get("title").toString());
                    bundle.putString("content", jsonObject.get("content").toString());
                    bundle.putString("time", jsonObject.get("writtenTime").toString());
                    bundle.putInt("reply", Integer.parseInt(jsonObject.get("reply").toString()));
                    bundle.putInt("heart", Integer.parseInt(jsonObject.get("heart").toString()));
                    bundle.putInt("article_ID", (Integer.parseInt(jsonObject.get("articleId").toString())));

                    fragment_article.setArguments(bundle);

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            ((MainActivity) getActivity()).replaceFragmentFull(fragment_article);
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
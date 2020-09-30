package com.example.myapplication.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.myapplication.Community.Article;
import com.example.myapplication.Community.Community;
import com.example.myapplication.Home.dataframe.HotArticleFrame;
import com.example.myapplication.Home.dataframe.MyCommunityFrame;
import com.example.myapplication.MainActivity;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;

public class Home extends Fragment implements HotArticleAdapter.OnListItemSelectedInterface, MyCommunityAdapter.OnListItemSelectedInterface {
    private static final String TAG = "RHC";

    private RecyclerView myCommunity_recyclerView;
    private RecyclerView.Adapter myCommunity_adapter;

    private RecyclerView hotArticle_recyclerView;
    private RecyclerView.Adapter hotArticle_adapter;

    private RecyclerView.LayoutManager myCommunity_layoutManager;
    private RecyclerView.LayoutManager hotArticle_layoutManager;

    private ArrayList<MyCommunityFrame> myCommunityList = new ArrayList<>();
    private ArrayList<HotArticleFrame> hotArticleList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_, container, false);
        view.setClickable(true);

        myCommunity_adapter = new MyCommunityAdapter(getContext(), myCommunityList, this);
        setMyCommunity();
        myCommunity_recyclerView = (RecyclerView) view.findViewById(R.id.rv_my_community);
        myCommunity_recyclerView.setHasFixedSize(true);
        myCommunity_layoutManager = new LinearLayoutManager(getActivity());
        myCommunity_recyclerView.setLayoutManager(myCommunity_layoutManager);
        myCommunity_recyclerView.setAdapter(myCommunity_adapter);

        hotArticle_adapter = new HotArticleAdapter(getContext(), hotArticleList, this);
        setHotArticle();
        hotArticle_recyclerView = (RecyclerView) view.findViewById(R.id.rv_hot_article);
        hotArticle_recyclerView.setHasFixedSize(true);
        hotArticle_layoutManager = new LinearLayoutManager(getActivity());
        hotArticle_recyclerView.setLayoutManager(hotArticle_layoutManager);
        hotArticle_recyclerView.setAdapter(hotArticle_adapter);

        return view;
    }

    public void setMyCommunity() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://49.50.164.11:5000/article/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<ArrayList<MyCommunityFrame>> call = service.getMyCommunity();

        call.enqueue(new retrofit2.Callback<ArrayList<MyCommunityFrame>>() {
            @Override
            public void onResponse(Call<ArrayList<MyCommunityFrame>> call, retrofit2.Response<ArrayList<MyCommunityFrame>> response) {
                if (response.isSuccessful()) {
                    ArrayList<MyCommunityFrame> result = response.body();
                    myCommunityList.clear();
                    myCommunityList.addAll(result);
                    myCommunity_adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MyCommunityFrame>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                setMyCommunity();
            }
        });
    }

    public void setHotArticle() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://49.50.164.11:5000/article/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<ArrayList<HotArticleFrame>> call = service.getHotArticle();

        call.enqueue(new retrofit2.Callback<ArrayList<HotArticleFrame>>() {
            @Override
            public void onResponse(Call<ArrayList<HotArticleFrame>> call, retrofit2.Response<ArrayList<HotArticleFrame>> response) {
                if (response.isSuccessful()) {
                    ArrayList<HotArticleFrame> result = response.body();
                    hotArticleList.clear();
                    hotArticleList.addAll(result);
                    hotArticle_adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<HotArticleFrame>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                setHotArticle();
            }
        });
    }

    public interface RetrofitService {
        @GET("latestArticleList")
        Call<ArrayList<MyCommunityFrame>> getMyCommunity();

        @GET("hotArticleList")
        Call<ArrayList<HotArticleFrame>> getHotArticle();
    }

    @Override
    public void onCommunityItemSelected(View v, int position) {
        MyCommunityAdapter.Holder holder = (MyCommunityAdapter.Holder) myCommunity_recyclerView.findViewHolderForAdapterPosition(position);
        int communityID = holder.communityID;
        ((MainActivity) getActivity()).replaceFragmentFull(new Community(communityID));
    }

    @Override
    public void onArticleItemSelected(View v, int position) {
        HotArticleAdapter.Holder holder = (HotArticleAdapter.Holder) hotArticle_recyclerView.findViewHolderForAdapterPosition(position);
        int communityID = holder.communityID;
        int articleID = Integer.parseInt(holder.articleID.getText().toString());

        ((MainActivity) getActivity()).replaceFragmentFull(new Article(articleID, communityID));
    }
}
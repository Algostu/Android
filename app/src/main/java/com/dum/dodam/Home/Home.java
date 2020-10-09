package com.dum.dodam.Home;

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
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Community.Article;
import com.dum.dodam.Community.Community;
import com.dum.dodam.Home.dataframe.HotArticleFrame;
import com.dum.dodam.Home.dataframe.HotArticleResponse;
import com.dum.dodam.Home.dataframe.MyCommunityFrame;
import com.dum.dodam.Home.dataframe.MyCommunityResponse;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
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

    private int cnt_myCommunity = 0;
    private int cnt_hotArticle = 0;

    private RecyclerView myCommunity_recyclerView;
    private RecyclerView.Adapter myCommunity_adapter;

    private RecyclerView hotArticle_recyclerView;
    private RecyclerView.Adapter hotArticle_adapter;

    private RecyclerView.LayoutManager myCommunity_layoutManager;
    private RecyclerView.LayoutManager hotArticle_layoutManager;

    private ArrayList<MyCommunityFrame> myCommunityList = new ArrayList<>();
    private ArrayList<HotArticleFrame> hotArticleList = new ArrayList<>();

    private TextView cafeteria;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_, container, false);
        view.setClickable(true);

        cafeteria = view.findViewById(R.id.cafeteria);

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
        RetrofitService service = RetrofitAdapter.getInstance("http://49.50.164.11:5000/", getContext());
        Call<MyCommunityResponse> call = service.getMyCommunity();

        call.enqueue(new retrofit2.Callback<MyCommunityResponse>() {
            @Override
            public void onResponse(Call<MyCommunityResponse> call, retrofit2.Response<MyCommunityResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<MyCommunityFrame> result = response.body().body;
                    myCommunityList.clear();
                    myCommunityList.addAll(result);
                    myCommunity_adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<MyCommunityResponse> call, Throwable t) {
                Log.d(TAG, "My Community " + String.valueOf(cnt_myCommunity) + " " + t.getMessage());
                if (cnt_myCommunity < 5) setMyCommunity();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_myCommunity++;
            }
        });
    }

    public void setHotArticle() {
        RetrofitAdapter adapter = new RetrofitAdapter();
        RetrofitService service = adapter.getInstance("http://49.50.164.11:5000/", getContext());
        Call<HotArticleResponse> call = service.getHotArticle();

        call.enqueue(new retrofit2.Callback<HotArticleResponse>() {
            @Override
            public void onResponse(Call<HotArticleResponse> call, retrofit2.Response<HotArticleResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<HotArticleFrame> result = response.body().body;
                    hotArticleList.clear();
                    hotArticleList.addAll(result);
                    hotArticle_adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<HotArticleResponse> call, Throwable t) {
                Log.d(TAG, "HotArticle " + String.valueOf(cnt_hotArticle) + " " + t.getMessage());
                if (cnt_hotArticle < 5) setHotArticle();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_hotArticle++;
            }
        });
    }

    @Override
    public void onCommunityItemSelected(View v, int position) {
        MyCommunityAdapter.Holder holder = (MyCommunityAdapter.Holder) myCommunity_recyclerView.findViewHolderForAdapterPosition(position);
        int communityID = holder.communityID;
        ((MainActivity) getActivity()).replaceFragmentFull(new Community(communityID, 0));
    }

    @Override
    public void onArticleItemSelected(View v, int position) {
        HotArticleAdapter.Holder holder = (HotArticleAdapter.Holder) hotArticle_recyclerView.findViewHolderForAdapterPosition(position);
        int communityType = holder.communityType;
        int communityID = holder.communityID;
        int articleID = Integer.parseInt(holder.articleID.getText().toString());

        ((MainActivity) getActivity()).replaceFragmentFull(new Article(articleID, communityType, communityID));
    }

    public void setTodayCafeteria(String menu) {
        cafeteria.setText(menu);
    }
}
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

import com.example.myapplication.Home.dataframe.MyCommunityFrame;
import com.example.myapplication.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;

public class Home extends Fragment implements HomeAdapter.OnListItemSelectedInterface {
    private static final String TAG = "RHC";

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private ArrayList<MyCommunityFrame> myCommunityList = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home, container, false);
        view.setClickable(true);

        adapter = new HomeAdapter(getContext(), myCommunityList, this);
        setMyCommunity();

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_my_community);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

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

        Call<ArrayList<MyCommunityFrame>> call = service.goArticle();

        call.enqueue(new retrofit2.Callback<ArrayList<MyCommunityFrame>>() {
            @Override
            public void onResponse(Call<ArrayList<MyCommunityFrame>> call, retrofit2.Response<ArrayList<MyCommunityFrame>> response) {
                if (response.isSuccessful()) {
                    ArrayList<MyCommunityFrame> result = response.body();
                    Log.d(TAG, "onResponse: SIZE" + String.valueOf(result.size()));
                    myCommunityList.clear();
                    myCommunityList.addAll(result);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<MyCommunityFrame>> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public void setHotArticle() {

    }

    public interface RetrofitService {
        @GET("latestArticleList")
        Call<ArrayList<MyCommunityFrame>> goArticle();
    }

    @Override
    public void onItemSelected(View v, int position) {

    }
}
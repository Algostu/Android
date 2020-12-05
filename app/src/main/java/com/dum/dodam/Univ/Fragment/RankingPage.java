package com.dum.dodam.Univ.Fragment;

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

import com.dum.dodam.R;
import com.dum.dodam.Univ.Adapter.RankingRecyclerAdapter;
import com.dum.dodam.Univ.dataframe.MajorFrame;
import com.dum.dodam.Univ.dataframe.RankingResponse;
import com.dum.dodam.Univ.dataframe.UnivFrame;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;

import java.util.ArrayList;

import retrofit2.Call;

public class RankingPage extends Fragment implements RankingRecyclerAdapter.OnListItemSelectedInterface {
    private static final String TAG = RankingPage.class.getName();
    // type : 학교, 학과
    public int type;

    private RankingRecyclerAdapter adapter;
    private ArrayList<UnivFrame> univList = new ArrayList<>();
    private ArrayList<MajorFrame> majorList = new ArrayList<>();

    public RankingPage(int type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.univ_ranking_page, container, false);

        if (type == 1) {
            adapter = new RankingRecyclerAdapter(getContext(), univList, majorList, type, this);
        } else {
            adapter = new RankingRecyclerAdapter(getContext(), univList, majorList, type, this);
        }

        loadRanking();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rankpage_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onItemSelected1(View v, UnivFrame univFrame) {

    }

    @Override
    public void onItemSelected2(View v, MajorFrame majorFrame) {

    }

    private void loadRanking() {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<RankingResponse> call = service.loadRanking();

        call.enqueue(new retrofit2.Callback<RankingResponse>() {
            @Override
            public void onResponse(Call<RankingResponse> call, retrofit2.Response<RankingResponse> response) {
                if (response.isSuccessful()) {
                    RankingResponse result = response.body();
                    univList.clear();
                    majorList.clear();
                    univList.addAll(result.univList);
                    majorList.addAll(result.majorList);
                    
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.toString());
                }
            }

            @Override
            public void onFailure(Call<RankingResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}

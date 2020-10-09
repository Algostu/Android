package com.dum.dodam.Simulation;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.R;
import com.dum.dodam.Simulation.dataframe.ContestListFrame;
import com.dum.dodam.Simulation.dataframe.ContestListResponse;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;

public class Simulation extends Fragment implements ContestListAdapter.OnListItemSelectedInterface {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "RHC";
    private int cnt_getContestList = 0;

    ArrayList<ContestListFrame> list = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.simulation, container, false);

        adapter = new ContestListAdapter(list, this);

        getContestList();

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_contest);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        return view;
    }

    public void getContestList() {

        com.dum.dodam.httpConnection.RetrofitService service = RetrofitAdapter.getInstance("http://49.50.164.11:5000/", getContext());

        Call<ContestListResponse> call = service.getContestList();

        call.enqueue(new Callback<ContestListResponse>() {
            @Override
            public void onResponse(Call<ContestListResponse> call, Response<ContestListResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<ContestListFrame> result = response.body().body;
                    list.clear();
                    list.addAll(result);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "Response but Fail in getContestList");
                }
            }

            @Override
            public void onFailure(Call<ContestListResponse> call, Throwable t) {
                Log.d(TAG, "getContestList" + t.getMessage());
                if (cnt_getContestList < 5) getContestList();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_getContestList++;
            }
        });
    }

    @Override
    public void onItemSelected(View v, int position) {

    }
}
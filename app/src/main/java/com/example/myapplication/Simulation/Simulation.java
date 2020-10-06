package com.example.myapplication.Simulation;

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

import com.example.myapplication.R;
import com.example.myapplication.Simulation.dataframe.ContestListFrame;

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
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://49.50.164.11:5000/contest/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<ArrayList<ContestListFrame>> call = service.getContestList();

        call.enqueue(new Callback<ArrayList<ContestListFrame>>() {
            @Override
            public void onResponse(Call<ArrayList<ContestListFrame>> call, Response<ArrayList<ContestListFrame>> response) {
                if (response.isSuccessful()) {
                    ArrayList<ContestListFrame> result = response.body();
                    list.clear();
                    list.addAll(result);
                    adapter.notifyDataSetChanged();
                } else {
                    Toast.makeText(getContext(), "Upload Fail", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "Response but Fail in getContestList");
                }
            }

            @Override
            public void onFailure(Call<ArrayList<ContestListFrame>> call, Throwable t) {
                Log.d(TAG, "getContestList" + t.getMessage());
                if (cnt_getContestList < 5) getContestList();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_getContestList++;
            }
        });
    }

    public interface RetrofitService {
        @GET("getList")
        Call<ArrayList<ContestListFrame>> getContestList();
    }

    @Override
    public void onItemSelected(View v, int position) {

    }
}
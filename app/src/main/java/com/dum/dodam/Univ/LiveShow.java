package com.dum.dodam.Univ;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.R;
import com.dum.dodam.Univ.dataframe.LiveShowFrame;
import com.dum.dodam.Univ.dataframe.LiveShowResponse;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveShow extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String TAG = "RHC";

    private ArrayList<LiveShowFrame> list = new ArrayList<LiveShowFrame>();
    private int cnt_readLiveShow = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collage_liveshow, container, false);
        view.setClickable(true);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle("라이브 쇼");
        toolbar.setSubtitle("대학생의 생생한 경험담을 듣고싶다면?!");

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.instagram.com/d.___.dam";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(Intent.createChooser(intent, "Browse with"));
            }
        });

        adapter = new LiveShowAdapter(getContext(), list);

        readLiveShowList();

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_liveshow);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        return view;
    }

    public void readLiveShowList() {
        com.dum.dodam.httpConnection.RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<LiveShowResponse> call = service.readLiveShowList();
        call.enqueue(new Callback<LiveShowResponse>() {
            @Override
            public void onResponse(Call<LiveShowResponse> call, Response<LiveShowResponse> response) {
                if (response.isSuccessful()) {
                    LiveShowResponse result = response.body();
                    if (result.checkError(getActivity()) != 0) return;

                    list.addAll(result.body);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail");
                }
            }

            @Override
            public void onFailure(Call<LiveShowResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                if (cnt_readLiveShow < 5) readLiveShowList();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_readLiveShow++;
            }
        });
    }

}

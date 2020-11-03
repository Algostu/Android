package com.dum.dodam.Univ;

import android.content.Intent;
import android.graphics.Typeface;
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
import androidx.core.content.res.ResourcesCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.RvItemDecoration;
import com.dum.dodam.Univ.dataframe.LiveShowFrame;
import com.dum.dodam.Univ.dataframe.LiveShowResponse;
import com.dum.dodam.Univ.dataframe.UnivFrame;
import com.dum.dodam.Univ.dataframe.UnivResponse;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LiveShow extends Fragment implements LiveShowAdapter.OnListItemSelectedInterface {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private String TAG = "RHC";

    private ArrayList<LiveShowFrame> list = new ArrayList<LiveShowFrame>();
    private UnivFrame univ = new UnivFrame();
    private int cnt_readLiveShow = 0;
    private int added_heart = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.univ_liveshow, container, false);
        view.setClickable(true);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle("라이브 쇼");
        toolbar.setSubtitle("대학생의 생생한 경험담을 듣고싶다면?!");
        Typeface typeface = ResourcesCompat.getFont(getContext(), R.font.title);
        toolBarLayout.setCollapsedTitleTypeface(typeface);
        toolBarLayout.setExpandedTitleTypeface(typeface);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "https://www.instagram.com/d.___.dam";
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(Intent.createChooser(intent, "Browse with"));
            }
        });

        adapter = new LiveShowAdapter(getContext(), list, this, this);

        readLiveShowList();

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_liveshow);
        recyclerView.addItemDecoration(new RvItemDecoration(getContext()));
        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);

        return view;
    }

    public void readLiveShowList() {
        com.dum.dodam.httpConnection.RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<LiveShowResponse> call = service.readLiveShowList("latest");
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

    @Override
    public void onItemSelected(View v, int position) {
        LiveShowAdapter.Holder holder = (LiveShowAdapter.Holder) recyclerView.findViewHolderForAdapterPosition(position);

        if (added_heart == 0) added_heart = 1;
        else added_heart = 0;

        if (added_heart == 0) {
            holder.heart_ic.setImageResource(R.drawable.ic_heart);
        } else {
            holder.heart_ic.setImageResource(R.drawable.explodin_heart);
        }

//        RetrofitAdapter adapter = new RetrofitAdapter();
//        com.dum.dodam.httpConnection.RetrofitService service = adapter.getInstance(getContext());
//        Call<BaseResponse> call = service.modifyHeart(articleID, communityType, communityID, added_heart);
//
//        call.enqueue(new retrofit2.Callback<BaseResponse>() {
//            @Override
//            public void onResponse(Call<BaseResponse> call, retrofit2.Response<BaseResponse> response) {
//                if (response.isSuccessful()) {
//                    BaseResponse result = response.body();
//                    if (result.checkError(getActivity()) != 0) return;
//                    heart.setText(String.valueOf(org_heart + added_heart));
//                    if (added_heart == 0) {
//                        heart_ic.setImageResource(R.drawable.ic_heart);
//                    } else {
//                        heart_ic.setImageResource(R.drawable.explodin_heart);
//                    }
//                } else {
//                    Log.d(TAG, "onResponse: Fail " + response.body());
//                }
//            }
//
//            @Override
//            public void onFailure(Call<BaseResponse> call, Throwable t) {
//                Log.d(TAG, "onFailure: " + t.getMessage());
//                Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
//            }
//        });
    }

    @Override
    public void onItemSelected2(View v, int position) {
        LiveShowAdapter.Holder holder = (LiveShowAdapter.Holder) recyclerView.findViewHolderForAdapterPosition(position);

        search(holder.school.getText().toString());
    }

    public void search(String query) {
        list.clear();
        if (query.equals("")) {
            adapter.notifyDataSetChanged();
            return;
        }

        RetrofitAdapter rAdapter = new RetrofitAdapter();
        RetrofitService service = rAdapter.getInstance(getActivity());
        Call<UnivResponse> call = service.searchCollageName(query);

        call.enqueue(new retrofit2.Callback<UnivResponse>() {
            @Override
            public void onResponse(Call<UnivResponse> call, retrofit2.Response<UnivResponse> response) {
                if (response.isSuccessful()) {
                    UnivResponse result = response.body();
                    univ = result.body.get(0);
                    ((MainActivity) getActivity()).replaceFragmentFull(new Univ(univ));
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<UnivResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }
}

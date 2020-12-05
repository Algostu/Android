package com.dum.dodam.Univ.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Community.dataframe.FeedResult;
import com.dum.dodam.Community.dataframe.ImageData;
import com.dum.dodam.R;
import com.dum.dodam.Univ.Adapter.InstagramViewerAdapter;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;

public class InstagramViewer extends Fragment {
    // debug
    private final String TAG = "InstagramViewer";
    // toolbar
    private androidx.appcompat.widget.Toolbar toolbar;
    private ActionBar actionbar;
    // instagram
    public String fields = "id,media_type,caption,media_url";
    public String access_token = "IGQVJYVFRlT1VRMDg3QWRQQ3ZAOTGNlbGQ2VGE3YWVvYUNaLWF0MWJaZA1VvQjlwX3lPZAEF3cHVvZAXEzWlhzOHMwSG10SXpmX1lPYXlldGpYZAFhCZAS1aSVR6WTN1ci1oU2FyV2V6OXl3";
    private ArrayList<ImageData> instagramList = new ArrayList<>();
    // recycler viewer
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.univ_instagram_viewer, container, false);

        // toolbar
        setHasOptionsMenu(true);
        toolbar = view.findViewById(R.id.toolbar2);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionbar.setDisplayHomeAsUpEnabled(true);

        // recycler viewer
        adapter = new InstagramViewerAdapter(getContext(), instagramList);
        recyclerView = (RecyclerView) view.findViewById(R.id.rv_contest);
        recyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);
        getFeed();

        return view;
    }

    public void getFeed(){
        // Retrofit 삽질 4시간 경험 한 후기
        // 1. Retrofit 의 Service는 BaseURL과는 연관이 없다.
        // 2. Retrofit은 여러개를 만들어서 사용해도 상관 없다.
        // 3. Retrofit의 Parameter는 반드시 URL encoded 되지 않은것으로 사용해야한다.
        // 4. Retrofit의 Parameter는 절대 getString(R.string.name) 으로 가지고 온것을 사용하면 안된다.
        RetrofitService service = RetrofitAdapter.getInstance(getContext(), "https://graph.instagram.com/");
        Call<FeedResult> call = service.getFeeds(fields, access_token);

        call.enqueue(new retrofit2.Callback<FeedResult>() {
            @Override
            public void onResponse(Call<FeedResult> call, retrofit2.Response<FeedResult> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "onResponse: Success " + response.body().data.size());
                    instagramList.clear();
                    ArrayList<ImageData> temp = response.body().data;
                    Iterator<ImageData> i = temp.iterator();
                    while (i.hasNext()) {
                        ImageData alarm = i.next(); // must be called before you can call i.remove()
                        // Do something
                        try {
                            if (!alarm.caption.contains("#학과정보"))
                                i.remove();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                    instagramList.addAll(temp);
                    adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<FeedResult> call, Throwable t) {
                Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //select back button
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}

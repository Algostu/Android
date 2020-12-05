package com.dum.dodam.Univ.Fragment;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.cardview.widget.CardView;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager.widget.ViewPager;

import com.dum.dodam.Alarm.Adapter.PageAdapter;
import com.dum.dodam.Community.dataframe.FeedResult;
import com.dum.dodam.Community.dataframe.ImageData;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.Univ.Adapter.InstagramAdapter;
import com.dum.dodam.Univ.LiveShow;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.Iterator;

import retrofit2.Call;

public class UnivMain extends Fragment implements InstagramAdapter.OnListItemSelectedInterface {

    // debug
    private final String TAG = "UnivMain";

    // search
    private LinearLayout llSearch;

    // ranking
    public TabLayout tabs;
    public PageAdapter pageAdapter;
    public ViewPager viewPage;

    // instagram
    private ArrayList<ImageData> instagramList = new ArrayList<>();
    public String fields = "id,media_type,caption,media_url";
    public String access_token = "IGQVJYVFRlT1VRMDg3QWRQQ3ZAOTGNlbGQ2VGE3YWVvYUNaLWF0MWJaZA1VvQjlwX3lPZAEF3cHVvZAXEzWlhzOHMwSG10SXpmX1lPYXlldGpYZAFhCZAS1aSVR6WTN1ci1oU2FyV2V6OXl3";

    private TextView more_contest;
    private TextView title_contest;
    private RecyclerView instagram_recyclerView;
    private RecyclerView.Adapter instagram_adapter;
    private RecyclerView.LayoutManager instagram_layoutManager;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.univ_main, container, false);
        view.setClickable(true);

        // Appbar 적용
        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle("수능 대.박.");
        actionBar.setSubtitle("대학 관련 자료들을 찾아보세요!");

        // layout 연결
        CardView cv_liveshow = (CardView) view.findViewById(R.id.cv_liveshow);
        llSearch = view.findViewById(R.id.search);

        // 각 layout listener연결
        // search
        llSearch.setClickable(true);
        llSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragmentFull(new UnivSearch());
            }
        });
        // livshow
        cv_liveshow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentFull(new LiveShow());
            }
        });

        // ranking
        pageAdapter = new PageAdapter(getChildFragmentManager());
        viewPage = (ViewPager) view.findViewById(R.id.vp_ranking);
        viewPage.setAdapter(pageAdapter);
        viewPage.setSaveEnabled(false);

        tabs = (TabLayout) view.findViewById(R.id.tab_ranking);
        tabs.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white_gray));
        tabs.setupWithViewPager(viewPage);

        // instagram
        more_contest = view.findViewById(R.id.more_contest);
        title_contest = view.findViewById(R.id.title_contest);
        title_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragmentFull(new InstagramViewer());
            }
        });

        more_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragmentFull(new InstagramViewer());
            }
        });
        instagram_adapter = new InstagramAdapter(getContext(), instagramList, this);
        instagram_recyclerView = (RecyclerView) view.findViewById(R.id.rv_contest);
        instagram_recyclerView.setHasFixedSize(true);
        instagram_layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        instagram_recyclerView.setLayoutManager(instagram_layoutManager);
        instagram_recyclerView.setAdapter(instagram_adapter);
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
                    instagram_adapter.notifyDataSetChanged();
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
    public void onFeedSelected(View v, int position) {
        ((MainActivity) getActivity()).replaceFragmentFull(new InstagramViewer());
    }



}

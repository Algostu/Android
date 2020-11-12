package com.dum.dodam.Community;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.fragment.app.DialogFragment;
import androidx.viewpager.widget.ViewPager;

import com.dum.dodam.Community.Adapter.viewPageAdapter;
import com.dum.dodam.Community.dataframe.FeedResult;
import com.dum.dodam.Community.dataframe.ImageData;
import com.dum.dodam.R;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.google.android.material.tabs.TabLayout;
import com.google.gson.Gson;

import java.util.ArrayList;

import retrofit2.Call;

public class InstagramPost2 extends DialogFragment implements View.OnClickListener {
    private static final int DP = 24;
    public ImageData feed;
    public ProgressBar progressBar;
    public Button btn;
    public static final String TAG_EVNET_DIALOG = "dialog_event";
    public viewPageAdapter adapter;
    public ArrayList<ImageData> imageDataList;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ActionBar actionbar;
    public String fields = "media_url";
    public String access_token = "IGQVJYVFRlT1VRMDg3QWRQQ3ZAOTGNlbGQ2VGE3YWVvYUNaLWF0MWJaZA1VvQjlwX3lPZAEF3cHVvZAXEzWlhzOHMwSG10SXpmX1lPYXlldGpYZAFhCZAS1aSVR6WTN1ci1oU2FyV2V6OXl3";


    public static InstagramPost2 newInstance(ImageData feed) {
        Bundle args = new Bundle();
        Gson gson = new Gson();
        args.putString("feed", gson.toJson(feed));
        InstagramPost2 InstagramPost2 = new InstagramPost2();
        InstagramPost2.setArguments(args);
        return InstagramPost2;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_instagram_post_2, container, false);
        view.setClickable(true);
        setHasOptionsMenu(true);
        setCancelable(false);
        Bundle bundle = getArguments();
        if (bundle != null) {
            Gson gson = new Gson();
            feed = gson.fromJson(bundle.getString("feed"), ImageData.class);
        }
        imageDataList = new ArrayList<>();
//        toolbar = view.findViewById(R.id.toolbar);
//        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
//        actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
//        actionbar.setDisplayShowCustomEnabled(true);
//        actionbar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
//        actionbar.setDisplayHomeAsUpEnabled(true);

        btn = view.findViewById(R.id.btn_confirm);
        btn.setOnClickListener(this);
        progressBar = view.findViewById(R.id.profileProgressBar);

        ViewPager viewPager = view.findViewById(R.id.viewPager);
        viewPager.setClipToPadding(false);

        float density = getResources().getDisplayMetrics().density;
        int margin = (int) (DP * density);
        viewPager.setPadding(margin, 0, margin, 0);
        viewPager.setPageMargin(margin/2);
        adapter = new viewPageAdapter(getContext(), imageDataList);
        viewPager.setAdapter(adapter);

        TabLayout tabLayout = (TabLayout) view.findViewById(R.id.tab_layout);
        tabLayout.setupWithViewPager(viewPager, true);

        getPhotos();
        return view;
    }

    public void getPhotos(){
        // Retrofit 삽질 4시간 경험 한 후기
        // 1. Retrofit 의 Service는 BaseURL과는 연관이 없다.
        // 2. Retrofit은 여러개를 만들어서 사용해도 상관 없다.
        // 3. Retrofit의 Parameter는 반드시 URL encoded 되지 않은것으로 사용해야한다.
        // 4. Retrofit의 Parameter는 절대 getString(R.string.name) 으로 가지고 온것을 사용하면 안된다.
        RetrofitService service = RetrofitAdapter.getInstance(getContext(), "https://graph.instagram.com/");
        Call<FeedResult> call = service.getFeedDetails(feed.id, fields, access_token);

        call.enqueue(new retrofit2.Callback<FeedResult>() {
            @Override
            public void onResponse(Call<FeedResult> call, retrofit2.Response<FeedResult> response) {
                if (response.isSuccessful()) {
                    Log.d("GridViewAdapter", "onResponse: Success " + response.body().data.size());
                    imageDataList.clear();
                    imageDataList.addAll(response.body().data);
                    adapter.refresh(imageDataList); //pass update list
                    progressBar.setVisibility(View.GONE);
                } else {
                    Log.d("GridViewAdapter", "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<FeedResult> call, Throwable t) {
                Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
            }
        });
    }

    @Override
    public void onClick(View v){
        dismiss();
    }


//    @Override
//    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
//        switch (item.getItemId()) {
//            case android.R.id.home:
//                //select back button
//                getActivity().onBackPressed();
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }
}

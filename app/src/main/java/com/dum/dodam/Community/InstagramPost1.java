package com.dum.dodam.Community;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.dum.dodam.Community.Adapter.GridViewAdapter;
import com.dum.dodam.Community.dataframe.FeedResult;
import com.dum.dodam.Community.dataframe.ImageData;
import com.dum.dodam.R;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;

import java.util.ArrayList;

import retrofit2.Call;

public class InstagramPost1 extends Fragment {
    public ArrayList<ImageData> imgList;
    public GridViewAdapter adapter;
    public ProgressBar progressBar;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ActionBar actionbar;
    public String fields = "id,media_type,caption,media_url";
    public String access_token = "IGQVJYVFRlT1VRMDg3QWRQQ3ZAOTGNlbGQ2VGE3YWVvYUNaLWF0MWJaZA1VvQjlwX3lPZAEF3cHVvZAXEzWlhzOHMwSG10SXpmX1lPYXlldGpYZAFhCZAS1aSVR6WTN1ci1oU2FyV2V6OXl3";

    public static InstagramPost1 newInstance() {
        Bundle args = new Bundle();
        InstagramPost1 instagramPost1 = new InstagramPost1();
        instagramPost1.setArguments(args);
        return instagramPost1;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.fragment_instagram_post_1, container, false);
        view.setClickable(true);
        setHasOptionsMenu(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
        }
        progressBar = view.findViewById(R.id.profileProgressBar);
        toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionbar.setDisplayHomeAsUpEnabled(true);


//        int img[] = {
//                R.drawable.ic_chiken_1,R.drawable.ic_logo_white,R.drawable.ic_chiken_1,R.drawable.ic_chiken_1
//        };
        imgList = new ArrayList<>();

        // 커스텀 아답타 생성
        adapter = new GridViewAdapter(
                getActivity().getApplicationContext(),
                R.layout.grid_view_item,
                imgList);
        GridView gv = (GridView)view.findViewById(R.id.gridView);
        gv.setAdapter(adapter);  // 커스텀 아답타를 GridView 에 적용
        gv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                InstagramPost2.newInstance(imgList.get(position)).show(getActivity().getSupportFragmentManager(), InstagramPost2.TAG_EVNET_DIALOG);

            }
        });

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
                    Log.d("GridViewAdapter", "onResponse: Success " + response.body().data.size());
                    imgList.clear();
                    imgList.addAll(response.body().data);
                    adapter.refresh(imgList); //pass update list
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

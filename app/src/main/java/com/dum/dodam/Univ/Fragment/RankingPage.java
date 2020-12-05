package com.dum.dodam.Univ.Fragment;

import android.os.Bundle;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.Univ.Adapter.RankingRecyclerAdapter;
import com.dum.dodam.Univ.UnivWebView;
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
    public void onItemSelected1(View v, final UnivFrame univFrame) {
        UnivSearchDialog dialog = new UnivSearchDialog(getContext(), univFrame, new UnivSearchDialog.myOnClickListener() {
            @Override
            public void onYoutubeClick() {
                Log.d("RHC", "col" + univFrame.youtube);
                if (univFrame.youtube == null) {
                    Toast.makeText(getContext(), "관련 정보가 없어 검색페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                    String youtubeUrl = "https://www.youtube.com/results?search_query=" + univFrame.univName;
                    ((MainActivity) getActivity()).replaceFragmentFull(new UnivWebView(youtubeUrl));
                } else {
                    ((MainActivity) getActivity()).replaceFragmentFull(new UnivWebView(univFrame.youtube));
                }
            }

            @Override
            public void onEduPageClick() {
                Log.d("RHC", "col" + univFrame.admission);
                if (univFrame.admission == null) {
                    Toast.makeText(getContext(), "입학처 정보가 없어 홈페이지로 이동합니다.", Toast.LENGTH_SHORT).show();
                    ((MainActivity) getActivity()).replaceFragmentFull(new UnivWebView(univFrame.homePage));
                } else {
                    ((MainActivity) getActivity()).replaceFragmentFull(new UnivWebView(univFrame.eduHomePage));
                }
            }
        });
        dialog.setCanceledOnTouchOutside(true);
        dialog.setCancelable(true);

        DisplayMetrics dm = getActivity().getResources().getDisplayMetrics(); //디바이스 화면크기를 구하기위해
        final int width = dm.widthPixels; //디바이스 화면 너비
        final int height = dm.heightPixels; //디바이스 화면 높이

        dialog.getWindow().setLayout(WindowManager.LayoutParams.MATCH_PARENT, WindowManager.LayoutParams.MATCH_PARENT);
        WindowManager.LayoutParams wm = dialog.getWindow().getAttributes();  //다이얼로그의 높이 너비 설정하기위해
        wm.copyFrom(dialog.getWindow().getAttributes());  //여기서 설정한값을 그대로 다이얼로그에 넣겠다는의미
        wm.width = (int) (width * 0.9);  //화면 너비의 절반
        wm.height = height / 2;  //화면 높이의 절반

        dialog.show();
    }

    @Override
    public void onItemSelected2(View v, MajorFrame majorFrame) {
        String careerNetUrl = "https://www.career.go.kr/cnet/front/base/job/jobView.do?SEQ=" + majorFrame.majorSeq;
        ((MainActivity) getActivity()).replaceFragmentFull(new UnivWebView(careerNetUrl));
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

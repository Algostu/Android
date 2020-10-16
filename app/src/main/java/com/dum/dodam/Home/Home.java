package com.dum.dodam.Home;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Community.Article;
import com.dum.dodam.Community.Community;
import com.dum.dodam.Home.dataframe.HotArticleFrame;
import com.dum.dodam.Home.dataframe.HotArticleResponse;
import com.dum.dodam.Home.dataframe.MyCommunityFrame;
import com.dum.dodam.Home.dataframe.MyCommunityResponse;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.School.School;
import com.dum.dodam.School.dataframe.CafeteriaFrame;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.kakao.usermgmt.response.model.User;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;

public class Home extends Fragment implements HotArticleAdapter.OnListItemSelectedInterface, MyCommunityAdapter.OnListItemSelectedInterface {
    private static final String TAG = "RHC";

    private int cnt_myCommunity = 0;
    private int cnt_hotArticle = 0;

    private RecyclerView myCommunity_recyclerView;
    private RecyclerView.Adapter myCommunity_adapter;

    private RecyclerView hotArticle_recyclerView;
    private RecyclerView.Adapter hotArticle_adapter;

    private RecyclerView.LayoutManager myCommunity_layoutManager;
    private RecyclerView.LayoutManager hotArticle_layoutManager;

    private ArrayList<MyCommunityFrame> myCommunityList = new ArrayList<>();
    private ArrayList<HotArticleFrame> hotArticleList = new ArrayList<>();
    public UserJson user;
    private TextView cafeteria;
    private TextView school_name;
    private TextView user_name;
    private LinearLayout today_lunch;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_, container, false);
        view.setClickable(true);

        user = ((MainActivity) getActivity()).getUser();

        user_name = view.findViewById(R.id.user_name);
        user_name.setText(user.userName);

        today_lunch = view.findViewById(R.id.today_lunch);
        today_lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragment(new School());
                ((MainActivity) getActivity()).setNavigationMenu();
            }
        });

        cafeteria = view.findViewById(R.id.cafeteria);
        setTodayCafeteria();

        school_name = view.findViewById(R.id.school_name);
        school_name.setText(user.schoolName);

        myCommunity_adapter = new MyCommunityAdapter(getContext(), myCommunityList, this, user);
        setMyCommunity();
        myCommunity_recyclerView = (RecyclerView) view.findViewById(R.id.rv_my_community);
        myCommunity_recyclerView.setHasFixedSize(true);
        myCommunity_layoutManager = new LinearLayoutManager(getActivity());
        myCommunity_recyclerView.setLayoutManager(myCommunity_layoutManager);
        myCommunity_recyclerView.setAdapter(myCommunity_adapter);

        hotArticle_adapter = new HotArticleAdapter(getContext(), hotArticleList, this, user);
        setHotArticle();
        hotArticle_recyclerView = (RecyclerView) view.findViewById(R.id.rv_hot_article);
        hotArticle_recyclerView.setHasFixedSize(true);
        hotArticle_layoutManager = new LinearLayoutManager(getActivity());
        hotArticle_recyclerView.setLayoutManager(hotArticle_layoutManager);
        hotArticle_recyclerView.setAdapter(hotArticle_adapter);

        return view;
    }

    public void setMyCommunity() {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<MyCommunityResponse> call = service.getMyCommunity();
        call.enqueue(new retrofit2.Callback<MyCommunityResponse>() {
            @Override
            public void onResponse(Call<MyCommunityResponse> call, retrofit2.Response<MyCommunityResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<MyCommunityFrame> result = response.body().body;
                    myCommunityList.clear();
                    myCommunityList.addAll(result);
                    myCommunity_adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<MyCommunityResponse> call, Throwable t) {
                Log.d(TAG, "My Community " + String.valueOf(cnt_myCommunity) + " " + t.getMessage());
                if (cnt_myCommunity < 5) setMyCommunity();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_myCommunity++;
            }
        });
    }

    public void setHotArticle() {
        RetrofitAdapter adapter = new RetrofitAdapter();
        RetrofitService service = adapter.getInstance(getContext());
        Call<HotArticleResponse> call = service.getHotArticle();

        call.enqueue(new retrofit2.Callback<HotArticleResponse>() {
            @Override
            public void onResponse(Call<HotArticleResponse> call, retrofit2.Response<HotArticleResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<HotArticleFrame> result = response.body().body;
                    hotArticleList.clear();
                    hotArticleList.addAll(result);
                    hotArticle_adapter.notifyDataSetChanged();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<HotArticleResponse> call, Throwable t) {
                Log.d(TAG, "HotArticle " + String.valueOf(cnt_hotArticle) + " " + t.getMessage());
                if (cnt_hotArticle < 5) setHotArticle();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_hotArticle++;
            }
        });
    }

    @Override
    public void onCommunityItemSelected(View v, int position) {
        MyCommunityAdapter.Holder holder = (MyCommunityAdapter.Holder) myCommunity_recyclerView.findViewHolderForAdapterPosition(position);
        int communityID = holder.communityID;
        String title = holder.community_name.getText().toString();
        int communityType = holder.communityType;
        ((MainActivity) getActivity()).replaceFragmentFull(new Community(communityType, communityID, title));
    }

    @Override
    public void onArticleItemSelected(View v, int position) {
        HotArticleAdapter.Holder holder = (HotArticleAdapter.Holder) hotArticle_recyclerView.findViewHolderForAdapterPosition(position);
        int communityType = holder.communityType;
        int communityID = holder.communityID;
        int articleID = Integer.parseInt(holder.articleID.getText().toString());

        ((MainActivity) getActivity()).replaceFragmentFull(new Article(articleID, communityType, communityID));
    }

    public void setTodayCafeteria() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        String filename = "curCafeteria";
        File file = new File(getContext().getFilesDir() + "/" + filename);
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                StringBuilder stringBuilder = new StringBuilder();
                String line = bufferedReader.readLine();
                while (line != null) {
                    stringBuilder.append(line).append("\n");
                    line = bufferedReader.readLine();
                }
                bufferedReader.close();
                String response = stringBuilder.toString();

                ArrayList<CafeteriaFrame> list = gson.fromJson(response, new TypeToken<ArrayList<CafeteriaFrame>>() {
                }.getType());

                String lunch = new String();

                if (getWeekNDate().get(0) == 2) {
                    lunch = list.get(getWeekNDate().get(1)).lunch_monday;
                } else if (getWeekNDate().get(0) == 3) {
                    lunch = list.get(getWeekNDate().get(1)).lunch_monday;
                } else if (getWeekNDate().get(0) == 4) {
                    lunch = list.get(getWeekNDate().get(1)).lunch_monday;
                } else if (getWeekNDate().get(0) == 5) {
                    lunch = list.get(getWeekNDate().get(1)).lunch_monday;
                } else if (getWeekNDate().get(0) == 6) {
                    lunch = list.get(getWeekNDate().get(1)).lunch_monday;
                }
                if (lunch.equals(" ")) cafeteria.setText("제공되는 식단이 없습니다.");
                else {
                    cafeteria.setText(lunch);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            cafeteria.setText("탭 해주세요 ㅎㅎ");
        }
    }

    public static ArrayList<Integer> getWeekNDate() {
        ArrayList<Integer> result = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        int this_week = c.get(Calendar.WEEK_OF_MONTH);
//        int today = c.get(Calendar.DATE); //오늘 일자 저장
        int sDayNum = c.get(Calendar.DAY_OF_WEEK);

        result.add(sDayNum);
        result.add(this_week);

        return result;
    }
}
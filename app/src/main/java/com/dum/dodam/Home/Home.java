package com.dum.dodam.Home;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Community.Article;
import com.dum.dodam.Community.Community;
import com.dum.dodam.Contest.Contest;
import com.dum.dodam.Contest.ContestInfo;
import com.dum.dodam.Contest.dataframe.ContestListResponse;
import com.dum.dodam.Home.dataframe.ContestFrame;
import com.dum.dodam.Home.dataframe.HotArticleFrame;
import com.dum.dodam.Home.dataframe.HotArticleResponse;
import com.dum.dodam.Home.dataframe.MyCommunityFrame;
import com.dum.dodam.Home.dataframe.MyCommunityResponse;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.Mypage.Mypage;
import com.dum.dodam.R;
import com.dum.dodam.School.School;
import com.dum.dodam.School.dataframe.CafeteriaFrame;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Home extends Fragment implements HotArticleAdapter.OnListItemSelectedInterface, MyCommunityAdapter.OnListItemSelectedInterface, ContestAdapter.OnListItemSelectedInterface {
    private static final String TAG = "RHC";

    private boolean firstTime = true;
    private int cnt_myCommunity = 0;
    private int cnt_hotArticle = 0;
    private int cnt_getContestList = 0;
    private int cnt_cafeteria = 0;

    private RecyclerView myCommunity_recyclerView;
    private RecyclerView.Adapter myCommunity_adapter;

    private RecyclerView hotArticle_recyclerView;
    private RecyclerView.Adapter hotArticle_adapter;

    private RecyclerView contest_recyclerView;
    private RecyclerView.Adapter contest_adapter;

    private RecyclerView.LayoutManager myCommunity_layoutManager;
    private RecyclerView.LayoutManager hotArticle_layoutManager;
    private RecyclerView.LayoutManager contest_layoutManager;

    private ArrayList<MyCommunityFrame> myCommunityList = new ArrayList<>();
    private ArrayList<com.dum.dodam.Contest.dataframe.ContestFrame> result = new ArrayList<>();
    private ArrayList<HotArticleFrame> hotArticleList = new ArrayList<>();
    private ArrayList<ContestFrame> contestList = new ArrayList<>();

    public UserJson user;
    private TextView cafeteria;
    private TextView school_name;
    private TextView user_name;
    private TextView pleaseTab;
    private TextView more_contest;
    private TextView title_contest;
    private ImageButton mySetting;

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

        pleaseTab = view.findViewById(R.id.pleaseTab);

        cafeteria = view.findViewById(R.id.cafeteria);
        setTodayCafeteria();

        school_name = view.findViewById(R.id.school_name);
        school_name.setText(user.schoolName);

        more_contest = view.findViewById(R.id.more_contest);
        title_contest = view.findViewById(R.id.title_contest);

        title_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragmentFull(new Contest());
            }
        });

        more_contest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragmentFull(new Contest());
            }
        });

        // Recyclerview Community
        myCommunity_adapter = new MyCommunityAdapter(getContext(), myCommunityList, this, user);
        myCommunity_recyclerView = (RecyclerView) view.findViewById(R.id.rv_my_community);
        myCommunity_recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        myCommunity_recyclerView.setHasFixedSize(true);
        myCommunity_layoutManager = new LinearLayoutManager(getActivity());
        myCommunity_recyclerView.setLayoutManager(myCommunity_layoutManager);
        myCommunity_recyclerView.setAdapter(myCommunity_adapter);

        // Recyclerview HotArticle
        hotArticle_adapter = new HotArticleAdapter(getContext(), hotArticleList, this, user);
        hotArticle_recyclerView = (RecyclerView) view.findViewById(R.id.rv_hot_article);
        hotArticle_recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        hotArticle_recyclerView.setHasFixedSize(true);
        hotArticle_layoutManager = new LinearLayoutManager(getActivity());
        hotArticle_recyclerView.setLayoutManager(hotArticle_layoutManager);
        hotArticle_recyclerView.setAdapter(hotArticle_adapter);

        // Contest
        contest_adapter = new ContestAdapter(getContext(), contestList, this, user);
        setContest();
        contest_recyclerView = (RecyclerView) view.findViewById(R.id.rv_contest);
        contest_recyclerView.setHasFixedSize(true);
        contest_layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        contest_recyclerView.setLayoutManager(contest_layoutManager);
        contest_recyclerView.setAdapter(contest_adapter);

        mySetting = view.findViewById(R.id.setting_button);
        mySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentFull(new Mypage());
            }
        });

        if (firstTime){
            setHotArticle();
            setMyCommunity();
            firstTime = false;
        }

        return view;
    }

    public void setMyCommunity() {
        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<MyCommunityResponse> call = service.getMyCommunity();
        call.enqueue(new retrofit2.Callback<MyCommunityResponse>() {
            @Override
            public void onResponse(Call<MyCommunityResponse> call, retrofit2.Response<MyCommunityResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().checkError(getContext()) != 0) return;
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
                    if (response.body().checkError(getContext()) != 0) return;
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

    public void setContest() {
        String filename = "contest";
        if (getContext() == null) return;
        File file = new File(getContext().getFilesDir() + "/" + filename);
        if (!file.exists()) {
            final int contestID = 0;
            com.dum.dodam.httpConnection.RetrofitService service = RetrofitAdapter.getInstance(getContext());
            Call<ContestListResponse> call = service.getContestList(contestID);
            call.enqueue(new Callback<ContestListResponse>() {
                @Override
                public void onResponse(Call<ContestListResponse> call, Response<ContestListResponse> response) {
                    if (response.isSuccessful()) {
                        ArrayList<com.dum.dodam.Contest.dataframe.ContestFrame> result = response.body().body;
                        if (result.size() == 0) return;
                        Log.d(TAG, "CONTEST RESULT" + result.size());

                        JSONArray jsArray = new JSONArray();//배열이 필요할때
                        try {
                            for (int i = 0; i < result.size(); i++) {
                                JSONObject sObject = new JSONObject();//배열 내에 들어갈 json
                                sObject.put("contestID", result.get(i).contestID);
                                sObject.put("title", result.get(i).title);
                                sObject.put("content", result.get(i).content);
                                sObject.put("area", result.get(i).area);
                                sObject.put("sponsor", result.get(i).sponsor);
                                sObject.put("prize", result.get(i).prize);
                                sObject.put("firstPrize", result.get(i).firstPrize);
                                sObject.put("homePage", result.get(i).homePage);
                                sObject.put("imageUrl", result.get(i).imageUrl);
                                sObject.put("start", result.get(i).start);
                                sObject.put("end", result.get(i).end);
                                jsArray.put(sObject);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

                        try {
                            Log.d(TAG, "CONTEST Write Start");
                            if (getContext() == null) return;
                            File file = new File(getContext().getFilesDir(), "contest");
                            FileWriter fileWriter = null;
                            if (contestID == 0) fileWriter = new FileWriter(file, false);
                            else fileWriter = new FileWriter(file, true);
                            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                            bufferedWriter.write(jsArray.toString());
                            bufferedWriter.close();
                            Log.d(TAG, "CONTEST Write End");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        setContestView();
                    } else {
                        Log.d(TAG, "Response but Fail in getContestList");
                    }
                }

                @Override
                public void onFailure(Call<ContestListResponse> call, Throwable t) {
                    Log.d(TAG, "getContestList" + t.getMessage());
                    if (cnt_getContestList < 5) setContest();
                    else
                        Toast.makeText(((MainActivity) getContext()), "Please reloading", Toast.LENGTH_SHORT).show();
                    cnt_getContestList++;
                }
            });
        } else {
            setContestView();
        }

    }

    public void setContestView(){
        String filename = "contest";
        if (getContext() == null) return;
        File file = new File(getContext().getFilesDir() + "/" + filename);
        if (!file.exists()) { return; }
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

            Gson gson = new GsonBuilder()
                    .setLenient()
                    .create();

            result = gson.fromJson(response, new TypeToken<ArrayList<com.dum.dodam.Contest.dataframe.ContestFrame>>() {
            }.getType());

            contestList.clear();
            for (int i = 0; i < 10; i++) {
                ContestFrame frame = new ContestFrame();
                frame.title = result.get(i).title;
                frame.imageUrl = result.get(i).imageUrl;
                frame.title = result.get(i).title;
                frame.content = result.get(i).content;
                frame.area = result.get(i).area;
                frame.sponsor = result.get(i).sponsor;
                frame.prize = result.get(i).prize;
                frame.firstPrize = result.get(i).firstPrize;
                frame.homePage = result.get(i).homePage;
                frame.imageUrl = result.get(i).imageUrl;
                frame.start = result.get(i).start;
                frame.end = result.get(i).end;

                contestList.add(frame);
            }
            contest_adapter.notifyDataSetChanged();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void onCommunityItemSelected(View v, int position) {
        MyCommunityAdapter.Holder holder = (MyCommunityAdapter.Holder) myCommunity_recyclerView.findViewHolderForAdapterPosition(position);
        int communityID = holder.communityID;
        String title = holder.community_name.getText().toString();
        int communityType = holder.communityType;
        ((MainActivity) getActivity()).replaceFragmentFull(Community.newInstance(communityType, communityID, title));
    }

    @Override
    public void onArticleItemSelected(View v, int position) {
        HotArticleAdapter.Holder holder = (HotArticleAdapter.Holder) hotArticle_recyclerView.findViewHolderForAdapterPosition(position);
        int communityType = holder.communityType;
        int communityID = holder.communityID;
        int articleID = Integer.parseInt(holder.articleID.getText().toString());

        ((MainActivity) getActivity()).replaceFragmentFull(Article.newInstance(articleID, communityType, communityID));
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

                String lunch = " ";

                if (getWeekNDate().get(0) == 1) {
                    lunch = list.get(getWeekNDate().get(1)).lunch_monday;
                } else if (getWeekNDate().get(0) == 2) {
                    lunch = list.get(getWeekNDate().get(1)).lunch_tuesday;
                } else if (getWeekNDate().get(0) == 3) {
                    lunch = list.get(getWeekNDate().get(1)).lunch_wednesday;
                } else if (getWeekNDate().get(0) == 4) {
                    lunch = list.get(getWeekNDate().get(1)).lunch_thursday;
                } else if (getWeekNDate().get(0) == 5) {
                    lunch = list.get(getWeekNDate().get(1)).lunch_friday;
                }
                if (lunch.equals(" ")) cafeteria.setText("제공되는 식단이 없습니다.");
                else {
                    cafeteria.setText(lunch);
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IndexOutOfBoundsException e) {
                cafeteria.setText("저장된 식단정보가 없습니다. 고객센터로 문의해주세요.");
                String[] filenames = {"nextCafeteria", "curCafeteria", "versionCafeteria"};
                for (String f : filenames) {
                    file = new File(getContext().getFilesDir() + "/" + f);
                    file.delete();
                }
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
        int sDayNum = c.get(Calendar.DAY_OF_WEEK);
        result.add(this_week);
        result.add(sDayNum);

        return result;
    }

    @Override
    public void onContestItemSelected(View v, int position) {
        ContestAdapter.Holder holder = (ContestAdapter.Holder) contest_recyclerView.findViewHolderForAdapterPosition(position);
        com.dum.dodam.Contest.dataframe.ContestFrame frame = new com.dum.dodam.Contest.dataframe.ContestFrame();
        frame.title = holder.contest_name.getText().toString();
        frame.content = holder.content;
        frame.area = holder.area;
        frame.sponsor = holder.sponsor;
        frame.prize = holder.prize;
        frame.firstPrize = holder.firstPrize;
        frame.homePage = holder.homePage;
        frame.imageUrl = holder.imageUrl;
        frame.start = holder.start;
        frame.end = holder.end;

        ((MainActivity) getActivity()).replaceFragmentFull(new ContestInfo(frame));
    }
}
package com.dum.dodam.Home;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Cafeteria.CafeteriaTab;
import com.dum.dodam.Cafeteria.dataframe.MealInfo;
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
import com.dum.dodam.LocalDB.CafeteriaDB;
import com.dum.dodam.LocalDB.CafeteriaModule;
import com.dum.dodam.LocalDB.CafeteriaWeek;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.Mypage.Mypage;
import com.dum.dodam.R;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.google.android.material.appbar.AppBarLayout;
import com.google.android.material.appbar.CollapsingToolbarLayout;
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
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class Home extends Fragment implements HotArticleAdapter.OnListItemSelectedInterface, MyCommunityAdapter.OnListItemSelectedInterface, ContestAdapter.OnListItemSelectedInterface {
    private static final String TAG = "RHC";

    private boolean firstTime = true;
    private int cnt_myCommunity = 0;
    private int cnt_hotArticle = 0;
    private int cnt_getContestList = 0;

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
    private TextView more_contest;
    private TextView title_contest;
    private ImageView mySetting;

    private LinearLayout today_lunch;

    private CollapsingToolbarLayout toolBarLayout;

    private Realm realm;
    private ArrayList<CafeteriaWeek> cafeteriaWeeks = new ArrayList<>();
    private boolean isFirstWeekNull;
    private String version;
    private ArrayList<MealInfo.MIF> resultMeal;
    private List<Integer> mydate;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.home_, container, false);
        view.setClickable(true);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            if (view != null) {
                // 23 버전 이상일 때 상태바 하얀 색상에 회색 아이콘 색상을 설정
                view.setSystemUiVisibility(View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR);
                getActivity().getWindow().setStatusBarColor(Color.parseColor("#fbdd56"));
            }
        } else if (Build.VERSION.SDK_INT >= 21) {
            // 21 버전 이상일 때
            getActivity().getWindow().setStatusBarColor(Color.BLACK);
        }

        RealmCafeteriaInit();
        mydate = getWeekNDate();
        version = String.format("%d%d", mydate.get(0), mydate.get(1));
        cafeteria = view.findViewById(R.id.cafeteria);
        checkCafeteriaList();

        user = ((MainActivity) getActivity()).getUser();

        final Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        toolBarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        AppBarLayout appBarLayout = (AppBarLayout) view.findViewById(R.id.app_bar);
        TextView textView = view.findViewById(R.id.welcome_text);
        textView.setText(String.format("%s\n%s님, 안녕하세요:)", user.schoolName, user.userName));
//        appbar가 접히는지에 따른 이벤트 발생
        appBarLayout.addOnOffsetChangedListener(new AppBarLayout.OnOffsetChangedListener() {
            int scrollRange = -1;

            @Override
            public void onOffsetChanged(final AppBarLayout appBarLayout, int verticalOffset) {
                //Initialize the size of the scroll
                if (scrollRange == -1) {
                    scrollRange = appBarLayout.getTotalScrollRange();
                }
                //Check if the view is collapsed
                if (scrollRange + verticalOffset == 0) {
                    toolBarLayout.setTitle(String.format("%s %s님", user.schoolName, user.userName));
                    appBarLayout.setBackgroundResource(R.color.saffron);
                    toolbar.setBackgroundColor(getActivity().getResources().getColor(R.color.saffron));
                } else {
                    toolBarLayout.setTitle("");
                    toolbar.setBackgroundColor(Color.TRANSPARENT);
                }
            }
        });

        today_lunch = view.findViewById(R.id.today_lunch);
        today_lunch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragmentFull(new CafeteriaTab(cafeteriaWeeks, isFirstWeekNull));
            }
        });

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
//        myCommunity_recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
        myCommunity_recyclerView.setHasFixedSize(true);
        myCommunity_layoutManager = new LinearLayoutManager(getActivity());
        myCommunity_recyclerView.setLayoutManager(myCommunity_layoutManager);
        myCommunity_recyclerView.setAdapter(myCommunity_adapter);

        // Recyclerview HotArticle
        hotArticle_adapter = new HotArticleAdapter(getContext(), hotArticleList, this, user);
        hotArticle_recyclerView = (RecyclerView) view.findViewById(R.id.rv_hot_article);
//        hotArticle_recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.VERTICAL));
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

        mySetting = view.findViewById(R.id.user_icon);
        mySetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentPopup(new Mypage());
            }
        });

        if (firstTime) {
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
            final String contestID = "0";
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
                                sObject.put("stroedDate", result.get(i).storedDate);
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
                            fileWriter = new FileWriter(file, false);
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

    public void setContestView() {
        String filename = "contest";
        if (getContext() == null) return;
        File file = new File(getContext().getFilesDir() + "/" + filename);
        if (!file.exists()) {
            return;
        }
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

    public void setTodayCafeteria() {
        //year, month, today, this_week, last_date, start_DOW
        CafeteriaWeek this_week_lunch;
        if (isFirstWeekNull) this_week_lunch = cafeteriaWeeks.get(mydate.get(3) - 2);
        else this_week_lunch = cafeteriaWeeks.get(mydate.get(3) - 1);

        String today = String.format("%02d", mydate.get(2));
        String today_menu = "";

        if (this_week_lunch.mondayDate.equals(today))
            today_menu = (this_week_lunch.monday);
        else if (this_week_lunch.tuesdayDate.equals(today))
            today_menu = (this_week_lunch.tuesday);
        else if (this_week_lunch.wednesdayDate.equals(today))
            today_menu = (this_week_lunch.wednesday);
        else if (this_week_lunch.thursdayDate.equals(today))
            today_menu = (this_week_lunch.thursday);
        else if (this_week_lunch.fridayDate.equals(today))
            today_menu = (this_week_lunch.friday);

        if (today_menu.equals("") || today_menu.equals("없음")) {
            today_menu = "가족과 집에서 함께 먹는 따듯한 식사";
        }

        cafeteria.setText(today_menu);
    }

    public void checkCafeteriaList() {
        CafeteriaDB cafeteriaDB = realm.where(CafeteriaDB.class).findFirst();

        if (cafeteriaDB == null || !cafeteriaDB.version.equals(version)) {
            saveCafeteria();
        } else {
            loadCafeteria(cafeteriaDB);
        }
    }

    public void loadCafeteria(CafeteriaDB cafeteriaDB) {
        cafeteriaWeeks.clear();

        if (cafeteriaDB.week1.mondayDate.equals("") && cafeteriaDB.week1.tuesdayDate.equals("") && cafeteriaDB.week1.wednesdayDate.equals("") && cafeteriaDB.week1.thursdayDate.equals("") && cafeteriaDB.week1.fridayDate.equals(""))
            isFirstWeekNull = true;
        else
            cafeteriaWeeks.add(cafeteriaDB.week1);
        cafeteriaWeeks.add(cafeteriaDB.week2);
        cafeteriaWeeks.add(cafeteriaDB.week3);
        cafeteriaWeeks.add(cafeteriaDB.week4);
        cafeteriaWeeks.add(cafeteriaDB.week5);
        if (!(cafeteriaDB.week6.mondayDate.equals("") && cafeteriaDB.week6.tuesdayDate.equals("") && cafeteriaDB.week6.wednesdayDate.equals("") && cafeteriaDB.week6.thursdayDate.equals("") && cafeteriaDB.week6.fridayDate.equals("")))
            cafeteriaWeeks.add(cafeteriaDB.week6);

        setTodayCafeteria();
    }

    public void saveCafeteria() {
        UserJson user = ((MainActivity) getActivity()).getUser();

        String ATPT_OFCDC_SC_CODE = user.I_CODE;
        String SD_SCHUL_CODE = user.SC_CODE;
        String MLSV_YMD = version;

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://open.neis.go.kr/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitService2 service = retrofit.create(RetrofitService2.class);

        Call<MealInfo> call = service.getMealDietInfo("4db607519a0e40b2910efcdf0070a215", "json", "2", ATPT_OFCDC_SC_CODE, SD_SCHUL_CODE, MLSV_YMD);
        call.enqueue(new Callback<MealInfo>() {
            @Override
            public void onResponse(Call<MealInfo> call, final Response<MealInfo> response) {
                if (response.isSuccessful()) {
                    resultMeal = response.body().mealServiceDietInfo.get(1).row;

                    realm.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm) {
                            CafeteriaDB beforeCafeteriaDB = realm.where(CafeteriaDB.class).findFirst();
                            RealmResults<CafeteriaWeek> beforeCafeteriaWeek = realm.where(CafeteriaWeek.class).findAll();
                            if (beforeCafeteriaDB != null) beforeCafeteriaDB.deleteFromRealm();
                            if (beforeCafeteriaWeek != null)
                                beforeCafeteriaWeek.deleteAllFromRealm();

                            CafeteriaDB cafeteriaDB = realm.createObject(CafeteriaDB.class);
                            CafeteriaWeek week1 = realm.createObject(CafeteriaWeek.class);
                            CafeteriaWeek week2 = realm.createObject(CafeteriaWeek.class);
                            CafeteriaWeek week3 = realm.createObject(CafeteriaWeek.class);
                            CafeteriaWeek week4 = realm.createObject(CafeteriaWeek.class);
                            CafeteriaWeek week5 = realm.createObject(CafeteriaWeek.class);
                            CafeteriaWeek week6 = realm.createObject(CafeteriaWeek.class);

                            int index = 0;
                            int DOW = mydate.get(5);
                            int last_date = mydate.get(4);
                            int this_week = 0;
                            int loop_start = 1;

                            String not_provided = "없음";

                            List<CafeteriaWeek> weeks = Arrays.asList(week1, week2, week3, week4, week5, week6);
                            cafeteriaDB.version = version;
                            CafeteriaWeek cafeteriaWeek;

                            if (DOW == 7) {
                                DOW = 2;
                                loop_start = 3;
                                this_week++;
                                cafeteriaWeek = weeks.get(1);
                            } else if (DOW == 1) {
                                DOW = 2;
                                loop_start = 2;
                                this_week++;
                                cafeteriaWeek = weeks.get(1);
                            } else {
                                cafeteriaWeek = weeks.get(0);
                            }
                            // 이전달 값 세팅
                            if (DOW != 2) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.add(calendar.MONTH, -1);
                                int last_date_of_before_month = calendar.getActualMaximum(Calendar.DAY_OF_MONTH);
                                int before_month = calendar.get(Calendar.MONTH) + 1;
                                int tmp_DOW = DOW - 1;
                                while (tmp_DOW > 1) {
                                    if (tmp_DOW == 6) {
                                        cafeteriaWeek.fridayDate = String.format("%02d/%02d", before_month, last_date_of_before_month);
                                        tmp_DOW--;
                                        last_date_of_before_month--;
                                    } else if (tmp_DOW == 5) {
                                        cafeteriaWeek.thursdayDate = String.format("%02d/%02d", before_month, last_date_of_before_month);
                                        tmp_DOW--;
                                        last_date_of_before_month--;
                                    } else if (tmp_DOW == 4) {
                                        cafeteriaWeek.wednesdayDate = String.format("%02d/%02d", before_month, last_date_of_before_month);
                                        tmp_DOW--;
                                        last_date_of_before_month--;
                                    } else if (tmp_DOW == 3) {
                                        cafeteriaWeek.tuesdayDate = String.format("%02d/%02d", before_month, last_date_of_before_month);
                                        tmp_DOW--;
                                        last_date_of_before_month--;
                                    } else if (tmp_DOW == 2) {
                                        cafeteriaWeek.mondayDate = String.format("%02d/%02d", before_month, last_date_of_before_month);
                                        tmp_DOW--;
                                        last_date_of_before_month--;
                                    }
                                }
                            }
                            //이번달 값 세팅
                            for (int i = loop_start; i < last_date + 1; i++) {
                                if (DOW == 2) { //월요일
                                    cafeteriaWeek.mondayDate = String.format("%02d", i);
                                    if (resultMeal.get(index).MLSV_YMD.substring(6).equals(String.format("%02d", i))) {
                                        cafeteriaWeek.monday = resultMeal.get(index).DDISH_NM.replace("<br/>", " ").replaceAll("\\d|\\.", "");
                                        cafeteriaWeek.mondayCal = resultMeal.get(index).CAL_INFO;
                                        index++;
                                    } else {
                                        cafeteriaWeek.monday = not_provided;
                                        cafeteriaWeek.mondayCal = " ";
                                    }
                                    DOW++;
                                } else if (DOW == 3) { //화요일
                                    cafeteriaWeek.tuesdayDate = String.format("%02d", i);
                                    if (resultMeal.get(index).MLSV_YMD.substring(6).equals(String.format("%02d", i))) {
                                        cafeteriaWeek.tuesday = resultMeal.get(index).DDISH_NM.replace("<br/>", " ").replaceAll("\\d|\\.", "");
                                        cafeteriaWeek.tuesdayCal = resultMeal.get(index).CAL_INFO;
                                        index++;
                                    } else {
                                        cafeteriaWeek.tuesday = not_provided;
                                        cafeteriaWeek.tuesdayCal = " ";
                                    }
                                    DOW++;
                                } else if (DOW == 4) { //수요일
                                    cafeteriaWeek.wednesdayDate = String.format("%02d", i);
                                    if (resultMeal.get(index).MLSV_YMD.substring(6).equals(String.format("%02d", i))) {
                                        cafeteriaWeek.wednesday = resultMeal.get(index).DDISH_NM.replace("<br/>", " ").replaceAll("\\d|\\.", "");
                                        cafeteriaWeek.wednesdayCal = resultMeal.get(index).CAL_INFO;
                                        index++;
                                    } else {
                                        cafeteriaWeek.wednesday = not_provided;
                                        cafeteriaWeek.wednesdayCal = " ";
                                    }
                                    DOW++;
                                } else if (DOW == 5) { //목요일
                                    cafeteriaWeek.thursdayDate = String.format("%02d", i);
                                    if (resultMeal.get(index).MLSV_YMD.substring(6).equals(String.format("%02d", i))) {
                                        cafeteriaWeek.thursday = resultMeal.get(index).DDISH_NM.replace("<br/>", " ").replaceAll("\\d|\\.", "");
                                        cafeteriaWeek.thursdayCal = resultMeal.get(index).CAL_INFO;
                                        index++;
                                    } else {
                                        cafeteriaWeek.thursday = not_provided;
                                        cafeteriaWeek.thursdayCal = " ";
                                    }
                                    DOW++;
                                } else if (DOW == 6) { //금요일
                                    cafeteriaWeek.fridayDate = String.format("%02d", i);
                                    if (resultMeal.get(index).MLSV_YMD.substring(6).equals(String.format("%02d", i))) {
                                        cafeteriaWeek.friday = resultMeal.get(index).DDISH_NM.replace("<br/>", " ").replaceAll("\\d|\\.", "");
                                        cafeteriaWeek.fridayCal = resultMeal.get(index).CAL_INFO;
                                        index++;
                                    } else {
                                        cafeteriaWeek.friday = not_provided;
                                        cafeteriaWeek.fridayCal = " ";
                                    }
                                    //주말 건너뛰기
                                    i += 2;
                                    this_week++;
                                    cafeteriaWeek = weeks.get(this_week);
                                    DOW = 2;
                                }
                            }
                            //다음달 값 세팅
                            int tmp_date = 1;
                            if (DOW != 2) {
                                Calendar calendar = Calendar.getInstance();
                                calendar.add(calendar.MONTH, +1);
                                int next_month = calendar.get(calendar.MONTH) + 1;
                                while (DOW < 7) {
                                    if (DOW == 2) {
                                        cafeteriaWeek.mondayDate = String.format("%02d/%02d", next_month, tmp_date);
                                        DOW++;
                                        tmp_date++;
                                    } else if (DOW == 3) {
                                        cafeteriaWeek.tuesdayDate = String.format("%02d/%02d", next_month, tmp_date);
                                        DOW++;
                                        tmp_date++;
                                    } else if (DOW == 4) {
                                        cafeteriaWeek.wednesdayDate = String.format("%02d/%02d", next_month, tmp_date);
                                        DOW++;
                                        tmp_date++;
                                    } else if (DOW == 5) {
                                        cafeteriaWeek.thursdayDate = String.format("%02d/%02d", next_month, tmp_date);
                                        DOW++;
                                        tmp_date++;
                                    } else if (DOW == 6) {
                                        cafeteriaWeek.fridayDate = String.format("%02d/%02d", next_month, tmp_date);
                                        DOW++;
                                        tmp_date++;
                                    }
                                }
                            }

                            cafeteriaDB.week1 = week1;
                            cafeteriaDB.week2 = week2;
                            cafeteriaDB.week3 = week3;
                            cafeteriaDB.week4 = week4;
                            cafeteriaDB.week5 = week5;
                            cafeteriaDB.week6 = week6;
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            CafeteriaDB cafeteriaDB = realm.where(CafeteriaDB.class).findFirst();
                            loadCafeteria(cafeteriaDB);
                        }
                    });
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<MealInfo> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });

    }

    private void RealmCafeteriaInit() {
        RealmConfiguration config = new RealmConfiguration.Builder().name("Cafeteria.realm").schemaVersion(1).modules(new CafeteriaModule()).build();
        realm = Realm.getInstance(config);
    }

    public interface RetrofitService2 {
        @GET("/hub/mealServiceDietInfo")
        Call<MealInfo> getMealDietInfo(@Query("KEY") String KEY,
                                       @Query("Type") String Type,
                                       @Query("MMEAL_SC_CODE") String MMEAL_SC_CODE,
                                       @Query("ATPT_OFCDC_SC_CODE") String ATPT_OFCDC_SC_CODE,
                                       @Query("SD_SCHUL_CODE") String SD_SCHUL_CODE,
                                       @Query("MLSV_YMD") String MLSV_YMD);
    }

    public static ArrayList<Integer> getWeekNDate() {
        ArrayList<Integer> result = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        int this_week = c.get(Calendar.WEEK_OF_MONTH);
        int today = c.get(Calendar.DATE); //오늘 일자 저장
        int month = c.get(Calendar.MONTH);
        int year = c.get(Calendar.YEAR);
        int last_date = c.getActualMaximum(Calendar.DAY_OF_MONTH);

        c.set(Calendar.DAY_OF_MONTH, 1); //DAY_OF_MONTH를 1로 설정 (월의 첫날)
        int start_DOW = c.get(Calendar.DAY_OF_WEEK); //그 주의 요일 반환 (일:1 ~ 토:7)

        result.add(year);
        result.add(month + 1);
        result.add(today);
        result.add(this_week);
        result.add(last_date);
        result.add(start_DOW);

        //year, month, today, this_week, last_date, start_DOW

        return result;
    }

}
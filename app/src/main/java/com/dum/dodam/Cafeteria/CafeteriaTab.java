package com.dum.dodam.Cafeteria;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import com.dum.dodam.Cafeteria.dataframe.MealInfo;
import com.dum.dodam.LocalDB.CafeteriaDB;
import com.dum.dodam.LocalDB.CafeteriaModule;
import com.dum.dodam.LocalDB.CafeteriaWeek;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

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

public class CafeteriaTab extends Fragment {

    private static String TAG = "RHC";

    private static ArrayList<CafeteriaDB> menu = new ArrayList<>();
    private List<Integer> mydate;
    private ArrayList<MealInfo.MIF> result;
    private String version;

    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cafeteria_, container, false);
        view.setClickable(true);
        RealmCafeteriaInit();

        mydate = getWeekNDate();
        version = String.format("%d%d", mydate.get(0), mydate.get(1));
        checkCafeteriaList();

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setTitle(String.format("%d 월", mydate.get(1)));

//        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
//        CafeteriaViewPageAdapter viewPageAdapter = new CafeteriaViewPageAdapter(getChildFragmentManager(), menu);
//        pager.setAdapter(viewPageAdapter);
//
//        TabLayout tab_layout = (TabLayout) view.findViewById(R.id.tab_layout);
//        tab_layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
//        tab_layout.setupWithViewPager(pager);
//        tab_layout.getTabAt(this_week).select();

        return view;
    }

    public void checkCafeteriaList() {
        Log.d(TAG, "Check Cafeteria");

        CafeteriaDB cafeteriaDB = realm.where(CafeteriaDB.class).findFirst();

        if (cafeteriaDB == null || !cafeteriaDB.version.equals(version)) {
            saveCafeteria();
        } else {
//            loadCafeteria();
            saveCafeteria();
        }
    }

    public void loadCafeteria() {
        Log.d(TAG, "loadCafeteria: ");
        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CafeteriaDB cafeteriaDB = realm.where(CafeteriaDB.class).findFirst();
            }
        });
    }

    public void saveCafeteria() {
        Log.d(TAG, "saveCafeteria: ");
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

        Log.d(TAG, String.format("%s %s %s", ATPT_OFCDC_SC_CODE, SD_SCHUL_CODE, MLSV_YMD));

        Call<MealInfo> call = service.getMealDietInfo("4db607519a0e40b2910efcdf0070a215", "json", "2", ATPT_OFCDC_SC_CODE, SD_SCHUL_CODE, MLSV_YMD);
        call.enqueue(new Callback<MealInfo>() {
            @Override
            public void onResponse(Call<MealInfo> call, final Response<MealInfo> response) {
                if (response.isSuccessful()) {
                    result = response.body().mealServiceDietInfo.get(1).row;

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

                            String not_provided = "제공되지 않음";

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

                            for (int i = loop_start; i < last_date + 1; i++) {
                                Log.d(TAG, "date " + i);
                                Log.d(TAG, "DOW " + DOW);
                                Log.d(TAG, "this_week " + this_week);
                                if (DOW == 2) { //월요일
                                    cafeteriaWeek.mondayDate = i;
                                    if (result.get(index).MLSV_YMD.substring(6).equals(String.format("%02d", i))) {
                                        cafeteriaWeek.monday = result.get(index).DDISH_NM.replace("<br/>", " ").replaceAll("\\d|\\.", "");
                                        cafeteriaWeek.mondayCal = result.get(index).CAL_INFO;
                                        index++;
                                    } else {
                                        cafeteriaWeek.monday = not_provided;
                                        cafeteriaWeek.mondayCal = " ";
                                    }
                                    DOW++;
                                } else if (DOW == 3) { //화요일
                                    cafeteriaWeek.tuesdayDate = i;
                                    if (result.get(index).MLSV_YMD.substring(6).equals(String.format("%02d", i))) {
                                        cafeteriaWeek.tuesday = result.get(index).DDISH_NM.replace("<br/>", " ").replaceAll("\\d|\\.", "");
                                        cafeteriaWeek.tuesdayCal = result.get(index).CAL_INFO;
                                        index++;
                                    } else {
                                        cafeteriaWeek.tuesday = not_provided;
                                        cafeteriaWeek.tuesdayCal = " ";
                                    }
                                    DOW++;
                                } else if (DOW == 4) { //수요일
                                    cafeteriaWeek.wednesdayDate = i;
                                    if (result.get(index).MLSV_YMD.substring(6).equals(String.format("%02d", i))) {
                                        cafeteriaWeek.wednesday = result.get(index).DDISH_NM.replace("<br/>", " ").replaceAll("\\d|\\.", "");
                                        cafeteriaWeek.wednesdayCal = result.get(index).CAL_INFO;
                                        index++;
                                    } else {
                                        cafeteriaWeek.wednesday = not_provided;
                                        cafeteriaWeek.wednesdayCal = " ";
                                    }
                                    DOW++;
                                } else if (DOW == 5) { //목요일
                                    cafeteriaWeek.thursdayDate = i;
                                    if (result.get(index).MLSV_YMD.substring(6).equals(String.format("%02d", i))) {
                                        cafeteriaWeek.thursday = result.get(index).DDISH_NM.replace("<br/>", " ").replaceAll("\\d|\\.", "");
                                        cafeteriaWeek.thursdayCal = result.get(index).CAL_INFO;
                                        index++;
                                    } else {
                                        cafeteriaWeek.thursday = not_provided;
                                        cafeteriaWeek.thursdayCal = " ";
                                    }
                                    DOW++;
                                } else if (DOW == 6) { //금요일
                                    cafeteriaWeek.fridayDate = i;
                                    if (result.get(index).MLSV_YMD.substring(6).equals(String.format("%02d", i))) {
                                        cafeteriaWeek.friday = result.get(index).DDISH_NM.replace("<br/>", " ").replaceAll("\\d|\\.", "");
                                        cafeteriaWeek.fridayCal = result.get(index).CAL_INFO;
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
                            cafeteriaDB.week1 = week1;
                            cafeteriaDB.week2 = week2;
                            cafeteriaDB.week3 = week3;
                            cafeteriaDB.week4 = week4;
                            cafeteriaDB.week5 = week5;
                            cafeteriaDB.week6 = week6;
                        }
                    });

                    loadCafeteria();
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

        return result;
    }

    private void RealmCafeteriaInit() {
        RealmConfiguration config = new RealmConfiguration.Builder().name("Cafeteria.realm").schemaVersion(1).modules(new CafeteriaModule()).build();
        realm = Realm.getInstance(config);
    }

    @Override
    public void onStop() {
        super.onStop();
        realm.close();
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
}
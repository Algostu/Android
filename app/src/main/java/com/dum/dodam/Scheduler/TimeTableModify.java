package com.dum.dodam.Scheduler;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.dum.dodam.LocalDB.CustomTimeTableDB;
import com.dum.dodam.LocalDB.CustomTimeTableModule;
import com.dum.dodam.LocalDB.TimeTableDB;
import com.dum.dodam.LocalDB.TimeTableDay;
import com.dum.dodam.LocalDB.TimeTableModule;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.Scheduler.dataframe.SchoolTimeTable;
import com.dum.dodam.databinding.SchedulerTimeTableModifyBinding;
import com.dum.dodam.databinding.SchedulerTimeTableModifyDayLayoutBinding;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;

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

public class TimeTableModify extends Fragment {

    private static final String TAG = "RHC";
    private SchedulerTimeTableModifyBinding layout;

    private SchedulerTimeTableModifyDayLayoutBinding monday;
    private SchedulerTimeTableModifyDayLayoutBinding tuesday;
    private SchedulerTimeTableModifyDayLayoutBinding wednesday;
    private SchedulerTimeTableModifyDayLayoutBinding thursday;
    private SchedulerTimeTableModifyDayLayoutBinding friday;

    private Realm realm;
    private Realm realm2;

    private ArrayList<TimeTableDay> list = new ArrayList<>();
    private ArrayList<SchedulerTimeTableModifyDayLayoutBinding> layoutList = new ArrayList<>();

    public TimeTableModify(ArrayList<TimeTableDay> list) {
        this.list = list;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = DataBindingUtil.inflate(inflater, R.layout.scheduler_time_table_modify, container, false);
        layout.getRoot().setClickable(true);
        RealmCustomTimeTableInit();
        RealmTimeTableInit();

        monday = layout.mondayLayout;
        tuesday = layout.tuesdayLayout;
        wednesday = layout.wednesdayLayout;
        thursday = layout.thursdayLayout;
        friday = layout.fridayLayout;

        layoutList.add(friday);
        layoutList.add(thursday);
        layoutList.add(wednesday);
        layoutList.add(tuesday);
        layoutList.add(monday);

        setDaySubject();

        monday.DOW.setText("월요일");
        tuesday.DOW.setText("화요일");
        wednesday.DOW.setText("수요일");
        thursday.DOW.setText("목요일");
        friday.DOW.setText("금요일");

        layout.btnDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveTimeTable(getThisMonthStartEndDate().get(0), getThisMonthStartEndDate().get(1));
            }
        });

        layout.btnModify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MakeCustomTimeTable();
            }
        });

        return layout.getRoot();
    }

    public void MakeCustomTimeTable() {
        realm2.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm2) {
                TimeTableDB timeTableDB = realm2.where(TimeTableDB.class).findFirst();
                timeTableDB.custom = true;
            }
        });

        realm.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                CustomTimeTableDB beforeCustomTimeTableDB = realm.where(CustomTimeTableDB.class).findFirst();
                if (beforeCustomTimeTableDB != null) beforeCustomTimeTableDB.deleteFromRealm();
                RealmResults<TimeTableDay> beforeTimeTableDay = realm.where(TimeTableDay.class).findAll();
                if (beforeTimeTableDay != null) beforeTimeTableDay.deleteAllFromRealm();

                CustomTimeTableDB customTimeTableDB = realm.createObject(CustomTimeTableDB.class);
                TimeTableDay timeTableDay = realm.createObject(TimeTableDay.class);
                int i;
                for (i = 4; i >= 0; i--) {
                    timeTableDay.subject.add(layoutList.get(i).subject1.getText().toString());
                    timeTableDay.subject.add(layoutList.get(i).subject2.getText().toString());
                    timeTableDay.subject.add(layoutList.get(i).subject3.getText().toString());
                    timeTableDay.subject.add(layoutList.get(i).subject4.getText().toString());
                    timeTableDay.subject.add(layoutList.get(i).subject5.getText().toString());
                    timeTableDay.subject.add(layoutList.get(i).subject6.getText().toString());
                    timeTableDay.subject.add(layoutList.get(i).subject7.getText().toString());
                    customTimeTableDB.days.add(timeTableDay);
                    timeTableDay = realm.createObject(TimeTableDay.class);
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "새로운 시간표가 생성되었습니다.", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    private void RealmCustomTimeTableInit() {
        RealmConfiguration config = new RealmConfiguration.Builder().name("CustomTimeTableDB.realm").schemaVersion(1).modules(new CustomTimeTableModule()).build();
        realm = Realm.getInstance(config);
    }

    private void RealmTimeTableInit() {
        RealmConfiguration config2 = new RealmConfiguration.Builder().name("TimeTableDB.realm").schemaVersion(1).modules(new TimeTableModule()).build();
        realm2 = Realm.getInstance(config2);
    }

    public void setDaySubject() {
        Collections.reverse(list);
        for (int i = 0; i < list.size(); i++) {
            try {
                layoutList.get(i).subject1.setText(list.get(i).subject.get(1));
            } catch (Exception e) {
            }
            try {
                layoutList.get(i).subject2.setText(list.get(i).subject.get(2));
            } catch (Exception e) {
            }
            try {
                layoutList.get(i).subject3.setText(list.get(i).subject.get(3));
            } catch (Exception e) {
            }
            try {
                layoutList.get(i).subject4.setText(list.get(i).subject.get(4));
            } catch (Exception e) {
            }
            try {
                layoutList.get(i).subject5.setText(list.get(i).subject.get(5));
            } catch (Exception e) {
            }
            try {
                layoutList.get(i).subject6.setText(list.get(i).subject.get(6));
            } catch (Exception e) {
            }
            try {
                layoutList.get(i).subject7.setText(list.get(i).subject.get(7));
            } catch (Exception e) {
            }
        }
    }

    public void checkTimeTable() {
        realm2.executeTransactionAsync(new Realm.Transaction() {
            @Override
            public void execute(Realm realm) {
                TimeTableDB timeTableDB = realm2.where(TimeTableDB.class).findFirst();
                ArrayList<String> startEnd = getThisMonthStartEndDate();
                String version = startEnd.get(0);

                if (timeTableDB == null || !timeTableDB.version.equals(version)) {
                    saveTimeTable(startEnd.get(0), startEnd.get(1));
                } else {
                    timeTableDB.custom = false;
                }
            }
        }, new Realm.Transaction.OnSuccess() {
            @Override
            public void onSuccess() {
                Toast.makeText(getContext(), "업데이트가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                getActivity().getSupportFragmentManager().popBackStack();
            }
        });
    }

    public void saveTimeTable(final String TI_FROM_YMD, final String TI_TO_YMD) {
        UserJson user = ((MainActivity) getActivity()).getUser();
        String ATPT_OFCDC_SC_CODE = user.I_CODE;
        String SD_SCHUL_CODE = user.SC_CODE;
        int classNum = user.classNum;

        int grade = user.grade - 9;
        if (grade == 4) grade = 3;
        else if (grade <= 0) grade = 1;

        String GRADE = String.valueOf(grade);
        if (classNum == 0) classNum = 1;
        String CLASS_NM = String.valueOf(classNum);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("https://open.neis.go.kr/")
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitService2 service = retrofit.create(RetrofitService2.class);

        Call<SchoolTimeTable> call = service.getTimeTable("4db607519a0e40b2910efcdf0070a215", "json", 300, ATPT_OFCDC_SC_CODE, SD_SCHUL_CODE, TI_FROM_YMD, TI_TO_YMD, GRADE, CLASS_NM);
        call.enqueue(new Callback<SchoolTimeTable>() {
            @Override
            public void onResponse(Call<SchoolTimeTable> call, Response<SchoolTimeTable> response) {
                if (response.isSuccessful()) {
                    final ArrayList<SchoolTimeTable.TimeTable> result2;
                    try {
                        result2 = response.body().hisTimetable.get(1).row;
                    } catch (Exception e) {
                        e.printStackTrace();
                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("잘못된 정보입니다.");
                        builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int id) {
                            }
                        });
                        builder.show();
                        return;
                    }

                    realm2.executeTransactionAsync(new Realm.Transaction() {
                        @Override
                        public void execute(Realm realm2) {
                            TimeTableDB beforeTimeTableDB = realm2.where(TimeTableDB.class).findFirst();
                            if (beforeTimeTableDB != null) beforeTimeTableDB.deleteFromRealm();
                            RealmResults<TimeTableDay> beforeTimeTableDays = realm2.where(TimeTableDay.class).findAll();
                            if (beforeTimeTableDays != null)
                                beforeTimeTableDays.deleteAllFromRealm();

                            TimeTableDB timeTableDB = realm2.createObject(TimeTableDB.class);
                            TimeTableDay timeTableDay;
                            timeTableDB.version = TI_FROM_YMD;

                            int i;
                            ArrayList<String> customDateSet = getThisMonthStartEndDate();
                            int DOW = Integer.parseInt(customDateSet.get(5)) - 1;
                            for (i = 0; i < Integer.parseInt(customDateSet.get(4)) + 1; i++) {
                                timeTableDay = realm2.createObject(TimeTableDay.class);
                                timeTableDay.date = i;
                                timeTableDay.subject.add("");
                                if (DOW == 1) {
                                    timeTableDay.subject.add("일요일");
                                }
                                timeTableDB.days.add(timeTableDay);
                                DOW++;
                                if (DOW == 8) DOW = 1;
                            }

                            int position;
                            for (SchoolTimeTable.TimeTable table : result2) {
                                position = Integer.parseInt(table.ALL_TI_YMD.substring(6));
                                timeTableDB.days.get(position).subject.add(table.ITRT_CNTNT);
                            }
                        }
                    }, new Realm.Transaction.OnSuccess() {
                        @Override
                        public void onSuccess() {
                            Toast.makeText(getContext(), "시간표 업데이트가 완료되었습니다.", Toast.LENGTH_SHORT).show();
                            getActivity().getSupportFragmentManager().popBackStack();
                        }
                    });
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.errorBody().toString());
                }
            }

            @Override
            public void onFailure(Call<SchoolTimeTable> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public interface RetrofitService2 {
        @GET("/hub/hisTimetable")
        Call<SchoolTimeTable> getTimeTable(@Query("KEY") String KEY,
                                           @Query("Type") String Type,
                                           @Query("pSize") int pSize,
                                           @Query("ATPT_OFCDC_SC_CODE") String ATPT_OFCDC_SC_CODE,
                                           @Query("SD_SCHUL_CODE") String SD_SCHUL_CODE,
                                           @Query("TI_FROM_YMD") String TI_FROM_YMD,
                                           @Query("TI_TO_YMD") String TI_TO_YMD,
                                           @Query("GRADE") String GRADE,
                                           @Query("CLASS_NM") String CLASS_NM);
    }

    public ArrayList<String> getThisMonthStartEndDate() {
        Calendar cal = Calendar.getInstance();
        ArrayList<String> res = new ArrayList<>();

        int last_date = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
        int this_month = cal.get(Calendar.MONTH);
        int this_year = cal.get(Calendar.YEAR);

        res.add(String.format("%d%02d%02d", this_year, this_month + 1, 1));
        res.add(String.format("%d%02d%02d", this_year, this_month + 1, last_date));
        res.add(String.format("%02d", this_year));
        res.add(String.format("%02d", this_month));
        res.add(String.valueOf(last_date));

        cal.set(Calendar.DAY_OF_MONTH, 1); //DAY_OF_MONTH를 1로 설정 (월의 첫날)
        int start_DOW = cal.get(Calendar.DAY_OF_WEEK); //그 주의 요일 반환 (일:1 ~ 토:7)

        res.add(String.valueOf(start_DOW));

        return res;
    }
}

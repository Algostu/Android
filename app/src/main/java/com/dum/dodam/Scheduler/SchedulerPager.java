package com.dum.dodam.Scheduler;

import android.app.AlertDialog;
import android.app.TimePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.TimePicker;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.LocalDB.Todo;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.Scheduler.dataframe.SchoolTimeTable;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import io.realm.Realm;
import io.realm.RealmResults;
import petrov.kristiyan.colorpicker.ColorPicker;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class SchedulerPager extends Fragment implements
        TimeTableAdapter.OnListItemSelectedInterface,
        TodoListAdapter.OnListItemSelectedInterface {
    private static final String TAG = "SchedulerPager";
    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public Calendar startCalender;
    public Calendar endCalender;
    public int classNum;
    public String I_CODE;
    public String SC_CODE;
    public TextView tv_colorPicker;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView todoRecyclerView;
    private RecyclerView.Adapter todoAdapter;
    private RecyclerView.LayoutManager todoLayoutManager;
    SlidingUpPanelLayout slidingPaneLayout;
    public Button btn;

    public ArrayList<Todo> todoArrayList = new ArrayList<Todo>();

    public ImageView ic_trashcan;
    public View color_box_view;
    public int new_color;

    private ArrayList<String> list = new ArrayList<>();
    final ArrayList<Todo> tmp = new ArrayList<>();

    private ArrayList<SchoolTimeTable.TimeTable> result;

    private ArrayList<String> monday = new ArrayList<>();
    private ArrayList<String> tuesday = new ArrayList<>();
    private ArrayList<String> wednesday = new ArrayList<>();
    private ArrayList<String> thursday = new ArrayList<>();
    private ArrayList<String> friday = new ArrayList<>();
    private ArrayList<String> time_table = new ArrayList<>();

    private Realm realm;

    private String ID;
    private int day_of_week;
    private int this_month;
    private int this_year;
    private int this_date;

    public SchedulerPager(int day_of_week, int this_date, int this_month, int this_year) {
        this.this_date = this_date;
        this.this_month = this_month;
        this.this_year = this_year;
        this.day_of_week = day_of_week;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (data.getExtras().containsKey("startDate")) {
            String startDateStr = data.getExtras().getString("startDate");
            try {
                startCalender.setTime(format.parse(startDateStr));
                Log.d(TAG, "startTime changed");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // Use the returned value
        } else if (data.getExtras().containsKey("endDate")) {
            String endDateStr = data.getExtras().getString("endDate");
            try {
                endCalender.setTime(format.parse(endDateStr));
                Log.d(TAG, "endTime changed");
            } catch (ParseException e) {
                e.printStackTrace();
            }
            // Use the returned value
        }

    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scheduler_pager, container, false);
        view.setClickable(true);

        new_color = Color.parseColor("#ffab91");

        todoArrayList.clear();

        realm = Realm.getDefaultInstance();

        ID = String.valueOf(this_year) + String.valueOf(this_month);
        RealmResults<Todo> results = realm.where(Todo.class).equalTo("ID", ID).findAll();

        Calendar calendar = Calendar.getInstance();
        for (Todo todo : realm.copyFromRealm(results)) {
            calendar.setTimeInMillis(todo.start);
            if (calendar.get(Calendar.DATE) == this_date) {
                todoArrayList.add(todo);
            }
        }

        todoAdapter = new TodoListAdapter(getContext(), todoArrayList, this);
        adapter = new TimeTableAdapter(getContext(), list, this);

        String filename = "TimeTable";
        File file = new File(requireContext().getFilesDir() + "/" + filename);
        if (file.exists()) {
            readTimeTable();
        } else {
            getTimeTable();
        }

        todoRecyclerView = (RecyclerView) view.findViewById(R.id.rv_todo_list);
        todoRecyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL));
        todoRecyclerView.setHasFixedSize(true);
        layoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        todoRecyclerView.setLayoutManager(layoutManager);
        todoRecyclerView.setAdapter(todoAdapter);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_time_table);
        recyclerView.addItemDecoration(new DividerItemDecoration(view.getContext(), DividerItemDecoration.HORIZONTAL));
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager2 = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        recyclerView.setLayoutManager(layoutManager2);
        recyclerView.setAdapter(adapter);

        classNum = ((MainActivity) getActivity()).user.classNum;
        SC_CODE = ((MainActivity) getActivity()).user.SC_CODE;
        I_CODE = ((MainActivity) getActivity()).user.I_CODE;

        final TextView startTimeTV = view.findViewById(R.id.todo_start_time);
        final TextView endTimeTV = view.findViewById(R.id.todo_end_time);
        final TextView dates = view.findViewById(R.id.todo_dates);
        // sliding window

        final ImageView arrow = view.findViewById(R.id.arrow);
        slidingPaneLayout = view.findViewById(R.id.slidingWindow);
        slidingPaneLayout.setAnchorPoint(600);
        slidingPaneLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.d(TAG, "[onPanelSlide]slideOffset: " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
//                Log.d(TAG, "[onPanelStateChanged]previousState: " + previousState);
//                Log.d(TAG, "[onPanelStateChanged]newState: " + newState);
                if (newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)) {
                    if (!tmp.isEmpty()) {
                        todoArrayList.clear();
                        todoArrayList.addAll(tmp);
                        todoAdapter.notifyDataSetChanged();
                        tmp.clear();
                    }

                    arrow.setImageResource(R.drawable.ic_free_icon_up_arrow_626004);
                } else if (newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)) {
                    arrow.setImageResource(R.drawable.ic_free_icon_down_arrow_625946);
                }
            }
        });
        // 완료 버튼
        final EditText todo_title = view.findViewById(R.id.todo_title);
        btn = view.findViewById(R.id.btn_black);
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(todo_title.getText().toString())) {
                    todo_title.setError("제목을 입력해주세요");
                    return;
                }
                tmp.clear();
                tmp.addAll(todoArrayList);

                // TODO : db에 항목 추가 하기 및 recycler view 초기화
                realm.executeTransactionAsync(new Realm.Transaction() {
                    @Override
                    public void execute(Realm realm) {
                        int year = startCalender.get(Calendar.YEAR);
                        int month = startCalender.get(Calendar.MONTH) + 1;

                        Todo todo = realm.createObject(Todo.class);
                        todo.ID = String.valueOf(year) + String.valueOf(month);
                        todo.end = endCalender.getTimeInMillis();
                        todo.start = startCalender.getTimeInMillis();
                        todo.title = todo_title.getText().toString();
                        todo.done = false;
                        todo.color = new_color;

                        if (startCalender.get(Calendar.DATE) == this_date && startCalender.get(Calendar.MONTH) + 1 == this_month) {
                            tmp.add(realm.copyFromRealm(todo));
                        }

                    }
                }, new Realm.Transaction.OnSuccess() {
                    @Override
                    public void onSuccess() {
                    }
                }, new Realm.Transaction.OnError() {
                    @Override
                    public void onError(Throwable error) {
                    }
                });

                startCalender.getTimeInMillis();
                endCalender.getTimeInMillis();

                todo_title.setText("");
                Date date = new Date();
                startCalender.setTime(date);
                startCalender.set(Calendar.HOUR, 0);
                startCalender.set(Calendar.MINUTE, 0);
                endCalender.setTime(date);
                endCalender.set(Calendar.HOUR, 23);
                endCalender.set(Calendar.MINUTE, 59);
                dates.setText("날짜를 선택해주세요");
                startTimeTV.setText("시간을 선택해주세요");
                endTimeTV.setText("시간을 선택해주세요");
                new_color = Color.parseColor("#ffab91");
                slidingPaneLayout.setPanelState(SlidingUpPanelLayout.PanelState.COLLAPSED);
            }
        });

        // default time set
        Date date = new Date();
        startCalender = Calendar.getInstance();
        startCalender.setTime(date);
        startCalender.set(Calendar.HOUR, 0);
        startCalender.set(Calendar.MINUTE, 0);

        endCalender = Calendar.getInstance();
        endCalender.setTime(date);
        endCalender.set(Calendar.HOUR, 23);
        endCalender.set(Calendar.MINUTE, 59);

        // calender popup
        MaterialDatePicker.Builder<Pair<Long, Long>> builder = MaterialDatePicker.Builder.dateRangePicker();
        builder.setTitleText("Select A Date");
        builder.setSelection(new Pair<Long, Long>(startCalender.getTimeInMillis(), endCalender.getTimeInMillis()));
        final MaterialDatePicker materialDatePicker = builder.build();

        dates.setClickable(true);
        dates.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                CalenderPopUpFragment calenderPopUpFragment = CalenderPopUpFragment.newInstance(format.format(startCalender.getTime()), format.format(endCalender.getTime()));
//                calenderPopUpFragment.setTargetFragment(((MainActivity)getActivity()).getVisibleFragment(), 0);
//                calenderPopUpFragment.show(getActivity().getSupportFragmentManager(), CalenderPopUpFragment.TAG);
                if (materialDatePicker.isAdded()) return;
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                dates.setText(materialDatePicker.getHeaderText());
                Pair<Long, Long> times = (Pair<Long, Long>) materialDatePicker.getSelection();
                Calendar temp = Calendar.getInstance();
                temp.setTimeInMillis(times.first);
                startCalender.set(Calendar.DATE, temp.get(Calendar.DATE));
                startCalender.set(Calendar.MONTH, temp.get(Calendar.MONTH));
                temp.setTimeInMillis(times.second);
                endCalender.set(Calendar.DATE, temp.get(Calendar.DATE));
                endCalender.set(Calendar.MONTH, temp.get(Calendar.MONTH));
            }
        });

        // time setting
        startTimeTV.setClickable(true);
        endTimeTV.setClickable(true);
        startTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        startTimeTV.setText(selectedHour + ":" + selectedMinute);
                        startCalender.set(Calendar.HOUR, selectedHour);
                        startCalender.set(Calendar.MINUTE, selectedMinute);
                    }
                }, startCalender.get(Calendar.HOUR), startCalender.get(Calendar.MINUTE), false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        endTimeTV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // TODO Auto-generated method stub
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(getContext(), new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        endTimeTV.setText(selectedHour + ":" + selectedMinute);
                        endCalender.set(Calendar.HOUR, selectedHour);
                        endCalender.set(Calendar.MINUTE, selectedMinute);
                    }
                }, endCalender.get(Calendar.HOUR), endCalender.get(Calendar.MINUTE), false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        // color setting
        color_box_view = view.findViewById(R.id.color_box);
        tv_colorPicker = view.findViewById(R.id.tv_color_picker);
        tv_colorPicker.setClickable(true);
        tv_colorPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openColorPicker();
            }
        });

        ic_trashcan = view.findViewById(R.id.ic_trashcan);

        ic_trashcan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                for (Todo todo : todoArrayList) {
                    todo.visible ^= true;
                }
                todoAdapter.notifyDataSetChanged();
            }
        });

        return view;
    }

    public void openColorPicker() {
        final ColorPicker colorPicker = new ColorPicker(getActivity());  // ColorPicker 객체 생성
        ArrayList<String> colors = new ArrayList<>();  // Color 넣어줄 list

        colors.add("#ffab91");
        colors.add("#F48FB1");
        colors.add("#ce93d8");
        colors.add("#b39ddb");
        colors.add("#9fa8da");
        colors.add("#90caf9");
        colors.add("#81d4fa");
        colors.add("#80deea");
        colors.add("#80cbc4");
        colors.add("#c5e1a5");
        colors.add("#e6ee9c");
        colors.add("#fff59d");
        colors.add("#ffe082");
        colors.add("#ffcc80");
        colors.add("#bcaaa4");

        colorPicker.setColors(colors)  // 만들어둔 list 적용
                .setColumns(5)  // 5열로 설정
                .setRoundColorButton(true)  // 원형 버튼으로 설정
                .setOnChooseColorListener(new ColorPicker.OnChooseColorListener() {
                    @Override
                    public void onChooseColor(int position, int color) {
                        color_box_view.setBackgroundColor(color);  // OK 버튼 클릭 시 이벤트
                        new_color = color;
                    }

                    @Override
                    public void onCancel() {
                        // Cancel 버튼 클릭 시 이벤트
                    }
                }).show();  // dialog 생성
    }

    public void getTimeTable() {
        Calendar cal = Calendar.getInstance();

        String AY = String.valueOf(cal.get(Calendar.YEAR));
        String SEM;
        if (cal.get(Calendar.MONTH) < 7) {
            SEM = "1";
        } else {
            SEM = "2";
        }

        UserJson user = ((MainActivity) getActivity()).getUser();

        String ATPT_OFCDC_SC_CODE = user.I_CODE;
        String SD_SCHUL_CODE = user.SC_CODE;
        int grade = user.grade - 9;
        if(grade == 4) grade = 3;
        else if(grade <= 0) grade = 1;

        String GRADE = String.valueOf(grade);
        if (classNum == 0) classNum = 1;
        String CLASS_NM = String.valueOf(classNum);

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                //서버 url설정
                .baseUrl("https://open.neis.go.kr/")
                //데이터 파싱 설정
                .addConverterFactory(GsonConverterFactory.create(gson))
                //객체정보 반환
                .build();

        RetrofitService2 service = retrofit.create(RetrofitService2.class);

        Call<SchoolTimeTable> call = service.getTimeTable("4db607519a0e40b2910efcdf0070a215", "json", 1, 50, ATPT_OFCDC_SC_CODE, SD_SCHUL_CODE, AY, SEM, GRADE, CLASS_NM);
        call.enqueue(new Callback<SchoolTimeTable>() {
            @Override
            public void onResponse(Call<SchoolTimeTable> call, Response<SchoolTimeTable> response) {
                if (response.isSuccessful()) {
                    try {
                        result = response.body().hisTimetable.get(1).row;
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
                    try {
                        makeTimeTable();
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                } else {
                    try {
                        Log.d(TAG, "onResponse: Fail " + response.errorBody().string());
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
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
                                           @Query("pIndex") int pIndex,
                                           @Query("pSize") int pSize,
                                           @Query("ATPT_OFCDC_SC_CODE") String ATPT_OFCDC_SC_CODE,
                                           @Query("SD_SCHUL_CODE") String SD_SCHUL_CODE,
                                           @Query("AY") String AY,
                                           @Query("SEM") String SEM,
                                           @Query("GRADE") String GRADE,
                                           @Query("CLASS_NM") String CLASS_NM);
    }

    public void makeTimeTable() throws ParseException {
        DateFormat dateFormat = new SimpleDateFormat("yyyyMMdd");
        Calendar calendar = Calendar.getInstance();
        Date date;

        boolean monday_flag = true;
        boolean tuesday_flag = true;
        boolean wednesday_flag = true;
        boolean thursday_flag = true;
        boolean friday_flag = true;

        int monday_before_period = 0;
        int tuesday_before_period = 0;
        int wednesday_before_period = 0;
        int thursday_before_period = 0;
        int friday_before_period = 0;

        int DOF;

        for (SchoolTimeTable.TimeTable table : result) {
            date = dateFormat.parse(table.ALL_TI_YMD);
            calendar.setTime(date);
            DOF = calendar.get(Calendar.DAY_OF_WEEK);

            if (DOF == 2 && monday_flag) {
                if (monday_before_period > Integer.parseInt(table.PERIO)) {
                    monday_flag = false;
                    continue;
                }
                monday.add(table.ITRT_CNTNT);
                monday_before_period = Integer.parseInt(table.PERIO);
            } else if (DOF == 3 && tuesday_flag) {
                if (tuesday_before_period > Integer.parseInt(table.PERIO)) {
                    tuesday_flag = false;
                    continue;
                }
                tuesday.add(table.ITRT_CNTNT);
                tuesday_before_period = Integer.parseInt(table.PERIO);
            } else if (DOF == 4 && wednesday_flag) {
                if (wednesday_before_period > Integer.parseInt(table.PERIO)) {
                    wednesday_flag = false;
                    continue;
                }
                wednesday.add(table.ITRT_CNTNT);
                wednesday_before_period = Integer.parseInt(table.PERIO);
            } else if (DOF == 5 && thursday_flag) {
                if (thursday_before_period > Integer.parseInt(table.PERIO)) {
                    thursday_flag = false;
                    continue;
                }
                thursday.add(table.ITRT_CNTNT);
                thursday_before_period = Integer.parseInt(table.PERIO);
            } else if (DOF == 6 && friday_flag) {
                if (friday_before_period > Integer.parseInt(table.PERIO)) {
                    friday_flag = false;
                    continue;
                }
                friday.add(table.ITRT_CNTNT);
                friday_before_period = Integer.parseInt(table.PERIO);
            }
        }
        saveTimeTable();
    }

    public void saveTimeTable() {
        String listString;

        if (getContext() == null) return;
        File file = new File(getContext().getFilesDir(), "TimeTable");
        FileWriter fileWriter = null;
        try {
            fileWriter = new FileWriter(file);
            BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
            bufferedWriter.write("  휴일  " + "\n");
            bufferedWriter.write("  휴일  " + "\n");
            listString = TextUtils.join(", ", monday);
            bufferedWriter.write(listString + "\n");
            listString = TextUtils.join(", ", tuesday);
            bufferedWriter.write(listString + "\n");
            listString = TextUtils.join(", ", wednesday);
            bufferedWriter.write(listString + "\n");
            listString = TextUtils.join(", ", thursday);
            bufferedWriter.write(listString + "\n");
            listString = TextUtils.join(", ", friday);
            bufferedWriter.write(listString + "\n");

            bufferedWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
        readTimeTable();
    }

    public void readTimeTable() {
        String filename = "TimeTable";
        File file = new File(requireContext().getFilesDir() + "/" + filename);
        if (file.exists()) {
            try {
                FileReader fileReader = new FileReader(file);
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String line = bufferedReader.readLine();
                while (line != null) {
                    time_table.add(line);
                    line = bufferedReader.readLine();
                }
                bufferedReader.close();

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        list.clear();
        List<String> obj = Arrays.asList(time_table.get(day_of_week).split(","));
        list.addAll(obj);
        adapter.notifyDataSetChanged();
    }

    @Override
    public void onItemSelected(View v, int position) {

    }

    @Override
    public void onItemSelected(ArrayList<Todo> list, final int position) {

    }

    @Override
    public void onResume() {
        super.onResume();

        todoArrayList.clear();

        RealmResults<Todo> results = realm.where(Todo.class).equalTo("ID", ID).findAll();

        Calendar calendar = Calendar.getInstance();
        for (Todo todo : realm.copyFromRealm(results)) {
            calendar.setTimeInMillis(todo.start);
            if (calendar.get(Calendar.DATE) == this_date) {
                todoArrayList.add(todo);
            }
        }
        todoAdapter.notifyDataSetChanged();

    }
}

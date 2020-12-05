package com.dum.dodam.Scheduler;

import android.app.TimePickerDialog;
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
import androidx.cardview.widget.CardView;
import androidx.core.util.Pair;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.LocalDB.CustomTimeTableDB;
import com.dum.dodam.LocalDB.CustomTimeTableModule;
import com.dum.dodam.LocalDB.TimeTableDB;
import com.dum.dodam.LocalDB.TimeTableModule;
import com.dum.dodam.LocalDB.Todo;
import com.dum.dodam.LocalDB.TodoModule;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import io.realm.RealmResults;
import petrov.kristiyan.colorpicker.ColorPicker;

public class SchedulerPager extends Fragment {
    //    private static final String TAG = "SchedulerPager";
    private static final String TAG = "RHC";
    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public Calendar startCalender = Calendar.getInstance();
    public Calendar endCalender = Calendar.getInstance();
    public int classNum;
    public String I_CODE;
    public String SC_CODE;
    public TextView tv_colorPicker;

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;

    private RecyclerView todoRecyclerView;
    private RecyclerView.Adapter todoAdapter;
    SlidingUpPanelLayout slidingPaneLayout;
    public Button btn;

    public ArrayList<Todo> todoArrayList = new ArrayList<Todo>();

    public ImageView ic_trashcan;
    public View color_box_view;
    public int new_color;

    private ArrayList<String> list = new ArrayList<>();
    final ArrayList<Todo> tmp = new ArrayList<>();

    private Realm realm;
    private Realm realm2;
    private Realm realm3;

    private String ID;
    private int day_of_week;
    private int this_month;
    private int this_year;
    private int this_date;

//    public static SchedulerPager(int day_of_week, int this_date, int this_month, int this_year) {
//        Bundle args = new Bundle();
//        args.putInt("this_date", this_date);
//        args.putInt("this_month", this_month);
//        args.putInt("this_year", this_year);
//        args.putInt("day_of_week", day_of_week);
//        SchedulerPager schedulerPager = new SchedulerPager();
//        schedulerPager.setArguments(args);
//        return schedulerPager;
//    }

    public SchedulerPager(int day_of_week, int this_date, int this_month, int this_year) {
        this.day_of_week = day_of_week;
        this.this_date = this_date;
        this.this_month = this_month;
        this.this_year = this_year;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            day_of_week = bundle.getInt("day_of_week");
//            this_month = bundle.getInt("this_month");
//            this_year = bundle.getInt("this_year");
//            this_date = bundle.getInt("this_date");
//        }

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
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup
            container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.scheduler_pager, container, false);
        view.setClickable(true);
        RealmTodoListInit();
        RealmTimeTableInit();
        CustomRealmTimeTableInit();
        loadTimeTable();

        new_color = Color.parseColor("#ffab91");
        Log.d("ShedulerPager", "new_color: " + new_color);
        todoArrayList.clear();

        ID = String.valueOf(this_year) + String.valueOf(this_month);
        final RealmResults<Todo> results = realm.where(Todo.class).equalTo("ID", ID).findAll();

        Calendar calendar = Calendar.getInstance();
        for (Todo todo : realm.copyFromRealm(results)) {
            calendar.setTimeInMillis(todo.start);
            if (calendar.get(Calendar.DATE) == this_date) {
                todoArrayList.add(todo);
            }
        }

        todoAdapter = new TodoListAdapter(todoArrayList, realm);
        adapter = new TimeTableAdapter(list, this_date);

        todoRecyclerView = (RecyclerView) view.findViewById(R.id.rv_todo_list);
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
                        todo.visible = false;
                        Log.d("SchedulerPager", "todo.color: " + todo.color);

                        if (startCalender.get(Calendar.DATE) == this_date && startCalender.get(Calendar.MONTH) + 1 == this_month) {
                            tmp.add(realm.copyFromRealm(todo));
                        }
//                        Log.d("RHC", String.format("%d %d %d", startCalender.get(Calendar.YEAR), startCalender.get(Calendar.MONTH), startCalender.get(Calendar.DATE)));
//                        Log.d("RHC", String.format("%d %d %d", endCalender.get(Calendar.YEAR), endCalender.get(Calendar.MONTH), endCalender.get(Calendar.DATE)));
//                        Log.d("RHC", "ss " + startCalender.getTimeInMillis());
//                        Log.d("RHC", "ee " + endCalender.getTimeInMillis());

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

                todo_title.setText("");

                startCalender.set(this_year, this_month - 1, this_date);
                startCalender.set(Calendar.HOUR, 0);
                startCalender.set(Calendar.MINUTE, 0);

                endCalender.set(this_year, this_month - 1, this_date);
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
        startCalender.set(this_year, this_month - 1, this_date);
        startCalender.set(Calendar.HOUR, 0);
        startCalender.set(Calendar.MINUTE, 0);

        endCalender.set(this_year, this_month - 1, this_date);
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

        CardView timetable_cv = view.findViewById(R.id.timetable_cv);
        timetable_cv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ((MainActivity) getActivity()).replaceFragmentPopup(new TimeTableFullPopUp(this_date));
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
                        Log.d("SchedulerPager-color", "color: " + color);
                        new_color = color;
                    }

                    @Override
                    public void onCancel() {
                        // Cancel 버튼 클릭 시 이벤트
                    }
                }).show();  // dialog 생성
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

    private void RealmTodoListInit() {
        RealmConfiguration config = new RealmConfiguration.Builder().name("TodoList.realm").schemaVersion(1).modules(new TodoModule()).build();
        realm = Realm.getInstance(config);
    }

    public void loadTimeTable() {
        list.clear();
        TimeTableDB timeTableDB = realm2.where(TimeTableDB.class).findFirst();

        if (timeTableDB.custom) {
            realm3.executeTransactionAsync(new Realm.Transaction() {
                @Override
                public void execute(Realm realm3) {
                    Calendar cal = Calendar.getInstance();
                    cal.set(Calendar.DATE, this_date);
                    int DOW = cal.get(Calendar.DAY_OF_WEEK);

                    CustomTimeTableDB customTimeTableDB = realm3.where(CustomTimeTableDB.class).findFirst();

                    if (DOW == 1) {
                        list.add("일요일");
                    } else if (DOW == 2) {
                        list.addAll(customTimeTableDB.days.get(0).subject);
                    } else if (DOW == 3) {
                        list.addAll(customTimeTableDB.days.get(1).subject);
                    } else if (DOW == 4) {
                        list.addAll(customTimeTableDB.days.get(2).subject);
                    } else if (DOW == 5) {
                        list.addAll(customTimeTableDB.days.get(3).subject);
                    } else if (DOW == 6) {
                        list.addAll(customTimeTableDB.days.get(4).subject);
                    } else if (DOW == 7) {
                        list.add("토요일");
                    }
                }
            });
        } else {
            for (String result : timeTableDB.days.get(this_date).subject) {
                list.add(result);
            }
            list.remove(0);
        }
    }

    private void RealmTimeTableInit() {
        RealmConfiguration config = new RealmConfiguration.Builder().name("TimeTableDB.realm").schemaVersion(1).modules(new TimeTableModule()).build();
        realm2 = Realm.getInstance(config);
    }

    private void CustomRealmTimeTableInit() {
        RealmConfiguration config = new RealmConfiguration.Builder().name("CustomTimeTableDB.realm").schemaVersion(1).modules(new CustomTimeTableModule()).build();
        realm3 = Realm.getInstance(config);
    }
}

package com.dum.dodam.Scheduler;

import android.app.TimePickerDialog;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.DisplayMetrics;
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
import androidx.annotation.RequiresApi;
import androidx.core.util.Pair;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.databinding.SchedulerCalendarBinding;
import com.google.android.material.datepicker.MaterialDatePicker;
import com.google.android.material.datepicker.MaterialPickerOnPositiveButtonClickListener;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;
import com.kizitonwose.calendarview.ui.ViewContainer;
import com.kizitonwose.calendarview.utils.Size;
import com.sothree.slidinguppanel.SlidingUpPanelLayout;

import org.jetbrains.annotations.NotNull;

import java.text.SimpleDateFormat;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.Locale;

import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CustomCalendar extends Fragment {
    private static final String TAG = "SchedulerPager";
    public SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss'Z'");
    public Calendar startCalender;
    public Calendar endCalender;
    SlidingUpPanelLayout slidingPaneLayout;
    public Button btn;

    private SchedulerCalendarBinding layout;

    private CalendarView calendarView;

    private ArrayList<TodoDataList> todoDataLists = new ArrayList<>();
    private ArrayList<TodoData> list = new ArrayList<>();
    private LocalDate selectedDate;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        list.add(new TodoData("Time", "Todo"));

        for (int i = 0; i < 32; i++) {
            if (i == 20) todoDataLists.add(new TodoDataList(list));
            todoDataLists.add(new TodoDataList());
        }

        layout = DataBindingUtil.inflate(inflater, R.layout.scheduler_calendar, container, false);

        final TextView startTimeTV = layout.todoStartTime;
        final TextView endTimeTV = layout.todoEndTime;
        final TextView dates = layout.todoDates;;
        // sliding window
        final ImageView arrow = layout.arrow;
        slidingPaneLayout = layout.slidingWindow;
        slidingPaneLayout.setAnchorPoint(600);
        slidingPaneLayout.addPanelSlideListener(new SlidingUpPanelLayout.PanelSlideListener() {
            @Override
            public void onPanelSlide(View panel, float slideOffset) {
                Log.d(TAG, "[onPanelSlide]slideOffset: " + slideOffset);
            }

            @Override
            public void onPanelStateChanged(View panel, SlidingUpPanelLayout.PanelState previousState, SlidingUpPanelLayout.PanelState newState) {
                Log.d(TAG, "[onPanelStateChanged]previousState: " + previousState);
                Log.d(TAG, "[onPanelStateChanged]newState: " + newState);
                if(newState.equals(SlidingUpPanelLayout.PanelState.COLLAPSED)){
                    arrow.setImageResource(R.drawable.ic_free_icon_up_arrow_626004);
                } else if (newState.equals(SlidingUpPanelLayout.PanelState.EXPANDED)){
                    arrow.setImageResource(R.drawable.ic_free_icon_down_arrow_625946);
                }
            }
        });
        // 완료 버튼
        final EditText todo_title = layout.todoTitle;
        btn = layout.btnBlack;
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (TextUtils.isEmpty(todo_title.getText().toString())) {
                    todo_title.setError("제목을 입력해주세요");
                    return;
                }
                // TODO : db에 항목 추가 하기 및 recycler view 초기화
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
                if(materialDatePicker.isAdded()) return;
                materialDatePicker.show(getActivity().getSupportFragmentManager(), "DATE_PICKER");
            }
        });
        materialDatePicker.addOnPositiveButtonClickListener(new MaterialPickerOnPositiveButtonClickListener() {
            @Override
            public void onPositiveButtonClick(Object selection) {
                dates.setText(materialDatePicker.getHeaderText());
                Pair<Long, Long> times = (Pair<Long, Long>)materialDatePicker.getSelection();
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
                        startTimeTV.setText( selectedHour + ":" + selectedMinute);
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
                        endTimeTV.setText( selectedHour + ":" + selectedMinute);
                        endCalender.set(Calendar.HOUR, selectedHour);
                        endCalender.set(Calendar.MINUTE, selectedMinute);
                    }
                }, endCalender.get(Calendar.HOUR), endCalender.get(Calendar.MINUTE), false);//Yes 24 hour time
                mTimePicker.setTitle("Select Time");
                mTimePicker.show();
            }
        });

        calendarView = layout.calendarView;

        final YearMonth currentMonth = YearMonth.now();
        DayOfWeek[] daysOfWeek = daysOfWeekFromLocale();

        YearMonth firstMonth = currentMonth.minusMonths(10);
        YearMonth lastMonth = currentMonth.plusMonths(10);
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        calendarView.setup(firstMonth, lastMonth, firstDayOfWeek);
        calendarView.scrollToMonth(currentMonth);

        DisplayMetrics metrics = getResources().getDisplayMetrics();
        int screenHeight = metrics.heightPixels;
        int screenWidth = metrics.widthPixels;
        calendarView.setDaySize(new Size(screenWidth / 7, screenHeight / 7));
        calendarView.setSelected(true);

        final class DayViewContainer extends ViewContainer {
            private TextView calendarDayText;
            private TextView todo1;
            private CalendarDay day;

            public DayViewContainer(@NotNull View view) {
                super(view);
                calendarDayText = view.findViewById(R.id.DayText);
                todo1 = view.findViewById(R.id.todo1);

                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        if (day.getOwner() == DayOwner.THIS_MONTH) {
                            if (selectedDate != day.getDate()) {
                                LocalDate oldDate = selectedDate;
                                selectedDate = day.getDate();
                                calendarView.notifyDateChanged(day.getDate());
                                if (oldDate != null) {
                                    calendarView.notifyDateChanged(oldDate);
                                }
                            }
                            if (todoDataLists.get(day.getDay()).todoList != null) {
                                ((MainActivity) getActivity()).replaceFragmentPopup(new CustomCalendarPopUp(day, todoDataLists.get(day.getDay()).todoList));
                            }
                        }
                    }
                });

                view.setOnLongClickListener(new View.OnLongClickListener() {
                    @Override
                    public boolean onLongClick(View view) {
                        slidingPaneLayout.setPanelState(SlidingUpPanelLayout.PanelState.EXPANDED);
                        LocalDate date = day.getDate();
                        return true;
                    }
                });
            }
        }

        calendarView.setDayBinder(new DayBinder<DayViewContainer>() {
            @NotNull
            @Override
            public DayViewContainer create(@NotNull View view) {
                return new DayViewContainer(view);
            }

            @Override
            public void bind(@NotNull DayViewContainer viewContainer, @NotNull CalendarDay calendarDay) {
                View view = viewContainer.getView();

                viewContainer.day = calendarDay;

                TextView calendarDayText = viewContainer.calendarDayText;
                calendarDayText.setText(String.valueOf(calendarDay.getDate().getDayOfMonth()));

                View bottomView = view.findViewById(R.id.bottomView);
                View topView = view.findViewById(R.id.topView);
                bottomView.setBackgroundResource(R.color.transparent);
                topView.setBackgroundResource(R.color.transparent);

                if (calendarDay.getOwner() == DayOwner.THIS_MONTH) {
                    calendarDayText.setTextColor(getResources().getColor(R.color.text_black));
                    if (selectedDate == calendarDay.getDate()) {
                        view.setBackgroundResource(R.drawable.calendar_today_bg);
                    } else {
                        view.setBackgroundResource(0);
                    }
                    if (calendarDay.getDate().toString().equals(LocalDate.now().toString())) {
                        calendarDayText.setBackgroundResource(R.color.today_bg);
                    }
                    if (todoDataLists.get(calendarDay.getDay()).todoList != null) {
                        bottomView.setBackgroundResource(R.color.classic_blue);
                        topView.setBackgroundResource(R.color.flame_scarlet);
                        viewContainer.todo1.setText(todoDataLists.get(calendarDay.getDay()).todoList.get(0).todo);
                        viewContainer.todo1.setBackgroundResource(R.color.faded_saffron);
                    }
                    if (calendarDay.getDate().getDayOfWeek().equals(DayOfWeek.SATURDAY)) {
                        calendarDayText.setTextColor(getResources().getColor(R.color.blue));
                    }

                    if (calendarDay.getDate().getDayOfWeek().equals(DayOfWeek.SUNDAY)) {
                        calendarDayText.setTextColor(getResources().getColor(R.color.red));
                    }
                } else {
                    calendarDayText.setTextColor(getResources().getColor(R.color.faded_gray));
                    view.setBackgroundColor(getResources().getColor(R.color.outdate_bg));
                }
            }
        });

        final class MonthViewContainer extends ViewContainer {
            public MonthViewContainer(@NotNull View view) {
                super(view);
            }
        }
        calendarView.setMonthHeaderBinder(new MonthHeaderFooterBinder<MonthViewContainer>() {
            @NotNull
            @Override
            public MonthViewContainer create(@NotNull View view) {
                return new MonthViewContainer(view);
            }

            @Override
            public void bind(@NotNull MonthViewContainer viewContainer, @NotNull CalendarMonth calendarMonth) {
            }
        });

        calendarView.setMonthScrollListener(new Function1<CalendarMonth, Unit>() {
            @Override
            public Unit invoke(CalendarMonth calendarMonth) {
                layout.yearText.setText(String.format("%d월 %d년", calendarMonth.getMonth(), calendarMonth.getYear()));
                return Unit.INSTANCE;
            }
        });

        layout.PreviousMonthImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarMonth calendarMonth = calendarView.findFirstVisibleMonth();
                if (calendarMonth != null) {
                    calendarView.smoothScrollToMonth(com.kizitonwose.calendarview.utils.ExtensionsKt.getPrevious(calendarMonth.getYearMonth()));
                }
            }
        });

        layout.NextMonthImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CalendarMonth calendarMonth = calendarView.findFirstVisibleMonth();
                if (calendarMonth != null) {
                    calendarView.smoothScrollToMonth(com.kizitonwose.calendarview.utils.ExtensionsKt.getNext(calendarMonth.getYearMonth()));
                }
            }
        });


        return layout.getRoot();
    }

    public DayOfWeek[] daysOfWeekFromLocale() {
        DayOfWeek firstDayOfWeek = WeekFields.of(Locale.getDefault()).getFirstDayOfWeek();
        DayOfWeek[] daysOfWeek = DayOfWeek.values();
        if (firstDayOfWeek != DayOfWeek.MONDAY) {
            DayOfWeek[] rhs = (DayOfWeek[]) ArraysKt.sliceArray(daysOfWeek, new IntRange(firstDayOfWeek.getValue(), ArraysKt.getIndices(daysOfWeek).getLast()));
            DayOfWeek[] lhs = (DayOfWeek[]) ArraysKt.sliceArray(daysOfWeek, RangesKt.until(0, firstDayOfWeek.ordinal()));
            daysOfWeek = (DayOfWeek[]) ArraysKt.plus(rhs, lhs);
        }

        return daysOfWeek;
    }
}

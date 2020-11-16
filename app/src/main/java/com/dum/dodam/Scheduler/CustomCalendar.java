package com.dum.dodam.Scheduler;

import android.os.Build;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.dum.dodam.LocalDB.TodoData;
import com.dum.dodam.LocalDB.TodoList;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.databinding.SchedulerCalendarBinding;
import com.kizitonwose.calendarview.CalendarView;
import com.kizitonwose.calendarview.model.CalendarDay;
import com.kizitonwose.calendarview.model.CalendarMonth;
import com.kizitonwose.calendarview.model.DayOwner;
import com.kizitonwose.calendarview.ui.DayBinder;
import com.kizitonwose.calendarview.ui.MonthHeaderFooterBinder;
import com.kizitonwose.calendarview.ui.ViewContainer;
import com.kizitonwose.calendarview.utils.Size;

import org.jetbrains.annotations.NotNull;

import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.YearMonth;
import java.time.temporal.WeekFields;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

import io.realm.Realm;
import io.realm.RealmList;
import io.realm.RealmResults;
import kotlin.Unit;
import kotlin.collections.ArraysKt;
import kotlin.jvm.functions.Function1;
import kotlin.ranges.IntRange;
import kotlin.ranges.RangesKt;

@RequiresApi(api = Build.VERSION_CODES.O)
public class CustomCalendar extends Fragment {
    private SchedulerCalendarBinding layout;

    private CalendarView calendarView;

    private ArrayList<TodoList> list = new ArrayList<>();
    private LocalDate selectedDate;

    private Realm realm;

    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable Bundle savedInstanceState) {
        layout = DataBindingUtil.inflate(inflater, R.layout.scheduler_calendar, container, false);
        layout.getRoot().setClickable(true);

        realm = Realm.getDefaultInstance();

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
                            String id = String.valueOf(day.getDate().getYear()) + String.valueOf(day.getDate().getMonth().getValue());
                            final RealmResults<TodoList> results = realm.where(TodoList.class).equalTo("ID", id).findAll();
                            if (selectedDate != day.getDate()) {
                                LocalDate oldDate = selectedDate;
                                selectedDate = day.getDate();
                                calendarView.notifyDateChanged(day.getDate());
                                if (oldDate != null) {
                                    calendarView.notifyDateChanged(oldDate);
                                }
                            }
                            if (results.size() != 0) {
                                ArrayList<TodoList> dataArrayList = new ArrayList<TodoList>();
                                dataArrayList.addAll(realm.copyFromRealm(results));
                                ((MainActivity) getActivity()).replaceFragmentPopup(new CustomCalendarPopUp(day, dataArrayList));
                            }
                        }
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

                String id = String.valueOf(calendarDay.getDate().getYear()) + String.valueOf(calendarDay.getDate().getMonth().getValue());
                RealmResults<TodoList> results = realm.where(TodoList.class).equalTo("ID", "202011").findAll();

                int isEmpty = 0;
                RealmList<TodoData> todoData;
                if (results != null) {
                    isEmpty = 1;
                    list.addAll(realm.copyFromRealm(results));
                }

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
                    if (isEmpty != 0) {
                        Calendar calendar = Calendar.getInstance();
                        for (TodoList data : list) {
                            calendar.setTimeInMillis(data.start);
                            if (calendar.get(Calendar.DATE) == calendarDay.getDay()) {
                                viewContainer.todo1.setText(data.title);
                                viewContainer.todo1.setBackgroundResource(R.color.faded_saffron);
//                                bottomView.setBackgroundResource(R.color.classic_blue);
//                                topView.setBackgroundResource(R.color.flame_scarlet);
                            }

                        }
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
                list.clear();
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

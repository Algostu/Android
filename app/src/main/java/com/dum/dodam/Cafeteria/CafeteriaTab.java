package com.dum.dodam.Cafeteria;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import com.dum.dodam.Cafeteria.dataframe.CafeteriaFrame;
import com.dum.dodam.Cafeteria.dataframe.LunchFrame;
import com.dum.dodam.Cafeteria.dataframe.LunchResponse;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.tabs.TabLayout;
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
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

import retrofit2.Call;

import static com.dum.dodam.Cafeteria.Cafeteria.TAG;

public class CafeteriaTab extends Fragment {

    private static ArrayList<CafeteriaFrame> menu = new ArrayList<CafeteriaFrame>();
    private static ArrayList<CafeteriaFrame> tmpmenu = new ArrayList<CafeteriaFrame>();

    private UserJson user;
    private int cnt_cafeteria = 0;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.cafeteria_, container, false);
        view.setClickable(true);

        List<Integer> weekNToday = getWeekNDate();
        int this_week = weekNToday.get(1) - 1;
        int today = weekNToday.get(0);

        if (today == 1) {
            downloadCafeteriaList(1);
        } else if (today % 5 == 0) {
            downloadCafeteriaList(0);
        }

        getCafeteriaMenu();

        user = ((MainActivity) getActivity()).getUser();

        TextView month = view.findViewById(R.id.month);
        month.setText(String.format("%d 월", getWeekNDate().get(2) + 1));

        Toolbar toolbar = view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        ActionBar actionBar = ((MainActivity) getActivity()).getSupportActionBar();
        actionBar.setTitle(user.schoolName);

        ViewPager pager = (ViewPager) view.findViewById(R.id.pager);
        CafeteriaViewPageAdapter viewPageAdapter = new CafeteriaViewPageAdapter(getChildFragmentManager(), menu);
        pager.setAdapter(viewPageAdapter);

        TabLayout tab_layout = (TabLayout) view.findViewById(R.id.tab_layout);
        tab_layout.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.white));
        tab_layout.setupWithViewPager(pager);

        return view;
    }

    public void downloadCafeteriaList(int isFirst) {
        Log.d(TAG, "DOWN MENU");
        String version = "0";

        BufferedReader bReader = null;
        try {
            if (getContext() == null) return;
            File file4 = new File(getContext().getFilesDir(), "versionCafeteria");
            bReader = new BufferedReader(new FileReader(file4));
            version = bReader.readLine();
            bReader.close();
        } catch (IOException e) {
//            e.printStackTrace();
        }

        RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<LunchResponse> call = service.getCafeteriaList(version);

        call.enqueue(new retrofit2.Callback<LunchResponse>() {
            @Override
            public void onResponse(Call<LunchResponse> call, retrofit2.Response<LunchResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<LunchFrame> curMonth = response.body().body.curMonth;
                    ArrayList<LunchFrame> nextMonth = response.body().body.nextMonth;
                    String version = response.body().body.version;

                    JSONArray current = new JSONArray();
                    JSONArray next = new JSONArray();
                    JSONObject jsonObject = new JSONObject();

                    int index = 0;
                    int before_index = 0;
                    try {
                        for (LunchFrame item : curMonth) {
//                            Log.d("dodam", "date" + item.date);
//                            Log.d("dodam", "week" + item.week);
//                            Log.d("dodam", "week_day" + item.week_day);
                            index = Math.round(Float.parseFloat(item.week)) - 1;
                            if (before_index != index) {
                                current.put(jsonObject);
                                jsonObject = new JSONObject();
                                before_index = index;
                            }
                            if (item.week_day.equals("월")) {
                                jsonObject.put("date_monday", item.date);
                                jsonObject.put("lunch_monday", item.lunch);
                            } else if (item.week_day.equals("화")) {
                                jsonObject.put("date_tuesday", item.date);
                                jsonObject.put("lunch_tuesday", item.lunch);
                            } else if (item.week_day.equals("수")) {
                                jsonObject.put("date_wednesday", item.date);
                                jsonObject.put("lunch_wednesday", item.lunch);
                            } else if (item.week_day.equals("목")) {
                                jsonObject.put("date_thursday", item.date);
                                jsonObject.put("lunch_thursday", item.lunch);
                            } else if (item.week_day.equals("금")) {
                                jsonObject.put("date_friday", item.date);
                                jsonObject.put("lunch_friday", item.lunch);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    current.put(jsonObject);
//                    Log.d("dodam", "cur Length" + current.length());
                    // Define the File Path and its Name
                    if (getContext() == null) return;
                    File file = new File(getContext().getFilesDir(), "curCafeteria");
                    FileWriter fileWriter = null;
                    try {
                        fileWriter = new FileWriter(file);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write(current.toString());
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    jsonObject = new JSONObject();
                    before_index = 0;
                    // NEXT MONTH
                    try {
                        for (LunchFrame item : nextMonth) {
                            index = Math.round(Float.parseFloat(item.week)) - 1;
                            if (before_index != index) {
                                next.put(jsonObject);
                                jsonObject = new JSONObject();
                                before_index = index;
                            }
                            if (item.week_day.equals("월")) {
                                jsonObject.put("date_monday", item.date);
                                jsonObject.put("lunch_monday", item.lunch);
                            } else if (item.week_day.equals("화")) {
                                jsonObject.put("date_tuesday", item.date);
                                jsonObject.put("lunch_tuesday", item.lunch);
                            } else if (item.week_day.equals("수")) {
                                jsonObject.put("date_wednesday", item.date);
                                jsonObject.put("lunch_wednesday", item.lunch);
                            } else if (item.week_day.equals("목")) {
                                jsonObject.put("date_thursday", item.date);
                                jsonObject.put("lunch_thursday", item.lunch);
                            } else if (item.week_day.equals("금")) {
                                jsonObject.put("date_friday", item.date);
                                jsonObject.put("lunch_friday", item.lunch);
                            }
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    next.put(jsonObject);
                    // Define the File Path and its Name
                    if (getContext() == null) return;
                    File file2 = new File(getContext().getFilesDir(), "nextCafeteria");
                    FileWriter fileWriter2 = null;
                    try {
                        fileWriter2 = new FileWriter(file2);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter2);
                        bufferedWriter.write(next.toString());
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    if (getContext() == null) return;
                    File file3 = new File(getContext().getFilesDir(), "versionCafeteria");
                    try {
                        BufferedWriter writer = new BufferedWriter(new FileWriter(file3));
                        writer.write(version);
                        writer.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                    getCafeteriaMenu();
                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<LunchResponse> call, Throwable t) {
                if (cnt_cafeteria < 5) downloadCafeteriaList(0);
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_cafeteria++;
            }
        });
    }

    public void getCafeteriaMenu() {
        Log.d(TAG, "GET MENU");

        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        String filename = "curCafeteria";
        File file = new File(requireContext().getFilesDir() + "/" + filename);
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

                makeMenu();

                for (int i = 0; i < list.size(); i++) {
                    if (list.get(i).date_monday.equals(" ")) {
                        list.get(i).date_monday = tmpmenu.get(i).date_monday;
                        list.get(i).lunch_monday = tmpmenu.get(i).lunch_monday;
                    }
                    if (list.get(i).date_tuesday.equals(" ")) {
                        list.get(i).date_tuesday = tmpmenu.get(i).date_tuesday;
                        list.get(i).lunch_tuesday = tmpmenu.get(i).lunch_tuesday;
                    }
                    if (list.get(i).date_wednesday.equals(" ")) {
                        list.get(i).date_wednesday = tmpmenu.get(i).date_wednesday;
                        list.get(i).lunch_wednesday = tmpmenu.get(i).lunch_wednesday;
                    }
                    if (list.get(i).date_thursday.equals(" ")) {
                        list.get(i).date_thursday = tmpmenu.get(i).date_thursday;
                        list.get(i).lunch_thursday = tmpmenu.get(i).lunch_thursday;
                    }
                    if (list.get(i).date_friday.equals(" ")) {
                        list.get(i).date_friday = tmpmenu.get(i).date_friday;
                        list.get(i).lunch_friday = tmpmenu.get(i).lunch_friday;
                    }
                }
                menu.clear();
                menu.addAll(list);
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            downloadCafeteriaList(0);
        }
    }

    public static void makeMenu() {
        CafeteriaFrame frame = new CafeteriaFrame();

        Calendar cal = Calendar.getInstance(); //캘린더 인스턴스 얻기

        cal.set(Calendar.DATE, 1); //현재 달을 1일로 설정.
        int sDayNum = cal.get(Calendar.DAY_OF_WEEK); // 1일의 요일 얻어오기, SUNDAY (1), MONDAY(2) , TUESDAY(3),.....
        int endDate = cal.getActualMaximum(Calendar.DATE); //달의 마지막일 얻기

        for (int i = 1; i < endDate + 1; i++) {
            if (sDayNum == 1 || sDayNum == 7) {
                sDayNum++;
                if (sDayNum > 7) sDayNum = 1;
                continue;
            }
            if (sDayNum == 2) {
                frame.lunch_monday = "-";
                frame.date_monday = String.valueOf(i);
            } else if (sDayNum == 3) {
                frame.lunch_tuesday = "-";
                frame.date_tuesday = String.valueOf(i);
            } else if (sDayNum == 4) {
                frame.lunch_wednesday = "-";
                frame.date_wednesday = String.valueOf(i);
            } else if (sDayNum == 5) {
                frame.lunch_thursday = "-";
                frame.date_thursday = String.valueOf(i);
            } else if (sDayNum == 6) {
                frame.lunch_friday = "-";
                frame.date_friday = String.valueOf(i);
                tmpmenu.add(frame);
                frame = new CafeteriaFrame();
            }
            if (i == endDate) {
                tmpmenu.add(frame);
            }
            sDayNum++;
        }
    }

    public static ArrayList<Integer> getWeekNDate() {
        ArrayList<Integer> result = new ArrayList<>();

        Calendar c = Calendar.getInstance();
        int this_week = c.get(Calendar.WEEK_OF_MONTH);
        int today = c.get(Calendar.DATE); //오늘 일자 저장

        int month = c.get(Calendar.MONTH);

        result.add(today);
        result.add(this_week);
        result.add(month);

        return result;
    }
}

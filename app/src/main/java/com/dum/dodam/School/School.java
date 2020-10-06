package com.dum.dodam.School;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;
import androidx.viewpager2.widget.ViewPager2;

import com.dum.dodam.R;
import com.dum.dodam.School.dataframe.CafeteriaFrame;
import com.dum.dodam.School.dataframe.LunchFrame;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.scalars.ScalarsConverterFactory;
import retrofit2.http.GET;

public class School extends Fragment {
    static String TAG = "RHC";
    private int cnt_cafeteria = 0;
    private String filename;

    private ArrayList<CafeteriaFrame> menu = new ArrayList<>(5);
    private RecyclerView.Adapter adapter;
    ViewPager2 ViewPager_cafeteria;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.school, container, false);

        menu.add(new CafeteriaFrame());
        menu.add(new CafeteriaFrame());
        menu.add(new CafeteriaFrame());
        menu.add(new CafeteriaFrame());
        menu.add(new CafeteriaFrame());
        menu.get(0).lunch_friday = "금요일 점심";
        menu.get(2).lunch_wednesday = "수요일 점심";

        int this_week = getWeek() - 1;

//        getCafeteriaMenu();

        adapter = new CafeteriaAdapter(menu);

        ViewPager_cafeteria = view.findViewById(R.id.vp_cafeteria);
        ViewPager_cafeteria.setAdapter(adapter);
        ViewPager_cafeteria.setCurrentItem(this_week);

        return view;
    }

    public void getCafeteriaMenu() {
        long now = System.currentTimeMillis();
        Date data = new Date(now);
        SimpleDateFormat dateFormat = new SimpleDateFormat("yymm");
        String date = dateFormat.format(data);
        filename = date + "_cafeteria_menu.tmp";
        File file = new File(getContext().getFilesDir() + "/" + filename);
        if (file.exists()) {
            try {
                FileInputStream fis = new FileInputStream(filename);
                ObjectInputStream ois = new ObjectInputStream(fis);
                menu = (ArrayList) ois.readObject();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            downloadCafeteriaList();
        }
    }

    public void downloadCafeteriaList() {
        Gson gson = new GsonBuilder()
                .setLenient()
                .create();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://49.50.164.11:5000/article/")
                .addConverterFactory(ScalarsConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build();

        RetrofitService service = retrofit.create(RetrofitService.class);

        Call<ArrayList<LunchFrame>> call = service.getCafeteriaList();

        call.enqueue(new retrofit2.Callback<ArrayList<LunchFrame>>() {
            @Override
            public void onResponse(Call<ArrayList<LunchFrame>> call, retrofit2.Response<ArrayList<LunchFrame>> response) {
                if (response.isSuccessful()) {
                    ArrayList<LunchFrame> result = response.body();
                    int index;
                    for (LunchFrame item : result) {
                        index = Integer.parseInt(item.week) - 1;
                        if (item.weed_day.equals("월")) {
                            menu.get(index).date_monday = item.date;
                            menu.get(index).lunch_monday = item.lunch;
                        } else if (item.weed_day.equals("화")) {
                            menu.get(index).date_tuesday = item.date;
                            menu.get(index).lunch_tuesday = item.lunch;
                        } else if (item.weed_day.equals("수")) {
                            menu.get(index).date_wednesday = item.date;
                            menu.get(index).lunch_wednesday = item.lunch;
                        } else if (item.weed_day.equals("목")) {
                            menu.get(index).date_thursday = item.date;
                            menu.get(index).lunch_thursday = item.lunch;
                        } else if (item.weed_day.equals("금")) {
                            menu.get(index).date_friday = item.date;
                            menu.get(index).lunch_friday = item.lunch;
                        }
                    }

                    adapter.notifyDataSetChanged();

                    FileOutputStream fos = null;
                    try {
                        fos = getActivity().openFileOutput(filename, getContext().MODE_PRIVATE);
                        ObjectOutputStream oos = null;
                        oos = new ObjectOutputStream(fos);
                        oos.writeObject(menu);
                        oos.close();
                        fos.close();
                    } catch (FileNotFoundException e) {
                        e.printStackTrace();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.d(TAG, "onResponse: Fail " + response.body());
                }
            }

            @Override
            public void onFailure(Call<ArrayList<LunchFrame>> call, Throwable t) {
                Log.d(TAG, "Cafeteria " + String.valueOf(cnt_cafeteria) + " " + t.getMessage());
                if (cnt_cafeteria < 5) downloadCafeteriaList();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_cafeteria++;
            }
        });
    }

    public interface RetrofitService {
        @GET("getCafeteriaList")
        Call<ArrayList<LunchFrame>> getCafeteriaList();
    }

    public static int getWeek() {
        Calendar c = Calendar.getInstance();
        int this_week = c.get(Calendar.WEEK_OF_MONTH);
        return this_week;

    }
}
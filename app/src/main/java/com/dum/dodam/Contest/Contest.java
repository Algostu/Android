package com.dum.dodam.Contest;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.Contest.dataframe.ContestFrame;
import com.dum.dodam.Contest.dataframe.ContestListResponse;
import com.dum.dodam.httpConnection.RetrofitAdapter;
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

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Contest extends Fragment implements ContestListAdapter.OnListItemSelectedInterface {
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "RHC";
    private int cnt_getContestList = 0;

    ArrayList<ContestFrame> list = new ArrayList<>();
    ArrayList<ContestFrame> result = new ArrayList<>();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.contest, container, false);

        adapter = new ContestListAdapter(getContext(), list, this);

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_contest);

        recyclerView.setHasFixedSize(true);

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);

        recyclerView.setAdapter(adapter);
        readContestList();
        Log.d(TAG, "list is empty?: " + list.isEmpty());
        Log.d(TAG, "list size " + list.size());
//        if (!list.isEmpty()) getContestList(Integer.parseInt(list.get(list.size() - 1).contestID));
        return view;
    }

    public void getContestList(final int contestID) {
        Log.d(TAG, "download contest list");

        com.dum.dodam.httpConnection.RetrofitService service = RetrofitAdapter.getInstance(getContext());

        Call<ContestListResponse> call = service.getContestList();

        call.enqueue(new Callback<ContestListResponse>() {
            @Override
            public void onResponse(Call<ContestListResponse> call, Response<ContestListResponse> response) {
                if (response.isSuccessful()) {
                    ArrayList<ContestFrame> result = response.body().body;

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

                    File file = new File(getContext().getFilesDir(), "contest");
                    FileWriter fileWriter = null;
                    try {
                        if (contestID == 0) fileWriter = new FileWriter(file, false);
                        else fileWriter = new FileWriter(file, true);
                        BufferedWriter bufferedWriter = new BufferedWriter(fileWriter);
                        bufferedWriter.write(jsArray.toString());
                        bufferedWriter.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    readContestList();
                } else {
                    Log.d(TAG, "Response but Fail in getContestList");
                }
            }

            @Override
            public void onFailure(Call<ContestListResponse> call, Throwable t) {
                Log.d(TAG, "getContestList" + t.getMessage());
                if (cnt_getContestList < 5) getContestList(0);
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_getContestList++;
            }
        });
    }

    public void readContestList() {
        String filename = "contest";
        File file = new File(getContext().getFilesDir() + "/" + filename);
        if (file.exists()) {
            Log.d(TAG, "contest File exist");
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

                result = gson.fromJson(response, new TypeToken<ArrayList<ContestFrame>>() {
                }.getType());

                list.addAll(result);
                adapter.notifyDataSetChanged();
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            getContestList(0);
        }
    }

    @Override
    public void onItemSelected(View v, int position) {
        ContestListAdapter.Holder holder = (ContestListAdapter.Holder) recyclerView.findViewHolderForAdapterPosition(position);
        ContestFrame frame = new ContestFrame();
        frame.title = holder.title.getText().toString();
        frame.content = holder.content.getText().toString();
        frame.area = holder.area;
        frame.sponsor = holder.sponsor.getText().toString();
        frame.prize = holder.prize.getText().toString();
        frame.firstPrize = holder.firstPrize;
        frame.homePage = holder.homePage;
        frame.imageUrl = holder.imageUrl;
        frame.start = holder.start;
        frame.end = holder.end;

        ((MainActivity) getActivity()).replaceFragmentFull(new ContestInfo(frame));
    }
}
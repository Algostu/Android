package com.dum.dodam.Univ.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.R;
import com.dum.dodam.Univ.Adapter.RankingRecyclerAdapter;
import com.dum.dodam.Univ.dataframe.MajorFrame;
import com.dum.dodam.Univ.dataframe.UnivFrame;

import java.util.ArrayList;

public class RankingPage extends Fragment implements RankingRecyclerAdapter.OnListItemSelectedInterface {
    // type : 학교, 학과
    public int type;

    private RankingRecyclerAdapter adapter;
    private ArrayList<UnivFrame> univList = new ArrayList<>();
    private ArrayList<MajorFrame> majorList = new ArrayList<>();

    public RankingPage(int type) {
        this.type = type;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.univ_ranking_page, container, false);

        if (type == 1) {
            adapter = new RankingRecyclerAdapter(getContext(), univList, majorList, type, this);
        } else {
            adapter = new RankingRecyclerAdapter(getContext(), univList, majorList, type, this);
        }
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.rankpage_recyclerview);
        recyclerView.setHasFixedSize(true);
        LinearLayoutManager layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);


        return view;
    }

    @Override
    public void onItemSelected(View v, int position) {

    }
}

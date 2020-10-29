package com.dum.dodam.Univ;

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
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Community.ArticleWrite;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.RvItemDecoration;
import com.dum.dodam.Univ.dataframe.UnivNewsFrame;
import com.dum.dodam.Univ.dataframe.UnivNewsResponse;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnivCommunity extends Fragment {

    private static final String TAG = "RHC";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UnivNewsFrame> list = new ArrayList<>();

    private String lastNewsWrittenTime = "latest";
    private int cnt_readNews = 0;
    private int readNewsToggle = 0;
    private int communityType;
    private int communityID;

    private UserJson user;

    public static UnivCommunity newInstance(int communityType, int communityID) {
        Bundle args = new Bundle();
        args.putInt("communityType", communityType);
        args.putInt("communityID", communityID);
        UnivCommunity f = new UnivCommunity();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collage_community, container, false);
        view.setClickable(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.communityType = bundle.getInt("communityType");
            this.communityID = bundle.getInt("communityID");
        }

        user = ((MainActivity) getActivity()).getUser();

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (user.authorized.equals("0")) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                    builder.setTitle("게시 권한이 없습니다.");
                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int id) {
                        }
                    });
                    builder.show();
                } else {
                    ((MainActivity) getActivity()).replaceFragmentFull(new ArticleWrite(communityType, communityID));
                }
            }
        });

        adapter = new UnivNewsAdapter(getContext(), list);
        if (list.size() == 0 && readNewsToggle == 0) {
            readUnivCommunity();
            readNewsToggle = 1;
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_articles);
        recyclerView.addItemDecoration(new RvItemDecoration(getContext()));
        recyclerView.setHasFixedSize(true); //

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(adapter);

        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                int curPosition = recyclerView.getAdapter().getItemCount() - 1;
                int lastVisibleItemPosition = ((LinearLayoutManager) recyclerView.getLayoutManager()).findLastCompletelyVisibleItemPosition();

                if ((lastVisibleItemPosition >= 15) && (curPosition >= lastVisibleItemPosition - 3)) {
                    readUnivCommunity();
                }
            }
        });

        return view;
    }

    public void readUnivCommunity() {
        com.dum.dodam.httpConnection.RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<UnivNewsResponse> call = service.readUnivCommunity(communityType, communityID, lastNewsWrittenTime);
        call.enqueue(new Callback<UnivNewsResponse>() {
            @Override
            public void onResponse(Call<UnivNewsResponse> call, Response<UnivNewsResponse> response) {
                if (response.isSuccessful()) {
                    UnivNewsResponse result = response.body();
                    if (result.checkError(getActivity()) != 0) return;

                    if (result.body.size() > 0) {
                        UnivNewsFrame lastArticle = result.body.get(result.body.size() - 1);
                        lastNewsWrittenTime = lastArticle.writtenTime;
                    }
                    list.addAll(result.body);
                    adapter.notifyDataSetChanged();
                    readNewsToggle = 0;
                } else {
                    Log.d(TAG, "onResponse: Fail");
                }
            }

            @Override
            public void onFailure(Call<UnivNewsResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                if (cnt_readNews < 5) readUnivCommunity();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_readNews++;
            }
        });
    }

    @Override
    public void onResume() {
        list.clear();
        lastNewsWrittenTime = "latest";
        if (list.size() == 0 && readNewsToggle == 0) {
            readNewsToggle = 1;
            readUnivCommunity();
        }
        super.onResume();
    }
}

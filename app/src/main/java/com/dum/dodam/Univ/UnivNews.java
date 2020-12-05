package com.dum.dodam.Univ;

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

import com.dum.dodam.R;
import com.dum.dodam.RvItemDecoration;
import com.dum.dodam.Univ.Adapter.UnivNewsAdapter;
import com.dum.dodam.Univ.dataframe.UnivArticleFrame;
import com.dum.dodam.Univ.dataframe.UnivArticleResponse;
import com.dum.dodam.httpConnection.RetrofitAdapter;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UnivNews extends Fragment implements UnivNewsAdapter.OnListItemSelectedInterface {

    private static final String TAG = "RHC";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<UnivArticleFrame> list = new ArrayList<>();

    private String lastNewsWrittenTime = "latest";
    private int cnt_readNews = 0;
    private int readNewsToggle = 0;
    private int univID;

    public static UnivNews newInstance(int univID) {
        Bundle args = new Bundle();
        args.putInt("univID", univID);
        UnivNews f = new UnivNews();
        f.setArguments(args);
        return f;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.univ_news, container, false);
        view.setClickable(true);

        Bundle bundle = getArguments();
        if (bundle != null) {
            this.univID = bundle.getInt("univID");
        }

        adapter = new UnivNewsAdapter(getContext(), list, this);
        if (list.size() == 0 && readNewsToggle == 0) {
            readUnivNews();
            readNewsToggle = 1;
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_news);
        recyclerView.addItemDecoration(new RvItemDecoration(getContext()));
        recyclerView.setHasFixedSize(true);

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
                    readUnivNews();
                }
            }
        });

        return view;
    }

    public void readUnivNews() {
        com.dum.dodam.httpConnection.RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<UnivArticleResponse> call = service.readUnivNews(0, univID * 10, lastNewsWrittenTime);
        call.enqueue(new Callback<UnivArticleResponse>() {
            @Override
            public void onResponse(Call<UnivArticleResponse> call, Response<UnivArticleResponse> response) {
                if (response.isSuccessful()) {
                    UnivArticleResponse result = response.body();
                    if (result.checkError(getActivity()) != 0) return;

                    if (result.body.size() > 0) {
                        UnivArticleFrame lastArticle = result.body.get(result.body.size() - 1);
                        lastNewsWrittenTime = lastArticle.writtenTime;
                    }
                    list.addAll(result.body);
                    adapter.notifyDataSetChanged();
                    readNewsToggle = 0;
                } else {
                    Log.d(TAG, "onResponse readUnivNews: Fail");
                }
            }

            @Override
            public void onFailure(Call<UnivArticleResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                if (cnt_readNews < 5) readUnivNews();
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
            readUnivNews();
        }
        super.onResume();
    }

    @Override
    public void onItemSelected(View v, int position) {

    }
}

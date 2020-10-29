package com.dum.dodam.Collage;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Collage.dataframe.CollageFrame;
import com.dum.dodam.Collage.dataframe.CollageLogoFrame;
import com.dum.dodam.Collage.dataframe.CollageLogoResponse;
import com.dum.dodam.Collage.dataframe.CollageNewsFrame;
import com.dum.dodam.Collage.dataframe.CollageNewsResponse;
import com.dum.dodam.Community.Community;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.RvItemDecoration;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.dum.dodam.httpConnection.RetrofitService;
import com.google.android.material.appbar.CollapsingToolbarLayout;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Collage extends Fragment {

    private static final String TAG = "RHC";
    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<CollageNewsFrame> list = new ArrayList<>();
    ;

    private CollageFrame collage;
    private ImageView iv_collage_logo;
    private TextView go_web;

    private String lastNewsWrittenTime = "latest";
    private int cnt_readNews = 0;
    private int readNewsToggle = 0;

    public Collage(CollageFrame collage) {
        this.collage = collage;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.collage_page, container, false);
        view.setClickable(true);

        Toolbar toolbar = (Toolbar) view.findViewById(R.id.toolbar);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        CollapsingToolbarLayout toolBarLayout = (CollapsingToolbarLayout) view.findViewById(R.id.toolbar_layout);
        toolBarLayout.setTitle(collage.univName);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Snackbar.make(view, "Move to ", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
                ((MainActivity) getActivity()).replaceFragmentFull(new Community(1, 0, collage.univName));
            }
        });

        iv_collage_logo = view.findViewById(R.id.iv_collage_logo);
        getUnivLogo(collage.univID);

        go_web = (TextView) view.findViewById(R.id.go_web);
        go_web.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ((MainActivity) getActivity()).replaceFragmentFull(new CollageNews(collage.eduHomePage));
            }
        });

        adapter = new CollageNewsAdapter(getContext(), list);
        if (list.size() == 0 && readNewsToggle == 0) {
            readCollageNews();
            readNewsToggle = 1;
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_news);
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
                    readCollageNews();
                }
            }
        });

        return view;
    }

    public void readCollageNews() {
        com.dum.dodam.httpConnection.RetrofitService service = RetrofitAdapter.getInstance(getContext());
        Call<CollageNewsResponse> call = service.readCollageNews(0, 1, lastNewsWrittenTime);
        call.enqueue(new Callback<CollageNewsResponse>() {
            @Override
            public void onResponse(Call<CollageNewsResponse> call, Response<CollageNewsResponse> response) {
                if (response.isSuccessful()) {
                    CollageNewsResponse result = response.body();
                    if (result.checkError(getActivity()) != 0) return;

                    if (result.body.size() > 0) {
                        CollageNewsFrame lastArticle = result.body.get(result.body.size() - 1);
                        lastNewsWrittenTime = lastArticle.writtenTime;
                    }
                    Log.d(TAG, "list size " + list.size());
                    list.addAll(result.body);
                    adapter.notifyDataSetChanged();
                    readNewsToggle = 0;
                } else {
                    Toast.makeText(getContext(), "Upload Fail", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: Fail");
                }
            }

            @Override
            public void onFailure(Call<CollageNewsResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                if (cnt_readNews < 5) readCollageNews();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_readNews++;
            }
        });
    }

    private void getUnivLogo(int univID) {
        RetrofitAdapter rAdapter = new RetrofitAdapter();
        RetrofitService service = rAdapter.getInstance(getActivity());
        Call<CollageLogoResponse> call = service.getUnivLogo(univID);

        call.enqueue(new retrofit2.Callback<CollageLogoResponse>() {
            @Override
            public void onResponse(Call<CollageLogoResponse> call, retrofit2.Response<CollageLogoResponse> response) {
                if (response.isSuccessful()) {
                    if (response.body().checkError(getContext()) != 0) return;
                    CollageLogoFrame result = response.body().body;
                    Bitmap bitmap_signiture = StringToBitmap(result.signiture);
                    iv_collage_logo.setImageBitmap(bitmap_signiture);
                } else {
                    Log.d(TAG, "onResponse: " + response.body());
                }
            }

            @Override
            public void onFailure(Call<CollageLogoResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
            }
        });
    }

    public static Bitmap StringToBitmap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onResume() {
        list.clear();
        lastNewsWrittenTime = "latest";
        if (list.size() == 0 && readNewsToggle == 0) {
            readNewsToggle = 1;
            readCollageNews();
        }
        super.onResume();
    }
}


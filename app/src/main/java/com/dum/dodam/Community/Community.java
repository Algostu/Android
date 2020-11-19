package com.dum.dodam.Community;

import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.dum.dodam.Community.dataframe.ArticleListFrame;
import com.dum.dodam.Community.dataframe.ArticleListResponse;
import com.dum.dodam.Login.Data.UserJson;
import com.dum.dodam.MainActivity;
import com.dum.dodam.R;
import com.dum.dodam.httpConnection.RetrofitAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class Community extends Fragment implements ArticleListAdapter.OnListItemSelectedInterface {

    private RecyclerView recyclerView;
    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private static final String TAG = "RHC";
    private int cnt_readArticleList = 0;
    private int readArticleToggle = 0;

    private String community_name;
    private int communityID;
    private int communityType;
    private String lastArticleWrittenString = "latest";

    private UserJson user;

    //    private ShimmerFrameLayout shimmerFrameLayout;
    private ArrayList<ArticleListFrame> list = new ArrayList<>();

    public static Community newInstance(int communityType, int communityID, String community_name) {
        Bundle args = new Bundle();
        args.putString("community_name", community_name);
        args.putInt("communityType", communityType);
        args.putInt("communityID", communityID);
        Community f = new Community();
        f.setArguments(args);
        return f;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.article_list, container, false);
        view.setClickable(true);
        Bundle bundle = getArguments();
        if (bundle != null) {
            this.community_name = bundle.getString("community_name");
            this.communityType = bundle.getInt("communityType");
            this.communityID = bundle.getInt("communityID");
        }

        user = ((MainActivity) getActivity()).getUser();

//        shimmerFrameLayout = view.findViewById(R.id.shimmerLayout);
//        shimmerFrameLayout.startShimmer();

        TextView title = (TextView) view.findViewById(R.id.tv_title);
        title.setText(community_name + " 게시판");

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.btn_write_article);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                if (user.authorized.equals("0")) {
//                    AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
//                    builder.setTitle("게시 권한이 없습니다.");
//                    builder.setPositiveButton("확인", new DialogInterface.OnClickListener() {
//                        @Override
//                        public void onClick(DialogInterface dialog, int id) {
//                        }
//                    });
//                    builder.show();
//                } else {
//                    ((MainActivity) getActivity()).replaceFragmentFull(new ArticleWrite(communityType, communityID));
//                }
                ((MainActivity) getActivity()).replaceFragmentFull(new ArticleWrite(communityType, communityID));
            }
        });

        adapter = new ArticleListAdapter(getContext(), list, this);
        if (list.size() == 0 && readArticleToggle == 0) {
            readArticleList();
            readArticleToggle = 1;
        }

        recyclerView = (RecyclerView) view.findViewById(R.id.rv_articles);

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
                    readArticleList();
                }
            }
        });

        return view;
    }

    @Override
    public void onDestroy() {
        BottomNavigationView navigation = getActivity().findViewById(R.id.nav_bar);
        View nav_view = getActivity().findViewById(R.id.nav_view);
        nav_view.setVisibility(View.VISIBLE);
        navigation.setVisibility(View.VISIBLE);
        super.onDestroy();
    }

    public void readArticleList() {
        com.dum.dodam.httpConnection.RetrofitService service = RetrofitAdapter.getInstance(getContext());

        Call<ArticleListResponse> call = service.readArticleList(communityType, communityID, lastArticleWrittenString);

        call.enqueue(new Callback<ArticleListResponse>() {
            @Override
            public void onResponse(Call<ArticleListResponse> call, Response<ArticleListResponse> response) {
                if (response.isSuccessful()) {
                    ArticleListResponse result = response.body();
                    if (result.checkError(getActivity()) != 0) return;
                    if (result.body.size() > 0) {
                        ArticleListFrame lastArticle = result.body.get(result.body.size() - 1);
                        lastArticleWrittenString = lastArticle.writtenTime;
                    }

                    list.addAll(result.body);
//                    shimmerFrameLayout.stopShimmer();
//                    shimmerFrameLayout.setVisibility(View.GONE);
                    adapter.notifyDataSetChanged();
                    readArticleToggle = 0;
                } else {
                    Toast.makeText(getContext(), "Upload Fail", Toast.LENGTH_SHORT).show();
                    Log.d(TAG, "onResponse: Fail");
                }
            }

            @Override
            public void onFailure(Call<ArticleListResponse> call, Throwable t) {
                Log.d(TAG, "onFailure: " + t.getMessage());
                if (cnt_readArticleList < 5) readArticleList();
                else Toast.makeText(getContext(), "Please reloading", Toast.LENGTH_SHORT).show();
                cnt_readArticleList++;
            }
        });
    }

    @Override
    public void onResume() {
        list.clear();
        lastArticleWrittenString = "latest";
        if (list.size() == 0 && readArticleToggle == 0) {
            readArticleToggle = 1;
            readArticleList();
        }
        super.onResume();
    }

    @Override
    public void onItemSelected(View v, int position) {
        ArticleListAdapter.Holder holder = (ArticleListAdapter.Holder) recyclerView.findViewHolderForAdapterPosition(position);
        String article_ID = holder.articleID.getText().toString();

        ((MainActivity) getActivity()).replaceFragmentFull(Article.newInstance(Integer.parseInt(article_ID), communityType, communityID));
    }
}
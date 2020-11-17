package com.dum.dodam.Contest;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.dum.dodam.Contest.dataframe.ContestFrame;
import com.dum.dodam.R;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ContestInfo extends Fragment {
    private ImageView imageView;
    private TextView contest_title;
    private TextView area;
    private TextView period;
    private TextView prize;
    private TextView contest_supporter;
    private TextView homepage;
    public BottomNavigationView bottom_nav;

    private ContestFrame frame;
    private androidx.appcompat.widget.Toolbar toolbar;
    private ActionBar actionbar;


    public ContestInfo(ContestFrame frame) {
        this.frame = frame;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.contest_info, container, false);
        view.setClickable(true);
        setHasOptionsMenu(true);
        imageView = view.findViewById(R.id.image);
        contest_title = view.findViewById(R.id.contest_title);
        area = view.findViewById(R.id.area);
        period = view.findViewById(R.id.period);
        prize = view.findViewById(R.id.prize);
        contest_supporter = view.findViewById(R.id.contest_supporter);
        homepage = view.findViewById(R.id.homepage);
        bottom_nav = view.findViewById(R.id.bottom_nav);

        Glide.with(getContext()).load(frame.imageUrl).into(imageView);
        contest_title.setText(frame.title);
        area.setText(frame.area);
        period.setText(frame.start + " ~ " + frame.end);
        prize.setText(frame.firstPrize + " / " + frame.prize);
        contest_supporter.setText(frame.sponsor);
        bottom_nav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(frame.homePage));
                startActivity(intent);
            }
        });
        // WebView
        String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/BMitra.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
        String pas = "</body></html>";
        WebView webView = view.findViewById(R.id.content_webview);
        webView.loadDataWithBaseURL(null, pish + frame.content + pas, "text/html", "utf-8", null);
        // AppBar
        toolbar = view.findViewById(R.id.toolbar2);
        ((AppCompatActivity) getActivity()).setSupportActionBar(toolbar);
        actionbar = ((AppCompatActivity) getActivity()).getSupportActionBar();
        actionbar.setDisplayShowCustomEnabled(true);
        actionbar.setDisplayShowTitleEnabled(false);//기본 제목을 없애줍니다.
        actionbar.setDisplayHomeAsUpEnabled(true);



        return view;
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.contest_menu, menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                //select back button
                getActivity().onBackPressed();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}

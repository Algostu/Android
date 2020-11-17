package com.dum.dodam.Contest;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.dum.dodam.Contest.dataframe.ContestFrame;
import com.dum.dodam.R;

public class ContestInfo extends Fragment {
    private ImageView imageView;
    private TextView contest_title;
    private TextView area;
    private TextView period;
    private TextView prize;
    private TextView sponsor;
    private TextView homepage;

    private ContestFrame frame;


    public ContestInfo(ContestFrame frame) {
        this.frame = frame;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.contest_info, container, false);
        view.setClickable(true);

        imageView = view.findViewById(R.id.image);
        contest_title = view.findViewById(R.id.contest_title);
        area = view.findViewById(R.id.area);
        period = view.findViewById(R.id.period);
        prize = view.findViewById(R.id.prize);

        sponsor = view.findViewById(R.id.sponsor);
        homepage = view.findViewById(R.id.homepage);

        Glide.with(getContext()).load(frame.imageUrl).into(imageView);
        contest_title.setText(frame.title);
        area.setText(frame.area);
        period.setText(frame.start + " ~ " + frame.end);
        prize.setText(frame.firstPrize + " / " + frame.prize);
        sponsor.setText(frame.sponsor);
        homepage.setText(frame.homePage);

        String pish = "<html><head><style type=\"text/css\">@font-face {font-family: MyFont;src: url(\"file:///android_asset/font/BMitra.ttf\")}body {font-family: MyFont;font-size: medium;text-align: justify;}</style></head><body>";
        String pas = "</body></html>";
        WebView webView = view.findViewById(R.id.content_webview);
        webView.loadDataWithBaseURL(null, pish + frame.content + pas, "text/html", "utf-8", null);

        return view;
    }
}

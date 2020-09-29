package com.example.myapplication.Community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.R;

public class Article extends Fragment {

    private TextView writer;
    private TextView title;
    private TextView content;
    private TextView time;
    private TextView reply;
    private TextView heart;
    private TextView article_ID;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.article, container, false);
        writer = view.findViewById(R.id.writer);
        title = view.findViewById(R.id.title);
        content = view.findViewById(R.id.content);
        time = view.findViewById(R.id.time);
        reply = view.findViewById(R.id.reply);
        heart = view.findViewById(R.id.heart);
        article_ID = view.findViewById(R.id.article_ID);
        if (getArguments() != null) {
            writer.setText(getArguments().getString("writer"));
            title.setText(getArguments().getString("title"));
            content.setText(getArguments().getString("content"));
            time.setText(getArguments().getString("time"));
            reply.setText(getArguments().getString("reply"));
            heart.setText(getArguments().getString("heart"));
            article_ID.setText(getArguments().getString("article_id"));
        }

        return view;
    }
}

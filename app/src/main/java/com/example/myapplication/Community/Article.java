package com.example.myapplication.Community;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.example.myapplication.Community.dataframe.ArticleFrame;
import com.example.myapplication.R;

public class Article extends Fragment {

    private TextView writer;
    private TextView title;
    private TextView content;
    private TextView time;
    private TextView reply;
    private TextView heart;
    private TextView article_ID;
    ArticleFrame articleFrame;

    public Article(ArticleFrame articleFrame) {
        this.articleFrame = articleFrame;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.article, container, false);
        view.setClickable(true);

        writer = view.findViewById(R.id.writer);
        title = view.findViewById(R.id.title);
        content = view.findViewById(R.id.content);
        time = view.findViewById(R.id.time);
        reply = view.findViewById(R.id.reply);
        heart = view.findViewById(R.id.heart);
        article_ID = view.findViewById(R.id.article_ID);

        writer.setText(articleFrame.nickName);
        title.setText(articleFrame.title);
        content.setText(articleFrame.content);
        time.setText(articleFrame.writtenTime);
        reply.setText(articleFrame.reply);
        heart.setText(articleFrame.heart);
        article_ID.setText(articleFrame.articleID);

        return view;
    }
}

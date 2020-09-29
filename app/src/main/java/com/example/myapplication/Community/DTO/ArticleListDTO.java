package com.example.myapplication.Community.DTO;

import com.google.gson.annotations.SerializedName;

public class ArticleListDTO {

    @SerializedName("article_ID")
    private int article_ID;

    @SerializedName("title")
    private String title;

    @SerializedName("content")
    private String content;

    @SerializedName("nickname")
    private String nickname;

    @SerializedName("time")
    private String time;

    @SerializedName("heart")
    private int heart;

    @SerializedName("reply")
    private int reply;

    @Override
    public String toString() {
        return "PostResult{" +
                "article_ID=" + article_ID +
                ", title=" + title +
                ", content='" + content + '\'' +
                ", nickname='" + nickname + '\'' +
                '}';
    }
}

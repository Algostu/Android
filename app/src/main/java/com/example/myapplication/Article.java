package com.example.myapplication;

import java.util.ArrayList;

public class Article {
    public int userID;
    public String title;
    public String nickname;
    public Boolean isAnonymous;
    public String content;
    public String articleType;
    public String time;

    public Article(int userID, String title, String nickname, Boolean isAnonymous, String content, String articleType, String time) {
        this.userID = userID;
        this.title = title;
        this.nickname = nickname;
        this.isAnonymous = isAnonymous;
        this.content = content;
        this.articleType = articleType;
        this.time = time;
    }

    public static ArrayList<Article> tempFunctionCreateArticle(int num) {

        ArrayList<Article> articels = new ArrayList<Article>();

        for (int i = 0; i < num + 1; i++) {
            articels.add(new Article(i, "title", "nickname", true, "Content", "Type", "00:00"));
        }

        return articels;
    }
}

package com.example.myapplication;

import java.util.ArrayList;

public class ArticleList {
    public int article_ID;
    public String title;
    public String content;
    public String nickname;
    public String time;
    public int heart;
    public int reply;
    //public String articleType;
    //https://t-okk.tistory.com/8

    public ArticleList(int article_ID, String title, String content, String nickname, String time, int heart, int reply) {
        this.article_ID = article_ID;
        this.title = title;
        this.content = content;
        this.nickname = nickname;
        this.time = time;
        this.heart = heart;
        this.reply = reply;
        //this.articleType = articleType;
    }

    public static ArrayList<ArticleList> tempFunctionCreateArticle(int num) {

        ArrayList<ArticleList> articles = new ArrayList<ArticleList>();

        for (int i = 0; i < num + 1; i++) {
            articles.add(new ArticleList(i, "Title", "Content", "Nickname", "time", 0, 0));
        }
        return articles;
    }
}

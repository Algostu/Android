package com.example.myapplication.Community.dataframe;

public class ArticleList {

    public int article_ID;
    public String title;
    public String content;
    public String nickname;
    public String time;
    public int heart;
    public int reply;
    //public String articleType;

    private static String articles;

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
}
package com.dum.dodam.Community.dataframe;

import com.dum.dodam.httpConnection.BaseResponse;

import java.util.ArrayList;

public class ArticleCommentResponse extends BaseResponse {
    public CommentRecommnet body;

    public class CommentRecommnet {
        public ArrayList<ArticleCommentFrame> replys;
        public ArrayList<ArticleCommentFrame> reReplys;
    }
}
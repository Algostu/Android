package com.dum.dodam.Community.dataframe;

import com.dum.dodam.httpConnection.BaseResponse;

public class ArticleResponse extends BaseResponse {
    public ArticleFrame body;

    public class ArticleFrame {
        public String edit;
        public String articleID;
        public String isAnonymous;
        public String content;
        public String title;
        public String viewNumber;
        public String reply;
        public String heart;
        public String writtenTime;
        public String nickName;
    }
}

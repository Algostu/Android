package com.dum.dodam.httpConnection;
import com.dum.dodam.Community.dataframe.ArticleCommentFrame;
import com.dum.dodam.Community.dataframe.ArticleFrame;
import com.dum.dodam.Home.dataframe.HotArticleFrame;
import com.dum.dodam.Home.dataframe.MyCommunityFrame;
import com.dum.dodam.Login.Data.School;
import com.dum.dodam.Login.Data.UserJson;
import com.google.gson.JsonObject;

import java.util.ArrayList;

import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("/auth/login")
    Call<UserJson> kakaoLogin(@Query("id") long id, @Query("token") String token);

    @Multipart
    @POST("/auth/kakaoSignup")
    Call<String> registerKAKAO(@Part MultipartBody.Part image, @Part("json") RequestBody body);

    @GET("/search/schoolList")
    Call<ArrayList<School>> searchSchoolName(@Query("schoolName") String schoolName);

    @GET("/article/latestArticleList")
    Call<ArrayList<MyCommunityFrame>> getMyCommunity();

    @GET("/article/hotArticleList")
    Call<ArrayList<HotArticleFrame>> getHotArticle();

    @GET("/article/read")
    Call<ArticleFrame> readArticle(@Query("articleID") int articleID, @Query("articleType") int articleType);

    @GET("/article/readComment")
    Call<ArrayList<ArticleCommentFrame>> readArticleComment(@Query("articleID") int articleID, @Query("articleType") int articleType);

    @POST("/article/uploadComment")
    Call<String> uploadComment(@Body JsonObject body);
}

package com.dum.dodam.httpConnection;

import com.dum.dodam.Collage.dataframe.CollageLogoResponse;
import com.dum.dodam.Collage.dataframe.CollageResponse;
import com.dum.dodam.Community.dataframe.ArticleCommentResponse;
import com.dum.dodam.Community.dataframe.ArticleListResponse;
import com.dum.dodam.Community.dataframe.ArticleResponse;
import com.dum.dodam.Home.dataframe.HotArticleResponse;
import com.dum.dodam.Home.dataframe.MyCommunityResponse;
import com.dum.dodam.Login.Data.LoginResponse;
import com.dum.dodam.Login.Data.SearchResponse;
import com.dum.dodam.School.dataframe.LunchResponse;
import com.dum.dodam.Contest.dataframe.ContestListResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("/auth/login")
    Call<LoginResponse> kakaoLogin(@Query("id") long id, @Query("token") String token);

    @POST("/auth/kakaoSignup")
    Call<BaseResponse> registerKAKAO(@Body JsonObject body);

    @GET("/search/schoolList")
    Call<SearchResponse> searchSchoolName(@Query("schoolName") String schoolName);

    @GET("/search/univList")
    Call<CollageResponse> searchCollageName(@Query("univName") String univName);

    @GET("/univ/logoImage")
    Call<CollageLogoResponse> getUnivLogo(@Query("univID") int univID);

    @GET("/article/latestArticleList")
    Call<MyCommunityResponse> getMyCommunity();

    @GET("/article/hotArticleList")
    Call<HotArticleResponse> getHotArticle();

    @GET("/article/read")
    Call<ArticleResponse> readArticle(@Query("articleID") int articleID, @Query("communityType") int communityType, @Query("communityID") int communityID);

    @GET("/article/modifyHeart")
    Call<BaseResponse> modifyHeart(@Query("articleID") int articleID, @Query("communityType") int communityType, @Query("communityID") int communityID, @Query("op") int op);

    @GET("/reply/read")
    Call<ArticleCommentResponse> readArticleComment(@Query("articleID") int articleID, @Query("communityType") int communityType, @Query("communityID") int communityID);

    @POST("/reply/write")
    Call<String> uploadComment(@Body JsonObject body);

    @POST("/article/write")
    Call<BaseResponse> writeArticle(@Body JsonObject body);

    @GET("/article/delete")
    Call<String> deleteArticle(@Query("communityType") int communityType, @Query("communityID") int communityID, @Query("articleID") int articleID);

    @GET("/article/articleList")
    Call<ArticleListResponse> goArticle(@Query("communityType") int communityType, @Query("communityID") int communityID, @Query("writtenAfter") String writtenAfter);

    @GET("/cafeteria/read")
    Call<LunchResponse> getCafeteriaList(@Query("version") String version);

    @GET("/contest/getList")
    Call<ContestListResponse> getContestList(@Query("version") int version);
}

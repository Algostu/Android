package com.dum.dodam.httpConnection;

import com.dum.dodam.Community.dataframe.ArticleCommentResponse;
import com.dum.dodam.Community.dataframe.ArticleListResponse;
import com.dum.dodam.Community.dataframe.ArticleResponse;
import com.dum.dodam.Contest.dataframe.ContestListResponse;
import com.dum.dodam.Home.dataframe.HotArticleResponse;
import com.dum.dodam.Home.dataframe.MyCommunityResponse;
import com.dum.dodam.Login.Data.LoginResponse;
import com.dum.dodam.Login.Data.SearchResponse;
import com.dum.dodam.School.dataframe.LunchResponse;
import com.dum.dodam.Univ.dataframe.LiveShowResponse;
import com.dum.dodam.Univ.dataframe.UnivLogoResponse;
import com.dum.dodam.Univ.dataframe.UnivNewsResponse;
import com.dum.dodam.Univ.dataframe.UnivResponse;
import com.google.gson.JsonObject;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface RetrofitService {
    @GET("/auth/login")
    Call<LoginResponse> kakaoLogin(@Query("id") long id, @Query("token") String token);

    @GET("/auth/logout")
    Call<BaseResponse> kakaoLogout();

    @GET("/auth/withdraw")
    Call<BaseResponse> kakaoSignOut(@Query("id") long id);

    @POST("/auth/kakaoSignup")
    Call<BaseResponse> registerKAKAO(@Body JsonObject body);

    @GET("/search/schoolList")
    Call<SearchResponse> searchSchoolName(@Query("schoolName") String schoolName);

    @GET("/search/univList")
    Call<UnivResponse> searchCollageName(@Query("univName") String univName);

    @GET("/univ/logoImage")
    Call<UnivLogoResponse> getUnivLogo(@Query("univID") int univID);

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
    Call<BaseResponse> uploadComment(@Body JsonObject body);

    @GET("/reply/delete")
    Call<BaseResponse> deleteComment(@Query("communityType") int communityType, @Query("communityID") int communityID, @Query("articleID") int articleID, @Query("replyID") int replyID, @Query("isRereply") int isReReply);

    @POST("/article/write")
    Call<BaseResponse> writeArticle(@Body JsonObject body);

    @GET("/article/delete")
    Call<BaseResponse> deleteArticle(@Query("communityType") int communityType, @Query("communityID") int communityID, @Query("articleID") int articleID);

    @GET("/article/report")
    Call<BaseResponse> reportArticle(@Query("communityType") int communityType, @Query("communityID") int communityID, @Query("articleID") int articleID);

    @GET("/article/articleList")
    Call<ArticleListResponse> readArticleList(@Query("communityType") int communityType, @Query("communityID") int communityID, @Query("writtenAfter") String writtenAfter);

    @GET("/cafeteria/read")
    Call<LunchResponse> getCafeteriaList(@Query("version") String version);

    @GET("/contest/getList")
    Call<ContestListResponse> getContestList(@Query("version") int version);

    @GET("/article/articleList")
    Call<UnivNewsResponse> readUnivNews(@Query("communityType") int communityType, @Query("communityID") int communityID, @Query("writtenAfter") String writtenAfter);

    @GET("/article/articleList")
    Call<UnivNewsResponse> readUnivCommunity(@Query("communityType") int communityType, @Query("communityID") int communityID, @Query("writtenAfter") String writtenAfter);

    @GET("/article/articleList")
    Call<LiveShowResponse> readLiveShowList();

}

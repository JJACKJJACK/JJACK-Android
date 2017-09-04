package com.mkm.hanium.jjack.util;

import com.mkm.hanium.jjack.request_api.KeywordRankingRequestApi;
import com.mkm.hanium.jjack.request_api.KeywordRankingSearchLogRequestApi;
import com.mkm.hanium.jjack.request_api.KeywordRequestApi;
import com.mkm.hanium.jjack.request_api.NewsRequestApi;
import com.mkm.hanium.jjack.request_api.RealtimeRequestApi;
import com.mkm.hanium.jjack.request_api.ScrapFolderRequestApi;
import com.mkm.hanium.jjack.request_api.ScrapRequestApi;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface ApiInterface {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("member/insert")
    Call<DefaultApi> submitUserPropertyRequest(
            @Field("id") long userId,
            @Field("gender") String gender,
            @Field("year") int year);

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("member/delete")
    Call<DefaultApi> unlinkUserRequest(@Field("id") long userId);

    @Headers({"Accept: application/json"})
    @GET("keyword/ranking")
    Call<KeywordRankingRequestApi> keywordRankingRequest();

    @Headers({"Accept: application/json"})
    @GET("article")
    Call<DefaultApi> acticleRequest(@Query("id") int articleId);

    @Headers({"Accept: application/json"})
    @GET("log/recent")
    Call<KeywordRankingSearchLogRequestApi> keywordRankingSearchLogRequest(@Query("keyword") String keywordName);

    @Headers({"Accept: application/json"})
    @GET("folder/list/")
    Call<ScrapFolderRequestApi> scrapFolderRequest(@Query("id") long id);

    @Headers({"Accept: application/json"})
    @GET("scrap/list/")
    Call<ScrapRequestApi> scrapPerFolderRequest(
            @Query("id") long id,
            @Query("folderName") String folderName,
            @Query("start") int start);

    @Headers({"Accept: application/json"})
    @GET("article/category/")
    Call<NewsRequestApi> rankingPerCategoryRequest(
            @Query("category") String category,
            @Query("start") int start);

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("folder/add/")
    Call<DefaultApi> scrapFolderCreateRequest(
            @Field("id") long id,
            @Field("folderName") String folderName);

    @Headers({"Accept: application/json"})
    @GET("folder/delete/")
    Call<DefaultApi> scrapFolderDeleteRequest(
            @Query("id") long id,
            @Query("folderName") String folderName);

    @Headers({"Accept: application/json"})
    @GET("scrap/delete/")
    Call<DefaultApi> scrapDeleteRequest(
            @Query("id") long id,
            @Query("ScrapID") int ScrapID);

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("scrap/update/")
    Call<DefaultApi> scrapModifyRequest(
            @Field("id") long id,
            @Field("ScrapID") int ScrapID,
            @Field("title") String title,
            @Field("content") String content,
            @Field("folderName") String folderName);

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("scrap/add/")
    Call<DefaultApi> scrapCreateRequest(
            @Field("id") long id,
            @Field("articleID") String articleID,
            @Field("title") String title,
            @Field("content") String content,
            @Field("folderName") String folderName);

    @Headers({"Accept: application/json"})
    @GET("realtime/list/")
    Call<RealtimeRequestApi>realtimeRequest();

    @Headers({"Accept: application/json"})
    @GET("article/keyword/")
    Call<NewsRequestApi> realtimeDetailRequest(
            @Query("item") String item,
            @Query("start") int start);

    @Headers({"Accept: application/json"})
    @GET("keyword/list")
    Call<KeywordRequestApi> keywordRequest(@Query("id") long id);

    @Headers({"Accept: application/json"})
    @GET("article/keyword/")
    Call<NewsRequestApi> keywordNewRequest(
            @Query("keyword") String keyword,
            @Query("start") int start);

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("keyword/add")
    Call<DefaultApi> keywordCreateRequest(@Field("id") long id,@Field("keyword") String keyword);

    @Headers({"Accept: application/json"})
    @GET("keyword/delete")
    Call<DefaultApi> keywordDeleteRequest(@Query("id") long id,@Query("keyword") String keyword);
}
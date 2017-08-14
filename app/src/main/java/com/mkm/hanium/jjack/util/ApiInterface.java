package com.mkm.hanium.jjack.util;

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
}
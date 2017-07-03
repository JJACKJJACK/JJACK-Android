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
    @POST("db_member/insert")
    Call<DefaultApi> sendUserProperty(
            @Field("id") long userId,
            @Field("gender") String gender,
            @Field("year") int year);

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("db_member/delete")
    Call<DefaultApi> unlinkUserProperty(@Field("id") long userId);

    @Headers({"Accept: application/json"})
    @GET("db_article/send")
    Call<DefaultApi> sendKakaoLinkArticle(@Query("id") int articleId);
}
package com.mkm.hanium.jjack;

import com.mkm.hanium.jjack.login.UserPropertyApi;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Headers;
import retrofit2.http.POST;

public interface ApiInterface {
    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("db_member/insert")
    Call<UserPropertyApi> sendUserProperty(
            @Field("id") long id,
            @Field("gender") String gender,
            @Field("year") int year);

    @Headers({"Accept: application/json"})
    @FormUrlEncoded
    @POST("db_member/delete")
    Call<UserPropertyApi> unlinkUserProperty(@Field("id") long id);
}
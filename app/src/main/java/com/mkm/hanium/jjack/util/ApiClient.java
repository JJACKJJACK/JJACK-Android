package com.mkm.hanium.jjack.util;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by 김민선 on 2017-05-21.
 * 레트로핏 기본 정의
 */

public class ApiClient {
    private static final String Base_URL = "http://211.253.8.172/";
    private static Retrofit retrofit = null;

    private ApiClient() {}

    public static Retrofit getClient() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(Base_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit;
    }
}
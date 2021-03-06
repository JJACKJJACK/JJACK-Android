/*
  Copyright 2014-2016 Kakao Corp.

  Licensed under the Apache License, Version 2.0 (the "License");
  you may not use this file except in compliance with the License.
  You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

  Unless required by applicable law or agreed to in writing, software
  distributed under the License is distributed on an "AS IS" BASIS,
  WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
  See the License for the specific language governing permissions and
  limitations under the License.
 */
package com.mkm.hanium.jjack.common;

import android.app.Application;

import com.kakao.auth.KakaoSDK;
import com.mkm.hanium.jjack.kakao.KakaoSDKAdapter;
import com.mkm.hanium.jjack.login.ProfileItem;
import com.mkm.hanium.jjack.util.ApiClient;
import com.mkm.hanium.jjack.util.ApiInterface;

import java.util.ArrayList;

/**
 * 이미지를 캐시를 앱 수준에서 관리하기 위한 애플리케이션 객체이다.
 * 로그인 기반 샘플앱에서 사용한다.
 *
 * @author MJ
 */
public class GlobalApplication extends Application {
    private static volatile GlobalApplication instance = null;
    private final ProfileItem user = new ProfileItem();
    private ApiInterface apiInterface;
    private int index;
    private final ArrayList<String> entries = new ArrayList<>();
    private String nowKeyword;

    /**
     * singleton 애플리케이션 객체를 얻는다.
     * @return singleton 애플리케이션 객체
     */
    public static GlobalApplication getGlobalApplicationContext() {
        if (instance == null)
            throw new IllegalStateException("this application does not inherit com.kakao.GlobalApplication");
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        apiInterface = ApiClient.getClient().create(ApiInterface.class);
        KakaoSDK.init(new KakaoSDKAdapter());
        // PushService.init(); // 푸쉬기능 추가할 때
    }

    public static ProfileItem getUser() {
        return instance.user;
    }

    public static ApiInterface getApiInterface() {
        return instance.apiInterface;
    }

    public static int getIndex() {
        return instance.index;
    }

    public static void setIndex(int index) {
        instance.index = index;
    }

    public static ArrayList<String> getEntries() {
        return instance.entries;
    }

    public static void addEntries(String entry) {
        instance.entries.add(entry);
    }

    public static void clearEntries() {
        instance.entries.clear();
    }

    public static void deleteKeyword(int position) {
        instance.entries.remove(position);
    }

    public static String getNowKeyword() {
        return instance.nowKeyword;
    }

    public static void setNowKeyword(String nowKeyword) {
        instance.nowKeyword = nowKeyword;
    }

    /**
     * 앱 종료시 싱글턴 객체 초기화
     */
    @Override
    public void onTerminate() {
        super.onTerminate();
        instance = null;
    }
}

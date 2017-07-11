package com.mkm.hanium.jjack.common;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;

/**
 * Created by MIN on 2017-05-28.
 */

public class BaseActivity extends AppCompatActivity {

    // 현재 클래스의 이름을 저장할 변수
    protected String TAG;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TAG = this.getClass().getSimpleName();
        Log.d(TAG, "onCreate()");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "onDestroy()");
    }

    protected void activityRefresh(Class<?> cls) {
        final Intent i = new Intent(getApplicationContext(), cls);
        i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(i);
    }

    protected void activityChangeAndFinish(Class<?> cls) {
        final Intent i = new Intent(getApplicationContext(), cls);
        startActivity(i);
        finish();
    }

    protected void activityChange(Class<?> cls) {
        final Intent i = new Intent(getApplicationContext(), cls);
        startActivity(i);
    }
}

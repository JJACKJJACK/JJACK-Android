package com.mkm.hanium.jjack.common;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;

/**
 * Created by MIN on 2017-05-28.
 */

public class BaseActivity extends AppCompatActivity {
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

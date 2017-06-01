package com.mkm.hanium.jjack.util;

import android.support.v7.app.AppCompatActivity;
import android.widget.Toast;

/**
 * Created by MIN on 2017-06-01.
 */

public class BackPressCloseSystem {
    private long backKeyPressedTime = 0;
    private Toast toast;
    private AppCompatActivity activity;

    private final String message = "\'뒤로\'버튼을 한번 더 누르시면 종료됩니다.";

    public BackPressCloseSystem(AppCompatActivity activity) {
        this.activity = activity;
    }

    public void onBackPressed() {
        // 뒤로 갈 프래그먼트가 존재할 경우
        if(activity.getSupportFragmentManager().getBackStackEntryCount() > 1){
            activity.getSupportFragmentManager().popBackStack();
        }else {        // 존재하지 않을 경우

            if (isAfter2Seconds()) {
                backKeyPressedTime = System.currentTimeMillis();
                // 현재시간을 다시 초기화

                toast = Toast.makeText(activity, message, Toast.LENGTH_SHORT);
                toast.show();

                return;
            }

            if (isBefore2Seconds()) {
                programShutdown();
                toast.cancel();
            }
        }
    }

    private boolean isAfter2Seconds() {
        return System.currentTimeMillis() > backKeyPressedTime + 2000;
        // 2초 지났을 경우
    }

    private boolean isBefore2Seconds() {
        return System.currentTimeMillis() <= backKeyPressedTime + 2000;
        // 2초가 지나지 않았을 경우
    }

    private void programShutdown() {
        activity.moveTaskToBack(true);
        activity.finish();
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }
}

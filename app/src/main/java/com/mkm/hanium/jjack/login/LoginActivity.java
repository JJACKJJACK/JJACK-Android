package com.mkm.hanium.jjack.login;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.kakao.auth.AuthType;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;
import com.mkm.hanium.jjack.MainActivity;
import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.BindActivity;
import com.mkm.hanium.jjack.databinding.ActivityLoginBinding;

/**
 * Created by MIN on 2017-04-17.
 * 로그인 액티비티를 정의함.
 */

public class LoginActivity extends BindActivity<ActivityLoginBinding> {
    private SessionCallback callback;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_login;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding.setActivity(this);
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);
        Session.getCurrentSession().checkAndImplicitOpen();

    }

    public void onClick(View v) {
        switch(v.getId()) {
            case R.id.btn_login_guest:
                activityChangeAndFinish(MainActivity.class);
                break;
            case R.id.btn_login_kakao:
                if(callback == null) {
                    callback = new SessionCallback();
                    Session.getCurrentSession().addCallback(callback);
                    Session.getCurrentSession().checkAndImplicitOpen();
                }
                Session.getCurrentSession().open(AuthType.KAKAO_LOGIN_ALL, this);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        // 간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        Log.d(TAG, "onActivityResult()");
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Session.getCurrentSession().removeCallback(callback);
    }

    private class SessionCallback implements ISessionCallback {
        @Override
        public void onSessionOpened() {
            Log.d(TAG, "onSessionOpened()");
            activityChange(SignupActivity.class);
        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            if(exception != null) {
                Log.e(TAG, "onSessionOpenFailed()");
                Logger.e(exception);
            }
        }
    }
}
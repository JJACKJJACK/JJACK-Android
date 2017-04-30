package com.mkm.hanium.jjack;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.kakao.auth.ErrorCode;
import com.kakao.auth.ISessionCallback;
import com.kakao.auth.Session;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.exception.KakaoException;
import com.kakao.util.helper.log.Logger;

/**
 * Created by MIN on 2017-04-17.
 */

public class LoginActivity extends AppCompatActivity {
    private SessionCallback callback;
    private Button mLoginGuestBtn;
    private final int DELAY_LENGTH = 100;
    private Intent toMainActivityIntent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login_activity);

        GlobalApplication.setCurrentActivity(this);
        toMainActivityIntent = new Intent(getApplicationContext(), MainActivity.class);
        callback = new SessionCallback();
        Session.getCurrentSession().addCallback(callback);

        mLoginGuestBtn = (Button)findViewById(R.id.btn_login_guest);

        mLoginGuestBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        toMainActivityIntent.putExtra("member", "guest");
                        startActivity(toMainActivityIntent);
                        finish();
                    }
                }, DELAY_LENGTH);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        //간편로그인시 호출 ,없으면 간편로그인시 로그인 성공화면으로 넘어가지 않음
        if (Session.getCurrentSession().handleActivityResult(requestCode, resultCode, data)) {
            return;
        }

        super.onActivityResult(requestCode, resultCode, data);
    }

    private class SessionCallback implements ISessionCallback {

        @Override
        public void onSessionOpened() {
            UserManagement.requestMe(new MeResponseCallback() {

                @Override
                public void onFailure(ErrorResult errorResult) {
                    String message = "failed to get user info. msg=" + errorResult;
                    Logger.d(message);

                    ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                    if (result == ErrorCode.CLIENT_ERROR_CODE) {
                        finish();
                    } else {
                        //redirectMainActivity();
                    }
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                }

                @Override
                public void onNotSignedUp() {
                }

                @Override
                public void onSuccess(UserProfile userProfile) {
                    /**
                     * 로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
                     * 사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
                     */
                    Log.e("UserProfile", userProfile.toString());
                    toMainActivityIntent.putExtra("member", "kakao");
                    startActivity(toMainActivityIntent);
                    finish();
                }
            });

        }

        @Override
        public void onSessionOpenFailed(KakaoException exception) {
            /**
             * 세션 연결에 실패한 경우
             */
            if(exception != null) {
                Logger.e(exception);
            }
        }
    }
}

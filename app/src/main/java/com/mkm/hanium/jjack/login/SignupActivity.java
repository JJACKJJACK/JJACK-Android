package com.mkm.hanium.jjack.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.kakao.auth.ApiResponseCallback;
import com.kakao.auth.ErrorCode;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;
import com.mkm.hanium.jjack.MainActivity;
import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.BaseActivity;
import com.mkm.hanium.jjack.common.GlobalApplication;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by MIN on 2017-05-20.
 */

public class SignupActivity extends BaseActivity {

    /**
     * requestMe를 호출한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
    }

    /**
     * 로그인 시 수행할 메소드
     */
    protected void requestMe() {
        UserManagement.requestMe(new MeResponseCallback() {

            /**
             * 로그인에 실패한 경우
             * @param errorResult
             */
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);

                ErrorCode result = ErrorCode.valueOf(errorResult.getErrorCode());
                if (result == ErrorCode.CLIENT_ERROR_CODE) {
                    finish();
                } else {
                    activityRefresh(LoginActivity.class);
                }
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            /**
             * 앱에 가입되어 있지 않은 사람을 대상으로 가입창이 출력된다.
             */
            @Override
            public void onNotSignedUp() {
                showSignupPage();
            }

            /**
             * 로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
             * 사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
             * @param userProfile : 유저의 프로필 정보. 카카오 API
             */
            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.i("UserProfile", userProfile.toString());
                GlobalApplication.setCurrentUserId(userProfile.getId());
                activityChangeAndFinish(MainActivity.class);
            }
        });
    }

    protected void showSignupPage() {
        setContentView(R.layout.layout_signup_extra_user_property);
        Button signup = (Button) findViewById(R.id.btn_signup);

        final EditText gender = (EditText) findViewById(R.id.gender);
        final EditText age = (EditText) findViewById(R.id.age);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, String> properties = new HashMap<>();
                final String data1 = gender.getText().toString();
                final String data2 = age.getText().toString();
                properties.put("sex", data1);
                properties.put("year", data2);

                UserManagement.requestSignup(new ApiResponseCallback<Long>() {
                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        activityRefresh(LoginActivity.class);
                    }

                    @Override
                    public void onNotSignedUp() {}

                    @Override
                    public void onSuccess(Long result) {
                        GlobalApplication.setCurrentUserId(result);
                        Log.i("sign up", data1 + " " + data2);
                        activityChangeAndFinish(MainActivity.class);
                    }

                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        final String message = "UsermgmtResponseCallback : failure : " + errorResult;
                        com.kakao.util.helper.log.Logger.w(message);
                        Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                        activityRefresh(LoginActivity.class);
                    }
                }, properties);
            }
        });


    }
}

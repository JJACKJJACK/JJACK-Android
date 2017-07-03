package com.mkm.hanium.jjack.login;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.mkm.hanium.jjack.util.DefaultApi;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MIN on 2017-05-20.
 */

public class SignupActivity extends BaseActivity {

    private Button signup;
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
            public void onSessionClosed(ErrorResult errorResult) {}

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
                Log.i("SignupActivity", "UserProfile : " + userProfile.toString());
                GlobalApplication.setCurrentUserId(userProfile.getId());
                activityChangeAndFinish(MainActivity.class);
            }
        });
    }

    protected void showSignupPage() {
        setContentView(R.layout.layout_signup);
        signup = (Button) findViewById(R.id.btn_signup);
        final ExtraUserPropertyLayout extraUserPropertyLayout = (ExtraUserPropertyLayout) findViewById(R.id.extra_user_property);

        signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestSignup(extraUserPropertyLayout.getProperties());
            }
        });
    }

    protected void requestSignup(final Map<String, String> properties) {
        UserManagement.requestSignup(new ApiResponseCallback<Long>() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                activityRefresh(LoginActivity.class);
            }

            @Override
            public void onNotSignedUp() {}

            @Override
            public void onSuccess(final Long result) {
                final int year = Integer.parseInt(properties.get("year"));
                final String gender = properties.get("gender");
                GlobalApplication.setCurrentUserId(result);

                Call<DefaultApi> call = GlobalApplication.getApiInterface().sendUserProperty(result, gender, year);

                call.enqueue(new Callback<DefaultApi>() {
                    @Override
                    public void onResponse(Call<DefaultApi> call, Response<DefaultApi> response) {
                        if(response.body().getCode() == 1){
                            Log.i("SignupActivity", "Sign up success : ID(" + result.toString()
                                    + "), year(" + year + "), gender(" + gender + ")");

                            activityChangeAndFinish(MainActivity.class);
                        }
                        else{
                            Log.e("SignupActivity", response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultApi> call, Throwable t) {
                        Log.e("SignupActivity", "Not Connected to server :\n" + t.getMessage() + call.request());
                    }
                });
            }

            @Override
            public void onFailure(ErrorResult errorResult) {
                final String message = "SignupActivity, UsermgmtResponseCallback : failure : " + errorResult;
                com.kakao.util.helper.log.Logger.w(message);
                Toast.makeText(getApplicationContext(), message, Toast.LENGTH_LONG).show();
                activityRefresh(LoginActivity.class);
            }
        }, properties);
    }

    public void setSignupBtnEnable(boolean state) {
        signup.setEnabled(state);
    }
}

package com.mkm.hanium.jjack.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
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
import com.mkm.hanium.jjack.databinding.LayoutSignupBinding;
import com.mkm.hanium.jjack.util.DefaultApi;

import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MIN on 2017-05-20.
 */

public class SignupActivity extends BaseActivity {

    private LayoutSignupBinding binding;

    /**
     * requestMe를 호출하여 로그인 작업을 수행한다.
     * @param savedInstanceState 기존 session 정보가 저장된 객체
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestMe();
    }

    protected void requestMe() {
        // 로그인 시 수행하는 메소드
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

            @Override
            public void onNotSignedUp() {
                // 비가입 상태이면 가입창을 띄운다.
                showSignupPage();
            }

            /**
             * 로그인에 성공하면 로그인한 사용자의 일련번호, 닉네임, 이미지url등을 리턴합니다.
             * 사용자 ID는 보안상의 문제로 제공하지 않고 일련번호는 제공합니다.
             * @param userProfile : 유저의 프로필 정보. 카카오 API
             */
            @Override
            public void onSuccess(UserProfile userProfile) {
                Log.i(TAG, "UserProfile : " + userProfile.toString());
                GlobalApplication.setCurrentUserId(userProfile.getId());
                activityChangeAndFinish(MainActivity.class);
            }
        });
    }

    protected void showSignupPage() {
        binding = DataBindingUtil.setContentView(this, R.layout.layout_signup);
        binding.setActivity(this);
    }

    public void onClick(View v) {
        requestSignup(binding.extraUserProperty.getProperties());
    }

    protected void requestSignup(final Map<String, String> properties) {
        UserManagement.requestSignup(new ApiResponseCallback<Long>() {
            @Override
            public void onSessionClosed(ErrorResult errorResult) {
                Log.d(TAG, "onSessionClosed()");
                activityRefresh(LoginActivity.class);
            }

            @Override
            public void onNotSignedUp() {}

            @Override
            public void onSuccess(final Long result) {
                Log.d(TAG, "onSuccess() : requestSignup");
                final int year = Integer.parseInt(properties.get("year"));
                final String gender = properties.get("gender");
                GlobalApplication.setCurrentUserId(result);

                Call<DefaultApi> call = GlobalApplication.getApiInterface().sendUserProperty(result, gender, year);

                call.enqueue(new Callback<DefaultApi>() {
                    @Override
                    public void onResponse(Call<DefaultApi> call, Response<DefaultApi> response) {
                        if(response.body().getCode() == 1) {
                            Log.i(TAG, "Sign up success : ID(" + result.toString()
                                    + "), year(" + year + "), gender(" + gender + ")");
                            activityChangeAndFinish(MainActivity.class);
                        } else{
                            Log.e(TAG, response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(Call<DefaultApi> call, Throwable t) {
                        Log.e(TAG, "Not Connected to server :\n" + t.getMessage() + call.request());
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
        binding.btnSignup.setEnabled(state);
    }
}
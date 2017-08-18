package com.mkm.hanium.jjack.login;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
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

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MIN on 2017-05-20.
 * 로그인 작업을 수행하는 액티비티를 정의
 */

public class SignupActivity extends BaseActivity
        implements AdapterView.OnItemSelectedListener{

    private LayoutSignupBinding binding;
    boolean enableButton[];

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
             * @param errorResult 에러 결과
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
        binding.extraUserProperty.setActivity(this);
        enableButton = new boolean[2];

        setSpinner(binding.extraUserProperty.spinnerYear);
        setSpinner(binding.extraUserProperty.spinnerGender);
    }

    private void setSpinner(Spinner spinner) {
        ArrayList<String> list = new ArrayList<>();

        setSpinnerItem(list, spinner);

        ArrayAdapter adapter = new ArrayAdapter(this,
                R.layout.simple_dropdown_item_1line,
                list);
        spinner.setSelection(0);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void setSpinnerItem(ArrayList<String> list, Spinner spinner) {
        if(spinner == binding.extraUserProperty.spinnerYear) {
            int y = Calendar.getInstance().get(Calendar.YEAR); // 현재 연도를 받아옴

            list.add("태어난 해");

            for (int i = 1960; i <= y; i++)
                list.add(Integer.toString(i));

        } else if (spinner == binding.extraUserProperty.spinnerGender) {
            list.add("성별");
            list.add("남자");
            list.add("여자");
        }
    }

    public void onClick(View v) {
        HashMap<String, String> properties = new HashMap<>();

        String year = binding.extraUserProperty.spinnerYear.getSelectedItem().toString();
        String gender = binding.extraUserProperty.spinnerGender.getSelectedItem().toString();

        properties.put("year", year);
        properties.put("gender", gender);

        requestSignup(properties);
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

                Call<DefaultApi> call = GlobalApplication.getApiInterface().submitUserPropertyRequest(result, gender, year);

                call.enqueue(new Callback<DefaultApi>() {
                    @Override
                    public void onResponse(@NonNull Call<DefaultApi> call, @NonNull Response<DefaultApi> response) {
                        if(response.body().getCode() == 1) {
                            Log.i(TAG, "Sign up success : ID(" + result.toString()
                                    + "), year(" + year + "), gender(" + gender + ")");
                            activityChangeAndFinish(MainActivity.class);
                        } else{
                            Log.e(TAG, response.body().getMessage());
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DefaultApi> call, @NonNull Throwable t) {
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

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int selectedSpinner = -1;

        if(parent.getId() == R.id.spinner_year)
            selectedSpinner = 0;
        else if(parent.getId() == R.id.spinner_gender)
            selectedSpinner = 1;

        enableButton[selectedSpinner] = !(position == 0 && selectedSpinner != -1);

        // 두 스피너 모두 정상적인 값이 선택되어야 버튼이 활성화됨
        setSignupBtnEnable(enableButton[0] && enableButton[1]);
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {
    }

    public void setSignupBtnEnable(boolean state) {
        binding.btnSignup.setEnabled(state);
    }
}
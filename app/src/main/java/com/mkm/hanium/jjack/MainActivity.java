package com.mkm.hanium.jjack;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v7.app.ActionBarDrawerToggle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.KakaoParameterException;
import com.kakao.util.helper.log.Logger;
import com.mkm.hanium.jjack.common.BindActivity;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.databinding.ActivityMainBinding;
import com.mkm.hanium.jjack.databinding.NavHeaderMainBinding;
import com.mkm.hanium.jjack.keyword_ranking.KeywordRankingFragment;
import com.mkm.hanium.jjack.login.LoginActivity;
import com.mkm.hanium.jjack.timeline.TimelineFragment;
import com.mkm.hanium.jjack.util.BackPressCloseSystem;
import com.mkm.hanium.jjack.util.DefaultApi;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BindActivity<ActivityMainBinding>
        implements NavigationView.OnNavigationItemSelectedListener {

    // 데이터바인딩에 사용될 변수.
    // 해당 레이아웃의 이름 + binding으로 자동 생성(activity_main -> ActivityMainBinding)
    private final BackPressCloseSystem mBack = new BackPressCloseSystem(this);
    private final String keywordRankingTag = KeywordRankingFragment.class.getSimpleName();
    private final String timelineTag = TimelineFragment.class.getSimpleName();

    private NavHeaderMainBinding navBinding;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String title = getString(R.string.title_keyword_ranking);

        // setContentView는 BindActivity에 선언되어 있다.

        // xml의 include 필드는 id를 반드시 지정해야 한다.
        // binding.(include필드의id).(include 레이아웃의 뷰ID) 식으로 호출한다.
        binding.includedAppBar.toolbar.setTitle(title);
        setSupportActionBar(binding.includedAppBar.toolbar);

        // 네비게이션 뷰 접근을 위한 바인딩 생성
        navBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0));

        if(GlobalApplication.getUser().isSignup()) {
            // 지금 막 가입한 유저인 경우 카카오에서 바로 유저 정보를 받을 수 없기 때문에 따로 받아와야 한다.
            // 카카오 서버에 요청-응답하는 과정이 있어 속도가 늦긴 하지만 profile을 제대로 저장한다.
            requestUserProperty(navBinding.getRoot().getContext());
        } else if(!GlobalApplication.getUser().isSignup() && GlobalApplication.getUser().isLogin()){
            // 이미 회원이면 카카오 로그인 절차에서 프로필 정보를 받아올 수 있다.
            setProfileImage(navBinding.getRoot().getContext(), GlobalApplication.getUser().getImagePath());

            // 프로필 정보를 네비게이션 헤더에 삽입
            navBinding.setUser(GlobalApplication.getUser());
        } else if(!GlobalApplication.getUser().isLogin()) {
            // 비회원이면 default 정보를 삽입한다.
            navBinding.navProfileImage.setImageResource(R.drawable.default_user);
            GlobalApplication.getUser().setProfile(
                    null,
                    getString(R.string.profile_nicname),
                    getString(R.string.profile_email)
            );

            // 프로필 정보를 네비게이션 헤더에 삽입
            navBinding.setUser(GlobalApplication.getUser());
        }

        // 비회원인 경우 프로필 정보를 클릭하여 가입할 수 있음.
        navBinding.navProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!GlobalApplication.getUser().isLogin()) {
                    activityChange(LoginActivity.class, "open");
                }
            }
        });

        // <layout> 요소 내에 있는 하위 뷰들의 id는 낙타표기법으로 자동 변환된다.
        // nav_view -> binding.navView
        // 변환된 인스턴스는 객체와 바로 연결된다. binding.navView는 NavigationView형 객체이다.
        binding.navView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this,
                binding.drawerLayout,
                binding.includedAppBar.toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close);

        binding.drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // 만약 ID값(int)이 필요하다면 ViewID.getId()를 이용한다.
        getSupportFragmentManager().beginTransaction().add(
                binding.includedAppBar.includedContents.fragmentComponentLayout.getId(),
                        KeywordRankingFragment.newInstance(), keywordRankingTag).commit();
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            mBack.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_kakaolink) {
            sendKakaoLink();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        String tag = null;

        switch (id) {
            case R.id.nav_keyword_ranking:
                Log.d(TAG, keywordRankingTag + " is selected.");
                tag = keywordRankingTag;
                fragment = KeywordRankingFragment.newInstance();
                binding.includedAppBar.toolbar.setTitle(getString(R.string.title_keyword_ranking));
                break;
            case R.id.nav_timeline:
                Log.d(TAG, timelineTag + " is selected.");
                tag = timelineTag;
                fragment = TimelineFragment.newInstance();
                binding.includedAppBar.toolbar.setTitle(getString(R.string.title_timeline));
                break;
            case R.id.nav_logout:
                logout();
                break;
            case R.id.nav_unlink:
                unlink();
                break;

            default:
                break;
        }

        if(fragment != null && tag != null) {
            for(int i=0; i<fragmentManager.getBackStackEntryCount(); ++i)
                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager.beginTransaction().replace(R.id.fragmentComponentLayout, fragment, tag).commit();
        }

        binding.drawerLayout.closeDrawer(GravityCompat.START);

        return true;
    }

    private void logout() {
        if(!GlobalApplication.getUser().isLogin()) {
            Toast.makeText(this, "비로그인 사용자입니다.", Toast.LENGTH_LONG).show();
        } else {
            UserManagement.requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    // GlobalApplication에 저장된 유저 ID를 비회원값(-5000)으로 변경하고
                    // 로그인 액티비티로 돌아간다.
                    Log.d(TAG, "logout");
                    GlobalApplication.getUser().clearProfile();
                    activityChange(LoginActivity.class);
                }
            });
        }
    }

    private void unlink() {
        if(!GlobalApplication.getUser().isLogin()) {
            Toast.makeText(this, "비로그인 사용자입니다.", Toast.LENGTH_LONG).show();
        } else {
            UserManagement.requestUnlink(new UnLinkResponseCallback() {
                /**
                 * 탈퇴처리
                 * onSuccess()에서 정상실행됨.
                 */
                @Override
                public void onFailure(ErrorResult errorResult) {
                    Log.e(TAG, "unlink : Fail");
                    Logger.e(errorResult.toString());
                }

                @Override
                public void onSessionClosed(ErrorResult errorResult) {
                    Log.d(TAG, "unlink : Session Closed.");
                    activityChange(LoginActivity.class);
                }

                @Override
                public void onNotSignedUp() {
                    Log.d(TAG, "unlink : Not Signed up.");
                    activityChange(LoginActivity.class);
                }

                @Override
                public void onSuccess(Long result) {
                    Log.d(TAG, "unlink to kakao is successful : , " + result.toString());

                    GlobalApplication.getUser().clearProfile();

                    Call<DefaultApi> call = GlobalApplication.getApiInterface().unlinkUserRequest(result);
                    call.enqueue(new Callback<DefaultApi>() {
                        // DB에 접근하여 등록된 회원 정보를 삭제한다.
                        @Override
                        public void onResponse(@NonNull Call<DefaultApi> call, @NonNull Response<DefaultApi> response) {
                            if (response.body().getCode() == 1) {
                                Log.d(TAG, "unlink to DB is successful");
                            } else {
                                Log.e(TAG, response.body().getMessage());
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<DefaultApi> call, @NonNull Throwable t) {
                            Log.e("SignupActivity", "Not Connected to server :\n" + t.getMessage() + call.request());
                        }
                    });
                    activityChange(LoginActivity.class);
                }
            });
        }
    }

    /**
     * 카카오 링크의 템플릿 버튼을 이용하려면 도메인 등록이 필요하다.
     * 도메인 등록을 할 수 없었기 때문에 사진과 text 형식으로 변경하였음.
     */
    public void sendKakaoLink() {
        try {
            final KakaoLink link = KakaoLink.getKakaoLink(this);
            final KakaoTalkLinkMessageBuilder builder = link.createKakaoTalkLinkMessageBuilder();

            String title = "청년이 ‘죄송’하지 않을때까지";
            String content = "신문 취재엔 응했지만 내심 ‘악플’을 걱정했다. 어려운 사람을 보고도...";
            String imgUrl = "http://dimg.donga.com/a/180/120/95/2/wps/NEWS/IMAGE/2017/06/27/85071743.1.jpg";
            String url = "http://news.donga.com/List/3/all/20170627/85071744/1";

            builder.addImage(imgUrl, 300, 200)
                    .addText(title + "\n\n"
                            + content + "\n\n"
                            + "자세히 보기 >> \n"
                            + url)
                    .build();

            /*메시지 발송*/
            link.sendMessage(builder, this);
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }

    /**
     * 유저의 정보를 로드하는 메소드
     */
    private void requestUserProperty(final Context context) {
        List<String> propertyKeys = new ArrayList<>();
        propertyKeys.add("kaccount_email");
        propertyKeys.add("nickname");
        propertyKeys.add("thumbnail_image");

        UserManagement.requestMe(new MeResponseCallback() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                String message = "failed to get user info. msg=" + errorResult;
                Logger.d(message);
            }

            @Override
            public void onSessionClosed(ErrorResult errorResult) {
            }

            @Override
            public void onSuccess(UserProfile userProfile) {
                Logger.d("UserProfile : " + userProfile);

                // 로그인 정보를 프로필에 저장
                GlobalApplication.getUser().setProfile(
                        userProfile.getId(),
                        userProfile.getThumbnailImagePath(),
                        userProfile.getNickname(),
                        userProfile.getEmail(),
                        true
                );

                setProfileImage(context, GlobalApplication.getUser().getImagePath());
                navBinding.setUser(GlobalApplication.getUser());
            }

            @Override
            public void onNotSignedUp() { }
        }, propertyKeys, false);
    }

    /**
     * 카카오 프로필 이미지가 있으면 로드하고 없으면 기본 이미지를 출력한다.
     * @param context 네비게이션 헤더의 context
     * @param url 카카오 섬네일 이미지 주소
     */
    private void setProfileImage(Context context, String url) {
        if(url != null) {
            Picasso.with(context).load(url).into(navBinding.navProfileImage);
        } else {
            Picasso.with(context).load(R.drawable.default_user).into(navBinding.navProfileImage);
        }
    }
}

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
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.MeResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.usermgmt.response.model.UserProfile;
import com.kakao.util.helper.log.Logger;
import com.mkm.hanium.jjack.common.BindActivity;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.databinding.ActivityMainBinding;
import com.mkm.hanium.jjack.databinding.NavHeaderMainBinding;
import com.mkm.hanium.jjack.keyword.KeywordFragment;
import com.mkm.hanium.jjack.keyword_ranking.KeywordRankingFragment;
import com.mkm.hanium.jjack.login.LoginActivity;
import com.mkm.hanium.jjack.ranking.RankingFragment;
import com.mkm.hanium.jjack.realtime.RealtimeFragment;
import com.mkm.hanium.jjack.scrap.ScrapFragment;
import com.mkm.hanium.jjack.timeline.TimelineFragment;
import com.mkm.hanium.jjack.util.DefaultApi;
import com.mkm.hanium.jjack.request_api.KeywordRequestApi;
import com.mkm.hanium.jjack.util.Network;
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
    private String tags[];
    private String titles[];
    private NavHeaderMainBinding navBinding;
    private long backButtonPressedTime = 0L;
    private final long FINISH_INTERVAL_TIME = 2000L;
    public List<KeywordRequestApi.KeywordRequestResultApi> keywordList;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_main;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        final String title = getString(R.string.menu_news_ranking);

        tags = new String[] {
                RankingFragment.class.getSimpleName(),
                KeywordFragment.class.getSimpleName(),
                RealtimeFragment.class.getSimpleName(),
                ScrapFragment.class.getSimpleName(),
                KeywordRankingFragment.class.getSimpleName(),
                TimelineFragment.class.getSimpleName()
        };

        titles = new String[] {
                getString(R.string.menu_news_ranking),
                getString(R.string.menu_keyword),
                getString(R.string.menu_realtime),
                getString(R.string.menu_scrap),
                getString(R.string.menu_keyword_ranking),
                getString(R.string.menu_timeline)
        };

        // setContentView는 BindActivity에 선언되어 있다.

        // xml의 include 필드는 id를 반드시 지정해야 한다.
        // binding.(include필드의id).(include 레이아웃의 뷰ID) 식으로 호출한다.
        binding.includedAppBar.toolbar.setTitle(title);
        setSupportActionBar(binding.includedAppBar.toolbar);

        // 네비게이션 뷰 접근을 위한 바인딩 생성
        navBinding = NavHeaderMainBinding.bind(binding.navView.getHeaderView(0));

        setNavigationProfile();

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
        getSupportFragmentManager()
                .beginTransaction()
                .add(binding.includedAppBar.includedContents.fragmentComponentLayout.getId(),
                        RankingFragment.newInstance(), tags[0])
                .addToBackStack(null)
                .commit();

        callApiRequest();
    }

    private void setNavigationProfile() {
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
    }

    @Override
    public void onBackPressed() {
        if (binding.drawerLayout.isDrawerOpen(GravityCompat.START)) {
            binding.drawerLayout.closeDrawer(GravityCompat.START);
        } else {
            FragmentManager fm = getSupportFragmentManager();
            if(fm.getBackStackEntryCount() >= 2) {
                // BackStack에 두 개 이상 있으면 뒤로
                fm.popBackStack();
            } else {
                // 아니라면 앱 종료
                long tempTime = System.currentTimeMillis();
                long intervalTime = tempTime - backButtonPressedTime;

                if (0 <= intervalTime && FINISH_INTERVAL_TIME >= intervalTime) {
                    moveTaskToBack(true);
                    finish();
                    android.os.Process.killProcess(android.os.Process.myPid());
                } else {
                    backButtonPressedTime = tempTime;
                    Toast.makeText(getApplicationContext(), "\'뒤로\' 버튼을 한 번 더 누르시면 종료됩니다.",
                            Toast.LENGTH_SHORT).show();
                }
            }
        }
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
            case R.id.nav_news_ranking:
                Log.d(TAG, tags[0] + " is selected.");
                tag = tags[0];
                fragment = RankingFragment.newInstance();
                binding.includedAppBar.toolbar.setTitle(titles[0]);
                break;
            case R.id.nav_keyword:
                Log.d(TAG, tags[1] + " is selected.");
                tag = tags[1];
                fragment = KeywordFragment.newInstance();
                binding.includedAppBar.toolbar.setTitle(titles[1]);
                break;
            case R.id.nav_realtime:
                Log.d(TAG, tags[2] + " is selected.");
                tag = tags[2];
                fragment = RealtimeFragment.newInstance();
                binding.includedAppBar.toolbar.setTitle(titles[2]);
                break;
            case R.id.nav_scrap:
                Log.d(TAG, tags[3] + " is selected.");
                tag = tags[3];
                fragment = ScrapFragment.newInstance();
                binding.includedAppBar.toolbar.setTitle(titles[3]);
                break;
            case R.id.nav_keyword_ranking:
                Log.d(TAG, tags[4] + " is selected.");
                tag = tags[4];
                fragment = KeywordRankingFragment.newInstance();
                binding.includedAppBar.toolbar.setTitle(titles[4]);
                break;
            case R.id.nav_timeline:
                Log.d(TAG, tags[5] + " is selected.");
                tag = tags[5];
                fragment = TimelineFragment.newInstance();
                binding.includedAppBar.toolbar.setTitle(titles[5]);
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
            for(int i = 0; i < fragmentManager.getBackStackEntryCount(); ++i)
                fragmentManager.popBackStackImmediate(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            fragmentManager
                    .beginTransaction()
                    .replace(R.id.fragmentComponentLayout, fragment, tag)
                    .addToBackStack(null)
                    .commit();
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

    private void callApiRequest() {
        // todo 실제 회원 데이터로 바꾸기
        if (Network.isNetworkConnected(this)) {
            Call<KeywordRequestApi> call = GlobalApplication.getApiInterface().keywordRequest(427719221);
            call.enqueue(new Callback<KeywordRequestApi>() {
                @Override
                public void onResponse(@NonNull Call<KeywordRequestApi> call, @NonNull Response<KeywordRequestApi> response) {
                    if (response.body().getCode() == 1) {
                        keywordList = response.body().getKeywords();
                        for (int i = 0; i < keywordList.size(); i++) {
                            GlobalApplication.addEntries(keywordList.get(i).getKeywordName());
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<KeywordRequestApi> call, @NonNull Throwable t) {
                    Log.e("not receive", "warning message" + t.getMessage() + call.request());
                }
            });
        }
    }
}

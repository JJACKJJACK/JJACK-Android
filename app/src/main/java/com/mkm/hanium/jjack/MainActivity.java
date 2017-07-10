package com.mkm.hanium.jjack;

import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.kakao.kakaolink.v2.KakaoLinkResponse;
import com.kakao.kakaolink.v2.KakaoLinkService;
import com.kakao.message.template.ButtonObject;
import com.kakao.message.template.ContentObject;
import com.kakao.message.template.FeedTemplate;
import com.kakao.message.template.LinkObject;
import com.kakao.message.template.SocialObject;
import com.kakao.network.ErrorResult;
import com.kakao.network.callback.ResponseCallback;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.util.helper.log.Logger;
import com.mkm.hanium.jjack.common.BaseActivity;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.keyword_ranking.KeywordRankingFragment;
import com.mkm.hanium.jjack.login.LoginActivity;
import com.mkm.hanium.jjack.timeline.TimelineFragment;
import com.mkm.hanium.jjack.util.BackPressCloseSystem;
import com.mkm.hanium.jjack.util.DefaultApi;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final BackPressCloseSystem mBack = new BackPressCloseSystem(this);
    private final String TAG = "MainActivity";
    private final String mTitle = "타임라인";
    private final String mKeywordRanking = "keyword_ranking";
    private final String mTimeline = "time_line";
    private final long defaultUserId = -5000;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
        setContentView(R.layout.activity_main);
        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mToolbar.setTitle(mTitle);
        setSupportActionBar(mToolbar);

        mDrawer = (DrawerLayout) findViewById(R.id.drawer_layout);

        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, mDrawer, mToolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        mDrawer.addDrawerListener(toggle);
        toggle.syncState();

        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragmentComponentLayout, new TimelineFragment(), mTimeline).commit();
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
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
            sendDefaultFeedTemplate();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment fragment = null;
        String tag = null;

        switch (id) {
            case R.id.nav_keyword_ranking:
                Log.d(TAG, mKeywordRanking + " is selected.");
                tag = mKeywordRanking;
                fragment = new KeywordRankingFragment();
                mToolbar.setTitle("키워드 랭킹");
                break;
            case R.id.nav_timeline:
                Log.d(TAG, mTimeline + " is selected.");
                tag = mTimeline;
                fragment = new TimelineFragment();
                mToolbar.setTitle("타임라인");
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

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    private void logout() {
        if(GlobalApplication.getCurrentUserId() == defaultUserId) {
            Toast.makeText(this, "비로그인 사용자입니다.", Toast.LENGTH_LONG).show();
        } else {
            UserManagement.requestLogout(new LogoutResponseCallback() {
                @Override
                public void onCompleteLogout() {
                    /**
                     * GlobalApplication에 저장된 유저 ID를 비회원값(-5000)으로 변경하고
                     * 로그인 액티비티로 돌아간다.
                     */
                    Log.d(TAG, "logout success.");
                    GlobalApplication.setCurrentUserId(defaultUserId);
                    activityChangeAndFinish(LoginActivity.class);
                }
            });
        }
    }

    private void unlink() {
        if(GlobalApplication.getCurrentUserId() == defaultUserId) {
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
                    activityChangeAndFinish(LoginActivity.class);
                }

                @Override
                public void onNotSignedUp() {
                    Log.d(TAG, "unlink : Not Signed up.");
                    activityChangeAndFinish(LoginActivity.class);
                }

                @Override
                public void onSuccess(Long result) {
                    Log.d(TAG, "unlink to kakao is successful : , " + result.toString());
                    GlobalApplication.setCurrentUserId(defaultUserId);

                    Call<DefaultApi> call = GlobalApplication.getApiInterface().unlinkUserProperty(result);
                    call.enqueue(new Callback<DefaultApi>() {
                        /**
                         * DB에 접근하여 등록된 회원 정보를 삭제한다.
                         */
                        @Override
                        public void onResponse(Call<DefaultApi> call, Response<DefaultApi> response) {
                            if (response.body().getCode() == 1) {
                                Log.d(TAG, "unlink to DB is successful");
                            } else {
                                Log.e(TAG, response.body().getMessage());
                            }
                        }

                        @Override
                        public void onFailure(Call<DefaultApi> call, Throwable t) {
                            Log.e("SignupActivity", "Not Connected to server :\n" + t.getMessage() + call.request());
                        }
                    });
                    activityChangeAndFinish(LoginActivity.class);
                }
            });
        }
    }

    private void sendDefaultFeedTemplate() {
        FeedTemplate params = FeedTemplate
                .newBuilder(ContentObject.newBuilder("딸기 치즈 케익",
                        "http://mud-kage.kakao.co.kr/dn/Q2iNx/btqgeRgV54P/VLdBs9cvyn8BJXB3o7N8UK/kakaolink40_original.png",
                        LinkObject.newBuilder().setWebUrl("https://developers.kakao.com")
                                .setMobileWebUrl("https://developers.kakao.com").build())
                        .setDescrption("#케익 #딸기 #삼평동 #카페 #분위기 #소개팅")
                        .build())
                .setSocial(SocialObject.newBuilder().setLikeCount(286).setCommentCount(45)
                        .setSharedCount(845).build())
                .addButton(new ButtonObject("웹으로 보기", LinkObject.newBuilder().setWebUrl("https://developers.kakao.com").setMobileWebUrl("https://developers.kakao.com").build()))
                .addButton(new ButtonObject("앱으로 보기", LinkObject.newBuilder()
                        .setWebUrl("https://developers.kakao.com")
                        .setMobileWebUrl("https://developers.kakao.com")
                        .setAndroidExecutionParams("key1=value1")
                        .setIosExecutionParams("key1=value1")
                        .build()))
                .build();


        KakaoLinkService.getInstance().sendDefault(this, params, new ResponseCallback<KakaoLinkResponse>() {
            @Override
            public void onFailure(ErrorResult errorResult) {
                Logger.e(errorResult.toString());
            }

            @Override
            public void onSuccess(KakaoLinkResponse result) {
                Log.d(TAG, "Send KakaoLink");
            }
        });
    }
}

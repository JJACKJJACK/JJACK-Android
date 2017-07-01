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

import com.kakao.network.ErrorResult;
import com.kakao.usermgmt.UserManagement;
import com.kakao.usermgmt.callback.LogoutResponseCallback;
import com.kakao.usermgmt.callback.UnLinkResponseCallback;
import com.kakao.util.helper.log.Logger;
import com.mkm.hanium.jjack.common.BaseActivity;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.login.LoginActivity;
import com.mkm.hanium.jjack.login.UserPropertyApi;
import com.mkm.hanium.jjack.ranking.KeywordRankingFragment;
import com.mkm.hanium.jjack.util.BackPressCloseSystem;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends BaseActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private final BackPressCloseSystem mBack = new BackPressCloseSystem(this);
    private final String mTitle = "키워드 랭킹";
    private final String mKeywordRanking = "keyword_ranking";
    private final long defaultUserId = -5000;
    private Toolbar mToolbar;
    private DrawerLayout mDrawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("MainActivity", "onCreate()");
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
                .add(R.id.fragmentComponentLayout, new KeywordRankingFragment(), mKeywordRanking).commit();
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
        if (id == R.id.action_settings) {
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

        if (id == R.id.nav_keyword_ranking) {
            Log.d("MainActivity", mKeywordRanking + " is selected.");
            tag = mKeywordRanking;
            fragment = new KeywordRankingFragment();
        } else if (id == R.id.nav_logout) {
            if(GlobalApplication.getCurrentUserId() == defaultUserId) {
                Toast.makeText(this, "비로그인 사용자입니다.", Toast.LENGTH_LONG).show();
            } else {
                UserManagement.requestLogout(new LogoutResponseCallback() {
                    @Override
                    public void onCompleteLogout() {
                        /**
                         * 로그아웃
                         * GlobalApplication에 저장된 유저 ID를 비회원값(-5000)으로 변경
                         * 위 내용은 탈퇴에서도 마찬가지
                         */
                        Log.d("MainActivity", "logout success.");
                        GlobalApplication.setCurrentUserId(defaultUserId);
                        activityChangeAndFinish(LoginActivity.class);
                    }
                });
            }
        } else if (id == R.id.nav_unlink) {
            if(GlobalApplication.getCurrentUserId() == defaultUserId) {
                Toast.makeText(this, "비로그인 사용자입니다.", Toast.LENGTH_LONG).show();
            } else {
                UserManagement.requestUnlink(new UnLinkResponseCallback() {
                    @Override
                    public void onFailure(ErrorResult errorResult) {
                        Log.e("MainActivity", "unlink : Fail");
                        Logger.e(errorResult.toString());
                    }

                    @Override
                    public void onSessionClosed(ErrorResult errorResult) {
                        Log.d("MainActivity", "unlink : Session Closed.");
                        activityChangeAndFinish(LoginActivity.class);
                    }

                    @Override
                    public void onNotSignedUp() {
                        /**
                         * TODO : 비회원이 탈퇴버튼을 누르지 못하게 할 것.
                         */
                        Log.d("MainActivity", "unlink : Not Signed up.");
                        activityChangeAndFinish(LoginActivity.class);
                    }

                    @Override
                    public void onSuccess(Long result) {
                        Log.d("MainActivity", "unlink to kakao is successful : , " + result.toString());
                        GlobalApplication.setCurrentUserId(defaultUserId);

                        ApiInterface apiInterface = ApiClient.getClient().create(ApiInterface.class);
                        Call<UserPropertyApi> call = apiInterface.unlinkUserProperty(result);
                        call.enqueue(new Callback<UserPropertyApi>() {
                            @Override
                            public void onResponse(Call<UserPropertyApi> call, Response<UserPropertyApi> response) {
                                if (response.body().getCode() == 1) {
                                    Log.d("MainActivity", "unlink to DB is successful");
                                } else {
                                    Log.e("MainActivity", response.body().getMessage());
                                }
                            }

                            @Override
                            public void onFailure(Call<UserPropertyApi> call, Throwable t) {
                                Log.e("SignupActivity", "Not Connected to server :\n" + t.getMessage() + call.request());
                            }
                        });
                        activityChangeAndFinish(LoginActivity.class);
                    }
                });
            }
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
}

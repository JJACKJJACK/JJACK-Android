package com.mkm.hanium.jjack.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;

import com.mkm.hanium.jjack.R;

/**
 * Created by MIN on 2017-08-24.
 * fragment 기본 정의
 */

public class BaseFragment extends Fragment {

    protected final String TAG = "LOG/" + getClass().getSimpleName();

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate");
        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        Log.d(TAG, "onViewCreated");
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onResume() {
        Log.d(TAG, "onResume");
        super.onResume();
    }

    @Override
    public void onDestroy() {
        Log.d(TAG, "onDestroy");
        super.onDestroy();
    }

    @Override
    public void onDestroyView() {
        Log.d(TAG, "onDestroyView");
        super.onDestroyView();
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.d(TAG, "onStart");
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        String tag = "";

        try {
            Fragment fragment = getFragmentManager().findFragmentById(R.id.fragmentComponentLayout);
            tag = fragment.getTag();
            if (tag.equals("issue news")) {
                tag = "news";
            }
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if(tag.equals("news")) {
            inflater.inflate(R.menu.news_menu, menu);
        } else {
            inflater.inflate(R.menu.main, menu);
        }
    }



    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

//        if(item.getItemId() == R.id.action_search)
//        {
//            // todo 검색 구현하기
//        }

        return super.onOptionsItemSelected(item);
    }
}

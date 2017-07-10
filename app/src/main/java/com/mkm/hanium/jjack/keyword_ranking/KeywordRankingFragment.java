package com.mkm.hanium.jjack.keyword_ranking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.util.KeywordRankingApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MIN on 2017-05-20.
 */

public class KeywordRankingFragment extends Fragment {

    private final String TAG = "KeywordRankingFragment";
    private List<RankingItem> mList;
    private RankingAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d(TAG, "onCreate()");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View layout = inflater.inflate(R.layout.fragment_keywordranking, container, false);

        mList = new ArrayList<>();

        loadData();

//        mAdapter = new RankingAdapter(getActivity(), mList);
//        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view_keyword_ranking);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    public void loadData() {
        Call<KeywordRankingApi> call = GlobalApplication.getApiInterface().loadKeywordRanking();
        call.enqueue(new Callback<KeywordRankingApi>() {
            @Override
            public void onResponse(Call<KeywordRankingApi> call, Response<KeywordRankingApi> response) {
                if(response.body().getCode() == 1){

                } else {
                    Log.d(TAG, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<KeywordRankingApi> call, Throwable t) {
                Log.e(TAG, "Not Connected to server :\n" + t.getMessage() + call.request());
            }
        });
    }
}
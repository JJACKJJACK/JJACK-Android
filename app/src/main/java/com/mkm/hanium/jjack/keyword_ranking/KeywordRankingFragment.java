package com.mkm.hanium.jjack.keyword_ranking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mkm.hanium.jjack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MIN on 2017-05-20.
 */

public class KeywordRankingFragment extends Fragment {

    private List<RankingItem> mList;
    private RankingAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("KeywordRankingFragment", "onCreate()");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("KeywordRankingFragment", "onCreateView()");
        View layout = inflater.inflate(R.layout.fragment_keywordranking, container, false);

        mList = new ArrayList<>();

        initData();

        mAdapter = new RankingAdapter(getActivity(), mList);
        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view_keyword_ranking);
        mRecyclerView.setAdapter(mAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        return layout;
    }

    public void initData() {
        /**
         * todo Test data -> DB data
         */
        for(int i = 1; i <= 20; i++)
            mList.add(new RankingItem(i, "하하"));
    }
}
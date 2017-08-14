package com.mkm.hanium.jjack.keyword_ranking;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.BindFragment;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.databinding.FragmentKeywordRankingBinding;
import com.mkm.hanium.jjack.util.KeywordRankingRequestApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MIN on 2017-05-20.
 */

public class KeywordRankingFragment extends BindFragment<FragmentKeywordRankingBinding> {

    public static KeywordRankingFragment newInstance() {
        return new KeywordRankingFragment();
    }

    private List<KeywordRankingItem> list;
    private KeywordRankingAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_keyword_ranking;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        this.list = new ArrayList<>();
        binding.setFragment(this);
        setRecyclerView();
        loadData();

        return binding.getRoot();
    }

    private void setRecyclerView() {
        adapter = new KeywordRankingAdapter(getActivity(), list, binding.recyclerViewKeywordRanking);
        binding.recyclerViewKeywordRanking.setAdapter(adapter);
        binding.recyclerViewKeywordRanking.setLayoutManager(new LinearLayoutManager(getActivity()));
    }

    private void loadData() {
        Call<KeywordRankingRequestApi> call = GlobalApplication.getApiInterface().keywordRankingRequest();
        call.enqueue(new Callback<KeywordRankingRequestApi>() {
            @Override
            public void onResponse(Call<KeywordRankingRequestApi> call, Response<KeywordRankingRequestApi> response) {
                if (response.body().getCode() == 1) {
                    list = response.body().getResult();
                    adapter.updateItems(list);
                    Log.d(TAG, "Keyword Ranking Data Load");
                } else {
                    Log.d(TAG, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<KeywordRankingRequestApi> call, Throwable t) {
                Log.e(TAG, "Not Connected to server :\n" + t.getMessage() + call.request());
            }
        });
    }
}
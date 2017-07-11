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
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.util.KeywordRankingRequestApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MIN on 2017-05-20.
 */

public class KeywordRankingFragment extends Fragment {

//    private FragmentKeywordRankingBinding binding;

    private final String TAG = this.getClass().getSimpleName();
    private List<KeywordRankingItem> list;
    private KeywordRankingAdapter adapter;
    private RecyclerView recyclerView;

//    @Override
//    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
//        super.onActivityCreated(savedInstanceState);
//        Log.d(TAG, "onActivityCreated()");
//        binding = FragmentKeywordRankingBinding.bind(getView());
//    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d(TAG, "onCreateView()");
        View layout = inflater.inflate(R.layout.fragment_keyword_ranking, container, false);

        list = new ArrayList<>();

        setRecyclerView(layout);
        loadData();

        return layout;
    }

    private void loadData() {
        Call<KeywordRankingRequestApi> call = GlobalApplication.getApiInterface().keywordRankingRequest();
        call.enqueue(new Callback<KeywordRankingRequestApi>() {
            @Override
            public void onResponse(Call<KeywordRankingRequestApi> call, Response<KeywordRankingRequestApi> response) {
                if(response.body().getCode() == 1) {
                    Log.d(TAG, "Keyword Ranking Data Load.");
                    list = response.body().getResult();
                    adapter.addList(list);
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

    private void setRecyclerView(View v) {
        adapter = new KeywordRankingAdapter(getActivity(), list);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_keyword_ranking);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}
package com.mkm.hanium.jjack.ranking;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.BaseFragment;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.request_api.NewsRequestApi;
import com.mkm.hanium.jjack.util.Network;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 김민선 on 2017-05-09.
 * 뉴스 랭킹 프래그먼트를 정의하는 클래스
 */

public class RankingFragment extends BaseFragment {

    public static RankingFragment newInstance() {
        return new RankingFragment();
    }

    private RankingRecyclerViewAdapter adapter;
    private List<NewsRequestApi.NewsRequestResultApi> list;
    private String nowDomain;
    private ArrayAdapter<CharSequence> arrayAdapter;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_ranking, container, false);
        GlobalApplication.setIndex(0);
        list = new ArrayList<>();
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_ranking);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_blank);

        arrayAdapter = ArrayAdapter.createFromResource(getActivity(),
                R.array.domain,
                R.layout.simple_dropdown_item_1line);

        spinner.setAdapter(arrayAdapter);
        spinner.setSelection(0);
        nowDomain = getResources().getStringArray(R.array.domain)[0];

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!arrayAdapter.getItem(position).equals(nowDomain)) {
                    nowDomain = (String) arrayAdapter.getItem(position);
                    adapter.setNowDomain(nowDomain);
                    list.clear();
                    GlobalApplication.setIndex(0);
                    callApiRequest();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        adapter = new RankingRecyclerViewAdapter(getContext(), this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);
        callApiRequest();

        return view;
    }

    private void callApiRequest() {
        if (Network.isNetworkConnected((getContext()))) {
            Call<NewsRequestApi> call = GlobalApplication.getApiInterface().rankingPerCategoryRequest(nowDomain, 0);
            call.enqueue(new Callback<NewsRequestApi>() {
                @Override
                public void onResponse(@NonNull Call<NewsRequestApi> call, @NonNull Response<NewsRequestApi> response) {
                    if (response.body().getCode() == 1) {
                        list = response.body().getResult();
                        adapter.clearThenAdd(list);
                        if (GlobalApplication.getIndex() != 10) {
                            GlobalApplication.setIndex(GlobalApplication.getIndex() + 10);
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<NewsRequestApi> call, @NonNull Throwable t) {
                    Log.e("not receive", "warning message" + t.getMessage() + call.request());
                }
            });
        } else {
            Toast.makeText(getActivity(), "인터넷이 연결되어 있지 않습니다", Toast.LENGTH_LONG).show();
        }
    }
}

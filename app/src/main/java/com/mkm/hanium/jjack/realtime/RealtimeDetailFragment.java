package com.mkm.hanium.jjack.realtime;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
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
 *
 */

public class RealtimeDetailFragment extends BaseFragment {

    private RealtimeDetailRecyclerViewAdapter adapter;
    private List<NewsRequestApi.NewsRequestResultApi> list;
    private String item;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_realtime_detail, container, false);
        list = new ArrayList<>();

        Bundle bundle = getArguments();
        item = null;

        if (bundle != null) {
            item = bundle.getString("item");
        }

        TextView subtitle = (TextView) view.findViewById(R.id.realtime_detail_subtitle);
        subtitle.setText("[" + item + "] 검색 결과");
        GlobalApplication.setIndex(0);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_blank_realtime_detail);
        adapter = new RealtimeDetailRecyclerViewAdapter(getContext(), this, item);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

        callApiRequest();

        return view;
    }


    private void callApiRequest() {
        if (Network.isNetworkConnected((getContext()))) {
            Log.e("Index넣기직전", GlobalApplication.getIndex() + "");
            Call<NewsRequestApi> call = GlobalApplication.getApiInterface().realtimeDetailRequest(item, GlobalApplication.getIndex());
            call.enqueue(new Callback<NewsRequestApi>() {
                @Override
                public void onResponse(@NonNull Call<NewsRequestApi> call, @NonNull Response<NewsRequestApi> response) {
                    if (response.body().getCode() == 1) {
                        list = response.body().getResult();
                        adapter.add(list);
                        GlobalApplication.setIndex(GlobalApplication.getIndex() + 10);
                        Log.e("Index넣은후", GlobalApplication.getIndex() + "");
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
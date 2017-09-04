package com.mkm.hanium.jjack.realtime;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.BaseFragment;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.util.Network;
import com.mkm.hanium.jjack.request_api.RealtimeRequestApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 김민선 on 2017-05-09.
 *
 */

public class RealtimeFragment extends BaseFragment {

    public static RealtimeFragment newInstance() {
        return new RealtimeFragment();
    }

    private RealtimeRecyclerViewAdapter adapter;
    private List<RealtimeRequestApi.RealtimeRequestResultApi> list;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_realtime, container, false);

        list = new ArrayList<>();

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_blank_realtime);
        adapter = new RealtimeRecyclerViewAdapter(getActivity(), this);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

        callApiRequest();

        return view;
    }


    private void callApiRequest() {
        if (Network.isNetworkConnected((getContext()))) {
            Call<RealtimeRequestApi> call = GlobalApplication.getApiInterface().realtimeRequest();
            call.enqueue(new Callback<RealtimeRequestApi>() {
                @Override
                public void onResponse(@NonNull Call<RealtimeRequestApi> call, @NonNull Response<RealtimeRequestApi> response) {
                    if (response.body().getCode() == 1) {
                        list = response.body().getResult();
                        adapter.add(list);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<RealtimeRequestApi> call, @NonNull Throwable t) {

                }
            });
        } else {
            Toast.makeText(getActivity(), "인터넷이 연결되어 있지 않습니다", Toast.LENGTH_LONG).show();
        }
    }
}
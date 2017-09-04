package com.mkm.hanium.jjack.keyword;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.Toast;

import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.BaseFragment;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.request_api.KeywordRequestApi;
import com.mkm.hanium.jjack.util.DefaultApi;
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

public class KeywordSettingFragment extends BaseFragment {

    private KeywordSettingRecyclerViewAdapter adapter;
    private List<KeywordRequestApi.KeywordRequestResultApi> list;
    private EditText et_keyword_request;


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyword_setting, container, false);

        list = new ArrayList<>();

        et_keyword_request = (EditText) view.findViewById(R.id.et_keyword_submit);
        ImageButton btn_keyword_request = (ImageButton) view.findViewById(R.id.btn_keyword_setting_add);
        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_blank_keyword_setting);
        adapter = new KeywordSettingRecyclerViewAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(adapter);

        callApiRequest();
        btn_keyword_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String keyword = et_keyword_request.getText().toString();
                if (!keyword.equals("")) {
                    if (Network.isNetworkConnected((getContext()))) {
                        Call<DefaultApi> call = GlobalApplication.getApiInterface().keywordCreateRequest(427719221, keyword);
                        call.enqueue(new Callback<DefaultApi>() {
                            @Override
                            public void onResponse(@NonNull Call<DefaultApi> call, @NonNull Response<DefaultApi> response) {
                                if (response.body().getCode() == 1) {
                                    GlobalApplication.clearEntries();
                                    Call<KeywordRequestApi> call2 = GlobalApplication.getApiInterface().keywordRequest(427719221);
                                    call2.enqueue(new Callback<KeywordRequestApi>() {
                                        @Override
                                        public void onResponse(@NonNull Call<KeywordRequestApi> call, @NonNull Response<KeywordRequestApi> response) {
                                            if (response.body().getCode() == 1) {
                                                list = response.body().getKeywords();
                                                for (int i = 0; i < list.size(); i++) {
                                                    GlobalApplication.clearEntries();
                                                    GlobalApplication.addEntries(list.get(i).getKeywordName());
                                                    adapter.add(list);
                                                    Toast.makeText(getActivity(), keyword + "가 추가 되었습니다", Toast.LENGTH_SHORT).show();
                                                }
                                            }
                                        }

                                        @Override
                                        public void onFailure(@NonNull Call<KeywordRequestApi> call, @NonNull Throwable t) {
                                            Log.e("not receive", "warning message" + t.getMessage() + call.request());
                                        }
                                    });
                                }
                            }

                            @Override
                            public void onFailure(@NonNull Call<DefaultApi> call, @NonNull Throwable t) {
                                Log.e("fail", "fail");
                            }
                        });
                    } else {
                        Toast.makeText(getActivity(), "인터넷이 연결되어 있지 않습니다", Toast.LENGTH_LONG).show();
                    }
                }
            }
        });

        return view;
    }


    private void callApiRequest() {
        if (Network.isNetworkConnected((getContext()))) {
            Call<KeywordRequestApi> call = GlobalApplication.getApiInterface().keywordRequest(427719221);
            call.enqueue(new Callback<KeywordRequestApi>() {
                @Override
                public void onResponse(@NonNull Call<KeywordRequestApi> call, @NonNull Response<KeywordRequestApi> response) {
                    if (response.body().getCode() == 1) {
                        list = response.body().getKeywords();
                        adapter.add(list);
                    }
                }
                @Override
                public void onFailure(@NonNull Call<KeywordRequestApi> call, @NonNull Throwable t) {
                    Log.e("fail", "fail");
                }
            });
        } else {
            Toast.makeText(getActivity(), "인터넷이 연결되어 있지 않습니다", Toast.LENGTH_LONG).show();
        }
    }
}

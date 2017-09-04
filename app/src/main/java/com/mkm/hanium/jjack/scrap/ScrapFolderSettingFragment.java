package com.mkm.hanium.jjack.scrap;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.request_api.ScrapFolderRequestApi;
import com.mkm.hanium.jjack.request_api.ScrapFolderRequestResultApi;
import com.mkm.hanium.jjack.util.DefaultApi;
import com.mkm.hanium.jjack.util.Network;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 김민선 on 2017-05-09.
 * 스크랩 폴더를 설정하는 프래그먼트
 */

public class ScrapFolderSettingFragment extends Fragment {
    private RecyclerView recyclerView;
    private ScrapFolderSettingRecyclerViewAdapter adapter;
    private Button btn_folder_request;
    private List<ScrapFolderRequestResultApi> scrapFolderRequestResultApis;
    private EditText et_folder_request;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scrap_folder_setting_layout, container, false);

        scrapFolderRequestResultApis = new ArrayList<>();

        et_folder_request = (EditText) view.findViewById(R.id.et_scrapfoldersetting_folder);
        btn_folder_request= (Button) view.findViewById(R.id.btn_scrapfoldersetting_folderadd);
        recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_blank_scrapfoldersetting);
        adapter = new ScrapFolderSettingRecyclerViewAdapter(getActivity());

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());

        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.setLinearLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

        callApiRequest();
        btn_folder_request.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                final String folderName = et_folder_request.getText().toString();

                if (Network.isNetworkConnected((getContext()))) {
                    Call<DefaultApi> call = GlobalApplication.getApiInterface().scrapFolderCreateRequest(452135965, folderName);
                    call.enqueue(new Callback<DefaultApi>() {
                        @Override
                        public void onResponse(Call<DefaultApi> call, Response<DefaultApi> response) {
                            if(response.body().getCode() == 1) {
                                scrapFolderRequestResultApis.clear();
                                callApiRequest();
                                Toast.makeText(getActivity(),folderName+" 추가 되었습니다",Toast.LENGTH_SHORT).show();
                            } else {
                                Log.e("fail","fail");
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<DefaultApi> call, @NonNull Throwable t) {
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "인터넷이 연결되어 있지 않습니다", Toast.LENGTH_LONG).show();
                }

            }
        });

        return view;
    }


    private void callApiRequest() {
        if (Network.isNetworkConnected((getContext()))) {

            Call<ScrapFolderRequestApi> call = GlobalApplication.getApiInterface().scrapFolderRequest(452135965);
            call.enqueue(new Callback<ScrapFolderRequestApi>() {
                @Override
                public void onResponse(@NonNull Call<ScrapFolderRequestApi> call, @NonNull Response<ScrapFolderRequestApi> response) {
                     if(response.body().getCode()==1){
                         scrapFolderRequestResultApis = response.body().getResult();
                         adapter.add(scrapFolderRequestResultApis);
                     } else {
                         Log.e("fail","fail");
                     }
                }
                @Override
                public void onFailure(@NonNull Call<ScrapFolderRequestApi> call, @NonNull Throwable t) {
                    Log.e("fail","fail");
                }
            });
        } else {
            Toast.makeText(getActivity(), "인터넷이 연결되어 있지 않습니다", Toast.LENGTH_LONG).show();
        }
    }
}

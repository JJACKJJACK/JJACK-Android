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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.request_api.ScrapFolderRequestApi;
import com.mkm.hanium.jjack.request_api.ScrapFolderRequestResultApi;
import com.mkm.hanium.jjack.request_api.ScrapRequestApi;
import com.mkm.hanium.jjack.request_api.ScrapRequestResultApi;
import com.mkm.hanium.jjack.util.Network;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 김민선 on 2017-05-10.
 *
 */

public class ScrapFragment extends Fragment {

    public static ScrapFragment newInstance() {
        return new ScrapFragment();
    }

    private ScrapRecyclerViewAdapter adapter;
    //    private Button scrapFolderSetting;
    private List<ScrapRequestResultApi> scrapList;
    private List<ScrapFolderRequestResultApi> folderList;
    private ArrayAdapter<String> arrAdapt;
    private String nowFolder = "전체";


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_scrap_layout, container, false);
        scrapList = new ArrayList<>();

        GlobalApplication.setIndex(0);

        RecyclerView recyclerView = (RecyclerView) view.findViewById(R.id.recyclerview_blank_scrap);
        adapter = new ScrapRecyclerViewAdapter(getContext(), this);
        Spinner spinner = (Spinner) view.findViewById(R.id.spinner_scrap);
        // todo 버튼 행방 물어보자
//        scrapFolderSetting = (Button) view.findViewById(R.id.btn_folder_setting);

        ArrayList<String> items = new ArrayList<>(Arrays.asList("전체"));
        arrAdapt = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_item, items);

        // todo 데이터 교체
        // callFolderApiRequest();

        scrapList = new ArrayList<>();
        folderList = new ArrayList<>();

        folderList.add(new ScrapFolderRequestResultApi("Folder 1"));
        folderList.add(new ScrapFolderRequestResultApi("Folder 2"));


        arrAdapt.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        spinner.setAdapter(arrAdapt);

        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(linearLayoutManager);
        adapter.setLinearLayoutManager(linearLayoutManager);

        recyclerView.setAdapter(adapter);

        // todo 데이터 교체
        // callApirequest();
        scrapList.add(new ScrapRequestResultApi(
                3, "철문 열리자… 49년 만에 완전히 개방된 청와대 앞길",
                "http://news.donga.com/List/3/all/20170627/85071739/1",
                "http://dimg.donga.com/a/180/120/95/2/wps/NEWS/IMAGE/2017/06/27/85071738.1.jpg",
                "2017/08/08",
                "scrap title 1",
                "scrap content 1",
                "Folder 1", 101));
        scrapList.add(new ScrapRequestResultApi(
                3, "철문 열리자… 49년 만에 완전히 개방된 청와대 앞길",
                "http://news.donga.com/List/3/all/20170627/85071739/1",
                "http://dimg.donga.com/a/180/120/95/2/wps/NEWS/IMAGE/2017/06/27/85071738.1.jpg",
                "2017/08/08",
                "scrap title 2",
                "scrap content 2",
                "Folder 1", 102));
        scrapList.add(new ScrapRequestResultApi(
                3, "철문 열리자… 49년 만에 완전히 개방된 청와대 앞길",
                "http://news.donga.com/List/3/all/20170627/85071739/1",
                "http://dimg.donga.com/a/180/120/95/2/wps/NEWS/IMAGE/2017/06/27/85071738.1.jpg",
                "2017/08/08",
                "scrap title 2",
                "scrap content 2",
                "Folder 2", 103));
        scrapList.add(new ScrapRequestResultApi(
                3, "철문 열리자… 49년 만에 완전히 개방된 청와대 앞길",
                "http://news.donga.com/List/3/all/20170627/85071739/1",
                "http://dimg.donga.com/a/180/120/95/2/wps/NEWS/IMAGE/2017/06/27/85071738.1.jpg",
                "2017/08/08",
                "scrap title 2",
                "scrap content 2",
                "Folder 2", 104));

        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (!arrAdapt.getItem(position).equals(nowFolder)) {
                    GlobalApplication.setIndex(0);
                    nowFolder = arrAdapt.getItem(position);
                    adapter.setNowFolder(nowFolder);
                    folderList.clear();
                    callApirequest();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        return view;
    }


    private void callApirequest() {
        if (Network.isNetworkConnected((getContext()))) {
            Call<ScrapRequestApi> call = GlobalApplication.getApiInterface().scrapPerFolderRequest(452135965, nowFolder, GlobalApplication.getIndex());
            call.enqueue(new Callback<ScrapRequestApi>() {
                @Override
                public void onResponse(@NonNull Call<ScrapRequestApi> call, @NonNull Response<ScrapRequestApi> response) {
                    if (response.body().getCode() == 1) {
                        scrapList = response.body().getResult();
                        adapter.clearthenadd(scrapList);
                        GlobalApplication.setIndex(GlobalApplication.getIndex() + 10);
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ScrapRequestApi> call, @NonNull Throwable t) {
                    Log.e("not receive", "warning message" + t.getMessage() + call.request());
                }
            });

        } else {
            Toast.makeText(getActivity(), "인터넷이 연결되어 있지 않습니다", Toast.LENGTH_LONG).show();
        }
    }

    private void callFolderApiRequest() {
        if (Network.isNetworkConnected((getContext()))) {
            Call<ScrapFolderRequestApi> call = GlobalApplication.getApiInterface().scrapFolderRequest(452135965);
            call.enqueue(new Callback<ScrapFolderRequestApi>() {
                @Override
                public void onResponse(@NonNull Call<ScrapFolderRequestApi> call, @NonNull Response<ScrapFolderRequestApi> response) {
                    if (response.body().getCode() == 1) {
                        folderList = response.body().getResult();
                        if (!folderList.isEmpty()) {
                            for (int i = 0; i < folderList.size(); i++) {
                                arrAdapt.add(folderList.get(i).getFolderName());
                            }
                            arrAdapt.notifyDataSetChanged();
                        }
                    }
                }

                @Override
                public void onFailure(@NonNull Call<ScrapFolderRequestApi> call, @NonNull Throwable t) {

                }
            });
        } else {
            Toast.makeText(getActivity(), "인터넷이 연결되어 있지 않습니다", Toast.LENGTH_LONG).show();
        }
    }

    public void refreshFolder() {
        nowFolder = "전체";
        GlobalApplication.setIndex(0);
        folderList.clear();
        arrAdapt.clear();
        arrAdapt.add("전체");
        callFolderApiRequest();
        callApirequest();
    }
}



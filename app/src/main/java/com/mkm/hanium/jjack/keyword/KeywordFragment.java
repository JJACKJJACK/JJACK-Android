
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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.BaseFragment;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.request_api.NewsRequestApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 김민선 on 2017-05-10.
 *
 */


public class KeywordFragment extends BaseFragment {

    public static KeywordFragment newInstance() {
        return new KeywordFragment();
    }

    private RecyclerView recyclerView;
    private KeywordRecyclerViewAdapter adapter;
    private ArrayAdapter<String> arrAdapt;
    private List<String> keywordList;
    private List<NewsRequestApi.NewsRequestResultApi> newsList;
    private Spinner spinner;
    private ImageButton keywordSetting;
    private TextView noKeyword;
    boolean flag = false;

    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_keyword, container, false);
        GlobalApplication.setIndex(0);
        spinner = (Spinner) view.findViewById(R.id.spinner_keyword);
        keywordSetting = (ImageButton) view.findViewById(R.id.btn_keyword_setting);

        arrAdapt = new ArrayAdapter<> (
                getContext(),
                R.layout.simple_dropdown_item_1line,
                GlobalApplication.getEntries());
        newsList = new ArrayList<>();
        keywordList = GlobalApplication.getEntries();
        recyclerView = (RecyclerView) view.findViewById(R.id.recycler_view_blank_keyword);
        noKeyword = (TextView) view.findViewById(R.id.tv_no_keyword);

        if (GlobalApplication.getEntries().isEmpty()) {
            arrAdapt.add(" ");
            arrAdapt.notifyDataSetChanged();
            spinner.setSelection(0);
            spinner.setAdapter(arrAdapt);
            recyclerView.setVisibility(View.GONE);
            noKeyword.setVisibility(View.VISIBLE);
        } else {
            GlobalApplication.setNowKeyword(GlobalApplication.getEntries().get(0));
            spinner.setSelection(0);
            spinner.setAdapter(arrAdapt);

            noKeyword.setVisibility(View.GONE);
            recyclerView.setVisibility(View.VISIBLE);
        }


        spinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    GlobalApplication.setNowKeyword(keywordList.get(position));
                    adapter.keywordClick();
                    GlobalApplication.setIndex(0);
                    if(!keywordList.get(position).equals(" "))
                        callKeywordApiRequest();
                } catch (NullPointerException e) {
                    Log.e("keyword", "err");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                spinner.setSelection(0);
            }
        });

        adapter = new KeywordRecyclerViewAdapter(getContext(), this);

        recyclerView.setAdapter(adapter);
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        recyclerView.setLayoutManager(linearLayoutManager);

        keywordSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                KeywordSettingFragment target = new KeywordSettingFragment();
                getFragmentManager()
                        .beginTransaction().replace(R.id.fragmentComponentLayout, target, getClass().getSimpleName())
                        .addToBackStack(null)
                        .commit();
            }
        });

        return view;
    }


    private void callKeywordApiRequest() {
        if (!flag) {
            flag = true;
            if (GlobalApplication.getIndex() == 0) {
                Call<NewsRequestApi> call = GlobalApplication.getApiInterface().keywordNewRequest(GlobalApplication.getNowKeyword(), GlobalApplication.getIndex());
                call.enqueue(new Callback<NewsRequestApi>() {
                    @Override
                    public void onResponse(@NonNull Call<NewsRequestApi> call, @NonNull Response<NewsRequestApi> response) {
                        if (response.body().getCode() == 1) {
                            noKeyword.setVisibility(View.GONE);
                            recyclerView.setVisibility(View.VISIBLE);
                            newsList = response.body().getResult();
                            adapter.add(newsList);
                            GlobalApplication.setIndex(GlobalApplication.getIndex() + 10);
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
        flag = false;
    }
}
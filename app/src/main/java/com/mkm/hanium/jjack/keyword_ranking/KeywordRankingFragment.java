package com.mkm.hanium.jjack.keyword_ranking;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;

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
 * 키워드 랭킹 프래그먼트를 정의하는 클래스
 */

public class KeywordRankingFragment extends BindFragment<FragmentKeywordRankingBinding>
        implements AdapterView.OnItemSelectedListener {

    public static KeywordRankingFragment newInstance() {
        return new KeywordRankingFragment();
    }

    private List<KeywordRankingItem> list = new ArrayList<>();
    private KeywordRankingAdapter adapter;
    private boolean itemChk[];

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_keyword_ranking;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        list = new ArrayList<>();

        itemChk = new boolean[2];
        binding.setFragment(this);
        setRecyclerView();
        loadData();

        setSpinner(binding.keywordRankingSubtitle.spinnerKeywordRankingGeneration);
        setSpinner(binding.keywordRankingSubtitle.spinnerKeywordRankingGender);

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
            public void onResponse(@NonNull Call<KeywordRankingRequestApi> call, @NonNull Response<KeywordRankingRequestApi> response) {
                if (response.body().getCode() == 1) {
                    list.addAll(response.body().getResult());
                    adapter.updateItems(list);
                    Log.d(TAG, "Keyword Ranking Data Load");
                } else {
                    Log.d(TAG, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(@NonNull Call<KeywordRankingRequestApi> call, @NonNull Throwable t) {
                Log.e(TAG, "Not Connected to server :\n" + t.getMessage() + call.request());
            }
        });
    }

    private void setSpinner(Spinner spinner) {
        ArrayList<String> list = new ArrayList<>();

        setSpinnerItem(list, spinner);

        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(),
                R.layout.simple_dropdown_item_1line,
                list);
        spinner.setSelection(0);
        spinner.setAdapter(adapter);
        spinner.setOnItemSelectedListener(this);
    }

    private void setSpinnerItem(ArrayList<String> list, Spinner spinner) {
        if(spinner == binding.keywordRankingSubtitle.spinnerKeywordRankingGeneration) {
            list.add("세대");

            for (int i = 10; i <= 60; i += 10)
                list.add(Integer.toString(i) + "대");

        } else if (spinner == binding.keywordRankingSubtitle.spinnerKeywordRankingGender) {
            list.add("성별");
            list.add("남성");
            list.add("여성");
        }
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        int selectedSpinner = -1;

        if(parent.getId() == R.id.spinner_keyword_ranking_generation)
            selectedSpinner = 0;
        else if(parent.getId() == R.id.spinner_keyword_ranking_gender)
            selectedSpinner = 1;

        itemChk[selectedSpinner] = !(position == 0 && selectedSpinner != -1);

        // todo 두 스피너 모두 정상적인 값이 선택되면 해당 데이터를 로드한다.
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }
}
package com.mkm.hanium.jjack.ranking;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.mkm.hanium.jjack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MIN on 2017-05-20.
 */

public class KeywordRankingFragment extends Fragment {

    private List<RankingItem> mList;
    private RankingAdapter mAdapter;
    private RecyclerView mRecyclerView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("KeywordRankingFragment", "onCreate()");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("KeywordRankingFragment", "onCreateView()");
        View layout = inflater.inflate(R.layout.fragment_keywordranking, container, false);

//        mList = new ArrayList<>();
//
//        initData();
//
//        mAdapter = new RankingAdapter(getActivity(), mList);
//        mRecyclerView = (RecyclerView) layout.findViewById(R.id.recycler_view_ranking);
//        mRecyclerView.setAdapter(mAdapter);
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        LineChart chart = (LineChart) layout.findViewById(R.id.chart_keyword_ranking);
        ArrayList<String> labels = new ArrayList<>();
        labels.add("A");
        labels.add("B");
        labels.add("C");
        labels.add("D");
        labels.add("E");
        labels.add("F");
        labels.add("G");

        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(5f, 4));
        entries.add(new Entry(9f, 5));

        LineDataSet dataSet = new LineDataSet(entries, "Test");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setDrawFilled(true); //선아래로 색상표시
        dataSet.setDrawValues(false);

        LineData lineData = new LineData(labels, dataSet);
        chart.setData(lineData);
        chart.invalidate();

        return layout;
    }

    public void initData() {
        /**
         * todo Test data -> DB data
         */
        for(int i = 1; i <= 20; i++)
            mList.add(new RankingItem(i, "하하"));
    }
}
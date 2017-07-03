package com.mkm.hanium.jjack.keyword_ranking;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
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

/**
 * Created by MIN on 2017-07-03.
 */

public class PopupChartDialogFragment extends DialogFragment {

    private LineChart chart;
    private ArrayList<String> labels;
    private ArrayList<Entry> entries;
    private LineDataSet dataSet;
    private LineData lineData;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_popupwindow_keyword_ranking_chart, container, false);

        chart = (LineChart) view.findViewById(R.id.chart_keyword_ranking);
        labels = new ArrayList<>();
        entries = new ArrayList<>();

        inputTestData();

        dataSet = new LineDataSet(entries, "Test");
        dataSet.setColors(ColorTemplate.MATERIAL_COLORS);
        dataSet.setDrawFilled(true); //선아래로 색상표시
        dataSet.setDrawValues(false);

        lineData = new LineData(labels, dataSet);
        chart.setData(lineData);
        chart.invalidate();

        return view;
    }

    private void inputTestData() {
        labels.add("A");
        labels.add("B");
        labels.add("C");
        labels.add("D");
        labels.add("E");
        labels.add("F");
        labels.add("G");

        entries.add(new Entry(4f, 0));
        entries.add(new Entry(8f, 1));
        entries.add(new Entry(6f, 2));
        entries.add(new Entry(2f, 3));
        entries.add(new Entry(5f, 4));
        entries.add(new Entry(9f, 5));
    }
}
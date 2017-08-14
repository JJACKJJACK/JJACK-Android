package com.mkm.hanium.jjack.keyword_ranking;

import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.utils.ColorTemplate;
import com.github.mikephil.charting.utils.ViewPortHandler;
import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.databinding.LayoutPopupwindowKeywordRankingChartBinding;
import com.mkm.hanium.jjack.util.KeywordRankingSearchLogRequestApi;

import java.text.DecimalFormat;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by MIN on 2017-07-03.
 */

public class PopupChartDialogFragment extends DialogFragment {

    // KeywordRankingAdapter.onClick에서 호출됨

    private String TAG = this.getClass().getSimpleName();
    private LayoutPopupwindowKeywordRankingChartBinding binding;

    private ArrayList<String> labels; // x축에 표시될 이름
    private ArrayList<Entry> entries; // y축(데이터)
    private LineDataSet lineDataSet;
    private LineData lineData;
    private String keyword;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.layout_popupwindow_keyword_ranking_chart, container, false);

        keyword = getArguments().getString("keyword");
        labels = new ArrayList<>();
        entries = new ArrayList<>();

        loadData();

        return binding.getRoot();
    }

    private void inputTestData() {
        String day[] = {"07/26", "07/27", "07/28", "07/29", "07/30"};
        int value[] = {8, 6, 2, 5, 9};

        for(int i = 0; i < 5; i++) {
            labels.add(day[i]);
            entries.add(new Entry(value[i], i));
        }
    }

    private void loadData() {
        Call<KeywordRankingSearchLogRequestApi> call =
                GlobalApplication.getApiInterface().keywordRankingSearchLogRequest(keyword);
        call.enqueue(new Callback<KeywordRankingSearchLogRequestApi>() {
            @Override
            public void onResponse(Call<KeywordRankingSearchLogRequestApi> call, Response<KeywordRankingSearchLogRequestApi> response) {
                if (response.body().getCode() == 1) {
                    Log.d(TAG, "(" + keyword + ") search log data load");
                    ArrayList<KeywordRankingSearchLogRequestApi.SearchLogItem> list = response.body().getResult();
                    for(int i = list.size() - 1; i > 0; i--) {
                        // 받아온 데이터를 월/일 형식으로 변환
                        String date = list.get(i).getDate().substring(5, 7)
                                + "/" + list.get(i).getDate().substring(8, 10);

                        labels.add(date);
                        entries.add(new Entry(list.get(i).getHits(), list.size() - 1 - i ));

                        Log.e(TAG, date + " " + list.get(i).getHits());
                    }

                    setGraph();

                } else {
                    Log.d(TAG, response.body().getMessage());
                }
            }

            @Override
            public void onFailure(Call<KeywordRankingSearchLogRequestApi> call, Throwable t) {
                Log.e(TAG, "Not Connected to server :\n" + t.getMessage() + call.request());
            }
        });
    }

    private void setGraph() {
        lineDataSet = new LineDataSet(entries, "검색량");

        lineDataSet.setDrawFilled(true); // 선아래로 색상표시
//        lineDataSet.setDrawValues(false); //

        lineDataSet.setAxisDependency(YAxis.AxisDependency.LEFT);
        lineDataSet.setColor(ColorTemplate.getHoloBlue());
        lineDataSet.setCircleColor(ColorTemplate.getHoloBlue());
        lineDataSet.setLineWidth(2f);
        lineDataSet.setCircleRadius(3f);
        lineDataSet.setFillAlpha(65);
        lineDataSet.setFillColor(ColorTemplate.getHoloBlue());
//        lineDataSet.setHighLightColor(Color.rgb(244, 117, 117));
        lineDataSet.setDrawCircleHole(false);

        XAxis xAxis = binding.chartKeywordRanking.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setTextSize(10f);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);

        // data has AxisDependency.LEFT
        YAxis left = binding.chartKeywordRanking.getAxisLeft();
        left.setDrawLabels(false); // no axis labels
        left.setDrawAxisLine(false); // no axis line
        left.setDrawGridLines(false); // no grid lines
        left.setDrawZeroLine(true); // draw a zero line
        binding.chartKeywordRanking.getAxisRight().setEnabled(false); // no right axis

        Legend legend = binding.chartKeywordRanking.getLegend();
        legend.setEnabled(false);

        lineData = new LineData(labels, lineDataSet);
        binding.chartKeywordRanking.setData(lineData);
        lineData.setValueTextSize(14f);
        lineData.setValueFormatter(new ValueFormatter() {
            @Override
            public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                DecimalFormat decimalFormat = new DecimalFormat("0");
                return decimalFormat.format(value);
            }
        });
        binding.chartKeywordRanking.setDescription("");
        binding.chartKeywordRanking.invalidate();
    }
}
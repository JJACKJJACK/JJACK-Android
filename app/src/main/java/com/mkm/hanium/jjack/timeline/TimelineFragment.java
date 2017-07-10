package com.mkm.hanium.jjack.timeline;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.baoyachi.stepview.VerticalStepView;
import com.mkm.hanium.jjack.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MIN on 2017-07-04.
 */

public class TimelineFragment extends Fragment {

    private VerticalStepView timeline;
    private List<String> list;
    private Context context;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Log.d("TimelineFragment", "onCreate()");
    }

    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("TimelineFragment", "onCreateView()");
        View layout = inflater.inflate(R.layout.fragment_timeline, container, false);

        timeline = (VerticalStepView) layout.findViewById(R.id.step_view_timeline);
        list = new ArrayList<>();
        context = getActivity();
        
        inputData();

        timeline.setStepsViewIndicatorComplectingPosition(list.size() - 2)//设置完成的步数
                .reverseDraw(false) // default is true
                .setStepViewTexts(list) // 总步骤
                .setLinePaddingProportion(0.85f) // 设置indicator线与线间距的比例系数
                .setStepsViewIndicatorCompletedLineColor(ContextCompat.getColor(context, android.R.color.white)) // 设置StepsViewIndicator完成线的颜色
                .setStepsViewIndicatorUnCompletedLineColor(ContextCompat.getColor(context, R.color.uncompleted_text_color)) // 设置StepsViewIndicator未完成线的颜色
                .setStepViewComplectedTextColor(ContextCompat.getColor(context, android.R.color.white)) // 设置StepsView text完成线的颜色
                .setStepViewUnComplectedTextColor(ContextCompat.getColor(context, R.color.uncompleted_text_color)) // 设置StepsView text未完成线的颜色
                .setStepsViewIndicatorCompleteIcon(ContextCompat.getDrawable(context, R.drawable.complted)) // 设置StepsViewIndicator CompleteIcon
                .setStepsViewIndicatorDefaultIcon(ContextCompat.getDrawable(context, R.drawable.default_icon)) // 设置StepsViewIndicator DefaultIcon
                .setStepsViewIndicatorAttentionIcon(ContextCompat.getDrawable(context, R.drawable.attention)); // 设置StepsViewIndicator AttentionIcon

        return layout;
    }

    private void inputData() {
        list.add("Test data 1");
        list.add("Test data 2");
        list.add("Test data 3");
        list.add("Test data 4");
        list.add("Test data 5");
    }
}

package com.mkm.hanium.jjack.timeline;

import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.BindFragment;
import com.mkm.hanium.jjack.databinding.FragmentTimelineBinding;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by MIN on 2017-07-04.
 */

public class TimelineFragment extends BindFragment<FragmentTimelineBinding> {

    public static TimelineFragment newInstance() {
        return new TimelineFragment();
    }

    private List<TimelineItem> list;
    private TimelineAdapter adapter;

    @Override
    protected int getLayoutId() {
        return R.layout.fragment_timeline;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);

        binding.setFragment(this);
        inputData();
        setRecyclerView();

        return binding.getRoot();
    }

    private void inputData() {
        list = new ArrayList<>();

        list.add(new TimelineHeaderItem(TimelineAdapter.TYPE_HEADER, "Timeline Header Item"));
        list.add(new TimelineContentItem(TimelineAdapter.TYPE_CONTENT, "2017/07/12", "Timeline Content Item 1"));
        list.add(new TimelineContentItem(TimelineAdapter.TYPE_CONTENT, "2017/07/15", "Timeline Content Item 2"));
        list.add(new TimelineContentItem(TimelineAdapter.TYPE_CONTENT, "2017/07/18", "Timeline Content Item 3"));
        list.add(new TimelineContentItem(TimelineAdapter.TYPE_CONTENT, "2017/07/20", "Timeline Content Item 4"));
        list.add(new TimelineContentItem(TimelineAdapter.TYPE_CONTENT, "2017/07/23", "Timeline Content Item 5"));
        list.add(new TimelineHeaderItem(TimelineAdapter.TYPE_FOOTER, "Timeline Footer Item"));
    }

    private void setRecyclerView() {
        adapter = new TimelineAdapter(getActivity(), list);
        binding.recyclerViewTimeline.setAdapter(adapter);
        binding.recyclerViewTimeline.setLayoutManager(new LinearLayoutManager(getActivity()));
    }
}

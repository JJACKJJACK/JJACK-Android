package com.mkm.hanium.jjack.timeline;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mkm.hanium.jjack.databinding.ItemTimelineContentBinding;

/**
 * Created by MIN on 2017-07-23.
 */

public class TimelineContentViewHolder extends RecyclerView.ViewHolder implements TimelineViewHolderBinder{

    protected ItemTimelineContentBinding binding;

    public TimelineContentViewHolder(View itemView, int viewType) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        binding.timelineMarkerContent.initLine(viewType);
    }

    @Override
    public void bind(TimelineAdapter adapter, TimelineItem item) {
        binding.setAdapter(adapter);
        binding.setItem((TimelineContentItem)item);
    }
}
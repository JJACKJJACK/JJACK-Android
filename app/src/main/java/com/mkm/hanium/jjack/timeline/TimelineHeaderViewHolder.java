package com.mkm.hanium.jjack.timeline;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mkm.hanium.jjack.databinding.ItemTimelineHeaderBinding;

/**
 * Created by MIN on 2017-07-29.
 */

public class TimelineHeaderViewHolder extends RecyclerView.ViewHolder implements TimelineViewHolderBinder {

    protected ItemTimelineHeaderBinding binding;

    public TimelineHeaderViewHolder(View itemView, int viewType) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        binding.timelineMarkerHeader.initLine(viewType);
    }

    @Override
    public void bind(TimelineAdapter adapter, TimelineItem item) {
        binding.setAdapter(adapter);
        binding.setItem((TimelineHeaderItem)item);
    }
}

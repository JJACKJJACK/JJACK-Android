package com.mkm.hanium.jjack.timeline;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mkm.hanium.jjack.databinding.ItemTimelineHeaderBinding;

/**
 * Created by MIN on 2017-07-29.
 * 타임라인 헤더 아이템 뷰홀더
 */

class TimelineHeaderViewHolder extends RecyclerView.ViewHolder implements TimelineViewHolderBinder {

    private ItemTimelineHeaderBinding binding;

    TimelineHeaderViewHolder(View itemView, int viewType) {
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

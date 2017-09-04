package com.mkm.hanium.jjack.timeline;

import android.content.Context;
import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mkm.hanium.jjack.databinding.ItemTimelineContentBinding;
import com.squareup.picasso.Picasso;

/**
 * Created by MIN on 2017-07-23.
 * 타임라인의 내용 아이템 뷰홀더
 */

class TimelineContentViewHolder extends RecyclerView.ViewHolder implements TimelineViewHolderBinder {

    protected ItemTimelineContentBinding binding;
    private Context context;

    TimelineContentViewHolder(View itemView, int viewType, Context context) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
        binding.timelineMarkerContent.initLine(viewType);
        this.context = context;
    }

    @Override
    public void bind(TimelineAdapter adapter, TimelineItem item) {
        binding.setAdapter(adapter);
        binding.setItem((TimelineContentItem)item);

        if(((TimelineContentItem) item).getImageUrl() != null)
            Picasso.with(context)
                    .load(((TimelineContentItem) item)
                            .getImageUrl()).into(binding.imageViewTimelineContent);
    }
}
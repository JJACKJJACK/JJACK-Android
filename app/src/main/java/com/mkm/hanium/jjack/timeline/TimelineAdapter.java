package com.mkm.hanium.jjack.timeline;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mkm.hanium.jjack.R;

import java.util.List;

/**
 * Created by MIN on 2017-07-23.
 * 타임라인 리사이클러뷰 어댑터
 */

public class TimelineAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    /**
     * Timeline viewType(LineType)
     * public static final int NORMAL = 0; (위아래 선)
     * public static final int BEGIN = 1;  (아래방향 단일 선)
     * public static final int END = 2;    (위방향 단일 선)
     * public static final int ONLYONE = 3;
     */
    static final int TYPE_HEADER = 1;
    static final int TYPE_CONTENT = 0;
    static final int TYPE_FOOTER = 2;

    private Context context;
    private List<TimelineItem> items;

    TimelineAdapter(Context context, List<TimelineItem> items) {
        this.context = context;
        this.items = items;
    }

    public void updateItems(List<TimelineItem> items) {
        this.items = items;
        this.notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        // HEADER와 FOOTER는 같은 레이아웃을 공유하지만, 레이아웃 왼쪽의 원과 결합된 선의 방향이 다르다.
        if(viewType == TYPE_HEADER || viewType == TYPE_FOOTER) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_timeline_header, parent, false);
            return new TimelineHeaderViewHolder(view, viewType);
        } else if (viewType == TYPE_CONTENT) {
            View view = LayoutInflater.from(context).inflate(R.layout.item_timeline_content, parent, false);
            return new TimelineContentViewHolder(view, viewType);
        }

        return null;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        TimelineItem item = items.get(position);

        // position 변수로 가져온 각각의 아이템을 연결한다.
        ((TimelineViewHolderBinder) holder).bind(this, item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public int getItemViewType(int position) {
        return items.get(position).getViewType();
    }
}
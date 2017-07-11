package com.mkm.hanium.jjack.keyword_ranking;

import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.mkm.hanium.jjack.R;

/**
 * Created by MIN on 2017-06-24.
 */

public class RankingViewHolder extends RecyclerView.ViewHolder {
    protected LinearLayout linearLayout;
    protected TextView ranking, name, count;

    public RankingViewHolder(View itemView) {
        super(itemView);
        linearLayout = (LinearLayout) itemView.findViewById(R.id.layout_ranking_item);
        ranking = (TextView) itemView.findViewById(R.id.tv_ranking);
        name = (TextView) itemView.findViewById(R.id.tv_rank_item_name);
        count = (TextView) itemView.findViewById(R.id.tv_rank_count);
    }
}

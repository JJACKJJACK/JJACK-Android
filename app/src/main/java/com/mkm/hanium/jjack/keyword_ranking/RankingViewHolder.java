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
    protected LinearLayout mLinearLayout;
    protected TextView mRank, mName;

    public RankingViewHolder(View itemView) {
        super(itemView);
        mLinearLayout = (LinearLayout) itemView.findViewById(R.id.layout_ranking_item);
        mRank = (TextView) itemView.findViewById(R.id.tv_rank);
        mName = (TextView) itemView.findViewById(R.id.tv_rank_item_name);
    }
}

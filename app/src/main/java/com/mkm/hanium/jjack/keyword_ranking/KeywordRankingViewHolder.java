package com.mkm.hanium.jjack.keyword_ranking;

import android.databinding.DataBindingUtil;
import android.support.v7.widget.RecyclerView;
import android.view.View;

import com.mkm.hanium.jjack.databinding.ItemKeywordRankingBinding;

/**
 * Created by MIN on 2017-06-24.
 */

public class KeywordRankingViewHolder extends RecyclerView.ViewHolder {

    protected ItemKeywordRankingBinding binding;

    public KeywordRankingViewHolder(View itemView) {
        super(itemView);
        binding = DataBindingUtil.bind(itemView);
    }
}
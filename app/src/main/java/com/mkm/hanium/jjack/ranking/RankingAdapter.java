package com.mkm.hanium.jjack.ranking;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mkm.hanium.jjack.R;

import java.util.List;

/**
 * Created by MIN on 2017-06-24.
 */

public class RankingAdapter extends RecyclerView.Adapter<RankingViewHolder> {

    private Context context;
    private List<RankingItem> mItems;

    public RankingAdapter(Context context, List<RankingItem> items) {
        this.context = context;
        mItems = items;
    }

    @Override
    public RankingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_ranking, parent, false);
        RankingViewHolder vh = new RankingViewHolder(v);
        return vh;
    }

    @Override
    public void onBindViewHolder(RankingViewHolder holder, int position) {
        RankingItem item = mItems.get(position);
        holder.mRank.setText(String.valueOf(item.getRank()));
        holder.mName.setText(item.getName());
//        holder.mLinearLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//            }
//        });
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }
}

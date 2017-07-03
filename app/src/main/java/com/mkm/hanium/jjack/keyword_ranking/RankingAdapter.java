package com.mkm.hanium.jjack.keyword_ranking;

import android.content.Context;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.FragmentActivity;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mkm.hanium.jjack.R;

import java.util.List;

/**
 * Created by MIN on 2017-06-24.
 */

public class RankingAdapter extends RecyclerView.Adapter<RankingViewHolder>
    implements View.OnClickListener {

    private Context context;
    private List<RankingItem> mItems;

    public RankingAdapter(Context context, List<RankingItem> items) {
        this.context = context;
        mItems = items;
    }

    @Override
    public RankingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_ranking, parent, false);
        return new RankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RankingViewHolder holder, int position) {
        RankingItem item = mItems.get(position);
        holder.mRank.setText(String.valueOf(item.getRank()));
        holder.mName.setText(item.getName());
        holder.mLinearLayout.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return mItems.size();
    }

    @Override
    public void onClick(View v) {
        /**
         * 키워드 랭킹을 터치하면 최근 검색 관련 통계가 출력된다.
         */
        PopupChartDialogFragment popupChart = new PopupChartDialogFragment();
        popupChart.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        popupChart.show(((FragmentActivity)context).getSupportFragmentManager(), "PopupChartDialogFragment");
    }
}

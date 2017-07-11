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

public class KeywordRankingAdapter extends RecyclerView.Adapter<RankingViewHolder>
    implements View.OnClickListener {

    private Context context;
    private List<KeywordRankingItem> items;

    public KeywordRankingAdapter(Context context, List<KeywordRankingItem> items) {
        this.context = context;
        this.items = items;
    }

    public void addList(List<KeywordRankingItem> list) {
        items = list;
        this.notifyDataSetChanged();
    }

    @Override
    public RankingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_keyword_ranking, parent, false);
        return new RankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RankingViewHolder holder, int position) {
        KeywordRankingItem item = items.get(position);
        holder.ranking.setText(String.valueOf(item.getRanking()));
        holder.name.setText(item.getKeywordName());
        holder.count.setText(String.valueOf(item.getCount()));
        holder.linearLayout.setOnClickListener(this);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    @Override
    public void onClick(View v) {
        // 키워드 랭킹을 터치하면 최근 검색 관련 통계가 출력된다.
        PopupChartDialogFragment popupChart = new PopupChartDialogFragment();
        popupChart.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        popupChart.show(((FragmentActivity)context).getSupportFragmentManager(), "PopupChartDialogFragment");
    }
}
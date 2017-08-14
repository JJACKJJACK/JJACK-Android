package com.mkm.hanium.jjack.keyword_ranking;

import android.content.Context;
import android.os.Bundle;
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

public class KeywordRankingAdapter extends RecyclerView.Adapter<KeywordRankingViewHolder> {

    private Context context;
    private List<KeywordRankingItem> items;
    private RecyclerView recyclerView;

    public KeywordRankingAdapter(Context context, List<KeywordRankingItem> items, RecyclerView recyclerView) {
        this.context = context;
        this.items = items;
        this.recyclerView = recyclerView;
    }

    public void updateItems(List<KeywordRankingItem> list) {
        items = list;
        this.notifyDataSetChanged();
    }

    @Override
    public KeywordRankingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.item_keyword_ranking, parent, false);
        return new KeywordRankingViewHolder(view);
    }

    @Override
    public void onBindViewHolder(KeywordRankingViewHolder holder, int position) {
        holder.binding.setAdapter(this);
        KeywordRankingItem item = items.get(position);

        // item_keyword_ranking.xml의 <variable>item과 연결한다.
        // variable에 name값을 name으로 준다면 binding.setName이 자동으로 생성된다. 여기서는 item으로 주었다.
        // 뷰와 아이템 클래스가 연결되어 xml에 간단히 명시하는 것으로 각 아이템이 출력된다.
        // 텍스트뷰의 text="@{item.xx}가 그 예시이다. xx는 아이템을 저장한 클래스의 멤버 변수이다.
        // @{ } 내부에는 String값을 넣어준다. Integer.ToString()이나 +, - 등 연산도 사용할 수 있다.
        holder.binding.setItem(item);
    }

    @Override
    public int getItemCount() {
        return items.size();
    }

    public void onClick(View v) {
        // 키워드 랭킹을 터치하면 최근 검색 관련 통계가 출력된다.
        int position = recyclerView.getChildLayoutPosition(v);

        PopupChartDialogFragment popupChart = new PopupChartDialogFragment();
        Bundle bundle = new Bundle(1); // 번들에 저장할 갯수 1개
        bundle.putString("keyword", items.get(position).getKeywordName());
        popupChart.setStyle(DialogFragment.STYLE_NO_TITLE, 0);
        popupChart.setArguments(bundle);
        popupChart.show(((FragmentActivity) context).getSupportFragmentManager(), "PopupChartDialogFragment");
    }
}
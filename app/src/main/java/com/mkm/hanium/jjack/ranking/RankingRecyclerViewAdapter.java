package com.mkm.hanium.jjack.ranking;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.balysv.materialripple.MaterialRippleLayout;
import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.common.NewsFragment;
import com.mkm.hanium.jjack.request_api.NewsRequestApi;
import com.mkm.hanium.jjack.util.Network;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 김민선 on 2017-05-09.
 * 뉴스 랭킹 리사이클러뷰를 정의하는 어댑터
 */

class RankingRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Fragment fragment;
    private List<NewsRequestApi.NewsRequestResultApi> list;
    private String nowDomain = "정치";


    void setNowDomain(String nowDomain) {
        this.nowDomain = nowDomain;
    }

    RankingRecyclerViewAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        list = new ArrayList<>();

    }
    
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        if (GlobalApplication.getIndex() - 3 == position) {
            if (Network.isNetworkConnected((context))) {
                Call<NewsRequestApi> call = GlobalApplication.getApiInterface().rankingPerCategoryRequest(nowDomain, GlobalApplication.getIndex());
                call.enqueue(new Callback<NewsRequestApi>() {
                    @Override
                    public void onResponse(@NonNull Call<NewsRequestApi> call, @NonNull Response<NewsRequestApi> response) {
                        if (response.body().getCode() == 1) {
                            list.addAll(response.body().getResult());
                            notifyItemRangeInserted(GlobalApplication.getIndex(), GlobalApplication.getIndex() + 10);
                            GlobalApplication.setIndex(GlobalApplication.getIndex() + 10);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<NewsRequestApi> call, @NonNull Throwable t) {
                        Log.e("not receive", "warning message" + t.getMessage() + call.request());
                    }

                });
            } else {
                Toast.makeText(context, "인터넷이 연결되어 있지 않습니다", Toast.LENGTH_LONG).show();
            }
        }

//        ((RecyclerViewHolder) holder).newsItemLayout.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                NewsFragment target = new NewsFragment();
//                Bundle bundle = new Bundle();
//                bundle.putString("url", list.get(position).getArticleURL());
//
//                bundle.putString("articleID", String.valueOf(list.get(position).getArticleID()));
//                target.setArguments(bundle);
//                fragment.getActivity().getSupportFragmentManager()
//                        .beginTransaction().add(R.id.fragmentComponentLayout, target, "news")
//                        .addToBackStack(null)
//                        .commit();
//            }
//        });

        if (!list.get(position).getImage().equals("")) {
            Picasso.with(context)
                    .load(list.get(position).getImage())
                    .fit()
                    .into(((RecyclerViewHolder) holder).newsItemImage);
        } else {
            Picasso.with(context)
                    .load(R.drawable.no_image)
                    .fit()
                    .into(((RecyclerViewHolder) holder).newsItemImage);
        }

        ((RecyclerViewHolder) holder).newsItemCompany.setText(list.get(position).getCompany());
        ((RecyclerViewHolder) holder).newsItemTitle.setText(list.get(position).getArticleTitle());
        ((RecyclerViewHolder) holder).newsItemReporter.setText(list.get(position).getReporter());
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        return new RecyclerViewHolder(
                MaterialRippleLayout.on(inflater.inflate(R.layout.item_news, parent, false))
                        .rippleOverlay(true)
                        .rippleAlpha(0.2f)
                        .rippleColor(ContextCompat.getColor(context, R.color.color2))
                        .rippleHover(true)
                        .create()
        );
    }

    void clearThenAdd(List<NewsRequestApi.NewsRequestResultApi> list) {
        this.list.clear();
        this.list.addAll(list);

        this.notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return list.size();
    }

    private class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        LinearLayout newsItemLayout;
        RelativeLayout cardLayout;
        ImageView newsItemImage;
        TextView newsItemCompany;
        TextView newsItemTitle;
        TextView newsItemReporter;

        RecyclerViewHolder(View itemView) {
            super(itemView);
            cardLayout = (RelativeLayout) itemView.findViewById(R.id.card_layout);
            newsItemLayout = (LinearLayout) itemView.findViewById(R.id.layout_news_item);
            newsItemImage = (ImageView) itemView.findViewById(R.id.image_view_news_item);
            newsItemCompany = (TextView) itemView.findViewById(R.id.tv_news_item_company);
            newsItemTitle = (TextView) itemView.findViewById(R.id.tv_news_item_title);
            newsItemReporter = (TextView) itemView.findViewById(R.id.tv_news_item_reporter);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            NewsFragment target = new NewsFragment();
            Bundle bundle = new Bundle();
            bundle.putString("url", list.get(getAdapterPosition()).getArticleURL());

            bundle.putString("articleID", String.valueOf(list.get(getAdapterPosition()).getArticleID()));
            target.setArguments(bundle);
            fragment.getActivity().getSupportFragmentManager()
                    .beginTransaction().add(R.id.fragmentComponentLayout, target, "news")
                    .addToBackStack(null)
                    .commit();
        }
    }
}
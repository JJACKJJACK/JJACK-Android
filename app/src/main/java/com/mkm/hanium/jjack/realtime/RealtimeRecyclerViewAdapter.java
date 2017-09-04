package com.mkm.hanium.jjack.realtime;

import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.balysv.materialripple.MaterialRippleLayout;
import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.request_api.RealtimeRequestApi;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by 김민선 on 2017-05-09.
 * 
 */

class RealtimeRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Fragment fragment;
    private List<RealtimeRequestApi.RealtimeRequestResultApi> list;


    RealtimeRecyclerViewAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        list = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, int position) {

        int rank = list.get(position).getRank();
        final String item = list.get(position).getItem();

        ((RealtimeRecyclerViewHolder) holder).realtimeRanking.setText(String.valueOf(rank));
        ((RealtimeRecyclerViewHolder) holder).realtimeItem.setText(item);
        ((RealtimeRecyclerViewHolder) holder).realtimeItemLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                RealtimeDetailFragment target = new RealtimeDetailFragment();
                Bundle bundle = new Bundle();
                bundle.putString("item", item);
                target.setArguments(bundle);
                fragment.getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.fragmentComponentLayout, target, "news list")
                        .addToBackStack(null)
                        .commit();
            }
        });

        if(rank == 1)
            ((RealtimeRecyclerViewHolder) holder).best.setVisibility(View.VISIBLE);
        else
            ((RealtimeRecyclerViewHolder) holder).best.setVisibility(View.INVISIBLE);
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new RealtimeRecyclerViewHolder(
                MaterialRippleLayout.on(LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_realtime, parent, false))
                        .rippleOverlay(true)
                        .rippleAlpha(0.2f)
                        .rippleColor(ContextCompat.getColor(context, R.color.color2))
                        .rippleHover(true)
                        .create()
        );
    }

    public void add(List<RealtimeRequestApi.RealtimeRequestResultApi> list) {
        this.list.addAll(list);
        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    private class RealtimeRecyclerViewHolder extends RecyclerView.ViewHolder {

        private RelativeLayout realtimeItemLayout;
        private TextView best;
        private TextView realtimeRanking;
        private TextView realtimeItem;

        RealtimeRecyclerViewHolder(View itemView) {

            super(itemView);
            realtimeItemLayout = (RelativeLayout) itemView.findViewById(R.id.layout_realtime_item);
            best = (TextView) itemView.findViewById(R.id.tv_realtime_best);
            realtimeRanking = (TextView) itemView.findViewById(R.id.tv_realtime_ranking);
            realtimeItem = (TextView) itemView.findViewById(R.id.tv_realtime_item);
        }
    }
}
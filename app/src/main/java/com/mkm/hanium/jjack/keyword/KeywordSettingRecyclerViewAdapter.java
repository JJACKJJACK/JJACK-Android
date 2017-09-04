package com.mkm.hanium.jjack.keyword;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.request_api.KeywordRequestApi;
import com.mkm.hanium.jjack.util.DefaultApi;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by 김민선 on 2017-05-09.
 *
 */

class KeywordSettingRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private List<KeywordRequestApi.KeywordRequestResultApi> list;

    KeywordSettingRecyclerViewAdapter() {
        list = new ArrayList<>();
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((KeywordSettingRecyclerViewHolder) holder).keyword.setText(list.get(position).getKeywordName());
        ((KeywordSettingRecyclerViewHolder) holder).button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<DefaultApi> call = GlobalApplication.getApiInterface().keywordDeleteRequest(427719221, list.get(position).getKeywordName());
                call.enqueue(new Callback<DefaultApi>() {
                    @Override
                    public void onResponse(@NonNull Call<DefaultApi> call, @NonNull Response<DefaultApi> response) {
                        if (response.body().getCode() == 1) {
                            GlobalApplication.deleteKeyword(position);
                            list.remove(position);
                            notifyItemRemoved(position);
                        }
                    }
                    @Override
                    public void onFailure(@NonNull Call<DefaultApi> call, @NonNull Throwable t) {

                    }
                });
            }
        });
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new KeywordSettingRecyclerViewHolder(
                LayoutInflater.from(parent.getContext())
                        .inflate(R.layout.item_setting, parent, false));
    }

    public void add(List<KeywordRequestApi.KeywordRequestResultApi> list) {
        this.list.clear();
        this.list.addAll(list);

        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    private class KeywordSettingRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView keyword;
        ImageButton button;

        KeywordSettingRecyclerViewHolder(View itemView) {
            super(itemView);
            keyword = (TextView) itemView.findViewById(R.id.tv_item_name);
            button = (ImageButton) itemView.findViewById(R.id.btn_item_delete);
        }
    }
}
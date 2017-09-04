package com.mkm.hanium.jjack.scrap;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.request_api.ScrapFolderRequestResultApi;
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

class ScrapFolderSettingRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private LinearLayoutManager LinearLayoutManager;
    private List<ScrapFolderRequestResultApi> list;

    ScrapFolderSettingRecyclerViewAdapter(Context context) {
        this.context = context;
        list = new ArrayList<>();

    }

    void setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.LinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
        ((ScrapFolderSettingRecyclerViewHolder) holder).folder.setText(list.get(position).getFolderName());
        ((ScrapFolderSettingRecyclerViewHolder) holder).button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Call<DefaultApi> call = GlobalApplication.getApiInterface().scrapFolderDeleteRequest(452135965, list.get(position).getFolderName());
                call.enqueue(new Callback<DefaultApi>() {
                    @Override
                    public void onResponse(@NonNull Call<DefaultApi> call, @NonNull Response<DefaultApi> response) {
                        if (response.body().getCode() == 1) {
                            list.remove(position);
                            notifyItemRemoved(position);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<DefaultApi> call, @NonNull Throwable t) {

                    }
                });
             /*   keywordRequestResultApis.remove(position);
                notifyItemRemoved(position);*/

            }
        });
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScrapFolderSettingRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_setting, parent, false));
    }

    public void add(List<ScrapFolderRequestResultApi> list) {
        this.list.clear();
        this.list.addAll(list);

        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    private class ScrapFolderSettingRecyclerViewHolder extends RecyclerView.ViewHolder {
        TextView folder;
        Button button;

        ScrapFolderSettingRecyclerViewHolder(View itemView) {
            super(itemView);
            folder = (TextView) itemView.findViewById(R.id.tv_item_name);
            button = (Button) itemView.findViewById(R.id.btn_item_delete);
        }

    }
}
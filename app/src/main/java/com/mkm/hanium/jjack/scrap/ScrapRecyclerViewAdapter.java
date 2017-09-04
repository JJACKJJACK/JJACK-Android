package com.mkm.hanium.jjack.scrap;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.common.NewsFragment;
import com.mkm.hanium.jjack.request_api.ScrapRequestApi;
import com.mkm.hanium.jjack.request_api.ScrapRequestResultApi;
import com.mkm.hanium.jjack.util.DefaultApi;
import com.mkm.hanium.jjack.util.Network;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


/**
 * Created by 김민선 on 2017-05-09.
 *
 */

class ScrapRecyclerViewAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private Context context;
    private Fragment fragment;
    private LinearLayoutManager LinearLayoutManager;
    private List<ScrapRequestResultApi> list;

    private String nowFolder = "전체";

    void setNowFolder(String nowFolder) {
        this.nowFolder = nowFolder;
    }



    ScrapRecyclerViewAdapter(Context context, Fragment fragment) {
        this.context = context;
        this.fragment = fragment;
        list = new ArrayList<>();

    }

    void setLinearLayoutManager(LinearLayoutManager linearLayoutManager) {
        this.LinearLayoutManager = linearLayoutManager;
    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int position) {
      if (GlobalApplication.getIndex() - 5 == position) {

            if (Network.isNetworkConnected(context)) {

                Call<ScrapRequestApi> call = GlobalApplication.getApiInterface().scrapPerFolderRequest(452135965,nowFolder, GlobalApplication.getIndex());
                call.enqueue(new Callback<ScrapRequestApi>() {
                    @Override
                    public void onResponse(@NonNull Call<ScrapRequestApi> call, @NonNull Response<ScrapRequestApi> response) {
                        if (response.body().getCode() == 1) {
                            list.addAll(response.body().getResult());
                            notifyItemRangeInserted(GlobalApplication.getIndex(), GlobalApplication.getIndex() + 10);
                            GlobalApplication.setIndex(GlobalApplication.getIndex() + 10);
                        }
                    }

                    @Override
                    public void onFailure(@NonNull Call<ScrapRequestApi> call, @NonNull Throwable t) {
                        Log.e("not receive", "warning message" + t.getMessage() + call.request());
                    }
                });


            } else {


            }
        }
        Log.e("scrapIDfirst", "" + list.get(position).getScrapID());
        ((ScrapRecyclerViewHolder)holder).scrap_time.setText(list.get(position).getScrapDate());
        ((ScrapRecyclerViewHolder)holder).scrap_news_title.setText(list.get(position).getArticleTitle());
        Log.e("content",list.get(position).getScrapContent());
        ((ScrapRecyclerViewHolder)holder).scrap_content.setText(list.get(position).getScrapContent());
        ((ScrapRecyclerViewHolder)holder).scrap_title.setText(list.get(position).getScrapTitle());
        ((ScrapRecyclerViewHolder)holder).scrap_modify.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ModifyScrap modifyScrap = new ModifyScrap();
                Bundle bundle = new Bundle();

                bundle.putInt("ScrapID",list.get(position).getScrapID());
                bundle.putString("title",list.get(position).getScrapTitle());
                bundle.putString("content",list.get(position).getScrapContent());
                bundle.putString("folderName",list.get(position).getFolderName());
                modifyScrap.setArguments(bundle);
                fragment.getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.fragmentComponentLayout,modifyScrap)
                        .addToBackStack(null)
                        .commit();

            }
        });
        ((ScrapRecyclerViewHolder)holder).scrap_delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Network.isNetworkConnected((context))) {
                    Call<DefaultApi> call = GlobalApplication.getApiInterface().scrapDeleteRequest(452135965, list.get(position).getScrapID());
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
                        Log.e("fail","fail");
                        }
                    });

                } else {
                    Toast.makeText(context, "인터넷이 연결되어 있지 않습니다", Toast.LENGTH_LONG).show();
                }
            }
            });






        if (!list.get(position).getImage().equals("")) {

            Picasso.with(context)
                    .load(list.get(position).getImage())
                    .fit()
                    .into(((ScrapRecyclerViewHolder) holder).scrap_news_image);


        } else {
            Picasso.with(context)
                    .load(R.drawable.no_image)
                    .into(((ScrapRecyclerViewHolder) holder).scrap_news_image);
        }

        ((ScrapRecyclerViewHolder) holder).scrap_news_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsFragment newsFragment = new NewsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", list.get(position).getArticleURL());
                bundle.putString("articleID", String.valueOf(list.get(position).getArticleID()));
                newsFragment.setArguments(bundle);
                fragment.getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.fragmentComponentLayout, newsFragment)
                        .addToBackStack(null)
                        .commit();
            }
        });
        ((ScrapRecyclerViewHolder) holder).scrap_news_title.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                NewsFragment newsFragment = new NewsFragment();
                Bundle bundle = new Bundle();
                bundle.putString("url", list.get(position).getArticleURL());
                bundle.putString("articleID", String.valueOf(list.get(position).getArticleID()));
                newsFragment.setArguments(bundle);
                fragment.getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.fragmentComponentLayout, newsFragment)
                        .addToBackStack(null)
                        .commit();

            }
        });
    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ScrapRecyclerViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_scrap, parent, false));
    }

    public void add(List<ScrapRequestResultApi> list) {
        //newsRequestResultApis.clear();
        this.list.addAll(list);

        this.notifyDataSetChanged();
    }

    public void clearthenadd(List<ScrapRequestResultApi> list) {
        this.list.clear();
        this.list.addAll(list);

        this.notifyDataSetChanged();
    }


    @Override
    public int getItemCount() {
        return list.size();
    }

    class ScrapRecyclerViewHolder extends RecyclerView.ViewHolder {

        TextView scrap_time;
        ImageView scrap_news_image;
        TextView scrap_content;
        TextView scrap_title;
        TextView scrap_news_title;
        Button scrap_modify;
        Button scrap_delete;

        public ScrapRecyclerViewHolder(View itemView) {
            super(itemView);
            scrap_news_image = (ImageView) itemView.findViewById(R.id.image_view_scrap_news_image);
            scrap_news_title = (TextView) itemView.findViewById(R.id.tv_scap_news_title);
            scrap_time = (TextView) itemView.findViewById(R.id.tv_scrap_news_time);
            scrap_content= (TextView) itemView.findViewById(R.id.tv_myscrapcardview_scrap_content);
            scrap_title= (TextView) itemView.findViewById(R.id.tv_myscrapcardview_scrap_title);
            scrap_modify = (Button) itemView.findViewById(R.id.btn_scrap_modify);
            scrap_delete = (Button) itemView.findViewById(R.id.btn_scrap_delete);
        }
    }
}
package com.mkm.hanium.jjack.scrap;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.util.DefaultApi;
import com.mkm.hanium.jjack.util.Network;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by minsun on 2017-07-16.
 * 
 */

public class ModifyScrap extends Fragment {
    private EditText title;
    private EditText content;
    private Button button;
    private String titleString;
    private String contentString;
    private String beforeTitle;
    private String beforeContent;
    private String beforeFolderName;
    private int scrapID;
    private String folderName;
    private Fragment fragment = this;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_register_scrap, container, false);
        title = (EditText) view.findViewById(R.id.et_scrap_title);
        content = (EditText) view.findViewById(R.id.et_scrap_content);
        button = (Button) view.findViewById(R.id.btn_scrap_send);

        Bundle bundle = getArguments();

        if (bundle != null) {
            scrapID = bundle.getInt("ScrapID");
            beforeTitle = bundle.getString("title");
            beforeContent = bundle.getString("content");
            beforeFolderName = bundle.getString("folderName");
        }
        
        title.setHint(beforeTitle);
        content.setHint(beforeContent);
        Log.e("scrapId", String.valueOf(scrapID));
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleString = title.getText().toString();
                contentString = content.getText().toString();
                if (titleString.isEmpty()) {
                    titleString = beforeTitle;
                }
                if (contentString.isEmpty()) {
                    contentString = beforeContent;
                }
                folderName = beforeFolderName;

                if (Network.isNetworkConnected((getContext()))) {
                    folderName = "기본";
                    Call<DefaultApi> call = GlobalApplication.getApiInterface().scrapModifyRequest(452135965, scrapID, titleString, contentString, folderName);

                    call.enqueue(new Callback<DefaultApi>() {
                        @Override
                        public void onResponse(@NonNull Call<DefaultApi> call, @NonNull Response<DefaultApi> response) {
                            if (response.body().getCode() == 1) {
                                ScrapFragment target = new ScrapFragment();
                                fragment.getActivity().getSupportFragmentManager()
                                        .beginTransaction().replace(R.id.fragmentComponentLayout, target)
                                        .addToBackStack(null)
                                        .commit();
                            }
                        }

                        @Override
                        public void onFailure(@NonNull Call<DefaultApi> call, @NonNull Throwable t) {
                            Log.e("click", "onFailure");
                        }
                    });
                } else {
                    Toast.makeText(getActivity(), "인터넷이 연결되어 있지 않습니다", Toast.LENGTH_LONG).show();
                }
            }

        });
        return view;
    }
}

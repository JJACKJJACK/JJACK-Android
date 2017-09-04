package com.mkm.hanium.jjack.scrap;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.common.GlobalApplication;
import com.mkm.hanium.jjack.request_api.ScrapFolderRequestApi;
import com.mkm.hanium.jjack.request_api.ScrapFolderRequestResultApi;
import com.mkm.hanium.jjack.util.DefaultApi;
import com.mkm.hanium.jjack.util.Network;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by minsun on 2017-07-16.
 * 
 */

public class RegisterScrap extends Fragment {

    private EditText title;
    private EditText content;
    private Button button;
    private String titleString;
    private String contentString;
    private String articleID;
    private String folderName;
    private Fragment fragment = this;
    private Button folderbutton;
    private List<ScrapFolderRequestResultApi> scrapFolderRequestResultApis;
    private ArrayList<String> scrapFolder;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.layout_register_scrap, container, false);
        title = (EditText) view.findViewById(R.id.et_scrap_title);
        content = (EditText) view.findViewById(R.id.et_scrap_content);
        button = (Button) view.findViewById(R.id.btn_scrap_send);
        folderbutton = (Button) view.findViewById(R.id.btn_folder_setting);
        Bundle bundle = getArguments();
        final Spinner s = (Spinner) view.findViewById(R.id.spinner_scrap);

        scrapFolder = new ArrayList<>();
        folderrequest();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                getContext(),
                android.R.layout.simple_dropdown_item_1line,
                scrapFolder);

        //adapter.notifyDataSetChanged();

        adapter.setDropDownViewResource(android.R.layout.simple_dropdown_item_1line);
        s.setSelection(0);
        s.setAdapter(adapter);

        if (bundle != null) {
            articleID = bundle.getString("articleID");
        }
        folderbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ScrapFolderSettingFragment target = new ScrapFolderSettingFragment();

                fragment.getActivity().getSupportFragmentManager()
                        .beginTransaction().replace(R.id.fragmentComponentLayout, target)
                        .addToBackStack(null)
                        .commit();
            }
        });


        s.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    folderName = scrapFolder.get(position);
                } catch (NullPointerException e) {
                    Log.e("keyword", "err");
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
                s.setSelection(0);
            }
        });
        
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleString = title.getText().toString();
                contentString = content.getText().toString();

                if (Network.isNetworkConnected((getContext()))) {
                    folderName = "기본";
                    Call<DefaultApi> call = GlobalApplication.getApiInterface().scrapCreateRequest(452135965, articleID, titleString, contentString, folderName);

                    call.enqueue(new Callback<DefaultApi>() {
                        @Override
                        public void onResponse(@NonNull Call<DefaultApi> call,@NonNull Response<DefaultApi> response) {
                            if (response.body().getCode() == 1) {
                                ScrapFragment target = new ScrapFragment();
                                /*Bundle bundle = new Bundle();
                                newsFragment.setArguments(bundle);*/
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

    public void folderrequest() {
        Call<ScrapFolderRequestApi> call = GlobalApplication.getApiInterface().scrapFolderRequest(452135965);
        call.enqueue(new Callback<ScrapFolderRequestApi>() {
            @Override
            public void onResponse(@NonNull Call<ScrapFolderRequestApi> call, @NonNull Response<ScrapFolderRequestApi> response) {
                if (response.body().getCode() == 1) {
                    scrapFolderRequestResultApis = response.body().getResult();
                    for (int i = 0; i < scrapFolderRequestResultApis.size(); i++) {
                        scrapFolder.add(scrapFolderRequestResultApis.get(i).getFolderName());
                    }
                }
            }

            @Override
            public void onFailure(@NonNull Call<ScrapFolderRequestApi> call, @NonNull Throwable t) {
                Log.e("click", "onFailure");
            }
        });
    }
}

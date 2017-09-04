
package com.mkm.hanium.jjack.common;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;

import com.kakao.kakaolink.KakaoLink;
import com.kakao.kakaolink.KakaoTalkLinkMessageBuilder;
import com.kakao.util.KakaoParameterException;
import com.mkm.hanium.jjack.R;
import com.mkm.hanium.jjack.scrap.RegisterScrap;

// Created by minsun on 2017-07-15.

public class NewsFragment extends BaseFragment {
    private Fragment fragment;
    private String articleID;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        super.onCreateView(inflater, container, savedInstanceState);
        View view = inflater.inflate(R.layout.layout_webview, container, false);

        fragment = this;

        WebView webView = (WebView) view.findViewById(R.id.webview);

        webView.setWebViewClient(new WebViewClient());
        WebSettings webSettings = webView.getSettings();

        Bundle bundle = getArguments();
        String url = null;

        if(bundle != null) {
            url = bundle.getString("url");
            articleID=bundle.getString("articleID");
        }

        webSettings.setJavaScriptEnabled(true);
        webView.loadUrl(url);

        setHasOptionsMenu(true);

        return view;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if(id == R.id.menu_scrap) {
            RegisterScrap target = new RegisterScrap();
            Bundle bundle = new Bundle();
            bundle.putString("articleID", articleID);
            target.setArguments(bundle);
            fragment.getActivity().getSupportFragmentManager()
                    .beginTransaction().replace(R.id.fragmentComponentLayout, target, "newsItem")
                    .addToBackStack(null)
                    .commit();
        } else if (id == R.id.action_kakaolink) {
            sendKakaoLink();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // 비워두지 않으면 메뉴가 두 번 생성됨.

        String tag = "";

        try {
            Fragment fragment = getFragmentManager().findFragmentById(R.id.fragmentComponentLayout);
            tag = fragment.getTag();
        } catch (NullPointerException e) {
            e.printStackTrace();
        }

        if(tag.equals("issue news")) {
            super.onCreateOptionsMenu(menu, inflater);
        }
    }

    /**
     * 카카오 링크의 템플릿 버튼을 이용하려면 도메인 등록이 필요하다.
     * 모든 언론사를 도메인으로 등록할 수 없기 때문에 사진과 text 형식으로 변경하였음.
     */
    public void sendKakaoLink() {
        try {
            final KakaoLink link = KakaoLink.getKakaoLink(getActivity());
            final KakaoTalkLinkMessageBuilder builder = link.createKakaoTalkLinkMessageBuilder();

            String title = "청년이 ‘죄송’하지 않을때까지";
            String content = "신문 취재엔 응했지만 내심 ‘악플’을 걱정했다. 어려운 사람을 보고도...";
            String imgUrl = "http://dimg.donga.com/a/180/120/95/2/wps/NEWS/IMAGE/2017/06/27/85071743.1.jpg";
            String url = "http://news.donga.com/List/3/all/20170627/85071744/1";

            builder.addImage(imgUrl, 300, 200)
                    .addText(title + "\n\n"
                            + content + "\n\n"
                            + "자세히 보기 >> \n"
                            + url)
                    .build();

            /*메시지 발송*/
            link.sendMessage(builder, getActivity());
        } catch (KakaoParameterException e) {
            e.printStackTrace();
        }
    }
}


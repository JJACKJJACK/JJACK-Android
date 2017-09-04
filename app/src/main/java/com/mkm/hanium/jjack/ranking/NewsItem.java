package com.mkm.hanium.jjack.ranking;

/**
 * Created by 김민선 on 2017-05-10.
 * 뉴스 아이템을 저장하는 클래스
 */

public class NewsItem {
    private String newsItemText;
    private String newsItemImagePath;

    public String getNewsItemText() {
        return newsItemText;
    }
    public void setNewsItemText(String newsItemText) {
        this.newsItemText = newsItemText;
    }

    public String getNewsItemImagePath() {
        return newsItemImagePath;
    }
    public void setNewsItemImagePath(String newsItemImagePath) {
        this.newsItemImagePath = newsItemImagePath;
    }
}

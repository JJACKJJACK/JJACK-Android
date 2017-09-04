package com.mkm.hanium.jjack.timeline;

/**
 * Created by MIN on 2017-07-15.
 * 타임라인의 내용을 저장하는 클래스
 */

public class TimelineContentItem extends TimelineItem {
    private String date;
    private String content;
    private String imageUrl;

    TimelineContentItem(int viewType, String date, String content, String imageUrl) {
        super(viewType);
        this.date = date;
        this.content = content;
        this.imageUrl = imageUrl;
    }

    public String getContent() {
        return content;
    }
    public void setContent(String content) {
        this.content = content;
    }

    public String getDate() {
        return date;
    }
    public void setDate(String date) {
        this.date = date;
    }

    public String getImageUrl() {
        return imageUrl;
    }
    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }
}

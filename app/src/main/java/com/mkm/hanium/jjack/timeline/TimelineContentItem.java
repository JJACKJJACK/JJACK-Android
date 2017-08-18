package com.mkm.hanium.jjack.timeline;

/**
 * Created by MIN on 2017-07-15.
 * 타임라인의 내용을 저장하는 클래스
 */

public class TimelineContentItem extends TimelineItem {
    private String date;
    private String content;

    TimelineContentItem(int viewType, String date, String content) {
        super(viewType);
        this.date = date;
        this.content = content;
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
}

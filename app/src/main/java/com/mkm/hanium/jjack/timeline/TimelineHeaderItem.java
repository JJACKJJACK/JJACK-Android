package com.mkm.hanium.jjack.timeline;

/**
 * Created by MIN on 2017-07-29.
 * 타임라인의 헤더 아이템은 주제만 갖는다.
 */

public class TimelineHeaderItem extends TimelineItem {
    private String topic;

    TimelineHeaderItem(int viewType, String topic) {
        super(viewType);
        this.topic = topic;
    }

    public String getTopic() {
        return topic;
    }
    public void setTopic(String topic) {
        this.topic = topic;
    }
}

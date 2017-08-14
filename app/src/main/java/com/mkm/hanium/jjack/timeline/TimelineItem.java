package com.mkm.hanium.jjack.timeline;

/**
 * Created by MIN on 2017-07-29.
 */

public class TimelineItem {
    int viewType;

    public TimelineItem(int viewType) {
        this.viewType = viewType;
    }

    public int getViewType() {
        return viewType;
    }
    public void setViewType(int viewType) {
        this.viewType = viewType;
    }
}

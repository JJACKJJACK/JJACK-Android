package com.mkm.hanium.jjack.timeline;

/**
 * Created by MIN on 2017-07-29.
 * 타임라인의 아이템 뷰 타입을 저장할 클래스
 */

class TimelineItem {
    private int viewType;

    TimelineItem(int viewType) {
        this.viewType = viewType;
    }

    int getViewType() {
        return viewType;
    }
    void setViewType(int viewType) {
        this.viewType = viewType;
    }
}

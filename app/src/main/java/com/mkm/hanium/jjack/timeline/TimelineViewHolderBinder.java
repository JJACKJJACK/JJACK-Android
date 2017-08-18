package com.mkm.hanium.jjack.timeline;

/**
 * Created by MIN on 2017-07-29.\
 * 타입마다 바인딩 할 요소들을 정의해야 하는 인터페이스
 */

interface TimelineViewHolderBinder {
    void bind(TimelineAdapter adapter, TimelineItem item);
}

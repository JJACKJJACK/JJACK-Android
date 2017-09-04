package com.mkm.hanium.jjack.request_api;

import com.mkm.hanium.jjack.util.DefaultApi;

import java.util.List;

/**
 * Created by minsun on 2017-07-24.
 *
 */

public class RealtimeRequestApi extends DefaultApi {

    private List<RealtimeRequestResultApi> result;

    public List<RealtimeRequestResultApi> getResult() {
        return result;
    }
    public void setResult(List<RealtimeRequestResultApi> result) {
        this.result = result;
    }

    public class RealtimeRequestResultApi {
        int rank;
        String item;

        public int getRank() {
            return rank;
        }
        public void setRank(int rank) {
            this.rank = rank;
        }

        public String getItem() {
            return item;
        }
        public void setItem(String item) {
            this.item = item;
        }
    }
}

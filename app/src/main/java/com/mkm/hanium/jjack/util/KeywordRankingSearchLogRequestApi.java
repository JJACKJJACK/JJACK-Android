package com.mkm.hanium.jjack.util;

import java.util.ArrayList;

/**
 * Created by MIN on 2017-08-04.
 */

public class KeywordRankingSearchLogRequestApi extends DefaultApi {

    private ArrayList<SearchLogItem> result;

    public ArrayList getResult() {
        return result;
    }

    public void setResult(ArrayList<SearchLogItem> result) {
        this.result = result;
    }

    public class SearchLogItem {
        private String date;
        private int hits;

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public int getHits() {
            return hits;
        }

        public void setHits(int hits) {
            this.hits = hits;
        }
    }
}

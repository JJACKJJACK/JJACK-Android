package com.mkm.hanium.jjack.request_api;

import com.mkm.hanium.jjack.util.DefaultApi;

import java.util.ArrayList;

/**
 * Created by MIN on 2017-08-04.
 * 키워드 검색 기록 아이템들을 저장하는 클래스
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

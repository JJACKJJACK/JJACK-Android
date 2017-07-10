package com.mkm.hanium.jjack.util;

import java.util.ArrayList;

/**
 * Created by MIN on 2017-07-09.
 */

public class KeywordRankingApi extends DefaultApi {
    private ArrayList<KeywordRankingElement> result;

    public ArrayList<KeywordRankingElement> getResult() { return result; }
    public void setResult(ArrayList<KeywordRankingElement> result) { this.result = result; }

    class KeywordRankingElement {
        private String keywordName;
        private int count;

        public String getKeywordName() {
            return keywordName;
        }

        public void setKeywordName(String keywordName) {
            this.keywordName = keywordName;
        }

        public int getCount() {
            return count;
        }

        public void setCount(int count) {
            this.count = count;
        }
    }
}

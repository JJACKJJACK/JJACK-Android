package com.mkm.hanium.jjack.keyword_ranking;

/**
 * Created by MIN on 2017-06-24.
 */

public class KeywordRankingItem {

    private int keywordID;
    private int ranking;
    private String keywordName;
    private int count;

    public KeywordRankingItem(int keywordID, int ranking, String keywordName, int count) {
        this.keywordID = keywordID;
        this.ranking = ranking;
        this.keywordName = keywordName;
        this.count = count;
    }

    public int getKeywordID() {
        return keywordID;
    }
    public void setKeywordID(int keywordId) {
        this.keywordID = keywordId;
    }

    public int getRanking() {
        return ranking;
    }
    public void setRanking(int ranking) {
        this.ranking = ranking;
    }

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

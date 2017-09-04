package com.mkm.hanium.jjack.request_api;

import com.mkm.hanium.jjack.keyword_ranking.KeywordRankingItem;
import com.mkm.hanium.jjack.util.DefaultApi;

import java.util.ArrayList;

/**
 * Created by MIN on 2017-07-09.
 *
 */

public class KeywordRankingRequestApi extends DefaultApi {

    private ArrayList<KeywordRankingItem> result;

    public ArrayList<KeywordRankingItem> getResult() { return result; }
    public void setResult(ArrayList<KeywordRankingItem> result) { this.result = result; }
}
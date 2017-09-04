package com.mkm.hanium.jjack.request_api;

import com.mkm.hanium.jjack.util.DefaultApi;

import java.util.List;

/**
 * Created by minsun on 2017-07-11.
 *
 */

public class KeywordRequestApi extends DefaultApi {
    private List<KeywordRequestResultApi> keywords;

    public List<KeywordRequestResultApi> getKeywords() {
        return keywords;
    }
    public void setKeywords(List<KeywordRequestResultApi> keywords) {
        this.keywords = keywords;
    }

    public class KeywordRequestResultApi {
        String keywordName;

        public String getKeywordName() {
            return keywordName;
        }
        public void setKeywordName(String keywordName) {
            this.keywordName = keywordName;
        }
    }
}

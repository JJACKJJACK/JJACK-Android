package com.mkm.hanium.jjack.request_api;

import com.mkm.hanium.jjack.util.DefaultApi;

import java.util.List;

/**
 * Created by minsun on 2017-07-11.
 *
 */

public class ScrapFolderRequestApi extends DefaultApi {
    private List<ScrapFolderRequestResultApi> result;

    public List<ScrapFolderRequestResultApi> getResult() {
        return result;
    }

    public void setResult(List<ScrapFolderRequestResultApi> result) {
        this.result = result;
    }
}

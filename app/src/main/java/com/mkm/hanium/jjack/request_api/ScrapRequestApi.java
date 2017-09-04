package com.mkm.hanium.jjack.request_api;

import com.mkm.hanium.jjack.util.DefaultApi;

import java.util.List;

/**
 * Created by 김민선 on 2017-05-21.
 *
 */

public class ScrapRequestApi extends DefaultApi {
    private List<ScrapRequestResultApi> result;

    public List<ScrapRequestResultApi> getResult() {
        return result;
    }
    public void setResult(List<ScrapRequestResultApi> result) {
        this.result = result;
    }
}

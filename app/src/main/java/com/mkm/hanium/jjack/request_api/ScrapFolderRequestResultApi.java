package com.mkm.hanium.jjack.request_api;

/**
 * Created by MIN on 2017-08-26.
 *
 */

public class ScrapFolderRequestResultApi {
    private String folderName;

    public ScrapFolderRequestResultApi(String folderName) {
        this.folderName = folderName;
    }

    public String getFolderName() {
        return folderName;
    }
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }
}
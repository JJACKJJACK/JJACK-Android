package com.mkm.hanium.jjack.request_api;

/**
 * Created by MIN on 2017-08-26.
 *
 */

public class ScrapRequestResultApi {
    private int articleID;
    private String articleTitle;
    private String articleURL;
    private String image;
    private String scrapDate;
    private String scrapTitle;
    private String scrapContent;
    private String folderName;
    private int scrapID;

    public ScrapRequestResultApi(int articleID,
                                 String articleTitle,
                                 String articleURL,
                                 String image,
                                 String scrapDate,
                                 String scrapTitle,
                                 String scrapContent,
                                 String folderName,
                                 int scrapID) {
        this.articleID = articleID;
        this.articleTitle = articleTitle;
        this.articleURL = articleURL;
        this.image = image;
        this.scrapDate = scrapDate;
        this.scrapTitle = scrapTitle;
        this.scrapContent = scrapContent;
        this.folderName = folderName;
        this.scrapID = scrapID;
    }

    public int getArticleID() {
        return articleID;
    }
    public void setArticleID(int articleID) {
        this.articleID = articleID;
    }

    public String getArticleTitle() {
        return articleTitle;
    }
    public void setArticleTitle(String articleTitle) {
        this.articleTitle = articleTitle;
    }

    public String getArticleURL() {
        return articleURL;
    }
    public void setArticleURL(String articleURL) {
        this.articleURL = articleURL;
    }

    public String getImage() {
        return image;
    }
    public void setImage(String image) {
        this.image = image;
    }

    public String getScrapDate() {
        return scrapDate;
    }
    public void setScrapDate(String scrapDate) {
        this.scrapDate = scrapDate;
    }

    public String getScrapTitle() {
        return scrapTitle;
    }
    public void setScrapTitle(String scrapTitle) {
        this.scrapTitle = scrapTitle;
    }

    public String getScrapContent() {
        return scrapContent;
    }
    public void setScrapContent(String scrapContent) {
        this.scrapContent = scrapContent;
    }

    public String getFolderName() {
        return folderName;
    }
    public void setFolderName(String folderName) {
        this.folderName = folderName;
    }

    public int getScrapID() {
        return scrapID;
    }
    public void setScrapID(int scrapID) {
        this.scrapID = scrapID;
    }
}
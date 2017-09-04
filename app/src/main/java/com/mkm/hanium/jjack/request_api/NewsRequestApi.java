package com.mkm.hanium.jjack.request_api;

import com.mkm.hanium.jjack.util.DefaultApi;

import java.util.List;

/**
 * Created by 김민선 on 2017-05-21.
 *
 */

public class NewsRequestApi extends DefaultApi {
    private List<NewsRequestResultApi> result;

    public List<NewsRequestResultApi> getResult() {
        return result;
    }
    public void setResult(List<NewsRequestResultApi> result) {
        this.result = result;
    }

    public class NewsRequestResultApi {
        private int articleID;
        private String articleTitle;
        private String articleURL;
        private String image;
        private String reporter;
        private String company;

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

        public String getReporter() {
            return reporter;
        }
        public void setReporter(String reporter) {
            this.reporter = reporter;
        }

        public String getCompany() {
            return company;
        }
        public void setCompany(String company) {
            this.company = company;
        }
    }
}
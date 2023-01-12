package com.beckytech.afaanoromoograde11th.model;

public class MoreAppsModel {
    String appName;
    String url;
    int images;

    public MoreAppsModel(String appName, String url, int images) {
        this.appName = appName;
        this.url = url;
        this.images = images;
    }

    public String getAppName() {
        return appName;
    }

    public String getUrl() {
        return url;
    }

    public int getImages() {
        return images;
    }

    public void setAppName(String appName) {
        this.appName = appName;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public void setImages(int images) {
        this.images = images;
    }
}

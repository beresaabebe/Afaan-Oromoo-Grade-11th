package com.beckytech.afaanoromoograde11th.model;

public class AboutModel {
    private final int image;
    private final String name;
    private final String url;

    public AboutModel(int image, String name, String url) {
        this.image = image;
        this.name = name;
        this.url = url;
    }

    public int getImage() {
        return image;
    }

    public String getName() {
        return name;
    }

    public String getUrl() {
        return url;
    }
}

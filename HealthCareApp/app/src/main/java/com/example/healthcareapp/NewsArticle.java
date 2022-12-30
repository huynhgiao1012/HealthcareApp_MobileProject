package com.example.healthcareapp;

import android.content.Context;

public class NewsArticle {
    private String newsTitle, newsContent, newsURL;
    private final int headerImage;
    Context context;

    // For normal news articles
    public NewsArticle(String newsTitle, String newsContent, String newsURL, int headerImage) {
        this.headerImage = headerImage;
        this.newsContent = newsContent;
        this.newsURL = newsURL;
        this.newsTitle = newsTitle;
    }

    // For dashboard news articles (top news)
    public NewsArticle(String newsTitle, int headerImage, String newsURL) {
        this.headerImage = headerImage;
        this.newsTitle = newsTitle;
        this.newsURL = newsURL;
    }

    // TODO: This constructor only serves as a test. Remove this in the future
    public NewsArticle(Context context, String newsTitle) {
        this.headerImage = R.mipmap.ic_placeholder;
        this.newsContent = context.getString(R.string.lorem_ipsum_text);
        this.newsURL = "https://google.com";
        this.newsTitle = newsTitle;
        this.context = context;
    }

    public NewsArticle(String newsTitle, String newsContent, int headerImage) {
        this.newsTitle = newsTitle;
        this.newsContent = newsContent;
        this.headerImage = R.mipmap.ic_placeholder;
    }

    public int getHeaderImage() {
        return headerImage;
    }

    public String getNewsContent() {
        return newsContent;
    }

    public String getNewsTitle() {
        return newsTitle;
    }

    public String getNewsURL() {
        return newsURL;
    }
}

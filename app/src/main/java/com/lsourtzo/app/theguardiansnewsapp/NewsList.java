package com.lsourtzo.app.theguardiansnewsapp;

/**
 * Created by lsourtzo on 14/05/2017.
 */

public class NewsList {

    private String mCat;
    private String mTitle;
    private String mTime;
    private String mLink;
    private String mImageURL;
    private int mVis;

    // title is for title , textd is for the main text, sorttext is for sortversion text in button and imageId its for image
    public NewsList(String cat, String title, String time, String imageURL, String link) {
        mCat = cat;
        mTitle = title;
        mTime = time;
        mLink = link;
        mImageURL = imageURL;
    }

    // this one needed for data-binding
    public NewsList(String cat, String title, String time, String imageURL,int vis) {
        mCat = cat;
        mTitle = title;
        mTime = time;
        mImageURL = imageURL;
        mVis = vis;
    }

    public String getCat() {
        return mCat;
    }

    public String getTitle() {
        return mTitle;
    }

    public String getTime() {
        return mTime;
    }

    public String getlinkText() {
        return mLink;
    }

    public String getImageURL() {
        return mImageURL;
    }

    public int isVis() {
        return mVis;
    }

    //this one returns which of the list layouts will be showing
    public void setVis(int Visible) {
        mVis = Visible;
    }
}

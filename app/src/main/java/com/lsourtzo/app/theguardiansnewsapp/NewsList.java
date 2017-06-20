package com.lsourtzo.app.theguardiansnewsapp;

/**
 * Created by lsourtzo on 14/05/2017.
 */

//that was as required for issues with name ... but from the link tou send me "Class Names should always start with upperCases".
//So, why this is wrong ?
public class NewsList {

    //I am not sure if I understand well this suggestion. But I hope this is what you mean.
    //One question only? if we want to call this class with different arguments how we can to that ?
    final private String mCat;
    final private String mTitle;
    final private String mTime;
    final private String mLink;
    final private String mImageURL;
    final private int mVis;

    // title is for title , textd is for the main text, sorttext is for sort version text in button and imageId its for image
    public NewsList(String cat, String title, String time, String imageURL, String link,int vis) {
        this.mCat = cat;
        this.mTitle = title;
        this.mTime = time;
        this.mLink = link;
        this.mImageURL = imageURL;
        this.mVis = vis;
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
}

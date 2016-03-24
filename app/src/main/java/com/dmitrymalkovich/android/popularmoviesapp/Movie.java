package com.dmitrymalkovich.android.popularmoviesapp;

import android.content.Context;

import java.io.Serializable;

public class Movie implements Serializable {

    private long mId;
    private String mTitle;
    private String mPoster;

    public Movie(long id, String title, String poster) {
        this.mId = id;
        this.mTitle = title;
        this.mPoster = poster;
    }

    public String getTitle() {
        return mTitle;
    }

    public long getId() {
        return mId;
    }

    public String getPosterUrl(Context context) {
        return context.getResources().getString(R.string.url_for_downloading_poster) + mPoster;
    }
}

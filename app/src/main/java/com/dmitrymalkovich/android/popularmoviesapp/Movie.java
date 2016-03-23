package com.dmitrymalkovich.android.popularmoviesapp;

import java.io.Serializable;

public class Movie implements Serializable {

    private long mId;
    private String mTitle;
    @SuppressWarnings("all")
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
}

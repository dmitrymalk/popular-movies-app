package com.dmitrymalkovich.android.popularmoviesapp;

import android.content.Context;
import android.util.Log;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class Movie implements Serializable {

    public static final String LOG_TAG = Movie.class.getSimpleName();
    public static final float POSTER_ASPECT_RATIO = 1.5f;
    private long mId;
    private String mTitle;
    private String mPoster;
    private String mOverview;
    private String mUserRating;
    private String mReleaseDate;
    private String mBackdrop;

    public Movie(long id, String title, String poster, String overview, String userRating,
                 String releaseDate, String backdrop) {
        this.mId = id;
        this.mTitle = title;
        this.mPoster = poster;
        this.mOverview = overview;
        this.mUserRating = userRating;
        this.mReleaseDate = releaseDate;
        this.mBackdrop = backdrop;
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

    public String getReleaseDate() {
        String inputPattern = "yyyy-MM-dd";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern, Locale.US);

        try {
            Date date = inputFormat.parse(mReleaseDate);
            return DateFormat.getDateInstance().format(date);
        } catch (ParseException e) {
            Log.e(LOG_TAG, "The Release data was not parsed successfully: " + mReleaseDate);
            // Return not formatted date
        }

        return mReleaseDate;
    }

    public String getOverview() {
        return mOverview;
    }

    public String getUserRating() {
        return mUserRating;
    }

    public String getBackdropUrl(Context context) {
        return context.getResources().getString(R.string.url_for_downloading_backdrop) + mBackdrop;
    }
}

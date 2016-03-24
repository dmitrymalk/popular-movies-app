package com.dmitrymalkovich.android.popularmoviesapp;

import java.io.Serializable;
import java.util.Arrays;
import java.util.List;

/**
 * Wrapper for storing a list of {@link Movie}s inside {@link android.os.Bundle} during
 * fragment/activity recreations.
 */
public class Movies implements Serializable {

    private List<Movie> mMovies;

    public Movies(List<Movie> movies) {
        mMovies = movies;
    }

    public Movies(Movie[] movies) {
        mMovies = Arrays.asList(movies);
    }

    public List<Movie> getMovies() {
        return mMovies;
    }
}

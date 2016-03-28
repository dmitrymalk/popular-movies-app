package com.dmitrymalkovich.android.popularmoviesapp.data;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class Movies {

    @SerializedName("results")
    private List<Movie> movies = new ArrayList<>();

    public List<Movie> getMovies() {
        return movies;
    }
}


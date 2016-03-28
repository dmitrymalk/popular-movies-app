package com.dmitrymalkovich.android.popularmoviesapp;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieDatabaseService {

    @GET("3/movie/{sort_by}")
    Call<Movies> listMovies(@Path("sort_by") String sortBy, @Query("api_key") String apiKey);
}

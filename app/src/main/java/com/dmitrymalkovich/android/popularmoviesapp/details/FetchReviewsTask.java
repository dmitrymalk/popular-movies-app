package com.dmitrymalkovich.android.popularmoviesapp.details;

import android.os.AsyncTask;
import android.util.Log;

import com.dmitrymalkovich.android.popularmoviesapp.BuildConfig;
import com.dmitrymalkovich.android.popularmoviesapp.data.MovieDatabaseService;
import com.dmitrymalkovich.android.popularmoviesapp.data.Review;
import com.dmitrymalkovich.android.popularmoviesapp.data.Reviews;
import com.dmitrymalkovich.android.popularmoviesapp.data.Trailer;
import com.dmitrymalkovich.android.popularmoviesapp.data.Trailers;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Encapsulates fetching the movie's reviews from the movie db api.
 */
public class FetchReviewsTask extends AsyncTask<Long, Void, List<Review>> {

    @SuppressWarnings("unused")
    public static String LOG_TAG = FetchReviewsTask.class.getSimpleName();
    private final Listener mListener;

    /**
     * Interface definition for a callback to be invoked when reviews are loaded.
     */
    interface Listener {
        void onReviewsFetchFinished(List<Review> reviews);
    }

    public FetchReviewsTask(Listener listener) {
        mListener = listener;
    }

    @Override
    protected List<Review> doInBackground(Long... params) {
        // If there's no movie id, there's nothing to look up.
        if (params.length == 0) {
            return null;
        }
        long movieId = params[0];

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDatabaseService service = retrofit.create(MovieDatabaseService.class);
        Call<Reviews> call = service.findReviewsById(movieId,
                BuildConfig.THE_MOVIE_DATABASE_API_KEY);
        try {
            Response<Reviews> response = call.execute();
            Reviews reviews = response.body();
            return reviews.getReviews();
        } catch (IOException e) {
            Log.e(LOG_TAG, "A problem occurred talking to the movie db ", e);
        }

        return null;
    }

    @Override
    protected void onPostExecute(List<Review> reviews) {
        mListener.onReviewsFetchFinished(reviews);
    }
}

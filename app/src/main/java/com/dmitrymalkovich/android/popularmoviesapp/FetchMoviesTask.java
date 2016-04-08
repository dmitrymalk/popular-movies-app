/*
 * Copyright 2016.  Dmitry Malkovich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.dmitrymalkovich.android.popularmoviesapp;

import android.os.AsyncTask;
import android.support.annotation.StringDef;
import android.util.Log;

import com.dmitrymalkovich.android.popularmoviesapp.network.Movie;
import com.dmitrymalkovich.android.popularmoviesapp.network.MovieDatabaseService;
import com.dmitrymalkovich.android.popularmoviesapp.network.Movies;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Encapsulates fetching the movies list from the movie db api.
 */
public class FetchMoviesTask extends AsyncTask<Void, Void, List<Movie>> {

    @SuppressWarnings("unused")
    public static String LOG_TAG = FetchMoviesTask.class.getSimpleName();

    public final static String MOST_POPULAR = "popular";
    public final static String TOP_RATED = "top_rated";
    public final static String FAVORITES = "favorites";

    // FetchMoviesTask cannot load favorites movies now, it's done by loaders (especially for two pane is
    // comfortable - without force updating left pane on removing/adding a favorite movie. Another
    // case when we simple returns from detail - list of favorites also will be updated, if needed).
    @StringDef({MOST_POPULAR, TOP_RATED, FAVORITES})
    public @interface SORT_BY {
    }

    /**
     * Will be called in {@link FetchMoviesTask#onPostExecute(List)} to notify subscriber to about
     * task completion.
     */
    private final NotifyAboutTaskCompletionCommand mCommand;
    private
    @SORT_BY
    String mSortBy = MOST_POPULAR;

    /**
     * Interface definition for a callback to be invoked when movies are loaded.
     */
    interface Listener {
        void onFetchFinished(Command command);
    }

    /**
     * Possible good idea to use {@link android.content.AsyncTaskLoader}, which by default is tied
     * to lifecycle method, but this approach has its own limitations
     * (i. e. publish progress is not possible in this case).
     * <p/>
     * Idea is to use AsyncTasks in combination with non-UI retained fragment and Command pattern.
     * It helps save calls which we cannot execute immediately for later.
     */
    public static class NotifyAboutTaskCompletionCommand implements Command {
        private FetchMoviesTask.Listener mListener;
        // The result of the task execution.
        private List<Movie> mMovies;

        public NotifyAboutTaskCompletionCommand(FetchMoviesTask.Listener listener) {
            mListener = listener;
        }

        @Override
        public void execute() {
            mListener.onFetchFinished(this);
        }

        public List<Movie> getMovies() {
            return mMovies;
        }
    }

    public FetchMoviesTask(@SORT_BY String sortBy, NotifyAboutTaskCompletionCommand command) {
        mCommand = command;
        mSortBy = sortBy;
    }

    @Override
    protected void onPostExecute(List<Movie> movies) {
        if (movies != null) {
            mCommand.mMovies = movies;
        } else {
            mCommand.mMovies = new ArrayList<>();
        }
        mCommand.execute();
    }

    @Override
    protected List<Movie> doInBackground(Void... params) {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        MovieDatabaseService service = retrofit.create(MovieDatabaseService.class);
        Call<Movies> call = service.discoverMovies(mSortBy,
                BuildConfig.THE_MOVIE_DATABASE_API_KEY);
        try {
            Response<Movies> response = call.execute();
            Movies movies = response.body();
            return movies.getMovies();

        } catch (IOException e) {
            Log.e(LOG_TAG, "A problem occurred talking to the movie db ", e);
        }
        return null;
    }
}

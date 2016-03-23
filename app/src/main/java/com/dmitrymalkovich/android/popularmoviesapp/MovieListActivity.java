package com.dmitrymalkovich.android.popularmoviesapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.io.Serializable;
import java.util.List;

/**
 * An activity representing a grid of Movies. This activity
 * has different presentations for handset and tablet-size devices.
 */
public class MovieListActivity extends AppCompatActivity implements FetchMoviesTask.Listener,
        MovieListAdapter.Callbacks {

    private static final int PERMISSIONS_REQUEST_INTERNET = 1;
    private static final String EXTRA_MOVIES = "EXTRA_MOVIES";
    /**
     * Whether or not the activity is in two-pane mode, i.e. running on a tablet
     * device.
     */
    private boolean mTwoPane;
    private RecyclerView mRecyclerView;
    private RetainedFragment retainedFragment;
    private MovieListAdapter mAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_list);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setTitle(getTitle());

        mRecyclerView = (RecyclerView) findViewById(R.id.movie_list);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        mRecyclerView.setHasFixedSize(true);

        String tag = RetainedFragment.class.getName();
        this.retainedFragment = (RetainedFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (this.retainedFragment == null) {
            this.retainedFragment = new RetainedFragment();
            getSupportFragmentManager().beginTransaction().add(this.retainedFragment, tag).commit();
        }

        if (findViewById(R.id.movie_detail_container) != null) {
            // large-screen layouts (res/values-w900dp).
            mTwoPane = true;
        }

        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_MOVIES)) {
            Serializable s = savedInstanceState.getSerializable(EXTRA_MOVIES);
            if (s instanceof Movies) {
                onFetchFinished(((Movies) s).getMovies());
            }
        } else {
            fetchMovies();
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        Movies movies = new Movies(mAdapter.getMovies());
        if (movies.getMovies() != null && !movies.getMovies().isEmpty()) {
            outState.putSerializable(EXTRA_MOVIES, movies);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSIONS_REQUEST_INTERNET: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    fetchMovies();
                }
                // TODO : Handle permission denied.
            }
        }
    }

    @Override
    public void onFetchFinished(List<Movie> movies) {
        mAdapter = new MovieListAdapter(movies, this);
        mRecyclerView.setAdapter(mAdapter);
    }

    @Override
    public void open(Movie movie, int position) {
        if (mTwoPane) {
            Bundle arguments = new Bundle();
            arguments.putSerializable(MovieDetailFragment.ARG_MOVIE, movie);
            MovieDetailFragment fragment = new MovieDetailFragment();
            fragment.setArguments(arguments);
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.movie_detail_container, fragment)
                    .commit();
        } else {
            Intent intent = new Intent(this, MovieDetailActivity.class);
            intent.putExtra(MovieDetailFragment.ARG_MOVIE, movie);
            startActivity(intent);
        }
    }

    private void fetchMovies() {
        // TODO : Should we show an explanation?
        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED) {
            new FetchMoviesTask(this.retainedFragment).execute();
        } else {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.INTERNET},
                    PERMISSIONS_REQUEST_INTERNET);
        }
    }

    public static class RetainedFragment extends Fragment implements FetchMoviesTask.Listener {

        public RetainedFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public void onFetchFinished(List<Movie> movies) {
            if (getActivity() instanceof FetchMoviesTask.Listener) {
                FetchMoviesTask.Listener listener = (FetchMoviesTask.Listener) getActivity();
                listener.onFetchFinished(movies);
            }
            // TODO : Add commands pattern to collect results and publish them.
        }
    }
}

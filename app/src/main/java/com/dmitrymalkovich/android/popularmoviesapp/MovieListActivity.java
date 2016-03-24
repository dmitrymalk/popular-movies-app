package com.dmitrymalkovich.android.popularmoviesapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;

import java.io.Serializable;
import java.util.ArrayList;

/**
 * An activity representing a grid of Movies. This activity
 * has different presentations for handset and tablet-size devices.
 */
public class MovieListActivity extends AppCompatActivity implements FetchMoviesTask.Listener,
        MovieListAdapter.Callbacks {

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
        // Create adapter with empty list to avoid "E/RecyclerView: No adapter attached; skipping layout"
        // during data loading.
        mAdapter = new MovieListAdapter(new ArrayList<Movie>(),
                this);
        mRecyclerView.setAdapter(mAdapter);

        String tag = RetainedFragment.class.getName();
        this.retainedFragment = (RetainedFragment) getSupportFragmentManager().findFragmentByTag(tag);
        if (this.retainedFragment == null) {
            this.retainedFragment = new RetainedFragment();
            getSupportFragmentManager().beginTransaction().add(this.retainedFragment, tag).commit();
        }

        if (findViewById(R.id.movie_detail_container) != null) {
            // For large-screen layouts (res/values-w900dp).
            mTwoPane = true;
        }

        // Fetch Movies only if savedInstanceState == null
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_MOVIES)) {
            Serializable s = savedInstanceState.getSerializable(EXTRA_MOVIES);
            if (s instanceof Movies) {
                Movies movies = (Movies) s;
                mAdapter.add(movies.getMovies());
            } else {
                throw new IllegalStateException("SavedInstanceState is not empty and without needed data.");
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
    public void onFetchFinished(Command command) {
        if (command instanceof FetchMoviesTask.NotifyAboutTaskCompletionCommand) {
            mAdapter = new MovieListAdapter(((FetchMoviesTask.NotifyAboutTaskCompletionCommand) command).getMovies(),
                    this);
            mRecyclerView.setAdapter(mAdapter);
        }
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
        FetchMoviesTask.NotifyAboutTaskCompletionCommand command =
                new FetchMoviesTask.NotifyAboutTaskCompletionCommand(this.retainedFragment);
        new FetchMoviesTask(command).execute();
    }

    /**
     * RetainedFragment with saving state mechanism.
     * The saving state mechanism helps to not lose user's progress even when app is in the
     * background state or user rotate device and also to avoid performing code which
     * will lead to "java.lang.IllegalStateException: Can not perform some actions after
     * onSaveInstanceState". As the result we have commands which we cannot execute now,
     * but we have to store it and execute later.
     *
     * @see com.dmitrymalkovich.android.popularmoviesapp.FetchMoviesTask.NotifyAboutTaskCompletionCommand
     */
    public static class RetainedFragment extends Fragment implements FetchMoviesTask.Listener {

        private boolean mPaused = false;
        // Currently allow to wait one command, because more is not needed. In future it can be
        // extended to list etc. Using "MacroCommand" which contain includes other commands as waiting command.
        private Command mWaitingCommand = null;

        public RetainedFragment() {
        }

        @Override
        public void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);
            setRetainInstance(true);
        }

        @Override
        public void onPause() {
            super.onPause();
            mPaused = true;
        }

        @Override
        public void onResume() {
            super.onResume();
            mPaused = false;
            if (mWaitingCommand != null) {
                onFetchFinished(mWaitingCommand);
            }
        }

        @Override
        public void onFetchFinished(Command command) {
            if (getActivity() instanceof FetchMoviesTask.Listener && !mPaused) {
                FetchMoviesTask.Listener listener = (FetchMoviesTask.Listener) getActivity();
                listener.onFetchFinished(command);
                mWaitingCommand = null;
            } else {
                // save command for later.
                mWaitingCommand = command;
            }
        }
    }
}
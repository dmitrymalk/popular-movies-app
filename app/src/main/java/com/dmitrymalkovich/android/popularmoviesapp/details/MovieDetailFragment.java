package com.dmitrymalkovich.android.popularmoviesapp.details;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.dmitrymalkovich.android.popularmoviesapp.data.Movie;
import com.dmitrymalkovich.android.popularmoviesapp.MovieListActivity;
import com.dmitrymalkovich.android.popularmoviesapp.R;
import com.dmitrymalkovich.android.popularmoviesapp.data.Trailer;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.BindDrawable;
import butterknife.ButterKnife;

/**
 * A fragment representing a single Movie detail screen.
 * This fragment is either contained in a {@link MovieListActivity}
 * in two-pane mode (on tablets) or a {@link MovieDetailActivity}
 * on handsets.
 */
public class MovieDetailFragment extends Fragment implements FetchTrailersTask.Listener,
        TrailerListAdapter.Callbacks {

    @SuppressWarnings("unused")
    public static final String LOG_TAG = MovieDetailFragment.class.getSimpleName();
    /**
     * The fragment argument representing the movie that this fragment
     * represents.
     */
    public static final String ARG_MOVIE = "ARG_MOVIE";
    public static final String EXTRA_TRAILERS = "EXTRA_TRAILERS";
    @Bind(R.id.trailer_list)
    RecyclerView mRecyclerView;

    private Movie mMovie;
    @Bind(R.id.trailer_title)
    TextView mMovieTitleView;
    @Bind(R.id.movie_overview)
    TextView mMovieOverviewView;
    @Bind(R.id.movie_release_date)
    TextView mMovieReleaseDateView;
    @Bind(R.id.movie_user_rating)
    TextView mMovieRatingView;
    @Bind(R.id.movie_poster)
    ImageView mMoviePosterView;

    @Bind({R.id.rating_first_star, R.id.rating_second_star, R.id.rating_third_star,
            R.id.rating_fourth_star, R.id.rating_fifth_star})
    List<ImageView> ratingStarViews;

    @BindDrawable(R.drawable.ic_star_black_24dp)
    Drawable starDrawable;
    @BindDrawable(R.drawable.ic_star_half_black_24dp)
    Drawable starHalfDrawable;
    private TrailerListAdapter mAdapter;

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOVIE)) {
            mMovie = getArguments().getParcelable(ARG_MOVIE);
        }
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Activity activity = getActivity();
        CollapsingToolbarLayout appBarLayout = (CollapsingToolbarLayout)
                activity.findViewById(R.id.toolbar_layout);
        if (appBarLayout != null && activity instanceof MovieDetailActivity) {
            appBarLayout.setTitle(mMovie.getTitle());
        }

        ImageView movieBackdrop = ((ImageView) activity.findViewById(R.id.movie_backdrop));
        if (movieBackdrop != null) {
            Picasso.with(activity)
                    .load(mMovie.getBackdropUrl(getContext()))
                    .config(Bitmap.Config.RGB_565)
                    .into(movieBackdrop);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.movie_detail, container, false);
        ButterKnife.bind(this, rootView);

        mMovieTitleView.setText(mMovie.getTitle());
        mMovieOverviewView.setText(mMovie.getOverview());
        mMovieReleaseDateView.setText(mMovie.getReleaseDate(getContext()));

        Picasso.with(getContext())
                .load(mMovie.getPosterUrl(getContext()))
                .config(Bitmap.Config.RGB_565)
                .into(mMoviePosterView);

        float userRating = 0.f;
        if (mMovie.getUserRating() != null && !mMovie.getUserRating().isEmpty()) {

            String userRatingStr = getResources().getString(R.string.user_rating_movie,
                    mMovie.getUserRating());
            mMovieRatingView.setText(userRatingStr);

            userRating = Float.valueOf(mMovie.getUserRating()) / 2;
        } else {
            mMovieRatingView.setVisibility(View.GONE);
        }

        if (userRating > 0.5f) {
            ratingStarViews.get(0).setImageDrawable(starHalfDrawable);
        }
        if (userRating >= 1.f) {
            ratingStarViews.get(0).setImageDrawable(starDrawable);
        }

        if (userRating >= 1.5f) {
            ratingStarViews.get(1).setImageDrawable(starHalfDrawable);
        }
        if (userRating >= 2f) {
            ratingStarViews.get(1).setImageDrawable(starDrawable);
        }

        if (userRating >= 2.5f) {
            ratingStarViews.get(2).setImageDrawable(starHalfDrawable);
        }
        if (userRating >= 3f) {
            ratingStarViews.get(2).setImageDrawable(starDrawable);
        }

        if (userRating >= 3.5f) {
            ratingStarViews.get(3).setImageDrawable(starHalfDrawable);
        }
        if (userRating >= 4f) {
            ratingStarViews.get(3).setImageDrawable(starDrawable);
        }

        if (userRating >= 4.5f) {
            ratingStarViews.get(4).setImageDrawable(starHalfDrawable);
        }
        if (userRating >= 5f) {
            ratingStarViews.get(4).setImageDrawable(starDrawable);
        }

        LinearLayoutManager layoutManager
                = new LinearLayoutManager(getContext(), LinearLayoutManager.HORIZONTAL, false);
        mRecyclerView.setLayoutManager(layoutManager);

        // Create adapter with empty list to avoid "E/RecyclerView: No adapter attached; skipping layout"
        // during data loading.
        mAdapter = new TrailerListAdapter(new ArrayList<Trailer>(),
                this);
        mRecyclerView.setAdapter(mAdapter);

        // Fetch Movies only if savedInstanceState == null
        if (savedInstanceState != null && savedInstanceState.containsKey(EXTRA_TRAILERS)) {
            List<Trailer> trailers = savedInstanceState.getParcelableArrayList(EXTRA_TRAILERS);
            mAdapter.add(trailers);
        } else {
            fetchTrailers();
        }

        return rootView;
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        ArrayList<Trailer> trailers = mAdapter.getTrailers();
        if (trailers != null && !trailers.isEmpty()) {
            outState.putParcelableArrayList(EXTRA_TRAILERS, trailers);
        }
    }

    @Override
    public void watch(Trailer trailer, int position) {
        startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse("http://www.youtube.com/watch?v=" +
                trailer.getKey())));
    }

    @Override
    public void onFetchFinished(List<Trailer> trailers) {
        mAdapter.add(trailers);
    }

    private void fetchTrailers() {
        FetchTrailersTask task = new FetchTrailersTask(this);
        task.execute(mMovie.getId());
    }
}

package com.dmitrymalkovich.android.popularmoviesapp;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.support.annotation.Nullable;
import android.support.design.widget.CollapsingToolbarLayout;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

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
public class MovieDetailFragment extends Fragment {
    /**
     * The fragment argument representing the movie that this fragment
     * represents.
     */
    public static final String ARG_MOVIE = "ARG_MOVIE";

    private Movie mMovie;
    @Bind(R.id.movie_title)
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

    public MovieDetailFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments().containsKey(ARG_MOVIE)) {
            mMovie = (Movie) getArguments().getSerializable(ARG_MOVIE);
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
        mMovieReleaseDateView.setText(mMovie.getReleaseDate());

        String userRatingStr = getResources().getString(R.string.user_rating_movie,
                mMovie.getUserRating());
        mMovieRatingView.setText(userRatingStr);

        Picasso.with(getContext())
                .load(mMovie.getPosterUrl(getContext()))
                .config(Bitmap.Config.RGB_565)
                .into(mMoviePosterView);

        float userRating = Float.valueOf(mMovie.getUserRating()) / 2;

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

        return rootView;
    }
}

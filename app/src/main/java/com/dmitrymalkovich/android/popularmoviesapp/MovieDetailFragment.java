package com.dmitrymalkovich.android.popularmoviesapp;

import android.app.Activity;
import android.graphics.Bitmap;
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

    /**
     * The dummy content this fragment is presenting.
     */
    private Movie mMovie;

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
        if (appBarLayout != null) {
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
        ((TextView) rootView.findViewById(R.id.movie_title)).setText(mMovie.getTitle());
        ((TextView) rootView.findViewById(R.id.movie_overview)).setText(mMovie.getOverview());
        ((TextView) rootView.findViewById(R.id.movie_release_date)).setText(mMovie.getReleaseDate());

        String userRatingStr = getResources().getString(R.string.user_rating_movie,
                mMovie.getUserRating());
        ((TextView) rootView.findViewById(R.id.movie_user_rating)).setText(userRatingStr);

        ImageView moviePoster = ((ImageView) rootView.findViewById(R.id.movie_poster));
        Picasso.with(getContext())
                .load(mMovie.getPosterUrl(getContext()))
                .config(Bitmap.Config.RGB_565)
                .into(moviePoster);

        float userRating = Float.valueOf(mMovie.getUserRating()) / 2;

        if (userRating > 0.5f) {
            ((ImageView) rootView.findViewById(R.id.rating_first_star))
                    .setImageResource(R.drawable.ic_star_half_black_24dp);
        }
        if (userRating >= 1.f) {
            ((ImageView) rootView.findViewById(R.id.rating_first_star))
                    .setImageResource(R.drawable.ic_star_black_24dp);
        }

        if (userRating >= 1.5f) {
            ((ImageView) rootView.findViewById(R.id.rating_second_star))
                    .setImageResource(R.drawable.ic_star_half_black_24dp);
        }
        if (userRating >= 2f) {
            ((ImageView) rootView.findViewById(R.id.rating_second_star))
                    .setImageResource(R.drawable.ic_star_black_24dp);
        }

        if (userRating >= 2.5f) {
            ((ImageView) rootView.findViewById(R.id.rating_third_star))
                    .setImageResource(R.drawable.ic_star_half_black_24dp);
        }
        if (userRating >= 3f) {
            ((ImageView) rootView.findViewById(R.id.rating_third_star))
                    .setImageResource(R.drawable.ic_star_black_24dp);
        }

        if (userRating >= 3.5f) {
            ((ImageView) rootView.findViewById(R.id.rating_fourth_star))
                    .setImageResource(R.drawable.ic_star_half_black_24dp);
        }
        if (userRating >= 4f) {
            ((ImageView) rootView.findViewById(R.id.rating_fourth_star))
                    .setImageResource(R.drawable.ic_star_black_24dp);
        }

        if (userRating >= 4.5f) {
            ((ImageView) rootView.findViewById(R.id.rating_fifth_star))
                    .setImageResource(R.drawable.ic_star_half_black_24dp);
        }
        if (userRating >= 5f) {
            ((ImageView) rootView.findViewById(R.id.rating_fifth_star))
                    .setImageResource(R.drawable.ic_star_black_24dp);
        }

        return rootView;
    }
}

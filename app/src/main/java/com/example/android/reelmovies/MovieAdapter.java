package com.example.android.reelmovies;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Jujuan on 11/10/2017.
 * This subclass takes in a list of movie objects and
 * incrementally extracts the details from that movie,
 * then assigns it to the required View in the listview_item layout
 * of the movie fragment UI
 */

public class MovieAdapter extends ArrayAdapter<Movie> {

    /**
     * This constructor receives a specific activity as it's operational context,
     * and a list of movie objects
     * @param context (getActivity()) in this case the movie fragment activity
     * @param movieList
     */
    public MovieAdapter(Context context, List<Movie> movieList){
        super(context, 0, movieList);
    }

    /**
     * This method locates
     * @param position
     * @param convertView
     * @param parent
     * @return
     */
    public View getView(int position, View convertView, ViewGroup parent){

        View listItemView = convertView;
        if(listItemView == null){
            listItemView = LayoutInflater.from(getContext()).inflate(
                    R.layout.listview_item, parent, false);
        }

        Movie currentMovie = getItem(position);

        ImageView imageView = (ImageView) listItemView.findViewById(R.id.movieImage);
        imageView.setImageResource(R.drawable.movie_reel_clipart);

        TextView titleView = (TextView) listItemView.findViewById(R.id.movieTitleView);
        titleView.setText(currentMovie.getMovieTitle());

        RatingBar ratingView = (RatingBar) listItemView.findViewById(R.id.ratingBar);
        ratingView.setRating(currentMovie.getMovieRating());

        TextView ratedTag = (TextView) listItemView.findViewById(R.id.rTagView);
        ratedTag.setText(currentMovie.getMovieAdultTag());

        TextView dateView = (TextView) listItemView.findViewById(R.id.dateView);
        dateView.setText(currentMovie.getReleaseDate());


        return listItemView;
    }

}

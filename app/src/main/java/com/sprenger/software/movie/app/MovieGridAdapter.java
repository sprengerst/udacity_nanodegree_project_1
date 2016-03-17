/*
 * Created by Stefan Sprenger
 */

package com.sprenger.software.movie.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

class MovieGridAdapter extends ArrayAdapter<MovieSpecification> {

    private final Context context;
    private final LayoutInflater inflater;

    private final List<MovieSpecification> movieSpecs;

    public MovieGridAdapter(Context context, List<MovieSpecification> movieSpecs) {
        super(context, R.layout.single_movie_grid_element, movieSpecs);

        this.context = context;
        this.movieSpecs = movieSpecs;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        if (null == convertView) {
            convertView = inflater.inflate(R.layout.single_movie_grid_element, parent, false);

        }

        Picasso
                .with(context)
                .load(movieSpecs.get(position).getPosterPath())
                .into((ImageView) convertView);

        return convertView;
    }

}
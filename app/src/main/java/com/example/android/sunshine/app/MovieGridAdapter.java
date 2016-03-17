package com.example.android.sunshine.app;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;

import com.squareup.picasso.Picasso;

import java.util.List;

public class MovieGridAdapter extends ArrayAdapter<MovieSpecification> {

    private Context context;
    private LayoutInflater inflater;

    private List<MovieSpecification> movieSpecs;

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
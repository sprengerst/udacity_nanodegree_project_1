/*
 * Created by Stefan Sprenger
 */

package com.sprenger.software.movie.app;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;


public class MovieDetailFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_detail, container, false);

        Intent intent = getActivity().getIntent();

        MovieSpecification mSpec = (MovieSpecification) intent.getExtras().getSerializable(Intent.EXTRA_SUBJECT);
        assert mSpec != null;

        ((TextView) rootView.findViewById(R.id.title_text_detail)).setText(mSpec.getTitle());

        List<String> movieDetailList = new ArrayList<>();
        movieDetailList.add(getString(R.string.release_text) + " " + String.valueOf(mSpec.getReleaseDate()));
        movieDetailList.add(getString(R.string.rating_text) + " " + String.valueOf(mSpec.getRating()) + "/10");

        ArrayAdapter<String> movieDetailAdapter =
                new ArrayAdapter<>(getActivity(), android.R.layout.simple_list_item_1, movieDetailList);

        ((ListView) rootView.findViewById(R.id.movie_details_list)).setAdapter(movieDetailAdapter);
        ((TextView) rootView.findViewById(R.id.synopsis_text_detail)).setText(mSpec.getSynopsis());
        ((TextView) rootView.findViewById(R.id.synopsis_text_detail)).setMovementMethod(new ScrollingMovementMethod());

        Picasso
                .with(getActivity())
                .load(mSpec.getPosterPath())
                .into(((ImageView) rootView.findViewById(R.id.image_view_detail)));

        return rootView;
    }

}

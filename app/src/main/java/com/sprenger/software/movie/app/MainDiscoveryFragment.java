/*
 * Created by Stefan Sprenger
 */

package com.sprenger.software.movie.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;

import java.util.ArrayList;

public class MainDiscoveryFragment extends Fragment {

    private MovieGridAdapter movieGridAdapter;

    public MainDiscoveryFragment() {
    }

    @Override
    public void onCreate(Bundle instance) {
        super.onCreate(instance);
        setHasOptionsMenu(true);
    }

    private void updateMovieGrid() {
        try {
            AsyncTask<String, Void, ArrayList<MovieSpecification>> fetchMovieTask = new FetchMovieDataTask(this);

            SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
            String sortOrder = prefs.getString(getString(R.string.pref_sortorder_key), getResources().getStringArray(R.array.pref_sortorder_keystore)[0]);

            assert movieGridAdapter != null;
            movieGridAdapter.clear();

            movieGridAdapter.addAll(fetchMovieTask.execute(sortOrder).get());
        } catch (Exception e) {
            Log.e("Sync Error",e.toString());
            e.printStackTrace();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_discovery_main, container, false);
        this.movieGridAdapter = new MovieGridAdapter(getActivity(), new ArrayList<MovieSpecification>());
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie_images);
        gridView.setAdapter(this.movieGridAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent detailedViewIntent = new Intent(getActivity(), MovieDetailActivity.class).putExtra(Intent.EXTRA_SUBJECT, movieGridAdapter.getItem(i));
                                                startActivity(detailedViewIntent);
                                            }
                                        }

        );

        updateMovieGrid();

        return rootView;
    }

    @Override
    public void onStart() {
        super.onStart();
        updateMovieGrid();
    }


}
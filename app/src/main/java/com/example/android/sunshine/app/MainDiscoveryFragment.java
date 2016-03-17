package com.example.android.sunshine.app;

import android.content.Intent;
import android.content.SharedPreferences;
import android.net.Uri;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * A placeholder fragment containing a simple view.
 */
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

        AsyncTask<String, Void, ArrayList<MovieSpecification>> fetchMovieTask = new FetchMovieDataTask();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String sortOrder = prefs.getString(getString(R.string.pref_sortorder_key),"DEFAULT");

        fetchMovieTask.execute(sortOrder);

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

    public class FetchMovieDataTask extends AsyncTask<String, Void, ArrayList<MovieSpecification>> {

        private final String LOG_TAG = FetchMovieDataTask.class.getSimpleName();

        @Override
        protected ArrayList<MovieSpecification> doInBackground(String... strings) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            String forecastJsonStr = null;

            try {

                Uri.Builder builder = new Uri.Builder();
                builder.scheme("http")
                        .authority("api.themoviedb.org")
                        .appendPath("3")
                        .appendPath("movie")
                        .appendPath("top_rated")
                        .appendQueryParameter("page", "1")
                        .appendQueryParameter("api_key", "d19539dd75c57ddc49feeaa144b95dba");
                String tmdbUrl = builder.build().toString();

                System.out.println("GENERATED URL: " + tmdbUrl);

                URL url = new URL(tmdbUrl);


                // Create the request to OpenWeatherMap, and open the connection
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");

                urlConnection.connect();


                // Read the input stream into a String
                InputStream inputStream = urlConnection.getInputStream();
                StringBuffer buffer = new StringBuffer();
                if (inputStream == null) {
                    // Nothing to do.
                    return null;
                }
                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;
                while ((line = reader.readLine()) != null) {
                    // Since it's JSON, adding a newline isn't necessary (it won't affect parsing)
                    // But it does make debugging a *lot* easier if you print out the completed
                    // buffer for debugging.
                    buffer.append(line + "\n");
                }

                if (buffer.length() == 0) {
                    // Stream was empty.  No point in parsing.
                    return null;
                }
                forecastJsonStr = buffer.toString();
            } catch (IOException e) {
                Log.e(LOG_TAG, "Error ", e);
                return null;
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e(LOG_TAG, "Error closing stream", e);
                    }
                }
            }




            try {
                return getMovieDataFromJson(forecastJsonStr,strings[0]);
            } catch (JSONException e) {
                e.printStackTrace();
            }

            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<MovieSpecification> movies) {

            //FIXME
            if (movies != null) {
                movieGridAdapter.clear();

                for (MovieSpecification mSpec : movies) {
                    movieGridAdapter.add(mSpec);
                }
            }
        }
    }


    private ArrayList<MovieSpecification> getMovieDataFromJson(String forecastJsonStr, final String sortOrder)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "results";


        final String ID = "id";
        final String TITLE = "original_title";
        final String SYNOPSIS = "overview";
        final String POSTER = "poster_path";
        final String RATING = "vote_average";
        final String RELEASEDATE = "release_date";
        final String POPULARITY = "popularity";

        JSONObject pageJSON = new JSONObject(forecastJsonStr);
        JSONArray movieArray = pageJSON.getJSONArray(OWM_LIST);

        ArrayList<MovieSpecification> resultStrs = new ArrayList<>();

        for (int i = 0; i < movieArray.length(); i++) {

            JSONObject singleMovieJSON = movieArray.getJSONObject(i);

            // description is in a child array called "weather", which is 1 element long.
            String movieId = singleMovieJSON.getString(ID);
            String movieTitle = singleMovieJSON.getString(TITLE);
            String movieSynopsis = singleMovieJSON.getString(SYNOPSIS);
            String moviePoster = singleMovieJSON.getString(POSTER);
            String movieRating = singleMovieJSON.getString(RATING);
            String movieReleaseDate = singleMovieJSON.getString(RELEASEDATE);
            String moviePopularity = singleMovieJSON.getString(POPULARITY);


            resultStrs.add(new MovieSpecification(movieId, movieTitle, moviePoster, movieSynopsis, movieRating, movieReleaseDate, moviePopularity));
        }

        // Sorting
        Collections.sort(resultStrs, new Comparator<MovieSpecification>() {
            @Override
            public int compare(MovieSpecification mSpec1, MovieSpecification mSpec2) {


                if (sortOrder.equals("Most Popular")) {
                    return Double.compare(Double.parseDouble(mSpec2.getPopularity()), Double.parseDouble(mSpec1.getPopularity()));
                } else {
                    return Double.compare(Double.parseDouble(mSpec2.getRating()), Double.parseDouble(mSpec1.getRating()));
                }
            }
        });

        for (MovieSpecification s : resultStrs) {
            Log.v("FORECAST", "Forecast entry: " + s);
        }
        return resultStrs;

    }


    private String getReadableDateString(long time) {
        // Because the API returns a unix timestamp (measured in seconds),
        // it must be converted to milliseconds in order to be converted to valid date.
        SimpleDateFormat shortenedDateFormat = new SimpleDateFormat("EEE MMM dd");
        return shortenedDateFormat.format(time);
    }



}
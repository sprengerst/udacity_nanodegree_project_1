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
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
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

/**
 * A placeholder fragment containing a simple view.
 */
public class MainDiscoveryFragment extends Fragment {

    private CustomGridAdapter mForecastAdapter;


    public MainDiscoveryFragment() {
    }

    @Override
    public void onCreate(Bundle instance) {
        super.onCreate(instance);
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.forecastfragment, menu);
    }


    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_refresh:

                updateWeather();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void updateWeather() {

        AsyncTask<String, Void, ArrayList<MovieSpecification>> weatherTask = new FetchWeatherTask();

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));

        weatherTask.execute(location);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        View rootView = inflater.inflate(R.layout.fragment_main, container, false);
        this.mForecastAdapter = new CustomGridAdapter(getActivity(), new ArrayList<MovieSpecification>());
        GridView gridView = (GridView) rootView.findViewById(R.id.gridview_movie_images);
        gridView.setAdapter(this.mForecastAdapter);

        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                                            @Override
                                            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                                                Intent detailedViewIntent = new Intent(getActivity(), DetailActivity.class).putExtra(Intent.EXTRA_SUBJECT, mForecastAdapter.getItem(i));
                                                startActivity(detailedViewIntent);
                                            }
                                        }


        );
        updateWeather();


        return rootView;
    }

    public class FetchWeatherTask extends AsyncTask<String, Void, ArrayList<MovieSpecification>> {

        private final String LOG_TAG = FetchWeatherTask.class.getSimpleName();

        @Override
        protected ArrayList<MovieSpecification> doInBackground(String... strings) {
            // so that they can be closed in the finally block.
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            // Will contain the raw JSON response as a string.
            String forecastJsonStr = null;

            try {
                // Construct the URL for the OpenWeatherMap query
                // Possible parameters are avaiable at OWM's forecast API page, at
                // http://openweathermap.org/API#forecast

                //http://api.themoviedb.org/3/movie/top_rated?page=1&api_key=d19539dd75c57ddc49feeaa144b95dba


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


            System.out.println("JSON STRING: " + forecastJsonStr);
            //FIXME
            try {
                return getMovieDataFromJson(forecastJsonStr);
            } catch (JSONException e) {
                e.printStackTrace();
            }
            //return new String[]{"http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg","http://image.tmdb.org/t/p/w185//nBNZadXqJSdt05SHLqgT0HuC5Gm.jpg"};

            return null;

        }

        @Override
        protected void onPostExecute(ArrayList<MovieSpecification> movies) {

            if (movies != null) {
                mForecastAdapter.clear();

                for (MovieSpecification mSpec : movies) {
                    mForecastAdapter.add(mSpec);
                }


            }
        }
    }


    private ArrayList<MovieSpecification> getMovieDataFromJson(String forecastJsonStr)
            throws JSONException {

        // These are the names of the JSON objects that need to be extracted.
        final String OWM_LIST = "results";


        final String ID = "id";
        final String TITLE = "original_title";
        final String SYNOPSIS = "overview";
        final String POSTER = "poster_path";
        final String RATING = "vote_average";
        final String RELEASEDATE = "release_date";


        JSONObject pageJSON = new JSONObject(forecastJsonStr);
        JSONArray movieArray = pageJSON.getJSONArray(OWM_LIST);


        ArrayList<MovieSpecification> resultStrs = new ArrayList<>();


        for (int i = 0; i < movieArray.length(); i++) {
            // For now, using the format "Day, description, hi/low"
            String day;
            String description;
            String highAndLow;

            JSONObject singleMovieJSON = movieArray.getJSONObject(i);

            // description is in a child array called "weather", which is 1 element long.
            String movieId = singleMovieJSON.getString(ID);
            String movieTitle = singleMovieJSON.getString(TITLE);
            String movieSynopsis = singleMovieJSON.getString(SYNOPSIS);
            String moviePoster = singleMovieJSON.getString(POSTER);
            String movieRating = singleMovieJSON.getString(RATING);
            String movieReleaseDate = singleMovieJSON.getString(RELEASEDATE);

            resultStrs.add(new MovieSpecification(movieId, movieTitle, moviePoster, movieSynopsis, movieRating, movieReleaseDate));
        }

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

    /**
     * Prepare the weather high/lows for presentation.
     */
    private String formatHighLows(double high, double low) {

        SharedPreferences prefs = PreferenceManager.getDefaultSharedPreferences(getActivity());
        String location = prefs.getString(getString(R.string.pref_location_key), getString(R.string.pref_location_default));
        String unit = prefs.getString(getString(R.string.pref_unit_key), "Metric");

        if (unit.equals("Imperial")) {
            high = (high * 1.8) + 32;
            low = (low * 1.8) + 32;
        }

        // For presentation, assume the user doesn't care about tenths of a degree.
        long roundedHigh = Math.round(high);
        long roundedLow = Math.round(low);

        String highLowStr = roundedHigh + "/" + roundedLow;
        return highLowStr;
    }


}
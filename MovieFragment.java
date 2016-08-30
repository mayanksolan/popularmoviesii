package com.application.mays.popularmoviesii;

import android.app.Fragment;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

public class MovieFragment extends Fragment {

    GridView mGridView;
    ArrayList<String> movieName;
    ArrayList<String> moviePosterLink;
    ArrayList<String> plot;
    ArrayList<String> voteAverage;
    ArrayList<String> releaseDate;
    Context context = getActivity();

    //@Override
    //public void onCreate(Bundle savedInstanceState) {
      //  super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_main);
//    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MoviesData asyncTask = new MoviesData();
        View rootView = inflater.inflate(R.layout.movie_fragment, container, false);
        Log.d("Mays","View Inflated");
        mGridView = (GridView) rootView.findViewById(R.id.gridView);
        Log.d("Mays","gridView ID referenced");
        if (isOnline(context)) {
            asyncTask.execute("popular");
        } else {
            Toast.makeText(getActivity(), "You are NOT online", Toast.LENGTH_SHORT).show();
        }
        mGridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getActivity(), MovieDesc.class);
                intent.putExtra("Movie String", movieName.get(position));
                intent.putExtra("Movie Poster Link", moviePosterLink.get(position));
                intent.putExtra("Movie Plot", plot.get(position));
                intent.putExtra("Movie Vote Average", voteAverage.get(position));
                intent.putExtra("Movie Release Date", releaseDate.get(position));
                startActivity(intent);
            }
        });
        return rootView;
    }

    private boolean isOnline(Context context) {
        ConnectivityManager mngr = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = mngr.getActiveNetworkInfo();
        return !(info == null || (info.getState() != NetworkInfo.State.CONNECTED));
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.popular_menu:
                Toast.makeText(getActivity(), "Popular Rated Movies Selected", Toast.LENGTH_SHORT).show();
                MoviesData asyncTask1 = new MoviesData();
                if (isOnline(context)) {
                    asyncTask1.execute("popular");
                } else {
                    Toast.makeText(getActivity(), "You are NOT online", Toast.LENGTH_SHORT).show();
                }
                return true;
            case R.id.top_rated_menu:
                Toast.makeText(getActivity(), "Top Rated Movies Selected", Toast.LENGTH_SHORT).show();
                MoviesData asyncTask2 = new MoviesData();
                if (isOnline(context)) {
                    asyncTask2.execute("top_rated");
                } else {
                    Toast.makeText(getActivity(), "You are NOT online", Toast.LENGTH_SHORT).show();
                }
                return true;
        }
        return true;
    }

    class MoviesData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... params) {
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;
            String jsonStr = "";
            final String moviesType = params[0];
            InputStream inputStream;
            String line;
            try {
                String baseURL = "https://api.themoviedb.org/3/movie/";
                final String apiParamKey = "api_key";
                Uri builtUri = Uri.parse(baseURL)
                        .buildUpon()
                        .appendPath(moviesType)
                        .appendQueryParameter(apiParamKey, BuildConfig.MOVIE_DATABASE_API_KEY)
                        .build();
                Log.d("MovieJson", builtUri.toString());
                URL url;
                url = new URL(builtUri.toString());
                urlConnection = (HttpURLConnection) url.openConnection();
                inputStream = urlConnection.getInputStream();
                StringBuilder buffer = new StringBuilder();
                if (inputStream != null) {
                    reader = new BufferedReader(new InputStreamReader(inputStream));
                }
                if (reader != null) {
                    while ((line = reader.readLine()) != null) {
                        buffer.append(line).append("\n");
                    }
                }
                jsonStr = buffer.toString();
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (urlConnection != null) {
                    urlConnection.disconnect();
                }
                if (reader != null) {
                    try {
                        reader.close();
                    } catch (final IOException e) {
                        Log.e("Mayank", "Error closing stream", e);
                    }
                }
            }
            return jsonStr;
        }

        @Override
        protected void onPostExecute(String jsonStr) {
            String subplot = jsonStr.substring(1, 20);
            Log.d("JsonStr", subplot);
            movieName = new ArrayList<>();
            moviePosterLink = new ArrayList<>();
            plot = new ArrayList<>();
            voteAverage = new ArrayList<>();
            releaseDate = new ArrayList<>();
            try {
                JSONObject reader = new JSONObject(jsonStr);
                JSONArray results = reader.getJSONArray("results");
                for (int i = 0; i < results.length(); i++) {
                    JSONObject jsonObject = results.getJSONObject(i);
                    movieName.add(jsonObject.getString("original_title"));
                    moviePosterLink.add(jsonObject.getString("poster_path"));
                    plot.add(jsonObject.getString("overview"));
                    voteAverage.add(jsonObject.getString("vote_average"));
                    releaseDate.add(jsonObject.getString("release_date"));
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
            mGridView.setAdapter(new MovieAdapter(getActivity(), movieName, moviePosterLink, plot, voteAverage, releaseDate));
        }
    }
}

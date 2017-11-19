package com.example.android.reelmovies;

import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.TextView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.List;


/**
 * ~ NOW PLAYING MOVIES ~ Movies Fragment
 * This class loads information about a single movie from the internet database URL
 * and displays it in the list view on the movie fragment UI layout
 *
 * SHOULD USE ASYNC TASK LOADER INSTEAD FOR BETTER EFFICIENCY & MEMORY MANAGEMENT
 */
public class NowPlayingFragment extends Fragment {

    /**
     * GLOBAL VARIABLES
     */
    //Make these variable global so that they can be used
    //to update the UI from multiple methods
    private MovieAdapter movieAdapter;
    private ListView movieListView;
    private TextView noDataFound;
    private View loadingbar;
    //Tag for the log messages
    public static final String LOG_TAG = NowPlayingFragment.class.getSimpleName();
    //URL to query the movie database for collection of movies and  their details
    public static final String NOWPLAYING_MOVIES_URL =
            "https://api.themoviedb.org/3/movie/now_playing?api_key=b3caf294f230b94bb4d393c37870f17d";
    public String movie_website_url = "";



    public NowPlayingFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        //Get reference to inflate the UI layout for this movie fragment
        View rootView = inflater.inflate(R.layout.nowplayingfragment,container,false);

        //Get reference to list view in the movie UI layout
        movieListView = (ListView) rootView.findViewById(R.id.nowPlayingListView);

        //Textview that alerts when list is empty/no data found
        noDataFound = (TextView) rootView.findViewById(R.id.noDatatextView);
        movieListView.setEmptyView(noDataFound);
        //Progress indicator that displays when data is loading
        loadingbar = rootView.findViewById(R.id.progressBar);

        //Create a new adapter that takes an empty list of movies just to
        //initialize UI list view layout while data loads in background
        movieAdapter = new MovieAdapter(getActivity(), new ArrayList<Movie>());
        movieListView.setAdapter(movieAdapter);

        //Set item click listener on the list view, which sends an intent to a web browser
        //to the google website with more information about the selected movie
        movieListView.setOnItemClickListener(new AdapterView.OnItemClickListener(){

            public void onItemClick(AdapterView<?> adapterView, View view, int position, long l){
                //Find the current movie that was clicked on
                Movie currentMovie = movieAdapter.getItem(position);
                String movieName = currentMovie.getMovieTitle();
                //Generate movie's website using the movie's unique id
                movie_website_url = "http://www.google.com/#q=" + movieName;

                //Convert the String URL into a URI object (to pass into the intent)
                Uri movieUri = Uri.parse(movie_website_url);
                //Create a new intent to view the movie URI
                Intent movieWebsiteIntent = new Intent (Intent.ACTION_VIEW, movieUri);
                //Send the intent to launch a new activity
                startActivity(movieWebsiteIntent);

            }
        }); //end of click listener

        //Start the AsyncTask to fetch the movie data in background thread
        MovieAsyncTask task = new MovieAsyncTask();
        task.execute(NOWPLAYING_MOVIES_URL);

        //Inflate this view
        return rootView;
    }



    /**
     * Performs the network request on a background thread, but since we
     * should not update the UI from a background thread, we return a
     * list of movies as a result to be passed to a method on the main
     * thread
     *
     * SHOULD USE ASYNC TASK LOADER INSTEAD FOR BETTER EFFICIENCY & MEMORY MANAGEMENT
     */
    private class MovieAsyncTask extends AsyncTask<String, Void, List<Movie>> {

        protected List<Movie> doInBackground(String... url){
            //create URL object
            URL urlmovies = createURL(NOWPLAYING_MOVIES_URL); //custom method call
            //Generate Json data from URL
            String moviesJsonResponse = getJSONFromURL(urlmovies);

            //Extract all required fields from JSON response to fill movie object
            //Then creates and returns a list of those movies
            List<Movie> movies = extractFeaturesFromJson(moviesJsonResponse); //custom method call

            return movies;
        }

        /**
         * This method runs on the main UI thread after the background work has
         * been completed. This method receives input from the doInBackground method,
         * and updates the screen with the given movie list
         */
        protected void onPostExecute(List<Movie> movies){
            //Hide loading progress bar indicator after data has been fetched
            loadingbar.setVisibility(View.GONE);

            //if no movie data is returned
            noDataFound.setText("No Movies Found");

            //Clear the adapter of any previous movies
            //to prepare it for new movie list data
            movieAdapter.clear();

            //If there is a valid list of movies, then add them to the adapter
            //This will trigger the UI list view layout to update
            if(movies != null && !movies.isEmpty()){
                movieAdapter.addAll(movies);
            }

        }

        /**
         * Returns new URL object form the given URL string
         * @param strUrl
         * @return
         */
        private URL createURL(String strUrl){
            URL url = null;
            try{
                url = new URL(strUrl);
            }catch (MalformedURLException exc){
                Log.e(LOG_TAG, "Problem building the URL", exc);
                return null;
            }

            return url;
        }

        private String getJSONFromURL(URL url){

            //Perform HTTP request to the URL and receive a JSON response
            String jsonResponse = "";
            try{
                jsonResponse = makeHttpRequest(url); //custom method call
            }catch (IOException e){

            }

            return jsonResponse;
        }

        /**
         * Makes an HTTP request to the given URL and return a String Json response
         */
        private String makeHttpRequest(URL url) throws IOException{
            String jsonResponse = "";
            HttpURLConnection urlConnection = null;
            InputStream inputStream = null;

            //If the URL is null, return early
            if (url == null) {
                return jsonResponse;
            }

            //Safely open and make URL connection, handle error if unsuccessful
            try{
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.setReadTimeout(10000); //milliseconds
                urlConnection.setConnectTimeout(15000); //milliseconds
                urlConnection.connect();

                //If the request was successful (code 200)
                //then read the input stream and parse response
                if(urlConnection.getResponseCode() == 200){
                    inputStream = urlConnection.getInputStream();
                    jsonResponse = readFromStream(inputStream); //custom method call
                }else{
                    Log.e(LOG_TAG,"Error response code: " + urlConnection.getResponseCode());
                }

            }catch (IOException e){

            }finally {
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if(inputStream != null){
                    //function must handle java.io.IOException here
                    inputStream.close();
                }
            }

            return jsonResponse;
        }

        /**
         * Convert InputStream into a readable String which contains the
         * whole, reassembled JSON response from the server
         */
        private String readFromStream(InputStream inputStream) throws IOException{
            //Used to reassemble Json data from broken strings, back into one whole string object
            StringBuilder incomingJSON = new StringBuilder();

            //if stream connection has incoming data
            if(inputStream != null){
                //receives raw binary bits of data from stream connection
                InputStreamReader inputStreamReader = new InputStreamReader(inputStream, Charset.forName("UTF-8"));
                //Converts those raw bits into individual readable characters
                BufferedReader reader = new BufferedReader(inputStreamReader);
                //accepts one line of characters/letters at a time from the reader
                String line = reader.readLine();
                while (line != null){
                    //attaches each incoming line onto the previous line
                    incomingJSON.append(line);
                    line = reader.readLine();
                }
            }

            return incomingJSON.toString();
        }

        /**
         * Returns a list of movie objects by parsing out information
         * about a movie from the the input JSON response string
         */
        private List<Movie> extractFeaturesFromJson(String movieJSON){

            //Create an empty ArrayList to start adding movie objects
            List<Movie> moviesArray = new ArrayList<>();

            //Safely try to parse the JSON response string. If there is a problem such
            //as incorrect JSON path format, a JSONException will be thrown
            //Catch and handle the exception so the app doesn't crash
            try{
                JSONObject rootNodeJson = new JSONObject(movieJSON);
                JSONArray resultsNode = rootNodeJson.getJSONArray("results");

                for(int i = 0; i < resultsNode.length(); i++){
                    //Get a single movie at position i in the list of movie objects
                    JSONObject currentMovie = resultsNode.getJSONObject(i);

                    //Get specific details from each movie
                    int id = currentMovie.getInt("id");
                    double votes = currentMovie.getDouble("vote_average");
                    float rating = Double.valueOf(votes).floatValue(); //convert vote to rating type
                    String title = currentMovie.getString("title");
                    String imagePath = currentMovie.getString("poster_path");
                    boolean adultRating = currentMovie.getBoolean("adult");
                    String date = currentMovie.getString("release_date");

                    //Create a new movie object and fill in it's attributes gathered
                    //from the url JSON response
                    //and add it to the array list of movies
                    Movie movie = new Movie(id,imagePath,title,rating,adultRating,date);
                    moviesArray.add(movie);
                }

            }catch (JSONException e){
                Log.e(LOG_TAG, "Problem parsing the movie detail JSON results", e);
            }

            return moviesArray;
        }

    }//end of MovieAsyncTask class


    /**
     *  NOTES TO SELF:
     *  Append "&page=1" to the end of MOVIE_REQUEST_URL to specify which page # of movies to load
     * Second key from rootNode is named "page" that has value of current page # of results
     * Fourth key from rootNode is named "total_pages" that list total number of possible pages to search
     *
     *
     * Get IMAGE url
     https://api.themoviedb.org/3/configuration?api_key=b3caf294f230b94bb4d393c37870f17d
     -from root, get first key "images", which has value object
     -in that object, get string value from key "base_url"
     -in that same object, Array value from key "poster_sizes"
     -loop thru that Array, get index (0) or string element that matches "w92" or "154"
     Then get specific movie id and insert between (movie/     /images?)in this path
     https://api.themoviedb.org/3/movie/284053/images?api_key=b3caf294f230b94bb4d393c37870f17d
     -in root, get key "posters" which has value Array
     -loop thru that Array, get first element which is an object
     -in that object, get string value from key "file_path"
     image file = IMAGE URL = base_url + poster_sizes + file_path
     (https://image.tmdb.org/t/p/w500/8uO0gUM8aNqYLs1OsTBQiXu0fEv.jpg)

     ~ EXAMPLE CODE ~
     ImageView image = (ImageView) view.findViewById(R.id.banner);
     ImageLoaderConfiguration configuration=new ImageLoaderConfiguration.Builder(getActivity()).build();
     ImageLoader.getInstance().init(configuration);
     DisplayImageOptions options=new DisplayImageOptions.Builder().cacheOnDisc(true).build();
     ImageLoader loader=ImageLoader.getInstance();
     loader.displayImage("https://www.google.com/images/srpr/logo11w.png", image, options);


     */
}

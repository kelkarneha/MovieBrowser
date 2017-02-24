package administrator.example.org.moviebrowser;

import android.net.Uri;
import android.os.AsyncTask;
import android.util.Log;

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
import java.util.List;

/**
 * Created by Administrator on 1/29/2016.
 */
public class getData {
    private String LogTag = getData.class.getSimpleName();
    private String tmdbData;
    private Uri TMDBFinalUri;
    private String[] TMDBAPIUrl;
    private List<Movie> TMDBFinalMovies = new ArrayList<Movie>();
    private String rawData;


    public List<Movie> getTMDBFinalMovies() {
        return TMDBFinalMovies;
    }

    public String[] createTmdbUrl(boolean popular) {
        String TMDB_API_BASE_URL_POPULAR = "http://api.themoviedb.org/3/movie/popular";
        String TMDB_API_BASE_URL_TOPRATED = "http://api.themoviedb.org/3/movie/top_rated";
        final String API_KEY = "api_key";
        final String SORT_BY = "sort_by";
        final String VOTE_COUNT = "vote_count.gte";

        if(popular){
            TMDBFinalUri = Uri.parse(TMDB_API_BASE_URL_POPULAR).buildUpon()
                    .appendQueryParameter(API_KEY, "api key")
                    .appendQueryParameter(VOTE_COUNT, "50")
                    .build();
        } else {
            TMDBFinalUri = Uri.parse(TMDB_API_BASE_URL_TOPRATED).buildUpon()
                    .appendQueryParameter(API_KEY, "api key")
                    .appendQueryParameter(VOTE_COUNT, "50")
                    .build();
        }

        TMDBAPIUrl = new String[] {TMDBFinalUri.toString()};
        return TMDBAPIUrl;

        //  return mDestinationUri != null;
    }

 /*   public List<Movie> JSONParser (String rawData){
        try{
            JSONObject jsonData = new JSONObject(rawData);
            JSONArray itemsArray = jsonData.getJSONArray(TMDB_RESULTS);
            for(int i=0; i<itemsArray.length();i++){
                JSONObject jsonMovie = itemsArray.getJSONObject(i);
                String movieTitle = jsonMovie.getString(TMDB_ORIGINAL_TITLE);
                String posterPath = "http://image.tmdb.org/t/p/w185" + jsonMovie.getString(TMDB_POSTER_PATH);
                String overview = jsonMovie.getString(TMDB_OVERVIEW);
                String topRated = jsonMovie.getString(TMDB_VOTE_AVERAGE);
                String releaseDate = jsonMovie.getString(TMDB_RELEASE_DATE);

                Movie movieObject = new Movie(movieTitle, posterPath, overview, topRated, releaseDate);
                TMDBFinalMovies.add(movieObject);

            }
            for (Movie singleMovie: TMDBFinalMovies){
                Log.v(LogTag, singleMovie.toString());
            }


        } catch (JSONException jsone){
            jsone.printStackTrace();;
            Log.e(LogTag, "Error processing JSON");
        } return TMDBFinalMovies;
    }*/


        //public String getTmdbData() {
       // return tmdbData;
    //}

    public class downloadMovieData extends AsyncTask<String, Void, String>{

        protected void onPostExecute (String rawData){

            if(rawData != null){
                tmdbData = rawData;
                Log.v(LogTag, tmdbData);

                final String TMDB_RESULTS = "results";
                final String TMDB_POSTER_PATH = "poster_path";
                final String TMDB_OVERVIEW = "overview";
                final String TMDB_RELEASE_DATE ="release_date";
                final String TMDB_ORIGINAL_TITLE = "original_title";
                final String TMDB_VOTE_AVERAGE = "vote_average";
                final String TMDB_ID = "id";

                try{
                    JSONObject jsonData = new JSONObject(rawData);
                    JSONArray itemsArray = jsonData.getJSONArray(TMDB_RESULTS);
                    for(int i=0; i<itemsArray.length();i++){
                        int _id = 0;
                        JSONObject jsonMovie = itemsArray.getJSONObject(i);
                        String movieTitle = jsonMovie.getString(TMDB_ORIGINAL_TITLE);
                        String posterPath = "http://image.tmdb.org/t/p/w185" + jsonMovie.getString(TMDB_POSTER_PATH);
                        String overview = jsonMovie.getString(TMDB_OVERVIEW);
                        String topRated = jsonMovie.getString(TMDB_VOTE_AVERAGE);
                        String releaseDate = jsonMovie.getString(TMDB_RELEASE_DATE);
                        String id = jsonMovie.getString(TMDB_ID);

                        Movie movieObject = new Movie(_id, movieTitle, posterPath, overview, topRated, releaseDate, id);
                        TMDBFinalMovies.add(movieObject);

                    }
                    for (Movie singleMovie: TMDBFinalMovies){
                        Log.v(LogTag, singleMovie.toString());
                    }


                } catch (JSONException jsone){
                    jsone.printStackTrace();;
                    Log.e(LogTag, "Error processing JSON");
                }
            }
        }

        protected String doInBackground (String... params){
            HttpURLConnection urlConnection = null;
            BufferedReader reader = null;

            if(params == null){
                return null;
            }

            try{
                URL url = new URL(params[0]);
                urlConnection = (HttpURLConnection) url.openConnection();
                urlConnection.setRequestMethod("GET");
                urlConnection.connect();

                InputStream inputStream = urlConnection.getInputStream();
                if(inputStream == null){
                    return null;
                }

                StringBuffer buffer = new StringBuffer();

                reader = new BufferedReader(new InputStreamReader(inputStream));

                String line;

                while ((line = reader.readLine())!=null){
                    buffer.append(line + "\n");
                }

                rawData = buffer.toString();

            }catch(IOException e){
                Log.e(LogTag, "Error", e);
                return null;
            }finally{
                if(urlConnection != null){
                    urlConnection.disconnect();
                }
                if (reader != null){
                    try{
                        reader.close();
                    }catch(final IOException e){
                        Log.e(LogTag, "Error closing stream", e);
                    }
                }
            }return rawData;
        }
    }
}

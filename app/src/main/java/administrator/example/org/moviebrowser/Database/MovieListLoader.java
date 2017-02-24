package administrator.example.org.moviebrowser.Database;


import android.content.AsyncTaskLoader;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.provider.BaseColumns;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import administrator.example.org.moviebrowser.Movie;

/**
 * Created by Administrator on 5/3/2016.
 */
public class MovieListLoader extends AsyncTaskLoader<List<Movie>> {
    private static final String LOG_TAG = MovieListLoader.class.getSimpleName();
    private List<Movie> favoriteMovies;
    private ContentResolver contentResolver;
    private Cursor cursor;

    public MovieListLoader(Context context, Uri uri, ContentResolver contentResolver1){
        super(context);
        contentResolver = contentResolver1;
    }

    @Override
    public List<Movie> loadInBackground() {
        String[] projection = { BaseColumns._ID,
        MovieContract.MovieColumns.MOVIE_RELEASE_DATE,
        MovieContract.MovieColumns.MOVIE_OVERVIEW,
        MovieContract.MovieColumns.MOVIE_POSTER,
        MovieContract.MovieColumns.MOVIE_TITLE,
        MovieContract.MovieColumns.MOVIE_TOP_RATED,
        MovieContract.MovieColumns.MOVIE_TMDB_ID};

        List<Movie> favorites = new ArrayList<Movie>();
        cursor = contentResolver.query(MovieContract.URI_TABLE, projection, null, null, null);

        if(cursor != null){
            if(cursor.moveToFirst()){
                do{
                    int _id = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
                    String release_date = cursor.getString((cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_RELEASE_DATE)));
                    String overview = cursor.getString((cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_OVERVIEW)));
                    String poster = cursor.getString((cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_POSTER)));
                    String title = cursor.getString((cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_TITLE)));
                    String top_rated = cursor.getString((cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_TOP_RATED)));
                    String tmdb_id = cursor.getString((cursor.getColumnIndex(MovieContract.MovieColumns.MOVIE_TMDB_ID)));
                    Movie movie = new Movie(_id, title, poster, overview, top_rated, release_date, tmdb_id);
                    favorites.add(movie);
                } while (cursor.moveToNext());
            }
        }
        Log.d(LOG_TAG, "loading finished");
        cursor.close();
        return favorites;
    }


    @Override
    public void deliverResult(List<Movie> favorites) {
        if(isReset()){
            if(favorites != null){
                cursor.close();
            }
        }
        if(favorites == null || favorites.size() == 0){
            Log.d(LOG_TAG, "No data returned");
        }

        favoriteMovies = favorites;
        super.deliverResult(favorites);
        cursor.close();

    }

    @Override
    protected void onStartLoading() {
        if(favoriteMovies != null){
            deliverResult(favoriteMovies);
        }

        if(takeContentChanged() || favoriteMovies == null){
            forceLoad();
        }
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }

    @Override
    protected void onReset() {
        onStopLoading();
        if(cursor != null){
            cursor.close();
        }

        favoriteMovies = null;
    }

    @Override
    public void onCanceled(List<Movie> data) {
        super.onCanceled(data);
        if(cursor != null){
            cursor.close();
        }
    }

    @Override
    protected void onForceLoad() {
        super.onForceLoad();
    }
}

package administrator.example.org.moviebrowser.Database;

import android.net.Uri;
import android.provider.BaseColumns;

/*
For all work on databases, have referred to another Android course from Udemy that I have duly purchased
*/

/**
 * Created by Administrator on 4/15/2016.
 */
public class MovieContract {
    public interface MovieColumns{
        String MOVIE_ID = "_id";
        String MOVIE_TITLE = "movie_title";
        String MOVIE_POSTER = "movie_poster";
        String MOVIE_OVERVIEW = "movie_overview";
        String MOVIE_TOP_RATED = "movie_top_rated";
        String MOVIE_RELEASE_DATE = "movie_release_date";
        String MOVIE_TMDB_ID = "movie_id";
    }

    public static final String CONTENT_AUTHORITY = "administrator.example.org.moviebrowser.Database.provider";
    public static final Uri BASE_CONTENT_URI = Uri.parse("content://"+CONTENT_AUTHORITY);

    private static final String PATH_MOVIE = "movie";
    public static final Uri URI_TABLE = Uri.parse(BASE_CONTENT_URI.toString() + "/" + PATH_MOVIE);

    public static final String[] TOP_LEVEL_PATHS = {PATH_MOVIE};

    public static class Movie implements MovieColumns, BaseColumns {
        public static final Uri CONTENT_URI =
                BASE_CONTENT_URI.buildUpon().appendEncodedPath(PATH_MOVIE).build();
        public static final String CONTENT_TYPE = "vnd.android.cursor.dir/"+CONTENT_AUTHORITY+".movie";
        public static final String CONTENT_ITEM_TYPE = "vnd.android.cursor.item/"+CONTENT_AUTHORITY+".movie";

        public static Uri buildMovieUri(String movieID){
            return CONTENT_URI.buildUpon().appendEncodedPath(movieID).build();
        }

        public static String getMovieID(Uri uri){
            return uri.getPathSegments().get(1);
        }
    }
}
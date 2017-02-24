package administrator.example.org.moviebrowser.Database;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.provider.BaseColumns;
import android.support.annotation.Nullable;
import android.text.TextUtils;
import android.util.Log;

// Have referred to lectures from Udemy - have purchased Android tutorial course from there as well

/**
 * Created by Administrator on 4/15/2016.
 */
public class MovieProvider extends ContentProvider{
    private MovieDatabase mOpenHelper;
    private static String TAG = MovieProvider.class.getSimpleName();
    private static final UriMatcher sUriMatcher = buildUriMatcher();

    private static final int MOVIE = 100;
    private static final int MOVIE_ID = 101;

    private static UriMatcher buildUriMatcher(){
        final UriMatcher matcher = new UriMatcher(UriMatcher.NO_MATCH);
        final String authority = MovieContract.CONTENT_AUTHORITY;
        matcher.addURI(authority, "movie", MOVIE);
        matcher.addURI(authority, "movie/*", MOVIE_ID);
        return matcher;
    }

    @Override
    public boolean onCreate() {
        mOpenHelper = new MovieDatabase(getContext());
        return true;
    }

    private void deleteDatabase(){
        mOpenHelper.close();
        MovieDatabase.deleteDatabase(getContext());
        mOpenHelper = new MovieDatabase(getContext());
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        final int match = sUriMatcher.match(uri);

        switch (match){
            case MOVIE:
                return MovieContract.Movie.CONTENT_TYPE;
            case MOVIE_ID:
                return MovieContract.Movie.CONTENT_ITEM_TYPE;
            default:
                throw new IllegalArgumentException(("Unknown Uri: "+ uri));
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        final SQLiteDatabase db = mOpenHelper.getReadableDatabase();
        final int match = sUriMatcher.match(uri);

        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();
        queryBuilder.setTables(MovieDatabase.Tables.MOVIE);

        switch(match){
            case MOVIE:
                break;
            case MOVIE_ID:
                String id = MovieContract.Movie.getMovieID(uri);
                queryBuilder.appendWhere(BaseColumns._ID +"=" + id);
                break;
            default:
                throw new IllegalArgumentException(("Unknown Uri: "+ uri));
        }
        Cursor cursor = queryBuilder.query(db, projection, selection, selectionArgs, null, null, sortOrder);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {
        Log.v(TAG, "insert(uri=" + uri + ", values" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch(match){
            case MOVIE:
                long recordId = db.insertOrThrow(MovieDatabase.Tables.MOVIE, null, values);
                return MovieContract.Movie.buildMovieUri(String.valueOf(recordId));
            default:
                throw new IllegalArgumentException(("Unknown Uri: "+ uri));
        }
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        Log.v(TAG, "insert(uri=" + uri + ", values" + values.toString());
        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);
        String selectionCriteria = selection;

        switch(match){
            case MOVIE:
                break;
            case MOVIE_ID:
                String id = MovieContract.Movie.getMovieID(uri);
                selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ")" : "");
                break;
            default:
                throw new IllegalArgumentException(("Unknown Uri: "+ uri));
        }

        return db.update(MovieDatabase.Tables.MOVIE, values, selectionCriteria, selectionArgs);
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        Log.v(TAG, "delete(uri=" + uri);

        if(uri.equals(MovieContract.BASE_CONTENT_URI)){
            deleteDatabase();
            return 0;
        }

        final SQLiteDatabase db = mOpenHelper.getWritableDatabase();
        final int match = sUriMatcher.match(uri);

        switch(match){
            case MOVIE_ID:
                String id = MovieContract.Movie.getMovieID(uri);
                String selectionCriteria = BaseColumns._ID + "=" + id
                        + (!TextUtils.isEmpty(selection) ? "AND (" + selection + ")" : "");
                return db.delete( MovieDatabase.Tables.MOVIE, selectionCriteria, selectionArgs);
            default:
                throw new IllegalArgumentException(("Unknown Uri: "+ uri));
        }
    }
}
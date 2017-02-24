package administrator.example.org.moviebrowser.Database;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.provider.BaseColumns;

/**
 * Created by Administrator on 4/15/2016.
 */
public class MovieDatabase extends SQLiteOpenHelper{
    private static final String TAG = MovieDatabase.class.getSimpleName();
    private static final String DATABASE_NAME = "movie.db";
    private static final int DATABASE_VERSION = 2;
    private final Context mContext;

    interface Tables{
        String MOVIE = "movie";
        //String TRAILERS = "trailers";
        //String REVIEWS = "reviews";
    }

    public MovieDatabase(Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
        mContext = context;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL("CREATE TABLE " + Tables.MOVIE + " ("
                + BaseColumns._ID +" INTEGER PRIMARY KEY AUTOINCREMENT, "
                + MovieContract.MovieColumns.MOVIE_TITLE +" TEXT NOT NULL, "
                + MovieContract.MovieColumns.MOVIE_POSTER +" TEXT NOT NULL, "
                + MovieContract.MovieColumns.MOVIE_TOP_RATED +" TEXT NOT NULL, "
                + MovieContract.MovieColumns.MOVIE_OVERVIEW +" TEXT NOT NULL, "
                + MovieContract.MovieColumns.MOVIE_RELEASE_DATE +" TEXT NOT NULL, "
                + MovieContract.MovieColumns.MOVIE_TMDB_ID +" TEXT NOT NULL)");
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        int version = oldVersion;
        if(version == 1){
            //Code to update database from version 1 to 2
            version = 2;
        }
        if(version != DATABASE_VERSION){
            db.execSQL("DROP TABLE IF EXISTS "+ Tables.MOVIE);
            onCreate(db);
        }

    }

    public static void deleteDatabase(Context context){
        context.deleteDatabase(DATABASE_NAME);
    }
}

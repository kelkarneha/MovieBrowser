package administrator.example.org.moviebrowser;


import android.content.ActivityNotFoundException;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.net.Uri;
import android.os.Bundle;
import android.provider.BaseColumns;
import android.support.v7.app.ActionBarActivity;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

import administrator.example.org.moviebrowser.Database.MovieContract;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MovieDetails extends ActionBarActivity{

    public static final String API_URL = "http://api.themoviedb.org/3/movie/";
    public static final String API_KEY = "a606a866cc15ed39312dd068cbb8c2f0";

    private ListView trailerListView;
    private ArrayAdapter<String> trailerListAdapter;
    movieTrailerObject trailersList2 = new movieTrailerObject();
    List<Result> trailersList3 = new ArrayList<Result>();
    ArrayList<String> trailersKey = new ArrayList<String>();
    ArrayList<String> trailersList = new ArrayList<String>();

    private ListView reviewListView;
    private ArrayAdapter<String> reviewListAdapter;
    movieReviewObject reviewsList2 = new movieReviewObject();
    List<ResultReview> reviewsList3 = new ArrayList<ResultReview>();
    ArrayList<String> reviewsList = new ArrayList<String>();

    Button favButtonOn;
    Button favButtonOff;
    private ContentResolver mContentResolver;
    private Cursor cursor;
    private String movieTmdbIdCheck;
    boolean movieInDb = false;
    int movieInDbId = 0;
    public Movie movieToBeSaved = new Movie(0, null, null, null, null, null, null);

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putSerializable("Movie displayed", movieToBeSaved);
        outState.putStringArrayList("Trailers", trailersList);
        Log.d("onSave", trailersList.toString());
        outState.putStringArrayList("Trailers key", trailersKey);
        outState.putStringArrayList("Reviews", reviewsList);
        outState.putBoolean("Is in favorites", movieInDb);
    }

    @Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        if(savedInstanceState != null){
            Movie movie = (Movie) savedInstanceState.getSerializable("Movie displayed");
            boolean movieInDb = (Boolean) savedInstanceState.getBoolean("Is in favorites");
            trailersList = (ArrayList<String>) savedInstanceState.getStringArrayList("Trailers");
            Log.d("onRestore", trailersList.toString());
            trailersKey = (ArrayList<String>) savedInstanceState.getStringArrayList("Trailers key");
            reviewsList = (ArrayList<String>) savedInstanceState.getStringArrayList("Reviews");

            TextView movie_title = (TextView) findViewById(R.id.movie_title);
            movie_title.setText(movie.getMovieTitle());

            TextView movie_release = (TextView) findViewById(R.id.release);
            movie_release.setText("Release Date: " + movie.getRelease_date());

            TextView movie_rating = (TextView) findViewById(R.id.rating);
            movie_rating.setText("Rating: " + movie.getTop_rated());

            TextView movie_overview = (TextView) findViewById(R.id.overview);
            movie_overview.setText(movie.getOverview());

            ImageView movie_poster = (ImageView) findViewById(R.id.movie_poster);
            Picasso.with(this).load(movie.getPosterPath())
                    .error(R.drawable.placeholder)
                    .placeholder(R.drawable.placeholder)
                    .into(movie_poster);

            trailerListAdapter = new ArrayAdapter<String>(this, R.layout.trailers_row, R.id.rowTextView, trailersList);
            trailerListView.setAdapter(trailerListAdapter);
            /*trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                    String key = trailersKey.get(position);
                    try {
                        Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                        startActivity(intent);
                    } catch (ActivityNotFoundException e) {
                        Intent intent = new Intent(Intent.ACTION_VIEW,
                                Uri.parse("http://www.youtube.com/watch?v=" + key));
                        startActivity(intent);
                    }
                }
            });*/

            reviewListAdapter = new ArrayAdapter<String>(this, R.layout.trailers_row, R.id.rowTextView, reviewsList);
            reviewListView.setAdapter(reviewListAdapter);

            if(movieInDb){
                favButtonOff.setVisibility(findViewById(R.id.fav_btn_off).INVISIBLE);
                favButtonOn.setVisibility(findViewById(R.id.fav_btn_on).VISIBLE);
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(false);
        Intent intent = getIntent();
        final Movie movie = (Movie) intent.getSerializableExtra("Movie_transfer");
        movieToBeSaved = movie;
        movieTmdbIdCheck = movie.getTmdb_id();
        mContentResolver = getContentResolver();
        String[] projection = { BaseColumns._ID,
                MovieContract.MovieColumns.MOVIE_RELEASE_DATE,
                MovieContract.MovieColumns.MOVIE_OVERVIEW,
                MovieContract.MovieColumns.MOVIE_POSTER,
                MovieContract.MovieColumns.MOVIE_TITLE,
                MovieContract.MovieColumns.MOVIE_TOP_RATED,
                MovieContract.MovieColumns.MOVIE_TMDB_ID};
        String selection = MovieContract.MovieColumns.MOVIE_TMDB_ID + " LIKE " + "'" + movieTmdbIdCheck + "'";

        cursor = mContentResolver.query(MovieContract.URI_TABLE, projection, selection, null, null);

        if(cursor != null && cursor.moveToFirst()){
            if(cursor.getCount()>0){
                movieInDb = true;
                movieInDbId = cursor.getInt(cursor.getColumnIndex(BaseColumns._ID));
                movie.set_id(movieInDbId);
                cursor.close();
            } else {
                cursor.close();
            }
        }

        favButtonOn = (Button) findViewById(R.id.fav_btn_on);
        favButtonOff = (Button) findViewById(R.id.fav_btn_off);

        if(movieInDb){
            favButtonOff.setVisibility(findViewById(R.id.fav_btn_off).INVISIBLE);
            favButtonOn.setVisibility(findViewById(R.id.fav_btn_on).VISIBLE);
        }

        favButtonOff.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Movie added to favorites", Toast.LENGTH_SHORT);
                toast1.show();
                favButtonOff.setVisibility(findViewById(R.id.fav_btn_off).INVISIBLE);
                favButtonOn.setVisibility(findViewById(R.id.fav_btn_on).VISIBLE);
                mContentResolver = MovieDetails.this.getContentResolver();
                ContentValues values = new ContentValues();
                values.put(MovieContract.MovieColumns.MOVIE_TITLE, movie.getMovieTitle());
                values.put(MovieContract.MovieColumns.MOVIE_OVERVIEW, movie.getOverview());
                values.put(MovieContract.MovieColumns.MOVIE_POSTER, movie.getPosterPath());
                values.put(MovieContract.MovieColumns.MOVIE_RELEASE_DATE, movie.getRelease_date());
                values.put(MovieContract.MovieColumns.MOVIE_TMDB_ID, movie.getTmdb_id());
                values.put(MovieContract.MovieColumns.MOVIE_TOP_RATED, movie.getTop_rated());
                Uri returned = mContentResolver.insert(MovieContract.URI_TABLE, values);
                movieInDb = true;

                long id = Long.valueOf(returned.getLastPathSegment());
                movieInDbId = (int) id;
                movie.set_id(movieInDbId);
                Log.d("MovieAdded", "record id returned is " + returned.toString());
                favButtonOff.setVisibility(findViewById(R.id.fav_btn_off).INVISIBLE);
                favButtonOn.setVisibility(findViewById(R.id.fav_btn_on).VISIBLE);
            }
        });

        favButtonOn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast toast1 = Toast.makeText(getApplicationContext(), "Movie removed from favorites", Toast.LENGTH_SHORT);
                toast1.show();
                mContentResolver = MovieDetails.this.getContentResolver();
                Uri uri = MovieContract.Movie.buildMovieUri(String.valueOf(movie.get_id()));
                mContentResolver.delete(uri, null, null);
                Log.d("Movie non-fav", String.valueOf(movieInDb));
                movieInDb = false;
                Log.d("Movie non-fav", String.valueOf(movieInDb));
                favButtonOff.setVisibility(findViewById(R.id.fav_btn_off).VISIBLE);
                favButtonOn.setVisibility(findViewById(R.id.fav_btn_on).INVISIBLE);
            }
        });

        TextView movie_title = (TextView) findViewById(R.id.movie_title);
        movie_title.setText(movie.getMovieTitle());

        TextView movie_release = (TextView) findViewById(R.id.release);
        movie_release.setText("Release Date: " + movie.getRelease_date());

        TextView movie_rating = (TextView) findViewById(R.id.rating);
        movie_rating.setText("Rating: " + movie.getTop_rated());

        TextView movie_overview = (TextView) findViewById(R.id.overview);
        movie_overview.setText(movie.getOverview());


        ImageView movie_poster = (ImageView) findViewById(R.id.movie_poster);
        Picasso.with(this).load(movie.getPosterPath())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(movie_poster);

        //http://api.themoviedb.org/3/movie/118340/videos?api_key=<>

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetTrailerMethod APImethod = retrofit.create(GetTrailerMethod.class);
        Call<movieTrailerObject> trailersList1 = APImethod.getTrailerList(movie.getTmdb_id(), "api key");

        trailersList1.enqueue(new Callback<movieTrailerObject>() {
            @Override
            public void onResponse(Call<movieTrailerObject> call, Response<movieTrailerObject> response) {
                if (response.isSuccessful()) {
                    trailersList2 = response.body();
                    trailersList3 = trailersList2.getResults();
                    trailersList.clear();

                    for(Result singleTrailer : trailersList3){
                        String name = singleTrailer.getName();
                        trailersList.add(name);
                    }

                    for(Result singleTrailer : trailersList3){
                        String key = singleTrailer.getKey();
                        trailersKey.add(key);
                    }

                    trailerListAdapter.notifyDataSetChanged();

                    for (Result singleTrailer : trailersList3) {
                        Log.v("Retrofit", singleTrailer.toString());
                    }

                } else {
                    int sc = response.code();
                    Log.v("Retrofit", "response code is " + sc);
                }
            }

            @Override
            public void onFailure(Call<movieTrailerObject> call, Throwable t) {
                Log.v("Retrofit", t.toString());
            }
        });

        Retrofit retrofit2 = new Retrofit.Builder()
                .baseUrl("http://api.themoviedb.org/3/")
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GetReviewMethod APImethod2 = retrofit2.create(GetReviewMethod.class);
        Call<movieReviewObject> reviewsList1 = APImethod2.getReviewList(movie.getTmdb_id(), "api key");

        reviewsList1.enqueue(new Callback<movieReviewObject>() {
            @Override
            public void onResponse(Call<movieReviewObject> call, Response<movieReviewObject> response) {
                if (response.isSuccessful()) {
                    reviewsList2 = response.body();
                    reviewsList3 = reviewsList2.getResults();
                    reviewsList.clear();

                    for(ResultReview singleTrailer : reviewsList3){
                        String content = singleTrailer.getContent();
                        reviewsList.add(content);
                    }

                    reviewListAdapter.notifyDataSetChanged();

                    for (ResultReview singleReview : reviewsList3) {
                        Log.v("Retrofit", singleReview.toString());
                    }

                } else {
                    int sc = response.code();
                    Log.v("Retrofit", "response code is " + sc);
                }
            }

            @Override
            public void onFailure(Call<movieReviewObject> call, Throwable t) {
                Log.v("Retrofit", t.toString());
            }
        });


        trailerListView = (ListView) findViewById(R.id.trailers_list);
        trailerListAdapter = new ArrayAdapter<String>(this, R.layout.trailers_row, R.id.rowTextView, trailersList);
        trailerListView.setAdapter(trailerListAdapter);
        trailerListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String key = trailersKey.get(position);
                try {
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + key));
                    startActivity(intent);
                } catch (ActivityNotFoundException e) {
                    Intent intent = new Intent(Intent.ACTION_VIEW,
                            Uri.parse("http://www.youtube.com/watch?v=" + key));
                    startActivity(intent);
                }
            }

        });

        trailerListView.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                v.getParent().requestDisallowInterceptTouchEvent(true);
                return false;
            }
        });

        reviewListView = (ListView) findViewById(R.id.reviews_list);
        reviewListAdapter = new ArrayAdapter<String>(this, R.layout.trailers_row, R.id.rowTextView, reviewsList);
        reviewListView.setAdapter(reviewListAdapter);
        SetListViewHeight setListViewHeight = new SetListViewHeight();
        setListViewHeight.setListViewHeightBasedOnChildren(reviewListView);
    }

    @Override
    public void onBackPressed() {
        Intent intent = new Intent();
        intent.putExtra("isInFav", String.valueOf(movieInDb));
        setResult(1, intent);
        super.onBackPressed();
    }
}
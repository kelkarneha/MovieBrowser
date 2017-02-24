package administrator.example.org.moviebrowser;

import android.app.LoaderManager;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.Loader;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import administrator.example.org.moviebrowser.Database.MovieContract;
import administrator.example.org.moviebrowser.Database.MovieListLoader;

public class MainActivity extends ActionBarActivity implements LoaderManager.LoaderCallbacks<List<Movie>>, FragmentMovieDetails.OnItemClickedListener {
    private boolean mTwoPane = false;
    private static int LOADER_ID = 1;
    private static int menuOptionSelected =  R.id.action_popular;
    private static String MENU_SELECTED;
    private ContentResolver contentResolver;
    private List<Movie> favoriteMovies = new ArrayList<Movie>();
    private List<Movie> APIretrievedMovies =  new ArrayList<Movie>();
    private ArrayList<Movie> savedMovieArray = new ArrayList<Movie>();
    private ArrayList<Movie> SaveInstanceMovieList = new ArrayList<Movie>();
    private RecyclerView mRecyclerView;
    public TMDBRecyclerViewAdapter tmdbRecyclerViewAdapter;
    processMovies ProcessMovies = new processMovies();
    processMovies.processMovieData ProcessMovieData = ProcessMovies.new processMovieData();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(savedInstanceState != null){
            savedMovieArray = (ArrayList<Movie>) savedInstanceState.getSerializable(MENU_SELECTED);
            Log.d("onCreate", MENU_SELECTED);
            Log.d("onCreate", savedMovieArray.toString());

            setContentView(R.layout.activity_main);
            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

            if (findViewById(R.id.movie_detail_container)!= null){
                mTwoPane = true;
            }

            tmdbRecyclerViewAdapter = new TMDBRecyclerViewAdapter(MainActivity.this, savedMovieArray, new RecyclerOnItemClickListener() {
                @Override
                public void onItemClick(View view, int position) {
                    if(mTwoPane){
                        Bundle arguments = new Bundle();
                        arguments.putSerializable("Movie_transfer", savedMovieArray.get(position));
                        FragmentMovieDetails fragmentTablet = new FragmentMovieDetails();
                        fragmentTablet.setArguments(arguments);
                        getFragmentManager().beginTransaction()
                                .replace(R.id.movie_detail_container, fragmentTablet)
                                .commit();
                    }
                    else {
                        Intent intent = new Intent(MainActivity.this, MovieDetails.class);
                        intent.putExtra("Movie_transfer", savedMovieArray.get(position));
                        int requestCode = 2;
                        if(MENU_SELECTED.equals("favorites"))
                            requestCode = 1;
                        startActivityForResult(intent, requestCode);
                    }
                }
            });

            mRecyclerView.setAdapter(tmdbRecyclerViewAdapter);

            MainActivity.this.tmdbRecyclerViewAdapter.notifyDataSetChanged();

            if(MENU_SELECTED.equals("favorites")){
                favoriteMovies = savedMovieArray;
            } else {
                APIretrievedMovies = savedMovieArray;
            }

        } else {
            setContentView(R.layout.activity_main);

            mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
            mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));

            if (findViewById(R.id.movie_detail_container) != null) {
                mTwoPane = true;
            }

            processMovies ProcessMovies = new processMovies();
            String[] TMDBUrlArray = ProcessMovies.createTmdbUrl(true);
            processMovies.processMovieData ProcessMovieData = ProcessMovies.new processMovieData();
            ProcessMovieData.execute(TMDBUrlArray);
        }
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        if(menuOptionSelected == R.id.favorites){
            SaveInstanceMovieList = (ArrayList<Movie>) favoriteMovies;
            MENU_SELECTED = "favorites";
        } else {
            SaveInstanceMovieList = (ArrayList<Movie>) APIretrievedMovies;
            MENU_SELECTED = "other";
        }
        outState.putSerializable(MENU_SELECTED, SaveInstanceMovieList);
        Log.d("onSave", MENU_SELECTED);
        Log.d("onSave", SaveInstanceMovieList.toString());
        super.onSaveInstanceState(outState);
    }

    /*@Override
    protected void onRestoreInstanceState(Bundle savedInstanceState) {
        super.onRestoreInstanceState(savedInstanceState);
        setContentView(R.layout.activity_main);
        savedMovieArray = (ArrayList<Movie>) savedInstanceState.getSerializable(MENU_SELECTED);

        mRecyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        mRecyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        Log.d("onRestore", MENU_SELECTED);
        Log.d("onRestore", savedMovieArray.toString());

        setContentView(R.layout.activity_main);
        if (findViewById(R.id.movie_detail_container)!= null){
            mTwoPane = true;
        }

        tmdbRecyclerViewAdapter = new TMDBRecyclerViewAdapter(MainActivity.this, savedMovieArray, new RecyclerOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mTwoPane){
                    Bundle arguments = new Bundle();
                    arguments.putSerializable("Movie_transfer", savedMovieArray.get(position));
                    FragmentMovieDetails fragmentTablet = new FragmentMovieDetails();
                    fragmentTablet.setArguments(arguments);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragmentTablet)
                            .commit();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, MovieDetails.class);
                    intent.putExtra("Movie_transfer", savedMovieArray.get(position));
                    startActivityForResult(intent, 1);
                }
            }
        });

        mRecyclerView.setAdapter(tmdbRecyclerViewAdapter);
        MainActivity.this.tmdbRecyclerViewAdapter.notifyDataSetChanged();
    }*/

    @Override
    public Loader<List<Movie>> onCreateLoader(int id, Bundle args) {
        Log.v("create loader called", String.valueOf(id));
        contentResolver = this.getContentResolver();
        return new MovieListLoader(this, MovieContract.URI_TABLE, contentResolver);
    }

    @Override
    public void onLoadFinished(Loader<List<Movie>> loader, List<Movie> data) {
        Log.v("Loader finished", "Loader finished loading");
        favoriteMovies = data;
        tmdbRecyclerViewAdapter = new TMDBRecyclerViewAdapter(MainActivity.this, favoriteMovies, new RecyclerOnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if(mTwoPane){
                    Bundle arguments = new Bundle();
                    arguments.putSerializable("Movie_transfer", favoriteMovies.get(position));
                    FragmentMovieDetails fragmentTablet = new FragmentMovieDetails();
                    fragmentTablet.setArguments(arguments);
                    getFragmentManager().beginTransaction()
                            .replace(R.id.movie_detail_container, fragmentTablet)
                            .commit();
                }
                else {
                    Intent intent = new Intent(MainActivity.this, MovieDetails.class);
                    intent.putExtra("Movie_transfer", favoriteMovies.get(position));
                    startActivityForResult(intent, 1);
                }
            }
        });
        mRecyclerView.setAdapter(tmdbRecyclerViewAdapter);
        MainActivity.this.tmdbRecyclerViewAdapter.notifyDataSetChanged();
    }



    @Override
    public void onLoaderReset(Loader<List<Movie>> loader) {
        favoriteMovies = null;
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        switch(id){
            case R.id.action_popular:
                menuOptionSelected = R.id.action_popular;
                processMovies ProcessMoviesPopular = new processMovies();
                String[] TMDBUrlArrayP = ProcessMoviesPopular.createTmdbUrl(true);
                processMovies.processMovieData ProcessMovieDataP = ProcessMoviesPopular.new processMovieData();
                ProcessMovieDataP.execute(TMDBUrlArrayP);

                break;
            case R.id.action_highest_rated:
                menuOptionSelected = R.id.action_highest_rated;
                processMovies ProcessMoviesHighrated = new processMovies();
                String[] TMDBUrlArrayHR = ProcessMoviesHighrated.createTmdbUrl(false);
                processMovies.processMovieData ProcessMovieDataHR = ProcessMoviesHighrated.new processMovieData();
                ProcessMovieDataHR.execute(TMDBUrlArrayHR);

                break;
            case R.id.favorites:
                menuOptionSelected = R.id.favorites;
                contentResolver = this.getContentResolver();
                getLoaderManager().initLoader(LOADER_ID++, null, this).forceLoad();
                break;
            case R.id.action_settings:
                Toast.makeText(this, "Settings selected", Toast.LENGTH_SHORT)
                        .show();

                break;
        }

        return super.onOptionsItemSelected(item);
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        String result = "true";
        if (requestCode == 1) {
            if(resultCode == 1){
                result = data.getStringExtra("isInFav");
            }
        }
        if(result.equals("false")){
            getLoaderManager().initLoader(LOADER_ID++, null, this).forceLoad();
        }
    }

    public void OnItemClicked() {
        if(menuOptionSelected == R.id.favorites){
            getLoaderManager().initLoader(LOADER_ID++, null, this).forceLoad();
        }
    }

    /**
     * A placeholder fragment containing a simple view.
     */

    public class processMovies extends getData{
        public class processMovieData extends downloadMovieData{
            protected void onPostExecute (String data){
                
                tmdbRecyclerViewAdapter = new TMDBRecyclerViewAdapter(MainActivity.this, getTMDBFinalMovies(), new RecyclerOnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        if(mTwoPane){
                            Bundle arguments = new Bundle();
                            arguments.putSerializable("Movie_transfer", getTMDBFinalMovies().get(position));
                            FragmentMovieDetails fragmentTablet = new FragmentMovieDetails();
                            fragmentTablet.setArguments(arguments);
                            getFragmentManager().beginTransaction()
                                    .replace(R.id.movie_detail_container, fragmentTablet)
                                    .commit();
                        }
                        else {
                            Intent intent = new Intent(MainActivity.this, MovieDetails.class);
                            intent.putExtra("Movie_transfer", getTMDBFinalMovies().get(position));
                            startActivityForResult(intent, 2);
                        }
                    }
                });
                mRecyclerView.setAdapter(tmdbRecyclerViewAdapter);

                MainActivity.this.tmdbRecyclerViewAdapter.notifyDataSetChanged();
                APIretrievedMovies = getTMDBFinalMovies();
            }
        }
    }
}
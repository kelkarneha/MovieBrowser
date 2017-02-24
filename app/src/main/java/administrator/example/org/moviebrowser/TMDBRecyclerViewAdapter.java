package administrator.example.org.moviebrowser;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.squareup.picasso.Picasso;

import java.util.List;

/**
 * Created by Administrator on 3/9/2016.
 */
public class TMDBRecyclerViewAdapter extends RecyclerView.Adapter<MoviePosterViewHolder>{
    private List<Movie> MovieList;
    private Context mContext;
    private RecyclerOnItemClickListener recyclerOnItemClickListener;



    public TMDBRecyclerViewAdapter(Context context, List<Movie> moviesList, RecyclerOnItemClickListener recyclerOnItemClickListener) {
        this.MovieList = moviesList;
        this.mContext = context;
        this.recyclerOnItemClickListener = recyclerOnItemClickListener;
    }

    public List<Movie> getMovieList() {
        return MovieList;
    }

    @Override
    public MoviePosterViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        //Log.v("onCreateViewHolder", "called");
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.movie_list, null);
        final MoviePosterViewHolder moviePosterViewHolder = new MoviePosterViewHolder(view);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                recyclerOnItemClickListener.onItemClick(v, moviePosterViewHolder.getPosition());
            }
        });
        return moviePosterViewHolder;
    }

    @Override
    public int getItemCount() {
        int itemCount = MovieList.size();
        //Log.v("getItemCount", Integer.toString(itemCount));
        return MovieList.size();
    }

    @Override
    public void onBindViewHolder(MoviePosterViewHolder moviePosterViewHolder, int position) {
        //Log.v("onBindViewHolder", "called");
        Movie movieObject = MovieList.get(position);
        Picasso.with(mContext).load(movieObject.getPosterPath())
                .error(R.drawable.placeholder)
                .placeholder(R.drawable.placeholder)
                .into(moviePosterViewHolder.poster);
    }

    public void newDataLoad(List<Movie> newMovieData){
        this.MovieList = newMovieData;
        notifyDataSetChanged();
    }
}

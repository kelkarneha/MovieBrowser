package administrator.example.org.moviebrowser;

import java.io.Serializable;

/**
 * Created by Administrator on 3/7/2016.
 */
public class Movie implements Serializable{

    private static final long serialVersionUID = 1L;
    private String movieTitle;
    private String posterPath;
    private String overview;
    private String top_rated;
    private String release_date;
    private String tmdb_id;
    private int _id;



    public Movie(int _id, String movieTitle, String posterPath, String overview, String top_rated, String release_date, String tmdb_id){
        this._id = _id;
        this.movieTitle = movieTitle;
        this.posterPath = posterPath;
        this.overview = overview;
        this.top_rated = top_rated;
        this.release_date = release_date;
        this.tmdb_id = tmdb_id;
    }

    public static long getSerialVersionUID() {
        return serialVersionUID;
    }

    public String getMovieTitle() {
        return movieTitle;
    }

    public String getPosterPath() {
        return posterPath;
    }

    public String getOverview() {
        return overview;
    }

    public String getTop_rated() {
        return top_rated;
    }

    public String getRelease_date() {
        return release_date;
    }

    public String getTmdb_id() {
        return tmdb_id;
    }

    public int get_id() { return _id; }

    public void set_id(int _id) { this._id = _id; }

    @Override
    public String toString() {
        return "Movie{" +
                "movieTitle='" + movieTitle + '\'' +
                ", posterPath='" + posterPath + '\'' +
                ", overview='" + overview + '\'' +
                ", top_rated='" + top_rated + '\'' +
                ", release_date='" + release_date + '\'' +
                ", id='" + tmdb_id + '\'' +
                '}';
    }
}

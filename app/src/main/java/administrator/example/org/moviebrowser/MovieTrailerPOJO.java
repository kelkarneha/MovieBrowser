package administrator.example.org.moviebrowser;

import java.util.List;

/**
 * Created by Administrator on 4/5/2016.
 */
public class MovieTrailerPOJO {
    public List<results> results;

    public class results{
        public String key;
        //public String name;
    }

    public List<MovieTrailerPOJO.results> getResults() {
        return results;
    }
}


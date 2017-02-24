package administrator.example.org.moviebrowser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 4/5/2016.
 */
public interface GetTrailerMethod {
    @GET("movie/{id}/videos")
    Call<movieTrailerObject> getTrailerList(
            @Path("id") String movieId,
            @Query("api_key") String key
    );
}

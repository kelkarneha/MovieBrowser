package administrator.example.org.moviebrowser;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Administrator on 5/14/2016.
 */
public interface GetReviewMethod {
    @GET("movie/{id}/reviews")
    Call<movieReviewObject> getReviewList(
            @Path("id") String movieId,
            @Query("api_key") String key
    );
}

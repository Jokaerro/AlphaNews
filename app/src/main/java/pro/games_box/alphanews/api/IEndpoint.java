package pro.games_box.alphanews.api;

import java.util.List;

import pro.games_box.alphanews.model.response.NewsItemResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by Tesla on 10.05.2017.
 */

//https://alfabank.ru/_/rss/_rss.html?subtype=1&category=2&city=21
public interface IEndpoint {
    @GET("_/rss/_rss.html")
    Call<NewsItemResponse> getFeed(@Query("subtype") int type,
                                   @Query("category") int cat,
                                   @Query("city") int city);
}

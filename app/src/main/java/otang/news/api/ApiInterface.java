package otang.news.api;

import otang.news.model.NewsResponse;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface ApiInterface {
	@GET("/v2/top-headlines")
	Call<NewsResponse> getTopHeadline(@Query("country") String country, @Query("apiKey") String apikey);

	@GET("/v2/top-headlines")
	Call<NewsResponse> getByCategory(@Query("category") String category, @Query("country") String country,
			@Query("apiKey") String apikey);

	@GET("/v2/everything")
	Call<NewsResponse> findNews(@Query("q") String keyword, @Query("apiKey") String apikey);
}
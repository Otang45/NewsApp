package otang.app.news.api

import otang.app.news.model.NewsResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiInterface {
    @GET("/v2/top-headlines")
    fun getTopHeadline(
        @Query("country") country: String?,
        @Query("apiKey") apikey: String?
    ): Call<NewsResponse?>

    @GET("/v2/top-headlines")
    fun getByCategory(
        @Query("category") category: String?, @Query("country") country: String?,
        @Query("apiKey") apikey: String?
    ): Call<NewsResponse?>

    @GET("/v2/everything")
    fun findNews(
        @Query("q") keyword: String?,
        @Query("apiKey") apikey: String?
    ): Call<NewsResponse?>
}
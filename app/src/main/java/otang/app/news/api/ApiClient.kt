package otang.app.news.api

object ApiClient {
    private const val BASE_URL = "https://newsapi.org"
    val apiInterface: ApiInterface
        get() = RetrofitInstance.getClient(BASE_URL).create(ApiInterface::class.java)
}